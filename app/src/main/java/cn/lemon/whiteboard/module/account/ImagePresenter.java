package cn.lemon.whiteboard.module.account;

import java.util.Collections;
import java.util.List;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.whiteboard.data.AccountModel;

/**
 * Created by user on 2016/10/27.
 */

public class ImagePresenter extends SuperPresenter<ImageActivity> {

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }

    public void getData() {
        AccountModel.getInstance().getImageList(new AccountModel.ImageCallback() {
            @Override
            public void onCallback(List<String> data) {
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
