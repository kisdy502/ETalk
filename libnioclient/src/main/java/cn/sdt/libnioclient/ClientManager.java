package cn.sdt.libnioclient;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;

import cn.sdt.libniocommon.BufferUtil;
import cn.sdt.libniocommon.IoHandler;
import cn.sdt.libniocommon.Packet;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class ClientManager {

    private final static String TAG = "ClientManager";

    public final static int MSG_TIMEOUT = 1;
    public final static int DELAY_TIME = 24 * 1000;

    private Handler mHandler;

    private NIOClientAcceptor nioClientAcceptor;

    private Context mContext;

    private static ClientManager instance;

    HeartRunnable heartRunnable;

    private ArrayBlockingQueue<Packet> msgQueue;
    private Gson gson = new Gson();

    private Charset charset = Charset.forName("UTF-8");

    private volatile boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    private ClientManager() {
        msgQueue = new ArrayBlockingQueue<Packet>(12);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_TIMEOUT) {
                    Log.d(TAG, "心跳超时");
                }
            }
        };
    }

    public static ClientManager getInstance() {
        synchronized (ClientManager.class) {
            if (instance == null) {
                synchronized (ClientManager.class) {
                    instance = new ClientManager();
                }
            }
        }
        return instance;
    }

    public void start(final String ip, final int port) {
        new Thread() {
            @Override
            public void run() {
                nioClientAcceptor = new NIOClientAcceptor(ip, port);
                nioClientAcceptor.setIoHandler(ioHandler);
                nioClientAcceptor.start();
            }
        }.start();
    }

    public void send(final Packet packet) {
        if (connected) {
            new Thread() {
                @Override
                public void run() {
                    String data = gson.toJson(packet);
                    ByteBuffer buffer = charset.encode(data);
                    nioClientAcceptor.send(buffer);
                }
            }.start();
        }
    }

    public void equeuePacke(final Packet packet) {
        try {
            msgQueue.put(packet);
        } catch (InterruptedException e) {
            Log.d(TAG, "消息放入队列发生异常:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        if (nioClientAcceptor != null) {
            nioClientAcceptor.stop();
        }
    }

    private IoHandler ioHandler = new IoHandler() {
        @Override
        public void onConnected(SocketChannel socketChannel) {
            Log.i(TAG, "onConnected");
            connected = true;
            heartRunnable = new HeartRunnable(ClientManager.this);
            new Thread(heartRunnable).start();
            MsgReceiver.boardConnectedMsg(mContext, "连接成功");
        }

        @Override
        public void onConnectFailed(SocketChannel socketChannel) {
            Log.i(TAG, "onConnectFailed");
            MsgReceiver.boardConnectedFailedMsg(mContext, "连接失败");
        }

        @Override
        public void onPacketReceived(SocketChannel socketChannel, ByteBuffer buffer) {
            String text = BufferUtil.getString(charset, buffer);
            Log.d(TAG, "Received:" + text);
            Packet packet = gson.fromJson(text, Packet.class);
            try {
                PacketDispatcher.dispatch(ClientManager.this, socketChannel, packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPacketSend(SocketChannel socketChannel, ByteBuffer buffer) {
        }

        @Override
        public void onDisconnected(SocketChannel client) {
            connected = false;
            Log.i(TAG, "onDisconnected");
            if (heartRunnable != null)
                heartRunnable.stop();
            MsgReceiver.boardDisConnectedMsg(mContext, "断开连接");
        }

        @Override
        public void onException(Exception e) {
            Log.i(TAG, "onException:" + e.getMessage());
        }
    };

    public void initContext(Context context) {
        this.mContext = context;
    }


    public Context getContext() {
        return mContext;
    }

    public Gson getGson() {
        return gson;
    }

    public Charset getCharset() {
        return charset;
    }

    public SocketChannel getSocketChannel() {
        if (nioClientAcceptor != null)
            return nioClientAcceptor.getSocketChannel();
        return null;
    }

    public Handler getHandler() {
        return mHandler;
    }
}
