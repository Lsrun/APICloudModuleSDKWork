package com.ysy15350.moduleaotaoreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DbUtil {
    private static DbHelper dbHelper;
    private static DbUtil dbUtil;
    private SQLiteDatabase sqLiteDatabase;


    public static DbUtil getInstence(Context context) {
        if (dbUtil == null) {
            dbUtil = new DbUtil(context);
        }

        return dbUtil;

    }


    private DbUtil(Context context) {
        dbHelper = new DbHelper(context);

        sqLiteDatabase = dbHelper.getReadableDatabase();

    }


    /**
     * 保存自动订阅的数据 章节
     *
     * @param cid 章节编号
     * @param aid 书籍编号
     * @param uid 用户编号
     */
    public void save(int cid, int aid, int uid) {
        if (!isHas(cid, aid, uid)) {
            ContentValues values = new ContentValues();
            values.put("aid", aid);
            values.put("cid", cid);
            values.put("uid", uid);
            sqLiteDatabase.insert(DbHelper.DING_TABLE, null, values);
        }

    }


    /**
     * 本地该章节是否购买
     */
    public boolean isHas(int cid, int aid, int uid) {
        Cursor cursor = sqLiteDatabase.query(DbHelper.DING_TABLE, null, "cid=? and aid=? and uid=?", new String[]{String.valueOf(cid), String.valueOf(aid), String.valueOf(uid)}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


    /**
     * 保存 修改缓存的章节id， 该书籍的章节存在修改， 不存在添加
     *
     * @param cid 章节id
     * @param aid 书籍id
     * @param uid 用户id
     */
    public void saveCache(int cid, int aid, int uid) {

        if (isHasCache(aid, uid)) {
            // 存在数据
            ContentValues values = new ContentValues();
            values.put("cid", cid);
            sqLiteDatabase.update(DbHelper.CACHE_TABLE, values, "uid=? and aid=?", new String[]{String.valueOf(uid), String.valueOf(aid)});

        } else {
            ContentValues values = new ContentValues();
            values.put("aid", aid);
            values.put("cid", cid);
            values.put("uid", uid);
            sqLiteDatabase.insert(DbHelper.CACHE_TABLE, null, values);
        }
    }


    private boolean isHasCache(int aid, int uid) {
        Cursor cursor = sqLiteDatabase.query(DbHelper.CACHE_TABLE, null, "aid=? and uid=?",
                new String[]{String.valueOf(aid), String.valueOf(uid)}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


    /**
     * 获取缓存的书籍的章节
     *
     * @param uid 用户信息
     * @param aid 书籍id
     * @return 章节id
     */
    public int get(int uid, int aid) {
        Cursor cursor = sqLiteDatabase.query(DbHelper.CACHE_TABLE, null, "aid=? and uid=?", new String[]{String.valueOf(aid), String.valueOf(uid)}, null, null, null);
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex("cid"));
            cursor.close();
            return cid;
        }
        cursor.close();
        return 0;
    }


    public void saveProgress(int mUid, int mAid, int mCid, int mPage, float mProgress) {
        ContentValues values = new ContentValues();
        if (isProGressHas(mUid, mAid, mCid)) {
            values.put("page", mPage);
            values.put("progress", mProgress);
            sqLiteDatabase.update(DbHelper.READ_PROGRESS, values, "uid=? and aid=? and cid=?", new String[]{String.valueOf(mUid), String.valueOf(mAid), String.valueOf(mCid)});

        } else {
            values.put("uid", mUid);
            values.put("aid", mAid);
            values.put("cid", mCid);
            values.put("page", mPage);
            values.put("progress", mProgress);
            sqLiteDatabase.insert(DbHelper.READ_PROGRESS, null, values);
        }

    }


    public float getProgress(int uid, int aid, int cid) {
        Cursor cursor = sqLiteDatabase.query(DbHelper.READ_PROGRESS, null, "uid=? and aid=? and cid=?",
                new String[]{String.valueOf(uid), String.valueOf(aid), String.valueOf(cid)}, null, null, null);
        while (cursor.moveToNext()) {
            float progress = cursor.getFloat(cursor.getColumnIndex("progress"));

            cursor.close();
            return progress;
        }
        cursor.close();
        return 0;
    }

    /**
     * 是否存在数据
     */
    private boolean isProGressHas(int mUid, int mAid, int mCid) {
        Cursor cursor = sqLiteDatabase.query(DbHelper.READ_PROGRESS, null, "uid=? and aid=? and cid=?",
                new String[]{String.valueOf(mUid), String.valueOf(mAid), String.valueOf(mCid)}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


    /**
     * 获取统计次数
     */
    public int getCountByAid(String aid, String cid) {
        Cursor cursor = sqLiteDatabase.query(DbHelper.GET_COUNT, null, "aid=? and cid=?", new String[]{aid, cid}, null, null, null);

        int count = 0;

        if (cursor != null) {
            if (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }
            cursor.close();
        }

        return count;

    }


    /**
     * 清除次数
     */
    public void clearCount(String aid, String cid) {
        ContentValues values = new ContentValues();
        values.put("count", 0);
        sqLiteDatabase.update(DbHelper.GET_COUNT, values, "aid=? and cid=?", new String[]{aid, cid});

    }


    /**
     * 添加数据
     */
    public void addCount(String aid, String cid) {
        int count = getCountByAid(aid, cid);

        count++;
        ContentValues values = new ContentValues();
        values.put("count", count);

        if (isCountHas(aid, cid)) {

            /// 存在就修改数据信息
            sqLiteDatabase.update(DbHelper.GET_COUNT, values, "aid=? and cid=?", new String[]{aid, cid});

        } else {

            values.put("aid", aid);
            values.put("cid", cid);
            sqLiteDatabase.insert(DbHelper.GET_COUNT, null, values);

        }

    }


    private boolean isCountHas(String aid, String cid) {
        Cursor cursor = sqLiteDatabase.query(DbHelper.GET_COUNT, null, "aid=? and cid=?", new String[]{aid, cid}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;

    }


    public List<Info> getAllCount() {
        List<Info> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DbHelper.GET_COUNT, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String aid = cursor.getString(cursor.getColumnIndex("aid"));
            String cid = cursor.getString(cursor.getColumnIndex("cid"));
            Info info = new Info();
            info.setAid(aid);
            info.setCid(cid);
            info.setCount(count);

            list.add(info);

        }

        cursor.close();
        return list;

    }


    public void clearAllCount() {
        sqLiteDatabase.delete(DbHelper.GET_COUNT, null, null);
    }


    public class Info {
        private String aid;
        private String cid;
        private int count;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
