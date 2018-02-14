package cn.sdt.chatserver;

import android.app.Application;

import cn.sdt.libnioserver.ServerManager;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ServerManager.getInstance().initContext(this);
    }
}
