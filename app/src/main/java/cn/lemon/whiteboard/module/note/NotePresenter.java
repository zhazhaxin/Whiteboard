package cn.lemon.whiteboard.module.note;

import java.util.List;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.whiteboard.data.NoteModel;

/**
 * Created by user on 2016/10/25.
 */

public class NotePresenter extends SuperPresenter<NoteActivity> {

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }

    public void getData(){
        NoteModel.getInstance().getNoteList(new NoteModel.Callback() {
            @Override
            public void onCallback(List<Note> data) {
                if(data.size() > 0){
                    getView().showContent();
                    getView().setData(data);
                }else {
                    getView().showEmpty();
                }
            }
        });
    }
}
