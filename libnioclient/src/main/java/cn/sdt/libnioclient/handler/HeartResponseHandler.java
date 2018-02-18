package cn.sdt.libnioclient.handler;

import android.util.Log;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import cn.sdt.libnioclient.ClientManager;
import cn.sdt.libniocommon.Packet;
import cn.sdt.libniocommon.util.DateUtil;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class HeartResponseHandler implements IPacketHandler {


    private final static String TAG = "HeartResponseHandler";

    @Override
    public void handler(ClientManager clientManager, SocketChannel socketChannel, Packet packet) throws IOException {
        Log.d(TAG, "at:" + DateUtil.timestampToDateString(System.currentTimeMillis(),DateUtil.FORMAT_HHMMSS)
                + ",heart from client:" + socketChannel.socket().getRemoteSocketAddress());
        clientManager.getHandler().removeMessages(ClientManager.MSG_TIMEOUT);
    }
}
