package cn.lemon.whiteboard.module.note;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.alien95.util.TimeTransform;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import cn.lemon.whiteboard.R;
import cn.lemon.whiteboard.app.Config;

/**
 * Created by user on 2016/10/25.
 */

public class NoteAdapter extends RecyclerAdapter<Note> {

    private Context mContext;

    public NoteAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<Note> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(parent);
    }

    class NoteViewHolder extends BaseViewHolder<Note>{

        private TextView mTitle;
        private TextView mTime;

        public NoteViewHolder(ViewGroup parent) {
            super(parent, R.layout.holder_note);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mTitle = findViewById(R.id.title);
            mTime = findViewById(R.id.create_time);
        }

        @Override
        public void onItemViewClick(Note object) {
            super.onItemViewClick(object);
            if(mContext instanceof NoteActivity){
                Intent intent = new Intent();
                intent.putExtra(Config.NOTE_DATA,object);
                ((NoteActivity) mContext).setResult(Config.NOTE_RESULT_CODE,intent);
                ((NoteActivity) mContext).finish();
            }
        }

        @Override
        public void setData(Note object) {
            super.setData(object);
            mTitle.setText(object.title);
            mTime.setText(TimeTransform.getRecentlyDate(object.createTime));
        }
    }
}
