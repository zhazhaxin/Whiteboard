package cn.lemon.whiteboard.app;

import android.app.Application;

import cn.alien95.util.Utils;

/**
 * Created by linlongxin on 2016/10/20.
 */

public class App extends Application {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.initialize(this);
    }

}
