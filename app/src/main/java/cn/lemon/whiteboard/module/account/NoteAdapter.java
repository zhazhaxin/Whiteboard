package cn.lemon.whiteboard.module.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.alien95.util.TimeTransform;
import cn.lemon.common.base.widget.MaterialDialog;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import cn.lemon.whiteboard.R;
import cn.lemon.whiteboard.app.Config;
import cn.lemon.whiteboard.data.AccountModel;

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

    private class NoteViewHolder extends BaseViewHolder<Note>{

        private TextView mTitle;
        private TextView mTime;

        public NoteViewHolder(ViewGroup parent) {
            super(parent, R.layout.account_holder_note);
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
        public void setData(final Note note) {
            super.setData(note);
            if(note != null){
                mTitle.setText(note.mTitle);
                mTime.setText(TimeTransform.getRecentlyDate(note.mCreateTime));
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteDialog(note);
                    return true;
                }
            });
        }

        private void deleteDialog(final Note note) {
            new MaterialDialog.Builder(itemView.getContext()).setTitle("是否删除")
                    .setCancelable(true)
                    .setOnPositiveClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AccountModel.getInstance().deleteNoteFile(note.mFileName);
                            remove(note);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
