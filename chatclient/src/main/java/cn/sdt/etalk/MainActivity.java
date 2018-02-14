package cn.sdt.etalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends BaseActivity {

    final static String TAG = "MainActivity";

    TextView tvIp;
    TextView tvUName;
    TextView tvMsg;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvIp = (TextView) findViewById(R.id.txt_server_ip);
        tvUName = (TextView) findViewById(R.id.txt_username);
        tvMsg = (TextView) findViewById(R.id.txt_msg);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void join(View view) {
        String serverIp = tvIp.getText().toString().trim();
    }

    public void send(View view) {
        final String msg = tvMsg.getText().toString();
    }

    @Override
    protected void parseMsg(int type, String msg) {

    }

}
