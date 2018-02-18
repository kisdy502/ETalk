package cn.sdt.libnioserver;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import cn.sdt.libniocommon.BufferUtil;
import cn.sdt.libniocommon.IoHandler;
import cn.sdt.libniocommon.Packet;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class ServerManager {

    private final static String TAG = "ServerManager";
    private ConcurrentHashMap<String, Connection> connectionMap;

    private Context mContext;
    private Gson gson;
    private Charset charset;
    private NioServerAcceptor nioServerAcceptor;

    private static ServerManager instance;

    private ServerManager() {
        charset = Charset.forName("UTF-8");
        connectionMap = new ConcurrentHashMap<>();
        gson = new Gson();
    }

    public void initContext(Context context) {
        this.mContext = context;
    }

    public static ServerManager getInstance() {
        synchronized (ServerManager.class) {
            if (instance == null) {
                synchronized (ServerManager.class) {
                    instance = new ServerManager();
                }
            }
        }
        return instance;
    }

    public void start(final int port) {
        new Thread() {
            @Override
            public void run() {
                nioServerAcceptor = new NioServerAcceptor(port);
                nioServerAcceptor.setIoHandler(ioHandler);
                nioServerAcceptor.start();
            }
        }.start();

    }

    public void stop() {
        if (nioServerAcceptor != null) {
            nioServerAcceptor.stop();
        }
        for (Connection connection : connectionMap.values()) {
            connection.close();
        }
        connectionMap.clear();
        nioServerAcceptor = null;
    }

    private IoHandler ioHandler = new IoHandler() {

        @Override
        public void onConnected(SocketChannel socketChannel) {
            String name = socketChannel.socket().getRemoteSocketAddress().toString();
            Log.i(TAG, name + ":连接上了");
            Connection connection = new Connection();
            connection.setLogin(false);
            connection.setConnectionName(name);
            connection.setSocketChannel(socketChannel);
            connection.setUser(null);
            if (!connectionMap.containsKey(name)) {
                connectionMap.put(name, connection);
            }
            Log.w(TAG, "客户端数量:" + connectionMap.size());
        }

        @Override
        public void onConnectFailed(SocketChannel socketChannel) {

        }

        @Override
        public void onPacketReceived(SocketChannel socketChannel, ByteBuffer buffer) {
            String text = BufferUtil.getString(charset, buffer);
            Log.i(TAG, "Received:" + text);
            Packet packet = gson.fromJson(text, Packet.class);
            try {
                PacketDispatcher.dispatch(ServerManager.this, socketChannel, packet);
            } catch (IOException e) {
                if (ioHandler != null) {
                    ioHandler.onException(e);
                }
                e.printStackTrace();
            }
        }

        @Override
        public void onPacketSend(SocketChannel socketChannel, ByteBuffer buffer) {

        }

        @Override
        public void onDisconnected(SocketChannel socketChannel) {
            String name = socketChannel.socket().getRemoteSocketAddress().toString();
            Log.w(TAG, name + ":断开连接");
            if (connectionMap.containsKey(name)) {
                Connection connection = connectionMap.remove(name);
                connection = null;
            }
            Log.w(TAG, "客户端数量:" + connectionMap.size());
        }

        @Override
        public void onException(Exception e) {

        }
    };


    public Context getContext() {
        return mContext;
    }

    public Charset getCharset() {
        return charset;
    }

    public ConcurrentHashMap<String, Connection> getConnectionMap() {
        return connectionMap;
    }

    public Gson getGson() {
        return gson;
    }
}
