package cn.sdt.etalk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.sdt.libnioclient.ClientManager;
import cn.sdt.libniocommon.IMsgType;
import cn.sdt.libniocommon.Packet;
import cn.sdt.libniocommon.util.SharePrefUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    final static String TAG = "login";
    private RelativeLayout contentLayout;
    private ProgressBar progressBar;
    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        findViewById(R.id.btnToRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (ClientManager.getInstance().isConnected()) {
            hideProgressbar(false);
            login();
        } else {
            hideProgressbar(false);
            initConn();
        }
    }

    private void hideProgressbar(boolean hide) {
        int v1 = hide ? View.GONE : View.VISIBLE;
        int v2 = hide ? View.VISIBLE : View.GONE;
        progressBar.setVisibility(v1);
        contentLayout.setVisibility(v2);
    }


    @Override
    protected void parseMsg(int type, String msg) {
        super.parseMsg(type, msg);
        hideProgressbar(true);
        if (type == IMsgType.MSG_CONNECTED) {
            login();
        } else if (type == IMsgType.MSG_CONNECTED_FAILED) {
            Toast.makeText(this, "无法连接到服务器，请检查网络是否连接", Toast.LENGTH_SHORT).show();
        } else if (type == IMsgType.MSG_LOGINED) {
            SharePrefUtil.saveUserInfo(getApplicationContext(), edtUserName.getText().toString().trim(),
                    edtPassword.getText().toString().trim());
            Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
            startActivity(intent);
        } else if (type == IMsgType.MSG_LOGIN_FAILED) {
            Toast.makeText(this, "用户名或密码错误，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        String userName = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        Log.d(TAG, "name:" + userName);
        Log.d(TAG, "pwd:" + password);
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return;
        }
        Packet packet = new Packet(Packet.LOGIN_ID);
        packet.add("userName", userName);
        packet.add("password", password);
        ClientManager.getInstance().send(packet);
    }

    private void initConn() {
        ClientManager.getInstance().start("192.168.1.102", 9000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientManager.getInstance().stop();
    }
}
