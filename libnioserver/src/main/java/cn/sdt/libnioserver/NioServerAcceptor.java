package cn.sdt.libnioserver;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import cn.sdt.libniocommon.IoHandler;
import cn.sdt.libniocommon.NIOAcceptor;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public class NioServerAcceptor extends NIOAcceptor {
    private final static String TAG = "NioServerAcceptor";
    private ServerSocketChannel serverSocketChannel;
    private ServerSocket serverSocket;
    private IoHandler ioHandler;

    public NioServerAcceptor(int port) {
        this.port = port;
    }

    public void setIoHandler(IoHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    @Override
    protected void start() {
        //打开选择器
        try {
            mSelector = Selector.open();
            // 开启服务器端通道，并指定端口号
            serverSocketChannel = ServerSocketChannel.open();
            serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocket.bind(address);
            serverSocketChannel.configureBlocking(false);
            //将选择器注册到服务器通道上
            serverSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
            //等待客户端的连接
            while (isRunning) {
                int nums = mSelector.select();
                if (nums <= 0) {
                    continue;
                }
                //存在连接
                Set<SelectionKey> selectionKeys = mSelector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    //得到当前的选择键
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    //处理当前的选择键
                    if (key.isAcceptable()) {
                        handleAcceptableKey(serverSocketChannel, key);
                    } else if (key.isReadable()) {
                        handleReadableKey(key);
                    }
                }
            }
        } catch (IOException e) {
            if (ioHandler != null) {
                ioHandler.onException(e);
            }
            e.printStackTrace();
        }

    }


    @Override
    protected void stop() {
        isRunning = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mSelector != null)
            try {
                mSelector.selectedKeys().clear();
                mSelector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void handleAcceptableKey(ServerSocketChannel server, SelectionKey key) {
        SocketChannel sChannel = null;
        try {
            sChannel = server.accept();
            sChannel.configureBlocking(false);
            sChannel.register(mSelector, SelectionKey.OP_READ);
            //将此对应的channel设置为准备接收其他客户端的请求
            key.interestOps(SelectionKey.OP_ACCEPT);
            if (ioHandler != null) {
                ioHandler.onConnected(sChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleReadableKey(SelectionKey key) {
        int receivedcount = 0;  //当一方断开连接时，值为-1

        SocketChannel sChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            while ((receivedcount = sChannel.read(buffer)) > 0) {
                buffer.flip();
            }
            key.interestOps(SelectionKey.OP_READ);

            Log.i(TAG, "receivedcount:" + receivedcount);
            Log.i(TAG, "buffer 已经写入了:" + buffer.limit());

            if (receivedcount == -1) {
                if (ioHandler != null) {
                    ioHandler.onDisconnected(sChannel);
                }
                key.cancel();
                sChannel.close();
            } else {
                if (ioHandler != null) {
                    ioHandler.onPacketReceived(sChannel, buffer);
                }
            }
        } catch (IOException e) {
            key.cancel();
            try {
                sChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }


}
