package cn.sdt.libnioserver.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.sdt.libnioserver.User;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class TbUserHelper {

    public final static String TB_NAME = "user";
    public final static String CL_USERNAME = "userName";
    public final static String CL_PASSWORD = "password";
    static StringBuffer stringBuffer = new StringBuffer();

    static {
        stringBuffer.append("create table ").append(TB_NAME)
                .append("(id integer primary key autoincrement,")
                .append(CL_USERNAME).append(" varchar(64) UNIQUE,")
                .append(CL_PASSWORD).append(" varchar(64))");
    }

    public static String getCreateSql() {
        String sql = stringBuffer.toString();
        Log.i("sql", "sql:::" + sql);
        return sql;
    }

    public final String sql = "create table user(id integer primary key autoincrement,userName varchar(64) UNIQUE,password varchar(64))";

    SQLiteDatabase sqLiteDatabase;
    Context mContext;

    public TbUserHelper(Context context) {
        this.mContext = context;
    }

    public void openDb() {
        sqLiteDatabase = new DBOpenHelper(mContext).getWritableDatabase();
    }

    public void closeDb() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }


    public long insert(String userName, String password) {
        openDb();
        ContentValues values = new ContentValues();
        values.put(CL_USERNAME, userName);
        values.put(CL_PASSWORD, password);
        long i = sqLiteDatabase.insert(TB_NAME, null, values);
        closeDb();
        return i;
    }

    public long insert(User user) {
        return insert(user.getUserName(), user.getPassword());
    }


    public User query(String userName) {
        openDb();
        Cursor cursor = null;
        User user = null;
        try {
            cursor = sqLiteDatabase.query(TB_NAME, null, CL_USERNAME + "='?'",
                    new String[]{userName}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int i2 = cursor.getColumnIndex(CL_PASSWORD);
                    String password = cursor.getString(i2);
                    user = new User(userName, password);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            closeDb();
        }
        return user;
    }


    public boolean isExist(String userName, String password) {
        openDb();
        Cursor cursor = null;
        User user = null;
        StringBuffer sbWhere = new StringBuffer();
        sbWhere.append(CL_USERNAME).append("=? and ").append(CL_PASSWORD).append(" = ?");
        String strWhere = sbWhere.toString();
        Log.d("test", strWhere);
        try {
            cursor = sqLiteDatabase.query(TB_NAME, null,
                    strWhere,
                    new String[]{
                            userName, password
                    }, null, null, null);
            if (cursor != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            closeDb();
        }
        return false;
    }

}
