package cn.lemon.whiteboard.data;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.AsyncThreadPool;
import cn.alien95.util.Utils;
import cn.lemon.common.base.model.SuperModel;
import cn.lemon.whiteboard.module.account.Note;

/**
 * Created by user on 2016/10/25.
 */

public class AccountModel extends SuperModel {

    private Handler mHandler;

    public AccountModel() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static AccountModel getInstance() {
        return getInstance(AccountModel.class);
    }

    public void saveNote(Note note) {
        if (note.mPaths.size() == 0) {
            Utils.Toast("笔迹不能为空");
        } else {
            putObject(note.mFileName, note);
            Utils.Toast("笔迹保存成功");
        }

    }

    public void deleteNoteFile(final String fileName){
        AsyncThreadPool.getInstance().executeRunnable(new Runnable() {
            @Override
            public void run() {
                File cacheDir = new File(getObjectCacheDir());
                if (!cacheDir.exists()) {
                    Utils.Toast("缓存目录不存在");
                } else {
                    File[] files = cacheDir.listFiles();
                    for (File f : files) {
                        if(f.getName().equals(fileName)){
                            f.delete();
                            return;
                        }
                    }
                }
            }
        });
    }

    public void getImageList(final ImageCallback callback) {
        AsyncThreadPool.getInstance().executeRunnable(new Runnable() {
            @Override
            public void run() {
                final List<String> filePaths = new ArrayList<>();
                File cacheDir = CurveModel.getInstance().getAppImageDir();
                if (!cacheDir.exists()) {
                    Utils.Toast("缓存目录不存在");
                } else {
                    File[] files = cacheDir.listFiles();
                    for (File f : files) {
                        filePaths.add(f.getAbsolutePath());
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCallback(filePaths);
                    }
                });
            }
        });
    }

    //获取笔迹
    public void getNoteList(final NoteCallback noteCallback) {
        AsyncThreadPool.getInstance().executeRunnable(new Runnable() {
            @Override
            public void run() {
                final List<Note> notes = new ArrayList<>();
                File cacheDir = new File(getObjectCacheDir());
                if (!cacheDir.exists()) {
                    Utils.Toast("缓存目录不存在");
                } else {
                    File[] files = cacheDir.listFiles();
                    for (File f : files) {
                        notes.add((Note) readObjectFromFile(f));
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        noteCallback.onCallback(notes);
                    }
                });
            }
        });
    }

    private Object readObjectFromFile(File objectFile) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectFile));
            Object result = ois.readObject();
            ois.close();
            return result;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("SuperModel", "对象缓存读取失败");
        }
        return null;
    }

    public interface NoteCallback {
        void onCallback(List<Note> data);
    }

    public interface ImageCallback {
        void onCallback(List<String> data);
    }

}
