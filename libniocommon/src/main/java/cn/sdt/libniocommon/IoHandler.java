package cn.sdt.libniocommon;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public interface IoHandler {
    void onConnected(SocketChannel socketChannel);

    void onConnectFailed(SocketChannel socketChannel);

    void onPacketReceived(SocketChannel socketChannel, ByteBuffer buffer);

    void onPacketSend(SocketChannel socketChannel, ByteBuffer buffer);

    void onDisconnected(SocketChannel client);

    void onException(Exception e);
}
