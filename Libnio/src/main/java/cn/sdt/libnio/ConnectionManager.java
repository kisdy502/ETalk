package cn.sdt.libnio;

import android.content.Context;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public class ConnectionManager {

    private ChatClient client = null;

    static ConnectionManager instance = new ConnectionManager();


    private ConnectionManager() {
    }

    public static ConnectionManager getInstance() {
        return instance;
    }

    public ChatClient getClient() {
        return client;
    }

    public boolean isConnected() {
        return client != null ? client.isConnected() : false;
    }

    public void startClient(final Context context, String ip, int port) {
        client = new ChatClient(context, ip, port);
        new Thread() {
            @Override
            public void run() {
                client.initClient();
            }
        }.start();
    }

    public void sendData(final String data) {
        client.sendData(data);

    }

    public void stopClient() {
        client.stopServer();
    }

}
