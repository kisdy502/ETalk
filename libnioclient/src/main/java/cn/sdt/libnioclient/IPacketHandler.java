package cn.sdt.libnioclient;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public interface IPacketHandler {
    void handler(ClientManager clientManager, SocketChannel socketChannel, Packet packet) throws IOException;
}
