package cn.sdt.libnioserver;

import android.support.v4.util.ArrayMap;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;
import cn.sdt.libnioserver.handler.ChatHandler;
import cn.sdt.libnioserver.handler.HeartHandler;
import cn.sdt.libnioserver.handler.IPacketHandler;
import cn.sdt.libnioserver.handler.LoginHandler;
import cn.sdt.libnioserver.handler.RegisterHandler;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class PacketDispatcher {

    final static String TAG = "PacketDispatcher";

    static ArrayMap<Integer, IPacketHandler> handlerArrayMap = new ArrayMap<>();

    public PacketDispatcher() {
    }

    static {
        handlerArrayMap.put(Packet.HEART_ID, new HeartHandler());
        handlerArrayMap.put(Packet.REGIST_ID, new RegisterHandler());
        handlerArrayMap.put(Packet.LOGIN_ID, new LoginHandler());
        handlerArrayMap.put(Packet.CHAT_ID, new ChatHandler());
    }

    public static void dispatch(ServerManager serverManager, SocketChannel socketChannel,
                                Packet packet) throws IOException {
        int id = packet.getpId();
        IPacketHandler packetHandler = handlerArrayMap.get(id);
        if (packetHandler != null) {
            packetHandler.handler(serverManager, socketChannel, packet);
        } else {
            //消息id错误
        }
    }
}
