package cn.sdt.libnioserver.handler;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;
import cn.sdt.libnioserver.Connection;
import cn.sdt.libnioserver.ServerManager;
import cn.sdt.libnioserver.User;
import cn.sdt.libnioserver.db.TbUserHelper;
import cn.sdt.libnioserver.handler.IPacketHandler;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class RegisterHandler implements IPacketHandler {

    private final static String TAG = "RegisterHandler";

    @Override
    public void handler(ServerManager serverManager, SocketChannel socketChannel, Packet packet) throws IOException {
        if (packet.getpId() == Packet.REGIST_ID) {
            String userName = packet.getValue("userName");
            String password = packet.getValue("password");
            Log.d(TAG, "userName:" + userName);
            Log.d(TAG, "password:" + password);
            TbUserHelper tbUserHelper = new TbUserHelper(serverManager.getContext());
            long i = tbUserHelper.insert(userName, password);
            Log.d(TAG, "注册用户结果:" + i);


            Packet responsePacket = new Packet(Packet.REGIST_RESPONSE_ID);
            if (i > 0) {
                Connection connection = serverManager.getConnectionMap().get(socketChannel.socket().getRemoteSocketAddress().toString());
                if (connection != null) {
                    connection.setUser(new User(userName, password));
                    connection.setLogin(true);
                    connection.setConnectionName(userName);
                }
                responsePacket.add("status", "0");
                responsePacket.add("msg", "register success");
            } else {
                responsePacket.add("status", "1");
                responsePacket.add("msg", "register failed");
            }
            ByteBuffer buffer = serverManager.getCharset().encode(new Gson().toJson(responsePacket));
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }


        }
    }
}
