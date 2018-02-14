package cn.sdt.libnioserver.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SDT13411 on 2018/2/13.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    /**
     * 这个数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
     */
    private static String name = "etalk.db"; //表示数据库的名称
    private static int version = 1; //表示数据库的版本号

    public DBOpenHelper(Context context) {
        super(context, name, null, version);
        // TODO Auto-generated constructor stub
    }

    //当数据库创建的时候，是第一次被执行，完成对数据库的表的创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(TbUserHelper.getCreateSql());
    }

    /**
     * onUpgrade() 方法是在什么时候被执行呢？
     * 【注意】：这里的删除等操作必须要保证新的版本必须要比旧版本的版本号要大才行。[即 Version 2.0 > Version 1.0 ] 所以这边我们不需要对其进行操作。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "alter table user add sex varchar(8)";
        db.execSQL(sql);
    }

}
