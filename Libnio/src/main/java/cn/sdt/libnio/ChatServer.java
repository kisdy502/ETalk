package cn.sdt.libnio;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by SDT13411 on 2018/2/9.
 */

public class ChatServer {

    private final static String TAG = "ChatServer";

    private int port;
    Context mContext;

    boolean isRunning = true;
    /**
     * 选择器
     */
    private Selector selector;

    /*******在线统计人名或人数********/
    private ArrayMap<String, String> online = new ArrayMap<>();

    /****编码*****/
    private Charset charset = Charset.forName("UTF-8");

    /****用户存在提示信息*****/
    private static String USER_EXIST = "system message: user exist, please change a name";

    /****相当于自定义协议格式，与客户端协商好*****/
    private static String USER_CONTENT_SPILIT = "#@#";

    public ChatServer(Context context, int port) {
        this.mContext = context;
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
        System.out.println("server is linstening...");
        OldMsgReceiver.boardTipMsg(mContext, "server isStart");
        //等待客户端的连接
        while (isRunning) {
            int nums = this.selector.select();
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
            //接收客户端
            SocketChannel sChannel = server.accept();

            //设置非阻塞
            sChannel.configureBlocking(false);
            //注册选择器，并设置为读取模式，收到一个连接请求，然后起一个SocketChannel，并注册到selector上，之后这个连接的数据，就由这个socketchannel处理。
            sChannel.register(selector, SelectionKey.OP_READ);
            //将此对应的channel设置为准备接收其他客户端的请求
            key.interestOps(SelectionKey.OP_ACCEPT);
            System.out.println("Server is listening from client :" + sChannel.socket().getRemoteSocketAddress());
            OldMsgReceiver.boardTipMsg(mContext, sChannel.socket().getRemoteSocketAddress() + ":连接上了");
        } else if (key.isReadable()) {                //处理来自客户端的数据读取请求
            //得到该key对应的channel，其中有数据需要读取
            int reccount = 0;
            SocketChannel sc = (SocketChannel) key.channel();
            StringBuffer content = new StringBuffer();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            try {
                //得到客户端传过来的消息
                while ((reccount = sc.read(buffer)) > 0) {
                    Log.d(TAG, "reccount:" + reccount);
                    buffer.flip();
                    content.append(charset.decode(buffer));
                }
                //将此对应的channel设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);
            } catch (Exception e) {
                e.printStackTrace();
                key.cancel();
                sc.close();
            }
            if (reccount == -1) {
                String remoteAddress = sc.socket().getRemoteSocketAddress().toString();
                Log.d(TAG, "is closed remoteAddress" + remoteAddress);
                remove(remoteAddress);
                sc.close();
            } else if (content.length() > 0) {   //如果内容不为空
                Log.d(TAG, "client msg:" + content.toString());
                //拆分规则
                String[] msgArr = content.toString().split(USER_CONTENT_SPILIT);
                //注册名字
                if (msgArr != null && msgArr.length == 1) {
                    //用户已经存在，则直接返回
                    if (online.containsKey(msgArr[0])) {
                        sc.write(charset.encode(USER_EXIST));
                    } else {
                        String name = msgArr[0];
                        online.put(name, sc.socket().getRemoteSocketAddress().toString());
                        OldMsgReceiver.boardLoginMsg(mContext, name);
                        int onlineNum = this.onlineTotal();
                        sc.write(charset.encode("logined"));
                        String msg = "welcome " + name + " to chat room,current online people num is:" + onlineNum;
                        OldMsgReceiver.boardTipMsg(mContext, msg);
                        key.interestOps(SelectionKey.OP_READ);
                        //通知所有的人
                        broadCast(selector, null, msg);
                    }
                } else if (msgArr != null && msgArr.length > 1) {  //聊天内容
                    String name = msgArr[0];
                    String message = content.substring(name.length() + USER_CONTENT_SPILIT.length());
                    message = name + " say " + message;
                    OldMsgReceiver.boardChatMsg(mContext, message);
                    if (online.containsKey(name)) {
                        //不回发给发送此内容的客户端
                        broadCast(selector, sc, message);
                    }
                }

            }
        }
    }


    /**
     * 通知所有人
     *
     * @param selector 选择器
     * @param except   不通知的客户端
     * @param msg      消息
     * @throws IOException
     * @author 1
     */
    private void broadCast(Selector selector, SocketChannel except, String msg) throws IOException {

        for (SelectionKey key : selector.keys()) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && channel != except) {
                SocketChannel socketChannel = (SocketChannel) channel;
                socketChannel.write(charset.encode(msg));
            }
        }

    }


    /**
     * 得到在线总人数
     *
     * @return
     * @author 1
     */
    private int onlineTotal() {
        int num = 0;
        for (SelectionKey key : this.selector.keys()) {
            Channel targetchannel = key.channel();
            if (targetchannel instanceof SocketChannel) {
                num++;
            }
        }
        return num;
    }


    private void remove(String remoteAddress) {
        int index = -1;
        String name = null;
        int size = online.values().size();
        for (int i = 0; i < size; i++) {
            if (remoteAddress.equals(online.valueAt(i))) {
                name = online.keyAt(i);
                index = i;
                break;
            }
        }
        if (index != -1) {
            online.removeAt(index);
            OldMsgReceiver.boardDisconnectedMsg(mContext, name);
        }
    }
}
