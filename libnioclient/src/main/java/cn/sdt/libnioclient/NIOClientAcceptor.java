package cn.sdt.libnioclient;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import cn.sdt.libniocommon.IoHandler;
import cn.sdt.libniocommon.NIOAcceptor;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class NIOClientAcceptor extends NIOAcceptor {

    private final static String TAG = "NIOClientAcceptor";
    private Selector selector = null;
    private SocketChannel socketChannel = null;
    private String ip;
    private int port;

    IoHandler ioHandler;

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setIoHandler(IoHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public NIOClientAcceptor(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void start() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            boolean connected = socketChannel.connect(new InetSocketAddress(ip, port));
            isRunning = true;
            Log.d(TAG, "01:isConnected:" + connected);
            // 开辟一个新线程来读取从服务器端的数据
            try {
                while (isRunning) {
                    int readyChannels = selector.select();
                    Log.d(TAG, "readyChannels:" + readyChannels);
                    if (readyChannels == 0)
                        continue;
                    Set selectedKeys = selector.selectedKeys();         // 可以通过这个方法，知道可用通道的集合
                    Iterator keyIterator = selectedKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey sk = (SelectionKey) keyIterator.next();
                        keyIterator.remove();
                        dealWithSelectionKey(sk);
                    }
                }
            } catch (IOException e) {
                if (ioHandler != null) {
                    ioHandler.onException(e);
                }
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (ioHandler != null) {
                ioHandler.onConnectFailed(socketChannel);
            }
            stop();
            e.printStackTrace();
        }
    }

    public void send(ByteBuffer buffer) {
        try {
            if (socketChannel.isConnected()) {
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void stop() {
        isRunning = false;
        stopServer();
    }


    public void stopServer() {
        Log.d(TAG, "stop");
        if (ioHandler != null && socketChannel != null) {
            ioHandler.onDisconnected(socketChannel);

        }
        if (socketChannel != null) {
            try {
                socketChannel.close();
                socketChannel = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null) {
            try {
                selector.close();
                selector = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void dealWithSelectionKey(SelectionKey sk) throws IOException {
        if (sk.isConnectable()) {
            SocketChannel client = (SocketChannel) sk.channel();
            if (client.isConnectionPending()) {
                try {
                    boolean result = client.finishConnect();
                    Log.i(TAG, "result:" + result);
                    if (ioHandler != null) {
                        ioHandler.onConnected(client);
                    }
                    sk.interestOps(SelectionKey.OP_READ);
                } catch (IOException e) {
                    Log.e(TAG, "无法连接打服务器");
                    if (ioHandler != null) {
                        ioHandler.onConnectFailed(client);
                    }
                    stop();
                }
            } else {
                Log.e(TAG, "没有尝试去连接了");
            }
        } else if (sk.isReadable()) {
            // 使用 NIO 读取 Channel中的数据，这个和全局变量sc是一样的，因为只注册了一个SocketChannel
            // sc既能写也能读，这边是读
            SocketChannel socketChannel = (SocketChannel) sk.channel();
            ByteBuffer buff = ByteBuffer.allocate(1024);
            int readCount = 0;
            while ((readCount = socketChannel.read(buff)) > 0) {
                buff.flip();
            }

            if (readCount == -1) {
                socketChannel.close();
                if (ioHandler != null) {
                    ioHandler.onDisconnected(socketChannel);
                }
            } else {
                if (ioHandler != null) {
                    ioHandler.onPacketReceived(socketChannel, buff);
                }
            }

        }
    }

}
