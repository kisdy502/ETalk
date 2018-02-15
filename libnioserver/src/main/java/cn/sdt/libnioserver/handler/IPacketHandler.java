package cn.sdt.libnioserver.handler;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import cn.sdt.libniocommon.Packet;
import cn.sdt.libnioserver.ServerManager;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public interface IPacketHandler {
    void handler(ServerManager serverManager, SocketChannel socketChannel, Packet packet) throws IOException;
}
