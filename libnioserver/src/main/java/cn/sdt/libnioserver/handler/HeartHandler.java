package cn.sdt.libnioserver.handler;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;
import cn.sdt.libniocommon.util.DateUtil;
import cn.sdt.libnioserver.ServerManager;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class HeartHandler implements IPacketHandler {

    private final static String TAG = "HeartHandler";

    @Override
    public void handler(ServerManager serverManager, SocketChannel socketChannel, Packet packet) throws IOException {
        Log.d(TAG, "at:" + DateUtil.timestampToDateString(System.currentTimeMillis(),DateUtil.FORMAT_MMDDHH)
                + ",heart response:" + socketChannel.socket().getRemoteSocketAddress());
        packet.setpId(Packet.HEART_RESPONSE_ID);
        ByteBuffer buffer = serverManager.getCharset().encode(serverManager.getGson().toJson(packet));
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
