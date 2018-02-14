package cn.sdt.libnioserver;

import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class ChatHandler implements IPacketHandler {

    @Override
    public void handler(ServerManager serverManager, SocketChannel socketChannel, Packet packet) throws IOException {
        String text = serverManager.getGson().toJson(packet);
        ByteBuffer buffer = serverManager.getCharset().encode(text);
        Log.w("ChatHandler", "socketChannel:" + socketChannel.socket().getRemoteSocketAddress().toString());
        for (Connection connection : serverManager.getConnectionMap().values()) {
            Log.w("ChatHandler", "connection:" + connection.getSocketChannel().socket().getRemoteSocketAddress().toString());
//            if (!socketChannel.socket().getRemoteSocketAddress().toString().equals(connection.getSocketChannel().socket().getRemoteSocketAddress().toString()))
            connection.write(buffer);
        }
    }
}
