package cn.sdt.libnio;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import cn.sdt.NIOConnection;

/**
 * Created by SDT13411 on 2018/2/9.
 */

public class ChatClient extends NIOConnection {

    private final static String TAG = "ChatClient";
    private Context mContext;

    private Selector selector = null;
    private Charset charset = Charset.forName("UTF-8");
    private SocketChannel sc = null;
    private String name = "";
    private static String USER_EXIST = "system message: user exist, please change a name";
    private static String USER_CONTENT_SPILIT = "#@#";

    private volatile boolean connected = false;

    private ExecutorService executorService;

    public boolean isConnected() {
        return connected;
    }

    private String ip;
    private int port;

    private volatile boolean isRunning = true;

    ArrayBlockingQueue<String> queue;

    IoHandler handler = new IoHandler() {
        @Override
        public void onConnected(NIOConnection nioConn) {
            OldMsgReceiver.boardConnectedMsg(mContext, "success");
        }

        @Override
        public void onConnectFailed(NIOConnection nioConn) {
            OldMsgReceiver.boardConnectedFailedMsg(mContext, "failed");
        }

        @Override
        public void onPacketReceived(Buffer buffer) {

        }

        @Override
        public void onPacketSend(Buffer buffer) {

        }

        @Override
        public void onDisconnected(SocketChannel client) {

        }
    };


    public ChatClient(Context context, String ip, int port) {
        mContext = context;
        this.ip = ip;
        this.port = port;
    }

    public void initClient() {
        try {
            selector = Selector.open();
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_CONNECT);
            sc.connect(new InetSocketAddress(ip, port));
            // 开辟一个新线程来读取从服务器端的数据
            new Thread(new ClientThread()).start();

            startMsgQueue();
        } catch (IOException e) {
            handler.onConnectFailed(this);
            e.printStackTrace();
        }
    }


    private void startMsgQueue() {
        try {
            queue = new ArrayBlockingQueue<>(32);
            while (isRunning) {
                Log.d(TAG, "等待数据加入队列:");
                String data = queue.take();
                Log.d(TAG, " 待发送的数据" + data);
                sc.write(charset.encode(data));
                handler.onPacketSend(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void sendData(String data) {
        String line;
        if ("".equals(data)) {
            return;
        }
        if ("".equals(name)) {
            name = data;
            line = name.concat(USER_CONTENT_SPILIT);
        } else {
            line = name.concat(USER_CONTENT_SPILIT).concat(data);
        }
        try {
            queue.put(line);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "队列加入待发送数据");
    }

    public void stopServer() {
        isRunning = false;
        try {
            stop();
        } catch (IOException e) {
            Log.e(TAG, "关闭客户端发生异常");
            e.printStackTrace();
        }
    }

    private void stop() throws IOException {
        Log.d(TAG, "stop");
        if (sc != null) {
            sc.close();
        }
        if (selector != null) {
            selector.close();
        }
        connected = false;
        handler.onDisconnected(sc);
        OldMsgReceiver.boardDisconnectedMsg(mContext, "服务器已经关闭");
    }

    private class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                while (isRunning) {
                    int readyChannels = selector.select();
                    if (readyChannels == 0)
                        continue;
                    Set selectedKeys = selector.selectedKeys(); // 可以通过这个方法，知道可用通道的集合
                    Iterator keyIterator = selectedKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey sk = (SelectionKey) keyIterator.next();
                        keyIterator.remove();
                        dealWithSelectionKey(sk);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void dealWithSelectionKey(SelectionKey sk) throws IOException {
            if (sk.isConnectable()) {
                SocketChannel client = (SocketChannel) sk.channel();
                if (client.isConnectionPending()) {
                    client.finishConnect();
                    connected = true;
                    Log.d("ClientThread", "isConnected:" + connected);
                    handler.onConnected(ChatClient.this);
                    sk.interestOps(SelectionKey.OP_READ);
                }
            } else if (sk.isReadable()) {
                // 使用 NIO 读取 Channel中的数据，这个和全局变量sc是一样的，因为只注册了一个SocketChannel
                // sc既能写也能读，这边是读
                SocketChannel sc = (SocketChannel) sk.channel();
                ByteBuffer buff = ByteBuffer.allocate(1024);
                StringBuilder content = new StringBuilder();
                int readCount = 0;
                while ((readCount = sc.read(buff)) > 0) {
                    buff.flip();
                    content.append(charset.decode(buff));
                }
                // 若系统发送通知名字已经存在，则需要换个昵称
                String msg = content.toString();
                Log.d(TAG, "msg:::" + msg);
                handler.onPacketReceived(null);
                if (USER_EXIST.equals(msg)) {
                    name = "";
                    OldMsgReceiver.boardLoginFailedMsg(mContext, USER_EXIST);
                } else if (msg.equals("logined")) {
                    OldMsgReceiver.boardLoginMsg(mContext, "login successed");
                } else {
                    OldMsgReceiver.boardChatMsg(mContext, msg);
                }
                sk.interestOps(SelectionKey.OP_READ);
                if (readCount == -1) {
                    sc.socket().close();
                    connected = false;
                    handler.onDisconnected(sc);
                    OldMsgReceiver.boardDisconnectedMsg(mContext, "服务器已经关闭");
                }
            }
        }

    }

}
