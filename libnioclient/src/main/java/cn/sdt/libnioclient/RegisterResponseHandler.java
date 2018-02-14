package cn.sdt.libnioclient;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import cn.sdt.libniocommon.Packet;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class RegisterResponseHandler implements IPacketHandler {

    @Override
    public void handler(ClientManager clientManager, SocketChannel socketChannel, Packet packet) throws IOException {
        if (packet.getpId() == Packet.REGIST_RESPONSE_ID) {
            String status = packet.getValue("status");
            String msg = packet.getValue("msg");
            if ("0".equals(status)) {
                MsgReceiver.boardRegisteredMsg(clientManager.getContext(),
                        msg);
            } else {
                MsgReceiver.boardRegisterFailedMsg(clientManager.getContext(),
                        msg);
            }
        }
    }
}
