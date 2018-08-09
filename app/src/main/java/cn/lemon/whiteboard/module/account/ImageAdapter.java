package cn.lemon.whiteboard.module.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.Serializable;

import cn.alien95.util.ImageUtil;
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

    private class ImageViewHolder extends BaseViewHolder<String> {

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
            Intent intent = new Intent(itemView.getContext(),ViewImageActivity.class);
            intent.putExtra(ViewImageActivity.IMAGES_DATA_LIST, (Serializable) ImageAdapter.this.getData());
            intent.putExtra(ViewImageActivity.IMAGE_NUM, getAdapterPosition());
            itemView.getContext().startActivity(intent);
        }

        @Override
        public void setData(String url) {
            super.setData(url);

            ImageUtil.compress(url, 200, 288, new ImageUtil.Callback() {
                @Override
                public void callback(Bitmap bitmap) {
                    mImage.setImageBitmap(bitmap);
                }
            });
        }
    }
}
