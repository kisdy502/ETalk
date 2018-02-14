package cn.sdt.libnio;

import java.nio.Buffer;
import java.nio.channels.SocketChannel;
import cn.sdt.NIOConnection;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public interface IoHandler {
    void onConnected(NIOConnection nioConn);

    void onConnectFailed(NIOConnection nioConn);

    void onPacketReceived(Buffer buffer);

    void onPacketSend(Buffer buffer);

    void onDisconnected(SocketChannel client);
}
