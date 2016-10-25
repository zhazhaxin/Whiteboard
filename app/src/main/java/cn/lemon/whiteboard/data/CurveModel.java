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

import cn.alien95.util.AsyncThreadPool;
import cn.alien95.util.ImageUtil;
import cn.alien95.util.Utils;
import cn.lemon.common.base.model.SuperModel;

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
//        mRootDir = new File(getContext().getCacheDir(), IMG_DIR);
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
        if (target == null) {
            Utils.Log("bitmap == null");
            return;
        }
        AsyncThreadPool.getInstance().executeRunnable(new Runnable() {
            @Override
            public void run() {
                File image = new File(mRootDir, IMAGE_HEADER + System.currentTimeMillis() + ".png");
                ImageUtil.saveBitmap(target, image);
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

}
