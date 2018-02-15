package cn.sdt.libnioclient.handler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import cn.sdt.libnioclient.ClientManager;
import cn.sdt.libnioclient.MsgReceiver;
import cn.sdt.libnioclient.handler.IPacketHandler;
import cn.sdt.libniocommon.Packet;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class LoginResponseHandler implements IPacketHandler {

    @Override
    public void handler(ClientManager clientManager, SocketChannel socketChannel, Packet packet) throws IOException {
        if (packet.getpId() == Packet.LOGIN_RESPONSE_ID) {
            String status = packet.getValue("status");
            String msg = packet.getValue("msg");
            if ("0".equals(status)) {
                MsgReceiver.boardLoginedMsg(clientManager.getContext(),
                        msg);
            } else {
                MsgReceiver.boardLoginFailedMsg(clientManager.getContext(),
                        msg);
            }
        }
    }
}
