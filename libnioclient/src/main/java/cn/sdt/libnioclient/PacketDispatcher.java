package cn.sdt.libnioclient;

import android.support.v4.util.ArrayMap;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class PacketDispatcher {

    final static String TAG = "PacketDispatcher";

    static ArrayMap<Integer, IPacketHandler> handlerArrayMap = new ArrayMap<>();

    public PacketDispatcher() {

    }

    static {
        handlerArrayMap.put(Packet.HEART_RESPONSE_ID, new HeartHandler());
        handlerArrayMap.put(Packet.REGIST_RESPONSE_ID, new RegisterResponseHandler());
        handlerArrayMap.put(Packet.LOGIN_RESPONSE_ID, new LoginResponseHandler());
        handlerArrayMap.put(Packet.CHAT_ID, new ChatComingHandler());
    }

    public static void dispatch(ClientManager clientManager, SocketChannel socketChannel,
                                Packet packet) throws IOException {
        int id = packet.getpId();
        IPacketHandler packetHandler = handlerArrayMap.get(id);
        packetHandler.handler(clientManager, socketChannel, packet);
    }
}
