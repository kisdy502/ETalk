package cn.sdt.libnioclient;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.sdt.libniocommon.Packet;

/**
 * Created by Administrator on 2018/2/14.
 */

public class HeartRunnable implements Runnable {

    private ClientManager clientManager;
    private boolean isRunning = true;
    private Packet packet;
    private ByteBuffer heartBuff;
    private String data;

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
            data = clientManager.getGson().toJson(packet);
        }
        try {
            while (isRunning) {
                heartBuff = null;
                heartBuff = clientManager.getCharset().encode(data);
                while (heartBuff.hasRemaining()) {
                    clientManager.getSocketChannel().write(heartBuff);
                }
                clientManager.getHandler().sendEmptyMessageDelayed(ClientManager.MSG_TIMEOUT, ClientManager.DELAY_TIME);
                Thread.sleep(ClientManager.DELAY_TIME);//60s一次的心跳
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
