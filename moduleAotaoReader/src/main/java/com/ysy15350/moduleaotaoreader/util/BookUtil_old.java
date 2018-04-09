package com.ysy15350.moduleaotaoreader.util;

import android.content.ContentValues;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.ysy15350.moduleaotaoreader.db.BookCatalogue;
import com.ysy15350.moduleaotaoreader.db.BookList;
import com.ysy15350.moduleaotaoreader.model.Cache;
import com.ysy15350.moduleaotaoreader.model.ChapterInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class BookUtil_old {

    private static final String TAG = "BookUtil";

    private static final String cachedPath = Environment.getExternalStorageDirectory() + "/treader/";// path:/storage/emulated/0/treader/null0
    //存储的字符数
    public static final int cachedSize = 30000;
//    protected final ArrayList<WeakReference<char[]>> myArray = new ArrayList<>();

    protected final ArrayList<Cache> myArray = new ArrayList<>();
    //目录
    private List<BookCatalogue> directoryList = new ArrayList<>();

    /**
     * 章节列表
     */
    private List<ChapterInfo> chapterInfoList = new ArrayList<>();

    private String m_strCharsetName = "utf-8";
    private String bookName;
    private int mAid;
    private int mCid;
    private String bookPath;
    private long bookLen;
    private long position;
    private BookList bookList;

    public BookUtil_old() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public synchronized void openBook(BookList bookList) throws IOException {
        this.bookList = bookList;
        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        if (bookPath == null || !bookPath.equals(bookList.getBookpath())) {
            cleanCacheFile();
            this.bookPath = bookList.getBookpath();
            bookName = bookList.getBookname();
            mAid = bookList.getAid();
            mCid = bookList.getCid();
            //cacheBook(bookList.getContent());
            cacheBook(bookList);
        }
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

    public char[] nextLine() {
        if (position >= bookLen) {
            return null;
        }
        String line = "";
        while (position < bookLen) {
            int word = next(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\r") && (((char) next(true)) + "").equals("\n")) {
                next(false);
                break;
            }
            line += wordChar;
        }
        return line.toCharArray();
    }

    public char[] preLine() {
        DIRECTION = DIRECTION_PRE;
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
//                line = "\r\n" + line;
                break;
            }
            line = wordChar + line;
        }
        return line.toCharArray();
    }

    public char current() {
//        int pos = (int) (position % cachedSize);
//        int cachePos = (int) (position / cachedSize);
        int cachePos = 0;
        int pos = 0;
        int len = 0;


        for (int i = 0; i < myArray.size(); i++) {

            Cache cache = myArray.get(i);

            long size = myArray.get(i).getSize();

            if (size + len - 1 >= position) {
                if (DIRECTION == DIRECTION_NEXT) {//如果是下一页，当前章节已经显示完，读取下一章内容
                    cachePos = cache.getCid();//i;
                    pos = 0;
                }

                pos = (int) (position - len);
                break;
            }

            len += size;
        }

        char[] charArray = block(cachePos);

        Log.d(TAG, "current() called****************************myArray.size()=" + myArray.size() + ",position=" + position + "DIRECTION=" + DIRECTION);

        return charArray[pos];
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

    public long getPosition() {
        return position;
    }

    public void setPostition(long position) {
        this.position = position;
    }

    //缓存书本
    private void cacheBook(BookList bookList) throws IOException {

        int cid = bookList.getCid();
        String content = bookList.getContent();

        Log.d(TAG + 12345678, "cacheBook: start" + content.length());

        if (TextUtils.isEmpty(bookList.getCharset())) {

            ContentValues values = new ContentValues();
            values.put("charset", m_strCharsetName);
        } else {
            m_strCharsetName = bookList.getCharset();
        }

        //File file = new File(bookPath);
        //InputStreamReader reader = new InputStreamReader(new FileInputStream(file), m_strCharsetName);
        //int index = 0;
        bookLen = 0;
        directoryList.clear();
        myArray.clear();

        char[] buf = new char[cachedSize];

        if (content != null && !"".equals(content)) {

            String bufStr = content;
//            bufStr = bufStr.replaceAll("\r\n","\r\n\u3000\u3000");
//            bufStr = bufStr.replaceAll("\u3000\u3000+[ ]*","\u3000\u3000");
            bufStr = bufStr.replaceAll("\r\n+\\s*", "\r\n\u3000\u3000");
//            bufStr = bufStr.replaceAll("\r\n[ {0,}]","\r\n\u3000\u3000");
//            bufStr = bufStr.replaceAll(" ","");
            bufStr = bufStr.replaceAll("\u0000", "");
            buf = bufStr.toCharArray();
            bookLen += buf.length;

            Cache cache = new Cache();
            cache.setSize(buf.length);
            cache.setData(new WeakReference<char[]>(buf));

//            bookLen += result;
            myArray.add(cache);

            char[] buf1 = new char[cachedSize];

            buf1 = "dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间dfklsdj是登录开发及卡了时代峻峰看了撒地方老框架克里斯多夫就离开时间到了开发及水电费极乐空间间".toCharArray();

            bookLen += buf1.length;

            Cache cache1 = new Cache();
            cache1.setSize(buf.length);
            cache1.setData(new WeakReference<char[]>(buf1));

            myArray.add(cache1);

//            myArray.add(new WeakReference<char[]>(buf));
//            myArray.set(index,);
            try {
                //模拟器：/storage/emulated/0/treader/null0
                //真机:/storage/emulated/0/treader/16910
                String path = fileName(cid);

                Log.d(TAG, "cacheBook() called with: content = [" + content + "]");
                Log.d(TAG, "cacheBook: ");
                File cacheBook = new File(path);
                Log.d(TAG + 12345678, "cacheBook: path=" + path);
                Log.d(TAG + 12345678, "cacheBook: cacheBook.exists():" + cacheBook.exists());
                if (!cacheBook.exists()) {
                    boolean isCreate = cacheBook.createNewFile();//此方法返回true，如果指定的文件不存在，并已成功创建。如果该文件存在，该方法返回false。
                    Log.d(TAG + 12345678, "cacheBook: isCreate:" + isCreate);
                }

                Log.d(TAG + 12345678, "cacheBook: cacheBook.exists():" + cacheBook.exists());

                Log.d(TAG, "cacheBook: ,cacheBook.exists()=" + cacheBook.exists());
                Log.d(TAG, "cacheBook() called with: content = [" + content + "]");
                final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName(cid)), "UTF-16LE");
                writer.write(buf);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException("Error during writing " + fileName(cid));
            }

        }

        new Thread() {
            @Override
            public void run() {
                getChapter();
            }
        }.start();
    }

    //获取章节
    public synchronized void getChapter() {
        try {
            long size = 0;
            for (int i = 0; i < myArray.size(); i++) {
                char[] buf = block(i);
                String bufStr = new String(buf);
                String[] paragraphs = bufStr.split("\r\n");
                for (String str : paragraphs) {
                    if (str.length() <= 30 && (str.matches(".*第.*章.*") || str.matches(".*第.*节.*"))) {
                        BookCatalogue bookCatalogue = new BookCatalogue();
                        bookCatalogue.setBookCatalogueStartPos(size);
                        bookCatalogue.setBookCatalogue(str);
                        bookCatalogue.setBookpath(bookPath);
                        directoryList.add(bookCatalogue);
                    }
                    if (str.contains("\u3000\u3000")) {
                        size += str.length() + 2;
                    } else if (str.contains("\u3000")) {
                        size += str.length() + 1;
                    } else {
                        size += str.length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BookCatalogue> getDirectoryList() {
        if (directoryList != null) {
            Log.d(TAG, "getDirectoryList() called" + directoryList.size());
        }
        return directoryList;
    }

    public long getBookLen() {
        return bookLen;
    }

    protected String fileName(int index) {
        return cachedPath + mAid + index;
    }

    //获取书本缓存
    public char[] block(int index) {
        if (myArray.size() == 0) {
            return new char[1];
        }
        char[] block = myArray.get(index).getData().get();
        if (block == null) {
            try {
                File file = new File(fileName(index));
                if (file.exists()) {
                    int size = (int) file.length();
                    if (size < 0) {
                        throw new RuntimeException("Error during reading " + fileName(index));
                    }
                    block = new char[size / 2];
                    InputStreamReader reader =
                            new InputStreamReader(
                                    new FileInputStream(file),
                                    "UTF-16LE"
                            );
                    if (reader.read(block) != block.length) {
                        throw new RuntimeException("Error during reading " + fileName(index));
                    }
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error during reading " + fileName(index));
            }
            Cache cache = myArray.get(index);
            cache.setData(new WeakReference<char[]>(block));
//            myArray.set(index, new WeakReference<char[]>(block));
        }
        return block;
    }

}
