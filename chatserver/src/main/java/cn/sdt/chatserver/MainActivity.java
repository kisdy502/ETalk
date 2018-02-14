package cn.sdt.chatserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;



public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private TextView tvServerIp;
    private RecyclerView recyclerViewMsgList;
    private RecyclerView recyclerViewUserList;

    List<String> msgList;
    List<String> userList;

    MsgAdapter msgAdapter;
    UserAdapter userAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }




    private class MsgBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }


}
