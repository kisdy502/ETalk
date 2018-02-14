package cn.sdt.chatserver;

import android.os.Bundle;
import android.util.Log;

import cn.sdt.libniocommon.util.NetTools;
import cn.sdt.libnioserver.ServerManager;

public class ServerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(){
            @Override
            public void run() {
                String ip = NetTools.getClientIp(ServerActivity.this);
                Log.d("server","本机ip:" + ip);
            }
        }.start();
        setContentView(R.layout.activity_server);


        initServer();
    }

    private void initServer() {
        ServerManager.getInstance().start(9000);
    }

}
