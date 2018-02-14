package cn.sdt.etalk;

import android.app.Application;

import cn.sdt.libnioclient.ClientManager;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ClientManager.getInstance().initContext(this);
    }
}
