package cn.sdt.libnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import cn.sdt.NIOConnection;

/**
 * Created by SDT13411 on 2018/2/9.
 */

public class NIOServer extends NIOConnection {

    private int port;
    boolean isRunning = true;
    /**
     * 选择器
     */
    private Selector selector;

    private IoHandler ioHandler = new IoHandler() {
        @Override
        public void onConnected(NIOConnection ionConn) {

        }

        @Override
        public void onConnectFailed(NIOConnection ionConn) {

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

    public NIOServer(int port) {
        this.port = port;
    }

    public void initServer() throws IOException {
        //打开选择器
        this.selector = Selector.open();
        // 开启服务器端通道，并指定端口号
        ServerSocketChannel server = ServerSocketChannel.open();
        ServerSocket serverSocket = server.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);
        server.configureBlocking(false);
        //将选择器注册到服务器通道上
        server.register(selector, SelectionKey.OP_ACCEPT);
        //等待客户端的连接
        int nums;
        while (isRunning) {
            nums = this.selector.select();
            if (nums <= 0) {
                continue;
            }
            //存在连接
            Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                //得到当前的选择键
                SelectionKey key = iterator.next();
                iterator.remove();
                //处理当前的选择键
                dealWithSelectionKey(server, key);
            }
        }

        if (serverSocket != null) {
            serverSocket.close();
        }
        if (server != null) {
            server.close();
        }
        if (selector != null)
            selector.close();
    }


    public void stopServer() {
        isRunning = false;
    }

    /**
     * @param server
     * @param key
     * @throws IOException
     * @author 1
     */
    private void dealWithSelectionKey(ServerSocketChannel server, SelectionKey key)
            throws IOException {
        if (key.isAcceptable()) {
            SocketChannel sChannel = server.accept();        //接收客户端
            sChannel.configureBlocking(false);               //设置非阻塞
            sChannel.register(selector, SelectionKey.OP_READ);
            key.interestOps(SelectionKey.OP_ACCEPT);
            ioHandler.onConnected(this);
        } else if (key.isReadable()) {                //处理来自客户端的数据读取请求
            handlerReadKey(key);
        }
    }


    private void handlerReadKey(SelectionKey key) {
        int readCount;            //得到该key对应的channel，其中有数据需要读取
        SocketChannel sc = (SocketChannel) key.channel();
        StringBuffer content = new StringBuffer();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            //得到客户端传过来的消息
            while ((readCount = sc.read(buffer)) > 0) {
                buffer.flip();
            }
            //将此对应的channel设置为准备下一次接受数据
            key.interestOps(SelectionKey.OP_READ);
            if (readCount == -1) {
                //客户端socket已经关闭,关闭服务器端对应的socketchannel的socket
                ioHandler.onDisconnected(sc);
                sc.socket().close();
            } else {
                ioHandler.onPacketReceived(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                key.cancel();
                sc.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }


}
