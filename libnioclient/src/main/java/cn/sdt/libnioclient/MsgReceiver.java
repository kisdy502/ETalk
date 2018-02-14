package cn.sdt.libnioclient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import cn.sdt.libniocommon.IMsgType;
import cn.sdt.libniocommon.Packet;

/**
 * 接收到服务器发回来的消息通过本地广播方式发送给主线程
 * Created by SDT13411 on 2018/2/12.
 */

public class MsgReceiver {

    public static final String KEY_TYPE = "type";
    public static final String KEY_BODY = "body";

    /**
     * 自定义广播
     */
    public static final String MSG_ACTION = "cn.sdt.chat.SEND_MSG_ACTION";
    public static final String PACKET_ACTION = "cn.sdt.chat.SEND_PACKET_ACTION";


    public static void boardConnectedMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_CONNECTED, msg);
    }

    public static void boardConnectedFailedMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_CONNECTED_FAILED, msg);
    }


    public static void boardRegisteredMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_REGISTERED, msg);
    }

    public static void boardRegisterFailedMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_REGISTER_FAILED, msg);
    }

    public static void boardLoginedMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_LOGINED, msg);
    }

    public static void boardLoginFailedMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_LOGIN_FAILED, msg);
    }


    public static void boardChatMsg(Context context, Packet packet) {
        boardPacket(context, IMsgType.MSG_CHAT, packet);
    }


    private static void boardMsg(Context context, int type, String msg) {
        Intent intent = new Intent(MSG_ACTION);
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_BODY, msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static void boardPacket(Context context, int type, Packet packet) {
        Intent intent = new Intent(PACKET_ACTION);
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_BODY, packet);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }



}
