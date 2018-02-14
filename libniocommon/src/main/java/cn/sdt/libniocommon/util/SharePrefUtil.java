package cn.sdt.libniocommon.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/2/13.
 */

public class SharePrefUtil {
    private final static String PREF_NAME = "user.config";

    public static void saveUserInfo(Context context, String userName, String password) {
        //指定操作的文件名称
        SharedPreferences share = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit(); //编辑文件
        edit.putString("userName", userName);         //根据键值对添加数据
        edit.putString("password", password);
        edit.commit();  //保存数据信息
    }

    public static String getUserName(Context context) {
        SharedPreferences share = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return share.getString("userName", "");
    }

    public static String getPassword(Context context) {
        SharedPreferences share = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return share.getString("password", "");
    }

}
