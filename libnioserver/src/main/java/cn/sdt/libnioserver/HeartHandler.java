package cn.sdt.libnioserver;

import android.util.Log;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;
import cn.sdt.libniocommon.util.DateUtil;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class HeartHandler implements IPacketHandler {

    private final static String TAG = "HeartHandler";

    @Override
    public void handler(ServerManager serverManager, SocketChannel socketChannel, Packet packet) throws IOException {
        Log.d(TAG, "at:" + DateUtil.timestampToDate(System.currentTimeMillis())
                + ",heart from client:" + socketChannel.socket().getRemoteSocketAddress());
    }
}
