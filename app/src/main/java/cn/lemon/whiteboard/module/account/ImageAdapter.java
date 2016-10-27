package cn.lemon.whiteboard.module.account;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import cn.lemon.whiteboard.R;

/**
 * Created by user on 2016/10/27.
 */

public class ImageAdapter extends RecyclerAdapter<String> {

    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<String> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(parent);
    }

    class ImageViewHolder extends BaseViewHolder<String> {

        private ImageView mImage;

        public ImageViewHolder(ViewGroup parent) {
            super(parent, R.layout.account_holder_image);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mImage = findViewById(R.id.image);
        }

        @Override
        public void onItemViewClick(String object) {
            super.onItemViewClick(object);
        }

        @Override
        public void setData(String url) {
            super.setData(url);
            mImage.setImageURI(Uri.parse(url));
        }
    }
}
