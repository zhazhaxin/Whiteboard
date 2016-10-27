package cn.lemon.whiteboard.module.account;

import java.util.Collections;
import java.util.List;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.whiteboard.data.AccountModel;

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
        AccountModel.getInstance().getNoteList(new AccountModel.NoteCallback() {
            @Override
            public void onCallback(List<Note> data) {
                if(data.size() > 0){
                    getView().showContent();
                    Collections.reverse(data);
                    getView().setData(data);
                }else {
                    getView().showEmpty();
                }
            }
        });
    }
}
