package cn.lemon.whiteboard.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.alien95.util.AsyncThreadPool;
import cn.alien95.util.Utils;
import cn.lemon.common.base.model.SuperModel;
import cn.lemon.whiteboard.module.note.Note;

/**
 * Created by user on 2016/10/24.
 */

public class CurveModel extends SuperModel {

    //保存图片目录地址
    private final String IMG_DIR = "Whiteboard";
    private final String IMAGE_HEADER = "Whiteboard_";
    private File mRootDir;
    private Handler mHandler;

    public CurveModel() {
        mRootDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMG_DIR);
        if (!mRootDir.exists()) {
            mRootDir.mkdir();
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static CurveModel getInstance() {
        return getInstance(CurveModel.class);
    }

    public void saveCurve(final Bitmap target){
        if (target.getByteCount() == 0) {
            Utils.Toast("bitmap is empty");
            return;
        }
        AsyncThreadPool.getInstance().executeRunnable(new Runnable() {
            @Override
            public void run() {
                File image = new File(mRootDir, IMAGE_HEADER + System.currentTimeMillis() + ".png");
                try {
                    FileOutputStream out = new FileOutputStream(image);
                    target.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    // 最后通知图库更新
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + image.getAbsolutePath())));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.Toast("保存成功");
                        }
                    });
                    Utils.Log("图片地址 ：" + image.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveNote(Note note) {
        putObject(System.currentTimeMillis() + "", note);
    }

    /**
     * MD5
     */
    public static String MD5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 字节转换成16进制字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
