package cn.sdt.etalk;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.List;

import cn.sdt.libnioclient.ClientManager;
import cn.sdt.libniocommon.IMsgType;
import cn.sdt.libniocommon.Packet;
import cn.sdt.libniocommon.util.SharePrefUtil;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    EditText edtMsg;
    Button btnMsg;

    private String userName;

    List<ChatMsg> msgList = new LinkedList<>();
    MsgAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRecyclerView = (RecyclerView) findViewById(R.id.msg_list);
        edtMsg = (EditText) findViewById(R.id.edt_send);
        btnMsg = (Button) findViewById(R.id.btn_send);
        btnMsg.setOnClickListener(this);
        userName = SharePrefUtil.getUserName(getApplicationContext());
        init();
    }


    void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(this, msgList);
        msgAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(msgAdapter);
    }


    @Override
    protected void parseMsg(int type, String msg) {
        super.parseMsg(type, msg);

    }

    @Override
    protected void parsePacket(int type, Packet packet) {
        super.parsePacket(type, packet);
        if (type == IMsgType.MSG_CHAT) {
            String userName = packet.getValue("userName");
            String msgBody = packet.getValue("body");
            ChatMsg msg = new ChatMsg(userName, false, msgBody);
            msgList.add(msg);
            msgAdapter.notifyItemRangeInserted(msgList.size(), 1);

        }
    }

    @Override
    public void onClick(View v) {
        String text = edtMsg.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            Packet packet = new Packet(Packet.CHAT_ID);
            packet.add("userName", userName);
            packet.add("body", text);
            ClientManager.getInstance().send(packet);
            ChatMsg msg = new ChatMsg(userName, true, text);
            msgList.add(msg);
            msgAdapter.notifyItemRangeInserted(msgList.size(), 1);
            edtMsg.setText("");
        }
    }
}
