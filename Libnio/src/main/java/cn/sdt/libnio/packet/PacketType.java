package cn.sdt.libnio.packet;

/**
 * Created by SDT13411 on 2018/2/11.
 */

public interface PacketType {

    final static int MSG_HEART = 0;
    final static int MSG_CONNECTED = MSG_HEART + 1;
    final static int MSG_DISCONNECTED = MSG_HEART + 2;
    final static int MSG_CONNECTED_FAILED = MSG_HEART + 3;


    final static int MSG_CHAT = MSG_HEART + 10;

    final static int MSG_LOGIN = MSG_HEART + 11;
    final static int MSG_LOGIN_FAILED = MSG_HEART + 12;

    final static int MSG_TIP = MSG_HEART + 13;
    final static int MSG_TIP_FAILED = MSG_HEART + 14;
}
