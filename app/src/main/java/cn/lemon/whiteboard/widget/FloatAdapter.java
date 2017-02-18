package cn.lemon.whiteboard.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.alien95.util.Utils;
import cn.lemon.whiteboard.R;


public abstract class FloatAdapter {

    private Context mContext;

    public FloatAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 获取每个子功能view
     */
    public View getItem(final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_float_view, null);
        ((TextView) view.findViewById(R.id.hint)).setText(getItemHint(position));
        ImageView item = (ImageView) view.findViewById(R.id.function_button);
        if (getItemResource(position) == 0) {
            item.setImageResource(R.drawable.ic_float_switch);
        } else {
            item.setImageResource(getItemResource(position));
        }
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        return view;
    }

    /**
     * 获取开关view
     */
    public ImageView getSwitchView() {
        ImageView switchView = new ImageView(mContext);
        switchView.setLayoutParams(new ViewGroup.LayoutParams(Utils.dip2px(56),Utils.dip2px(56)));
        switchView.setImageResource(getMainResource());
        return switchView;
    }

    public Context getContext(){
        return mContext;
    }

    public abstract int getCount();

    public abstract String getItemHint(int position);

    public abstract int getItemResource(int position);

    public abstract int getMainResource();

    public abstract void onItemClick(int position);

}
