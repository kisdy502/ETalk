package cn.sdt.libnioserver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import cn.sdt.libniocommon.IMsgType;

/**
 * Created by SDT13411 on 2018/2/12.
 */

public class MsgReceiver {
    public static final String KEY_TYPE = "type";
    public static final String KEY_BODY = "body";

    /**
     * 自定义广播
     */
    public static final String MSG_ACTION = "cn.sdt.chat.SEND_MSG_ACTION";


    public static void boardConnectedMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_CONNECTED, msg);
    }

    public static void boardConnectedFailedMsg(Context context, String msg) {
        boardMsg(context, IMsgType.MSG_CONNECTED_FAILED, msg);
    }

    private static void boardMsg(Context context, int type, String msg) {
        Intent intent = new Intent(MSG_ACTION);
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_BODY, msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
