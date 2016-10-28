package cn.lemon.whiteboard.module.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.whiteboard.app.Config;
import cn.lemon.whiteboard.data.AccountModel;
import cn.lemon.whiteboard.data.CurveModel;
import cn.lemon.whiteboard.module.account.Note;
import cn.lemon.whiteboard.widget.shape.ShapeResource;

/**
 * Created by user on 2016/10/24.
 */

public class MainPresenter extends SuperPresenter<MainActivity> {

    private Note mLocalNote;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void saveNote(String title, List<ShapeResource> paths){
        if (TextUtils.isEmpty(title)) {
            Utils.Toast("标题不能为空");
            return;
        }
        Note note = new Note();
        note.mTitle = title;
        note.mPaths = paths;
        long time = System.currentTimeMillis();
        note.mCreateTime = time;
        note.mFileName = time + "";
        AccountModel.getInstance().saveNote(note);
        if (mLocalNote != null) {
            AccountModel.getInstance().deleteNoteFile(mLocalNote.mFileName);
            mLocalNote = null;
        }
    }

    public void saveImage(Bitmap bitmap){
        CurveModel.getInstance().saveCurveToApp(bitmap);
    }

    public void setLocalNoteNull(){
        mLocalNote = null;
    }

    public Note getLocalNote(){
        return mLocalNote;
    }

    public void startActivity(Class target) {
        Intent intent = new Intent(getView(), target);
        getView().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.NOTE_REQUEST_CODE && resultCode == Config.NOTE_RESULT_CODE) {
            mLocalNote = (Note) data.getSerializableExtra(Config.NOTE_DATA);
            getView().updateDrawPaths(mLocalNote.mPaths);
        }
    }
}
