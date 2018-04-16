package com.ysy15350.moduleaotaoreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    public static String NAME = "data.db";
    public static int VERSION = 2;

    /**
     * 是否购买的数据库
     */
    public static String DING_TABLE = "user";

    /**
     * 阅读记录缓存的数据库
     */
    public static String CACHE_TABLE = "cache";

    /**
     * 阅读进度
     */
    public static String READ_PROGRESS = "progress";

    /**
     * 获取点击次数的数据库
     */
    public static String GET_COUNT = "get_count";

    private String sql = "CREATE TABLE IF NOT EXISTS " + DING_TABLE + "(id integer PRIMARY KEY, cid integer, aid integer, uid integer)";

    private String cache_sql = "CREATE TABLE IF NOT EXISTS " + CACHE_TABLE + "(id integer PRIMARY KEY, cid integer, aid integer, uid integer)";

    private String progress = "CREATE TABLE IF NOT EXISTS " + READ_PROGRESS + "(id integer PRIMARY KEY, cid integer, aid integer, uid integer, page integer, progress float)";

    private String getCount = "CREATE TABLE IF NOT EXISTS " + GET_COUNT + "(id integer PRIMARY KEY, aid integer, cid integer, count integer)";


    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(cache_sql);
        sqLiteDatabase.execSQL(progress);
        sqLiteDatabase.execSQL(getCount);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(getCount);

    }

}
