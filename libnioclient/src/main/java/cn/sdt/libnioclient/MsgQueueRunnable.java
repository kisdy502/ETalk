package cn.sdt.libnioclient;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.sdt.libniocommon.Packet;

/**
 * Created by Administrator on 2018/2/14.
 */

public class MsgQueueRunnable implements Runnable {

    private ClientManager clientManager;
    private boolean isRunning = true;

    public MsgQueueRunnable(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        try {
            Packet packet=null;
            while (isRunning) {
                packet = clientManager.getMsgQueue().take();
                clientManager.directSend(packet);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
