package cn.lemon.whiteboard.app;

import android.app.Application;

import cn.alien95.util.Utils;
import cn.lemon.common.base.model.SuperModel;
import cn.lemon.whiteboard.BuildConfig;

/**
 * Created by linlongxin on 2016/10/24.
 */

public class App extends Application {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.initialize(this);
        SuperModel.initialize(this);
        if(BuildConfig.DEBUG){
            Utils.setDebug(true,"Whiteboard");
        }
    }
}
