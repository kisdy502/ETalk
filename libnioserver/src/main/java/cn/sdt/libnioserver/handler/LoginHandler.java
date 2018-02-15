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

public class LoginHandler implements IPacketHandler {

    private final static String TAG = "RegisterHandler";

    @Override
    public void handler(ServerManager serverManager, SocketChannel socketChannel, Packet packet) throws IOException {
        if (packet.getpId() == Packet.LOGIN_ID) {
            String userName = packet.getValue("userName");
            String password = packet.getValue("password");
            Log.d(TAG, "userName:" + userName);
            Log.d(TAG, "password:" + password);
            TbUserHelper tbUserHelper = new TbUserHelper(serverManager.getContext());
            boolean exist = tbUserHelper.isExist(userName, password);
            Log.d(TAG, "查询用户是否存在:" + exist);


            Packet responsePacket = new Packet(Packet.LOGIN_RESPONSE_ID);
            if (exist) {
                Connection connection = serverManager.getConnectionMap().get(socketChannel.socket().getRemoteSocketAddress().toString());
                if (connection != null) {
                    connection.setUser(new User(userName, password));
                    connection.setLogin(true);
                    connection.setConnectionName(userName);
                }
                responsePacket.add("status", "0");
                responsePacket.add("msg", "login success");
            } else {
                responsePacket.add("status", "1");
                responsePacket.add("msg", "login failed");
            }
            ByteBuffer buffer = serverManager.getCharset().encode(new Gson().toJson(responsePacket));
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }


        }
    }
}
