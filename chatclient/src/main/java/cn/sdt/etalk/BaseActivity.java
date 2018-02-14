package cn.sdt.etalk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import cn.sdt.libnioclient.MsgReceiver;
import cn.sdt.libniocommon.Packet;

public class BaseActivity extends Activity {

    private final static String TAG = "BaseActivity";
    private MsgBroadcastReceiver mReceiver = new MsgBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initBroadCast();

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MsgReceiver.MSG_ACTION);
        intentFilter.addAction(MsgReceiver.PACKET_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    protected void parseMsg(int type, String msg) {

    }

    protected void parsePacket(int type, Packet packet) {

    }

    private class MsgBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MsgReceiver.MSG_ACTION)) {
                String msg = intent.getStringExtra("body");
                int type = intent.getIntExtra("type", -1);
                Log.d(TAG, "type:" + type);
                Log.d(TAG, "msg:" + msg);
                parseMsg(type, msg);
            } else if (intent.getAction().equals(MsgReceiver.PACKET_ACTION)) {
                Packet packet = intent.getParcelableExtra("body");
                int type = intent.getIntExtra("type", -1);
                parsePacket(type, packet);
            }
        }
    }
}
