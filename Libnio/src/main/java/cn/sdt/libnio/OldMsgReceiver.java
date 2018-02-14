package cn.sdt.libnio;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import cn.sdt.libnio.packet.OldPacket;
import cn.sdt.libnio.packet.PacketType;

/**
 * Created by SDT13411 on 2018/2/9.
 */
public class OldMsgReceiver {
    /**
     * 自定义广播
     */
    public static final String MSG_ACTION = "cn.sdt.chat.SEND_MSG_ACTION";
    /**
     * 自定义广播
     */
    public static final String PACKET_ACTION = "cn.sdt.chat.SEND_PACKET_ACTION";


    public static void boardConnectedMsg(Context context, String msg) {
        boardMsg(context, PacketType.MSG_CONNECTED, msg);
    }

    public static void boardDisconnectedMsg(Context context, String msg) {
        boardMsg(context, PacketType.MSG_DISCONNECTED, msg);
    }

    public static void boardLoginMsg(Context context, String msg) {
        boardMsg(context, PacketType.MSG_LOGIN, msg);
    }

    public static void boardLoginFailedMsg(Context context, String msg) {
        boardMsg(context, PacketType.MSG_LOGIN_FAILED, msg);
    }

    public static void boardChatMsg(Context context, String msg) {
        boardMsg(context, PacketType.MSG_CHAT, msg);
    }

    public static void boardTipMsg(Context context, String msg) {
        boardMsg(context, PacketType.MSG_TIP, msg);
    }

    public static void boardConnectedFailedMsg(Context context, String msg) {
        boardMsg(context, PacketType.MSG_CONNECTED_FAILED, msg);
    }


    private static void boardMsg(Context context, int type, String msg) {
        Intent intent = new Intent(MSG_ACTION);
        intent.putExtra("type", type);
        intent.putExtra("body", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static void boardPacket(Context context, int type, OldPacket packet) {
        Intent intent = new Intent(PACKET_ACTION);
        intent.putExtra("type", type);
        intent.putExtra("body", packet);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
