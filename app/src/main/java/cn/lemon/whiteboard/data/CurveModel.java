package cn.lemon.whiteboard.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.alien95.util.AsyncThreadPool;
import cn.alien95.util.Utils;
import cn.lemon.common.base.model.SuperModel;

/**
 * Created by user on 2016/10/24.
 */

public class CurveModel extends SuperModel {

    private File mAppRootDir;
    private Handler mHandler;

    public CurveModel() {
        mAppRootDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mAppRootDir.exists()) {
            mAppRootDir.mkdir();
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static CurveModel getInstance() {
        return getInstance(CurveModel.class);
    }

    //保存到APP目录
    public void saveCurveToApp(final Bitmap resource) {
        File image = new File(mAppRootDir, System.currentTimeMillis() + ".png");
        saveImage(resource, image);
    }

    private void saveImage(final Bitmap resource, final File target) {
        if (resource.getByteCount() == 0) {
            Utils.Toast("bitmap is empty");
            return;
        }
        AsyncThreadPool.getInstance().executeRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream out = new FileOutputStream(target);
                    resource.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    // 最后通知图库更新
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://" + target.getAbsolutePath())));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.Toast("保存图片成功");
                        }
                    });
                    Utils.Log("图片地址 ：" + target.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public File getAppImageDir(){
        return mAppRootDir;
    }

}
