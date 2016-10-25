package cn.lemon.whiteboard;

import cn.lemon.common.base.presenter.SuperPresenter;

/**
 * Created by user on 2016/10/24.
 */

public class MainPresenter extends SuperPresenter<MainActivity> {

    private Integer[] mResData = {R.drawable.ic_curve,
            R.drawable.ic_line, R.drawable.ic_rectangle,
            R.drawable.ic_oval};

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
