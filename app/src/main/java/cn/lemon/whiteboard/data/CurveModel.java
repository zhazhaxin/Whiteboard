package cn.lemon.whiteboard.data;

import android.graphics.Bitmap;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.alien95.util.ImageUtil;
import cn.lemon.common.base.model.SuperModel;

/**
 * Created by user on 2016/10/24.
 */

public class CurveModel extends SuperModel {

    //下载图片目录地址
    private static final String IMG_DIR = "Whiteboard";

    public CurveModel getInstance() {
        return getInstance(CurveModel.class);
    }

    public void saveCurve(Bitmap target){

        ImageUtil.saveBitmap(target,getImageFile(System.currentTimeMillis() + ".jpg"));
    }


    public File getImageFile(String imgName) {
        File rootDir = new File(getContext().getFilesDir(), IMG_DIR);
        if(rootDir.exists()){
            return new File(rootDir, imgName);
        }else {
            rootDir.mkdir();
            return new File(rootDir, imgName);
        }
    }

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
