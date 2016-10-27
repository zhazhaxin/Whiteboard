package cn.lemon.whiteboard.module.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.ImageUtil;

class ViewImageAdapter extends PagerAdapter {

    private static final String TAG = "ViewImageAdapter";
    private List<WeakReference<ImageView>> cacheViews;
    private List<String> imageUrls;
    private Context mContext;

    public ViewImageAdapter(List<String> data, Context context) {
        imageUrls = data;
        cacheViews = new ArrayList<>();
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView;
        if (cacheViews.isEmpty()) {
            initView(2);
        }
        imageView = cacheViews.get(0).get();

        if (imageView == null) {  //由于用了弱引用，所以当垃圾回收器进行回收的时候就回收所有内存
            cacheViews.clear();   //需要先清理，GC后对象内存被回收
            initView(2);
            imageView = cacheViews.get(0).get();
            Log.i(TAG, "----发生了GC----  init view size : " + cacheViews.size());
        }
        container.addView(imageView);

        final ImageView finalImageView = imageView;
        ImageUtil.getBitmapFromPath(imageUrls.get(position), new ImageUtil.Callback() {
            @Override
            public void callback(Bitmap bitmap) {
                finalImageView.setImageBitmap(bitmap);
            }
        });

        cacheViews.remove(0);
        Log.i(TAG, "instantiateItem cache size : " + cacheViews.size());

        return imageView;
    }

    public void initView(int num) {
        for (int i = 0; i < num; i++) {
            ImageView image = new ImageView(mContext);
            image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cacheViews.add(new WeakReference<>(image));
        }
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        cacheViews.add(new WeakReference<>((ImageView) object));   //添加进缓存
        container.removeView((View) object);
        Log.i(TAG, "destroyItem cache size : " + cacheViews.size());
    }

}