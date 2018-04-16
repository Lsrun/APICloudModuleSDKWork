package com.ysy15350.moduleaotaoreader.util;

import android.content.ContentValues;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.db.BookList;
import com.ysy15350.moduleaotaoreader.model.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;

import common.CommFun;

/**
 * 书本操作类
 */
public class BookUtil {

    private static final String TAG = "BookUtil";
    public static String cachedPath = Environment.getExternalStorageDirectory() + "/treader/";

    // 存储的字符数

    public static final int cachedSize = 30000;

    protected final SparseArray<Cache> myArray = new SparseArray<>();


    private String m_strCharsetName = "utf-8";

    private int mAid;
    private int mCid;
    private String bookPath;

    /**
     * 章节的总长度
     */
    private long bookLen;
    private long page;
    private long begin;
    private long end;
    private long position;


    public BookUtil() {
    }

    public synchronized void cacheBook(BookList bookList) throws IOException {
        // 如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        cacheFileBook(bookList);
    }


    /**
     * 缓存书本
     *
     * @param bookList
     * @throws IOException
     */
    private void cacheFileBook(BookList bookList) throws IOException {

        int aid = bookList.getAid();
        int cid = bookList.getCid();

        int uniqueId = getUniqueId(aid, cid);

        String content = bookList.getContent();

        if (TextUtils.isEmpty(bookList.getCharset())) {

            ContentValues values = new ContentValues();
            values.put("charset", m_strCharsetName);

        } else {
            m_strCharsetName = bookList.getCharset();
        }

        bookLen = 0;

        if (content != null && !content.equals("")) {

            String bufStr = content;

//            bufStr = bufStr.replaceAll("\r\n+\\s*", "\r\n\u3000\u3000");
//            bufStr = bufStr.replaceAll("\u0000", "");

            char[] buf = bufStr.toCharArray();

            ReadActivity.log("转换数据长度-----" + buf.length);

            ReadActivity.log( "转换的buf-------" + bufStr);

            bookLen = buf.length;

            Cache cache = new Cache();
            cache.setNext(bookList.getNext());
            cache.setPrevious(bookList.getPrevious());
            cache.setSize(buf.length);
            cache.setData(new WeakReference<char[]>(buf));

            myArray.put(uniqueId, cache);

            String path = cachedPath + "/" + uniqueId;

            try {
                /// 保存到文件中
                File cacheBook = new File(fileName(uniqueId));
                if (cacheBook.exists())
                    cacheBook.delete();
                if (!cacheBook.exists()) {
                    boolean isCreate = cacheBook.createNewFile(); //此方法返回true，如果指定的文件不存在，并已成功创建。如果该文件存在，该方法返回false。
                }
                final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName(uniqueId)), "UTF-16LE");
                writer.write(buf);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException("Error during writing " + path);
            }
        }
    }


    private BookList bookList;

    public synchronized void openBook(BookList bookList) throws IOException {
        this.bookList = bookList;
        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        if (!CommFun.isNullOrEmpty(bookList.getBookpath())) {

            this.bookPath = bookList.getBookpath();

            cacheBook(bookList);

        }
    }


    public int getUniqueId(int aid, int cid) {
        String uniqueIdStr = aid + "" + cid;
        int uniqueId = CommFun.toInt32(uniqueIdStr, aid + cid);
        return uniqueId;
    }

    protected String fileName(int uniqueId) {
        String fullPath = cachedPath + "/" + uniqueId;


        return fullPath;

    }


    private void cleanCacheFile() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
    }


    private int mUniqueId;

    public void setUniqueId(int aid, int cid) {

        mUniqueId = getUniqueId(aid, cid);
    }

    public int getUniqueId() {
        return mUniqueId;
    }

    public int isCache(int aid, int cid) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nmyArray.size=" + myArray.size());
        stringBuilder.append("\nisCache: " + aid + cid);


        int uniqueId = getUniqueId(aid, cid);

        Cache cacheBook = myArray.get(uniqueId);

        stringBuilder.append("\ncacheBook==null: " + (cacheBook == null));

        char[] charArray = block(uniqueId);

        if (charArray != null && charArray.length != 1) {

            return charArray.length;
        }

        return 0;
    }

    public void startCacheBook(int aid, int cid) {
        mAid = aid;
        mCid = cid;
        mUniqueId = getUniqueId(aid, cid);
        position = 0;
        bookLen = block(mUniqueId).length;
    }

    public long getBookLen(int aid, int cid) {
        int uniqueId = getUniqueId(aid, cid);
        char[] block = block(uniqueId);
        if (block != null) {
            return block.length;
        }
        return 0;
    }

    public long getPosition() {
        return position;
    }

    public void setPostition(long position) {
        this.position = position;
    }

    private int DIRECTION = 0;

    private final int DIRECTION_PRE = 1;
    private final int DIRECTION_NEXT = 2;


    public int next(boolean back) {
        DIRECTION = DIRECTION_NEXT;
        position += 1;
        if (position > bookLen) {
            position = bookLen;
            return -1;
        }

        char result = current();

        if (back) {
            position -= 1;
        }

        return result;
    }



    public char current() {
        int pos = 0;
        char[] charArray = block(mUniqueId);

        if (charArray != null) {
            long size = charArray.length;

            if (size - 1 >= position) {
                pos = (int) position;
            }
        }

        return charArray[pos];
    }


    /**
     * 向前翻页
     *
     * @return
     */
    public char[] preLine() {
        if (position <= 0) {
            return null;
        }
        String line = "";
        while (position >= 0) {
            int word = pre(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\n") && (((char) pre(true)) + "").equals("\r")) {
                pre(false);
                break;
            }
            line = wordChar + line;
        }
        return line.toCharArray();
    }

    public int pre(boolean back) {
        position -= 1;
        if (position < 0) {
            position = 0;
            return -1;
        }
        char result = current();
        if (back) {
            position += 1;
        }
        return result;
    }


    public long getBookLen() {
        bookLen = block(mUniqueId).length;
        return bookLen;
    }

    /**
     * 获取书本缓存
     *
     * @param uniqueId
     * @return
     */
    public char[] block(int uniqueId) {
        if (myArray.size() == 0 || uniqueId == 0) {
            return new char[1];
        }

        Cache cacheBook = myArray.get(uniqueId);

        char[] block = null;

        if (cacheBook != null) {
            block = cacheBook.getData().get();
        }

        if (block == null) {
            try {
                String fullPath = fileName(uniqueId);
                File file = new File(fullPath);

                if (file.exists()) {
                    int size = (int) file.length();
                    if (size < 0) {
                        throw new RuntimeException("Error during reading " + fileName(uniqueId));
                    }

                    block = new char[size / 2];
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-16LE");
                    if (reader.read(block) != block.length) {
                        throw new RuntimeException("Error during reading " + fileName(uniqueId));
                    }
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error during reading " + fileName(uniqueId));
            }
        }

        return block;
    }

}
