package cn.sdt.libniocommon;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public interface IMsgType {

    final static int MSG_HEART = 0;
    final static int MSG_CONNECTED = MSG_HEART + 1;
    final static int MSG_DISCONNECTED = MSG_HEART + 2;
    final static int MSG_CONNECTED_FAILED = MSG_HEART + 3;

    final static int MSG_CHAT = MSG_HEART + 10;

    final static int MSG_REGISTERED = MSG_HEART + 20;
    final static int MSG_REGISTER_FAILED = MSG_HEART + 21;

    final static int MSG_LOGINED = MSG_HEART + 30;
    final static int MSG_LOGIN_FAILED = MSG_HEART + 31;
}
