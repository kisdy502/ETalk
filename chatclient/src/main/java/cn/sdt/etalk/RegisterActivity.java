package cn.sdt.etalk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.sdt.libnioclient.ClientManager;
import cn.sdt.libniocommon.IMsgType;
import cn.sdt.libniocommon.Packet;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    final static String TAG = "regster";
    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPwd);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ClientManager.getInstance().isConnected()) {
            register();
        } else {
            initConn();
        }
    }

    @Override
    protected void parseMsg(int type, String msg) {
        super.parseMsg(type, msg);
        if (type == IMsgType.MSG_CONNECTED) {
            register();
        } else if (type == IMsgType.MSG_REGISTERED) {
            Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
            startActivity(intent);
        } else if (type == IMsgType.MSG_REGISTER_FAILED) {
            Toast.makeText(this, "用户名已经存在，请换一个吧", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        String userName = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        Log.d(TAG, "name:" + userName);
        Log.d(TAG, "pwd:" + password);
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return;
        }
        Packet packet = new Packet(Packet.REGIST_ID);
        packet.add("userName", userName);
        packet.add("password", password);
        ClientManager.getInstance().send(packet);
    }

    private void initConn() {
        ClientManager.getInstance().start("192.168.1.102", 9000);
    }

}
