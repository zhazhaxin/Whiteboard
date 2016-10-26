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
import cn.lemon.whiteboard.module.note.Note;

/**
 * Created by user on 2016/10/25.
 */

public class NoteModel extends SuperModel {

    private Handler mHandler;

    public NoteModel() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static NoteModel getInstance() {
        return getInstance(NoteModel.class);
    }

    public void saveNote(Note note) {
        putObject(note.mFileName, note);
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

    public void getNoteList(final Callback callback) {
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
                        callback.onCallback(notes);
                    }
                });
            }
        });
    }

    public Object readObjectFromFile(File objectFile) {
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

    public interface Callback {
        void onCallback(List<Note> data);
    }

}
