package cn.sdt.libnioclient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.sdt.libniocommon.Packet;

/**
 * Created by Administrator on 2018/2/14.
 */

public class HeartRunnable implements Runnable {

    private ClientManager clientManager;
    private boolean isRunning = true;
    private Packet packet;
    private ByteBuffer heartBuff;

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public HeartRunnable(ClientManager clientManager) {
        this.clientManager = clientManager;
        packet = new Packet(Packet.HEART_ID);

    }

    @Override
    public void run() {
        if (clientManager.isConnected()) {
            heartBuff = ByteBuffer.allocate(16);
            String data = clientManager.getGson().toJson(packet);
            heartBuff = clientManager.getCharset().encode(data);
        }
        try {
            while (isRunning) {
                clientManager.getSocketChannel().write(heartBuff);
                Thread.sleep(1000 * 60 * 2);//2分钟一次的心跳
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
