package com.ysy15350.moduleaotaoreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ysy15350.moduleaotaoreader.db.BookList;
import com.ysy15350.moduleaotaoreader.db.DbUtil;
import com.ysy15350.moduleaotaoreader.dialog.DownloadBookDialog;
import com.ysy15350.moduleaotaoreader.dialog.PageModeDialog;
import com.ysy15350.moduleaotaoreader.dialog.PayGiftDialog;
import com.ysy15350.moduleaotaoreader.dialog.RechargeDialog;
import com.ysy15350.moduleaotaoreader.dialog.SettingDialog;
import com.ysy15350.moduleaotaoreader.fragment.MainTab1Fragment;
import com.ysy15350.moduleaotaoreader.fragment.MainTab2Fragment;
import com.ysy15350.moduleaotaoreader.fragment.MainTab3Fragment;
import com.ysy15350.moduleaotaoreader.fragment.MainTab4Fragment;
import com.ysy15350.moduleaotaoreader.model.BookMark;
import com.ysy15350.moduleaotaoreader.model.ChapterBlockInfo;
import com.ysy15350.moduleaotaoreader.model.ChapterBlockModel;
import com.ysy15350.moduleaotaoreader.model.ChapterInfo;
import com.ysy15350.moduleaotaoreader.model.CommentInfo;
import com.ysy15350.moduleaotaoreader.model.CommentModel;
import com.ysy15350.moduleaotaoreader.model.DownloadBookModel;
import com.ysy15350.moduleaotaoreader.model.FlagInfo;
import com.ysy15350.moduleaotaoreader.model.GiftInfo;
import com.ysy15350.moduleaotaoreader.model.ParagraphInfo;
import com.ysy15350.moduleaotaoreader.model.ReadCache;
import com.ysy15350.moduleaotaoreader.model.ResponseBook;
import com.ysy15350.moduleaotaoreader.model.ViewCount;
import com.ysy15350.moduleaotaoreader.model.ViewCountModel;
import com.ysy15350.moduleaotaoreader.popupwindow.AddCommentPopupwindow;
import com.ysy15350.moduleaotaoreader.popupwindow.FlagListPopupwindow;
import com.ysy15350.moduleaotaoreader.util.BookUtil;
import com.ysy15350.moduleaotaoreader.util.BrightnessUtil;
import com.ysy15350.moduleaotaoreader.util.CommonUtil;
import com.ysy15350.moduleaotaoreader.util.PageFactory;
import com.ysy15350.moduleaotaoreader.view.PageWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import base.data.BaseData;
import base.model.directory.DirectoryInfo;
import base.model.directory.DirectoryModel;
import base.mvp.MVPBaseActivity;
import common.CommFun;
import common.CommFunAndroid;
import common.file.FileUtils;
import common.message.MessageBox;
import common.string.JsonConvertor;
import common.string.MD5Util;
import custom_view.dialog.ConfirmDialog;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文章读取界面
 */

public class ReadActivity extends MVPBaseActivity<ReadViewInterface, ReadPresenter>
        implements ReadViewInterface, View.OnClickListener {

    /**
     * 吐槽进度提示
     */
    public static float tuCaoProgress = 0f;

    @Override
    protected ReadPresenter createPresenter() {
        return new ReadPresenter(ReadActivity.this);
    }

    private static final String TAG = "ReadActivity";

    private static final int CODE111 = 111;
    private static final int CODE222 = 222;
    private static final int CODE333 = 333;
    private static final int CODE444 = 444;
    private static final int CODE555 = 555;
    private static final int CODE666 = 666;
    private static final int CODE777 = 777;
    private static final int CODE888 = 888;
    private static final int CODE999 = 999;
    private static final int CODE100 = 100;

    private View view_default;
    private View btn_back;
    private LinearLayout rl_top;
    private RelativeLayout rl_progress;
    private RelativeLayout rl_read_bottom;

    private TextView tv_page_test;

    private View tv_pre;
    private SeekBar sb_progress;
    private TextView tv_progress;
    private View tv_next;
    private View tv_directory;
    private TextView tv_dayornight;
    private View tv_pagemode;
    private View tv_setting;
    private View tv_download;

    private TextView tv_auto_subscribe;//自动订阅
    private View tv_buy;//购买
    private View tv_award;//打赏
    private ImageView img_bookmark;//书签
    private View tv_menu;//菜单

    private int isExistBookmark = 0;

    /**
     * 底部菜单
     */
    private RelativeLayout rl_bottom;


    /**
     * 图书页
     */
    private PageWidget bookpage;


    private PageModeDialog mPageModeDialog;
    private SettingDialog mSettingDialog;
    private PayGiftDialog mPayGiftDialog;
    private DownloadBookDialog mDownloadBookDialog;
    private RechargeDialog rechargeDialog;

    private Boolean mDayOrNight;

    private Config config;
    private PageFactory pageFactory;
    private final static int MESSAGE_CHANGEPROGRESS = 1;

    /**
     * token
     */
    private static String TOKEN;


    /**
     * 阅读进度记录
     */
    public final static String READ_PROGRESSCACHE = "read_progress_cache";

    /**
     * 书签
     */
    public final static String READ_BOOK_MARK = "read_book_mark_cache";

    /**
     * 段落想法
     */
    public final static String READ_BOOK_COMMETLIST = "read_book_commentlist";

    /**
     * 段落想法数量
     */
    public final static String READ_BOOK_COMMETLISTCOUNT = "read_book_commentlist_count";

    /**
     * 下载章节信息
     */
    public final static String READ_BOOK_CHAPTERBLOCK = "read_book_chapter_block";

    /**
     * 阅读记录
     */
    private ReadCache mReadCache;


    /**
     * 当前作品的id
     */
    public static int mAid = 0;
    /**
     * 章节的id
     */
    public static int mCid = 0;
    /**
     * 我的用户 id
     */
    public static int mUid = 0;

    /**
     * 从哪儿调用   1 为正常阅读  2 为从主目录跳转
     */
    public static int type = 1;

    private int currentCid = 0;

    /**
     * 上一章节的ID(0时为首页)
     */
    private int previousCid = 0;
    /**
     * 下一章节的ID(0时为最末 页)
     */
    private int nextCid = 0;


    /**
     * 当前进度
     */
    public static float mProgress;

    /**
     * 当前页码
     */
    private int mPage;

    /**
     * 打开方式；0：正常打开；1：阅读缓存；2：书签进入
     */
    public static int openType = 0;

    /**
     * 原进度，打开书本后跳转至此位置,默认为 0
     */
    public static float mOldProgress = 0f;

    /**
     * 当前章节
     */
    private DirectoryInfo mCurrentDirectoryInfo;


    public static String savePath = "";

    /**
     * 是否启用调试
     */
    public static boolean isDebug = true;

    public static Button buttonBuy;


    public static void log(String str) {
        if (isDebug) {
            Log.i("result", str);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mo_aotao_reader_activity);
        buttonBuy = (Button) findViewById(R.id.button_buy);

        Intent intent = getIntent();
//        String url = intent.getStringExtra("url");
//        String uid = intent.getStringExtra("uid");
//        String aid = intent.getStringExtra("aid");
//        String cid = intent.getStringExtra("cid");
//
//        String typeStr = intent.getStringExtra("type");
////测试数据
//        String isDebugStr = intent.getStringExtra("isDebug");
//
//        String sign = intent.getStringExtra("sign");
        //
        String url="https://www.hanwujinian.com/riku/reader";
        String uid="300865";
        String aid="9126";
        String cid="113888";
        String bookPath="fs://hwjn/article/9126";
        String isDebugStr="";
        String typeStr="1";
        String sign="cUa3dixDR7nHTcX3gZ5SBHfga04SvW0u";

        if (!CommFun.isNullOrEmpty(isDebugStr)) {
            if ("true".equals(isDebugStr)) {
                isDebug = true;
            }
        }

        String path = intent.getStringExtra("path");

        if (path != null && !path.equals("")) {

            if (isDebug) {
                MessageBox.show("参数path=" + path);
            }

            savePath = path;

        } else {
            savePath = CommFunAndroid.getCahePath(this);

        }


        /// 测试的信息
//
//        mUid = 17013;
//        mAid = 20;
//        mCid = 0;
//        type = 1;
//        SERVICE_URL = "http://www.hanwujinian.com/riku/reader";
//

        if (typeStr != null && !typeStr.equals("")) {
            type = Integer.parseInt(typeStr);
        }

        if (!CommFun.isNullOrEmpty(cid)) {
            if (CommFun.isNum(cid)) {
                mCid = CommFun.toInt32(cid, 0);
            }
        }

        if (!CommFun.isNullOrEmpty(uid) && !CommFun.isNullOrEmpty(aid)) {

            mUid = CommFun.toInt32(uid, -1);
            mAid = CommFun.toInt32(aid, -1);

        }

        if (mAid == 0) {

            MessageBox.show("书籍参数错误");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ReadActivity.this.finish();
                }
            }, 1500);

        }

        /// 密钥
        if (!CommFun.isNullOrEmpty(sign)) {
            CommonUtil.PRIVATE_KEY = sign;
        }

        /// 服务器地址
        if (!CommFun.isNullOrEmpty(url)) {
            ///  添加服务器地址
            SERVICE_URL = url;
        }


        log("uid====" + mUid + "---aid====" + mAid + "----cid====" + mCid + "--url=====" + SERVICE_URL);

        init();

        BookUtil.cachedPath = savePath;

        Intent intentBroadcast = new Intent(APIModuleReader.ACTION_NAME);
        intentBroadcast.putExtra("type", "path");
        intentBroadcast.putExtra("savepath", savePath);
        sendBroadcast(intentBroadcast);

        initView();

        initDayOrNight();

//
//        List<DbUtil.Info> list = DbUtil.getInstence(this).getAllCount();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        log("json数据------"+json);
//


    }

    int network_type = -1;


    /**
     * 检查网络状态
     *
     * @return 网络状态
     */
    private int checkNetWork() {
        network_type = CommFunAndroid.getConnectedType(this);
        switch (network_type) {
            case -1:
                MessageBox.show("手机无网络");
                break;
            case 0:
                MessageBox.show("正在使用手机网络");
                break;
            case 1:
                MessageBox.show("正在使用WIFI网络");
                break;

        }
        return network_type;
    }


    /**
     * 是否存在吐槽按钮  1 存在  0 不存在
     */
    int isTCCode;

    @Override
    public void loadData() {
        super.loadData();

        checkNetWork();

        if (network_type != -1) {
            getToken();
        } else {
            MessageBox.show("手机无网络");
        }

        isTCCode = get();

        if (isTCCode != -1) {
            if (isTCCode == 1) {
                //textViewTC.setText("关闭吐槽");
                textViewTC.setText("关闭看法");//2018/3/28 Lu 修改BugClose No.483
            } else if ((isTCCode == 0)) {
                //textViewTC.setText("打开吐槽");
                textViewTC.setText("打开看法");//2018/3/28 Lu 修改BugClose No.483
            }

        } else {
            getTuCaoChange();
        }

        loadBookData();

        getBuyRecord();

    }


    /**
     * 获取订阅记录
     */
    private void getBuyRecord() {
        if (mUid == 0 || mUid == -1) {

            changetv_auto_subscribe("", 1);

            return;
        }

        String url = String.format(Locale.CHINA, "%s/buylog.php?aid=%d&uid=%d", SERVICE_URL, mAid, mUid);

        log("自动订阅获取------" + url);

        if (mOkHttpClient == null) {
            setOkHttp();
        }

        final Request request = new Request.Builder().get().url(url).build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message message = mHandler.obtainMessage();
                message.obj = result;
                message.what = CODE222;
                mHandler.sendMessage(message);

            }
        });

    }

    /**
     * 加载书的数据
     */
    public void loadBookData() {

        MessageBox.showWaitDialog(this, "数据加载中");

        String fileName = String.format(Locale.CHINA, "chapter_%d_%d.txt", mUid, mAid);

        String fullPath = savePath + "/" + fileName;

        String chapterlistJson = FileUtils.readFile(fullPath);

        if (CommFun.isNullOrEmpty(chapterlistJson)) {

            chapterlistJson = BaseData.getCache("chapterlistJson" + mAid);

        }

        if (CommFun.isNullOrEmpty(chapterlistJson)) {

            // 如果没有目录缓存
            chapterlist(mAid);    /// 网络请求作品目录

        } else {

            List<DirectoryInfo> directoryInfos = getDirectoryInfo(chapterlistJson);   ///  从缓存读取目录记录

            bindDirectoryInfo(directoryInfos);
        }

    }


    /**
     * 绑定目录信息,并刷新章节信息
     *
     * @param directoryInfos 目录的集合
     */
    public void bindDirectoryInfo(List<DirectoryInfo> directoryInfos) {
        //// 目录刷新
        if (indexFragment != null) {
            indexFragment.onResume();
        }

        if (type == 2) {

            /// 从目录跳转，直接获取，无阅读记录信息
////            getChapter(mCid, true);

            /// 直接从网络上获取
            chapter(mCid, true);

        } else if (type == 1) {

            /// 检查本地是否有阅读记录，本地无阅读记录,获取集合中的数据
            int indexCid = DbUtil.getInstence(this).get(mUid, mAid);

            /// 本地为零
            if (indexCid == 0) {
                /// 获取第一章节的内容
                if (directoryInfos != null) {
                    if (directoryInfos.size() > 1) {
                        DirectoryInfo d = directoryInfos.get(0);
                        if (d.getChaptertype() == 1) {
                            /// 是卷名,读取1位置的章节的信息
                            mCid = directoryInfos.get(1).getChapterid();
                        } else {
                            mCid = d.getChapterid();
                        }
                    }
                }
            } else {
                /// 本地存在阅读记录
                mCid = indexCid;

                // 第一次进入获取本地缓存的进度
                if (isFirst) {

                    mOldProgress = DbUtil.getInstence(this).getProgress(mUid, mAid, indexCid);

                    tuCaoProgress = mOldProgress;

                } else {
                    mOldProgress = 0;
                    tuCaoProgress = 0f;
                }
            }

            getChapter(mCid, true);

        }

    }


    /**
     * 章节切换时加载前后两章数据,不显示当前章节
     *
     * @param aid 书籍id
     * @param cid 章节id
     */
    private void loadBookCache(int aid, int cid) {
        String bookListJson = BaseData.getCache("bookListJson" + aid + cid);   ///  读取章节本地缓存
        if (!CommFun.isNullOrEmpty(bookListJson)) {
            BookList model = JsonConvertor.fromJson(bookListJson, BookList.class);
            if (model != null) {
                int previous = model.getPrevious();
                final int next = model.getNext();

                log("判断自动订阅====" + mUid);

                boolean isDing = Config.getIsDing(this, mUid, aid);
                int ismy = model.getIsmy();
                int isvip = model.getIsvip();
                if(isDing && isvip == 1 && ismy == 0 ){
                    MessageBox.show("你还未购买本章节");
                    // 未购买 提示 购买
//                    Intent intent = new Intent(ReadActivity.this, BuyActivity.class);
//                    intent.putExtra("aid", aid);
//                    intent.putExtra("cid", cid);
//                    intent.putExtra("uid", mUid);
//                    intent.putExtra("url", SERVICE_URL);
//
//                    startActivityForResult(intent, 789);
                    showVipBy(aid, cid);
                    return;
                }else {

                    currentCid = model.getCid(); // 当前章节
                    previousCid = model.getPrevious();  // 上一章节
                    nextCid = model.getNext();    // 下一章节

                    if (isDebug) {
                        MessageBox.show("loadBookCache: 加载前后两章数据,aid=" + aid + ",cid=" + cid + ",previous=" + previous + ",next=" + next);
                    }

                    if (previous != 0) {
                        cacheNext(previousCid, model.getPremy(), model.getPrevip());
                    }

                    if (next != 0) {
                        if(isDing && isvip == 1 && ismy!=1){

                        }else {
                            cacheNext(next, model.getNextmy(), model.getNextvip());
                        }
                    }
                }
            }

        }
    }


    /**
     * 获取阅读记录
     *
     * @param uid 用户id
     * @param aid 图书id
     * @return 阅读记录对象
     */
    private ReadCache getReadCache(int uid, int aid) {
        String read_progress_cache = CommFunAndroid.getSharedPreferences(READ_PROGRESSCACHE + mUid + mAid);

        try {
            if (!CommFun.isNullOrEmpty(read_progress_cache)) {
                mReadCache = JsonConvertor.fromJson(read_progress_cache, ReadCache.class);
                return mReadCache;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取准备打开的章节id
     *
     * @param readCache      阅读缓存
     * @param directoryInfos 目录列表
     * @return
     */
    private int getChapterId(ReadCache readCache, List<DirectoryInfo> directoryInfos) {
        int chapterid = 0;

        try {
            // 1、从阅读记录信息获取章节id
            if (readCache != null) {
                if (readCache.getCid() != 0)
                    chapterid = readCache.getCid();
            }
            if (chapterid == 0) {
                chapterid = getChapterIdFromDirectoryInfo(directoryInfos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterid;
    }


    /**
     * 获取打开的章节
     *
     * @param directoryInfos
     * @return
     */
    private int getChapterIdFromDirectoryInfo(List<DirectoryInfo> directoryInfos) {
        int chapterid = 0;
        try {
            if (directoryInfos != null && directoryInfos.size() > 0) {
                for (DirectoryInfo directoryInfo :
                        directoryInfos) {

                    if (directoryInfo != null) {
                        int chaptertype = directoryInfo.getChaptertype();
                        //该章节是否是卷名0:一般章节 1:卷
                        if (chaptertype == 0) {
                            mCurrentDirectoryInfo = directoryInfo;
                            chapterid = directoryInfo.getChapterid();//章节id

                            break;
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterid;
    }

    /**
     * 检查是否有阅读缓存
     *
     * @param aid
     * @param cid
     * @return
     */
    private boolean isReadCache(int aid, int cid) {
        return false;
    }


    /**
     * 获取目录章节信息
     *
     * @param chapterlistJson
     */
    private List<DirectoryInfo> getDirectoryInfo(String chapterlistJson) {
        try {
            if (!CommFun.isNullOrEmpty(chapterlistJson)) {
                DirectoryModel directoryModel = JsonConvertor.fromJson(chapterlistJson, DirectoryModel.class);
                if (directoryModel != null) {
                    List<DirectoryInfo> directoryInfos = directoryModel.getData();
                    return directoryInfos;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    OkHttpClient mOkHttpClient = null;


    /**
     * 服务器正式地址    http://www.hanwujinian.com/riku/reader
     */
    public static String SERVICE_URL = "http://www.hanwujinian.com/riku/reader";


    private static final long cacheSize = 1024 * 1024 * 20;// 缓存文件最大限制大小20M
    private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/okttpcaches";  // 设置缓存文件路径
    private static Cache cache = new Cache(new File(cacheDirectory), cacheSize);

    /**
     * 作品目录
     *
     * @param aid 作品的id
     */
    private void chapterlist(int aid) {

        setOkHttp();

        String url = String.format(Locale.CHINA, "%s/chapterlist.php?aid=%d&uid=%d", SERVICE_URL, aid, mUid);

        log("作品目录信息url=====" + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log("=====获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                Message message = mHandler.obtainMessage();
                message.what = CODE333;
                message.obj = str;
                mHandler.sendMessage(message);

                log("获取到的数据=====" + str);

            }

        });

    }

    /**
     * 初始化 ohhttp对象
     */
    private void setOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS); // 设置连接超时时间
        builder.writeTimeout(30, TimeUnit.SECONDS);// 设置写入超时时间
        builder.readTimeout(30, TimeUnit.SECONDS);// 设置读取数据超时时间
        builder.retryOnConnectionFailure(true);// 设置进行连接失败重试
        builder.cache(cache);  // 设置缓存
        mOkHttpClient = builder.build();

    }


    @Override
    public void chapterCallback(Response response) {
        try {
            String str = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        CommFunAndroid.mContext = getApplicationContext();
        MessageBox.mContext = getApplicationContext();
        BaseData.getInstance(this);
        Config.createConfig(this);
        PageFactory.createPageFactory(this);
        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();

        checkNetWork();

    }


    /**
     * 吐槽按钮
     */
    TextView textViewTC;


    @Override
    public void initView() {
        super.initView();

///        AndroidBug5497Workaround.assistActivity(this);   // 解决底部输入框键盘遮挡问题

        view_default = this.findViewById(R.id.view_default);
        btn_back = this.findViewById(R.id.btn_back);

        tv_page_test = (TextView) this.findViewById(R.id.tv_page_test);

        tv_pre = this.findViewById(R.id.tv_pre);
        sb_progress = (SeekBar) this.findViewById(R.id.sb_progress);
        tv_progress = (TextView) this.findViewById(R.id.tv_progress);
        tv_next = this.findViewById(R.id.tv_next);
        tv_directory = this.findViewById(R.id.tv_directory);
        tv_dayornight = (TextView) this.findViewById(R.id.tv_dayornight);
        tv_pagemode = this.findViewById(R.id.tv_pagemode);
        tv_setting = this.findViewById(R.id.tv_setting);
        tv_download = this.findViewById(R.id.tv_download);

        rl_top = (LinearLayout) this.findViewById(R.id.rl_top);
        rl_read_bottom = (RelativeLayout) this.findViewById(R.id.rl_read_bottom);
        rl_bottom = (RelativeLayout) this.findViewById(R.id.rl_bottom);
        rl_progress = (RelativeLayout) this.findViewById(R.id.rl_progress);
        bookpage = (PageWidget) this.findViewById(R.id.bookpage);
        textViewTC = (TextView) findViewById(R.id.tv_auto_close);


        tv_auto_subscribe = (TextView) this.findViewById(R.id.tv_auto_subscribe);//自动订阅
        tv_buy = this.findViewById(R.id.tv_buy);//购买
        tv_award = this.findViewById(R.id.tv_award);//打赏
        img_bookmark = (ImageView) this.findViewById(R.id.img_bookmark);//书签
        tv_menu = this.findViewById(R.id.tv_menu);//菜单


        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);

        mPageModeDialog = new PageModeDialog(this);
        mSettingDialog = new SettingDialog(this);
        mPayGiftDialog = new PayGiftDialog(this);

        initLeftMenu();

        initTopTabMenu();

        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 19) {
            bookpage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //隐藏
        hideSystemUI();   //隐藏系统UI
        hideReadSetting();//隐藏设置菜单
        hideTopBar();
        hideSystemUI();  //隐藏顶部菜单
        //改变屏幕亮度
        if (!config.isSystemLight()) {
            BrightnessUtil.setBrightness(this, config.getLight());
        }

        initListener();

    }


    /**
     * 获取吐槽状态
     */
    private void getTuCaoChange() {
        String url = String.format(Locale.CHINA, "%s/isOpen.php?uid=%d", SERVICE_URL, mUid);
        if (mOkHttpClient == null) {
            setOkHttp();
        }

        Request get = new Request.Builder().get().url(url).build();

        mOkHttpClient.newCall(get).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = mHandler.obtainMessage();
                message.obj = response.body().string();
                message.what = CODE111;
                mHandler.sendMessage(message);

            }
        });

    }

    /**
     * 保存 是否打开吐槽
     *
     * @param isOpen 1 为打开  0 为 关闭
     */
    private void saveIsOpen(int isOpen) {
        SharedPreferences.Editor editor = getSharedPreferences("t_data", Context.MODE_PRIVATE).edit();
        editor.putInt("isopen", isOpen);
        editor.apply();
    }


    /**
     * 是否打开吐槽按钮
     *
     * @return
     */
    private int get() {
        return getSharedPreferences("t_data", MODE_PRIVATE).getInt("isopen", -1);
    }


    /**
     * popwindow是否显示
     */
    private Boolean isShow = false;

    /**
     * 是否正在阅读
     */
    private boolean isSpeaking = false;

    private void initListener() {
        btn_back.setOnClickListener(this);
        tv_pre.setOnClickListener(this);
        sb_progress.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_directory.setOnClickListener(this);
        tv_dayornight.setOnClickListener(this);
        tv_pagemode.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        tv_download.setOnClickListener(this);
        textViewTC.setOnClickListener(this);

        tv_auto_subscribe.setOnClickListener(this);//自动订阅
        tv_buy.setOnClickListener(this);//购买
        tv_award.setOnClickListener(this);//打赏
        img_bookmark.setOnClickListener(this);//书签
        tv_menu.setOnClickListener(this);//菜单

        pageFactory.setPageEvent(new PageFactory.PageEvent() {
            @Override
            public void changeProgress(float progress, int page) {
                /// 当前进度 大于 0 购买按钮不显示
                if (progress > 0) {
                    buttonBuy.setVisibility(View.GONE);
                }

                Message message = new Message();
                message.what = MESSAGE_CHANGEPROGRESS;
                message.obj = progress;
                mPage = page;
                mHandler.sendMessage(message);

            }

            @Override
            public void changeContent(String content) {
                mCurrentPageContent = content;
            }

            // 章节切换缓存
            @Override
            public void changeChapter(int aid, int cid, final int code) {
                mAid = aid;
                mCid = cid;
                loadBookCache(aid, cid);    /// 缓存前后章节
            }

            @Override
            public void firstChapter() {
                /// 当前章节第一页，切换到上一章

                if (previousCid == 0) {
                    MessageBox.show("已经是第一页");
                } else {
                    openType = 2;
                    mOldProgress = 1.0f;

                    preChapter();

                }
            }

            @Override
            public void lastChapter() {
                //最后一页
                // nextCid
                if (nextCid == 0) {
                    MessageBox.show("已经是最后一页");
                } else {
                    openType = 2;
                    mOldProgress = 0.0f;

                    nextChapter();

                }

            }

            @Override
            public void getChapterFromWeb(int cid, boolean isBack) {

                if (!isBack) {

///                    MessageBox.showWaitDialog(ReadActivity.this, "正在读取文章内容...");

                }

                getChapter(cid, !isBack);

            }

            @Override
            public void isBuy(BookList bookList, int code) {
                showIsBuy(bookList, code);
            }

            /**
             * 显示购买按钮
             * @param buy  是否购买
             * @param code  code==1 为向前翻页   2 为向后翻页
             */
            @Override
            public void onButtonBuy(final boolean buy, int code) {
                ReadActivity.this.indexCode = code;
                try {
                    getWindow().getDecorView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (buy) {
                                bookpage.setPageMode(Config.PAGE_MODE_NONE);
                                pageFactory.setPageWidget(bookpage);
                                buttonBuy.setVisibility(View.VISIBLE);
                            } else {
                                buttonBuy.setVisibility(View.GONE);
                            }
                        }
                    }, 200);  // 200毫秒后执行


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 自动订阅章节
             */
            @Override
            public void onDing(BookList bookList, int code) {
                ding(bookList, code);
            }

        });

        bookpage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {
                if (isShow) {
                    hideTopBar();
                    hideSystemUI();
                    hideReadSetting();
                } else {
                    showTopBar();
                    showReadSetting();
                }
            }

            @Override
            public void flagClick(List<FlagInfo> flagInfoList) {
                try {
                    if (flagInfoList != null && flagInfoList.size() != 0) {

                        log("吐槽位置总长度------" + flagInfoList.size() + "---------" + isTCCode);

                        if (isTCCode == 1) {
                            ///  如果打开吐槽
                            getIdFromByProgress(flagInfoList);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void paragraphLongClick(final ParagraphInfo paragraphInfo) {
                if (isShow) {
                    hideTopBar();
                    hideSystemUI();
                    hideReadSetting();
                } else {
                    showTopBar();
                    showReadSetting();
                }

            }

            @Override
            public void bottom() {

            }

            @Override
            public Boolean prePage() {

                DbUtil.getInstence(ReadActivity.this).addCount(String.valueOf(mAid), String.valueOf(bookList.getCid()));

                if (isShow || isSpeaking) {
                    return false;
                }

                mOldProgress = 0f;
                tuCaoProgress = 0f;

                log("向前翻页======");

                // 向前滑动
                if (indexCode == 2) {

                    bookpage.setPageMode(config.getPageMode());
                    indexCode = 0;
                }

                pageFactory.prePage();

                if (pageFactory.isfirstPage()) {
                    return false;
                }

                return true;
            }


            @Override
            public Boolean nextPage() {
                mOldProgress = 0f;
                tuCaoProgress = 0f;

                DbUtil.getInstence(ReadActivity.this).addCount(String.valueOf(mAid), String.valueOf(bookList.getCid()));

                if (isShow || isSpeaking) {
                    return false;
                }

                log("向后---翻页======" + indexCode);

                // 向后滑动
                if (indexCode == 1) {
                    bookpage.setPageMode(config.getPageMode());
                    indexCode = 0;
                }

                pageFactory.nextPage();

                if (pageFactory.islastPage()) {
                    return false;
                }

                return true;
            }

            @Override
            public void cancel() {

            }
        });

        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float pro;

            // 触发操作，拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000);

            }

            // 表示进度条刚开始拖动，开始拖动时候触发的操作
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /// 停止拖动时候
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                log("拖动到下一章-----" + pro);

                if (pro == 1) {

                    nextChapter();

                } else {

                    log("拖动到下一章-----改变进度");

                    pageFactory.changeProgress(pro);

                }

                showProgress(pro);

            }

        });


        mPageModeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mPageModeDialog.setPageModeListener(new PageModeDialog.PageModeListener() {
            @Override
            public void changePageMode(int pageMode) {
                bookpage.setPageMode(pageMode);
            }
        });

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mSettingDialog.setSettingListener(new SettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtil.setBrightness(ReadActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ReadActivity.this);
                    BrightnessUtil.setBrightness(ReadActivity.this, bh);
                }

            }

            @Override
            public void changeFontSize(int fontSize) {
                // 改变字体大小
                pageFactory.changeFontSize(fontSize);

            }

            @Override
            public void changeTypeFace(Typeface typeface) {
                pageFactory.changeTypeface(typeface);
            }

            @Override
            public void changeBookBg(int type) {
                pageFactory.changeBookBg(type);
            }

            @Override
            public void changeLineSpaceStyle(int type) {
                pageFactory.changeLineSpaceStyle(type);
            }

            @Override
            public void changeTextColor(int type) {
                pageFactory.changeTextColor(type);
            }
        });


        mPayGiftDialog.setDialogListener(new PayGiftDialog.DialogListener() {
            @Override
            public void payGift(GiftInfo giftInfo) {
                if (giftInfo != null) {
                    paygift(giftInfo);
                }
            }
        });

    }


    int wordsLen;

    /**
     * 获取吐槽id
     *
     * @param flagInfoList 吐槽集合
     */
    private void getIdFromByProgress(List<FlagInfo> flagInfoList) {
        for (FlagInfo temp : flagInfoList) {
            getReviews(temp.getIndex());
            return;
        }

    }

    int indexCode = 0;

    /**
     * 购买章节 提示按钮 点击购买章节
     *
     * @param bookList 书的对象
     * @param code     1 为向前看   2 为向后看
     */
    private void showIsBuy(final BookList bookList, final int code) {

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                log("已经点击跳转购买");

                Intent intent = new Intent(ReadActivity.this, BuyActivity.class);
                intent.putExtra("aid", bookList.getAid());
                if (code == 1) {
                    intent.putExtra("cid", bookList.getPrevious());
                } else if (code == 2) {
                    intent.putExtra("cid", bookList.getNext());
                } else {
                    intent.putExtra("cid", bookList.getCid());
                }

                intent.putExtra("uid", mUid);
                intent.putExtra("url", SERVICE_URL);
                startActivityForResult(intent, 456);
            }
        });

    }


    /**
     * 自动订阅章节信息
     *
     * @param bookList 书的集合
     */
    private void ding(final BookList bookList, int code) {
        log("是否执行自动订阅章节信息");

        final int cid;
        if (code == 1) {
            // 订阅上一章节
            cid = bookList.getPrevious();

        } else if (code == 2) {
            // 订阅下一章节
            cid = bookList.getNext();
        } else {
            cid = 0;
        }

        String getUrl = SERVICE_URL + "/buychapter.php?aid=" + bookList.getAid() + "&cid=" + cid + "&uid=" + mUid + "&autobuy="
                + 1 + "&act=buy&sign=" + getSign(bookList.getAid(), bookList.getCid(), mUid) + "&ajaxapp=1&ajax_request=1";

        log("wwwwww====" + getUrl);

        if (mOkHttpClient == null) {
            setOkHttp();
        }

        final Request request = new Request.Builder().url(getUrl).get().build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                log("收到的数据======"+response.toString());

//                showMsg(str);

                log("获取到的数据response=====" + str);

                getChapter(cid, false);

            }
        });
    }


    private String getSign(int aid, int cid, int uid) {
        String sign = CommonUtil.getMd5Sign_sendreview(aid, cid, uid);
        return sign;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        buttonBuy.setVisibility(View.GONE);

        if (requestCode == 456 && resultCode == Activity.RESULT_OK) {

            final int cid = data.getIntExtra("cid", 0);
            int aid = data.getIntExtra("aid", 0);

            log("wwwww---cid====" + cid);

            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (type == 2) {

                        chapter(cid, true);

                        type = 1;

                    } else {
                        /// 获取内容并显示
                        getChapter(cid, true);

                    }

                }
            }, 200);//200毫秒后执行

            DbUtil dbUtil = DbUtil.getInstence(ReadActivity.this);
            dbUtil.save(cid, aid, mUid);

        } else {

            if (requestCode == 789 && resultCode == Activity.RESULT_OK) {
                final int cid = data.getIntExtra("cid", 0);
                int aid = data.getIntExtra("aid", 0);

                log("wwwww---cid====" + cid);

                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (type == 2) {

                            // 从目录跳转已经加载数据，将type 置为 1，正常阅读
                            chapter(cid, true);

                            type = 1;

                        } else {
                            /// 获取内容并显示
                            getChapter(cid, true);

                        }


                    }
                }, 200); // 200毫秒后执行

                DbUtil dbUtil = DbUtil.getInstence(ReadActivity.this);
                dbUtil.save(cid, aid, mUid);

            }
        }

    }


    private void showTopBar() {
        isShow = true;

        if (isSpeaking) {
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
            rl_read_bottom.startAnimation(topAnim);
            rl_read_bottom.setVisibility(View.VISIBLE);
        } else {
            showSystemUI();

            Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
            rl_top.startAnimation(topAnim);

            rl_top.setVisibility(View.VISIBLE);

        }

    }

    private void hideTopBar() {
        isShow = false;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_exit);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(topAnim);
        }

        if (rl_read_bottom.getVisibility() == View.VISIBLE) {
            rl_read_bottom.startAnimation(topAnim);
        }

        rl_top.setVisibility(View.GONE);
        rl_read_bottom.setVisibility(View.GONE);

        hideSystemUI();
    }

    private void showReadSetting() {
        isShow = true;
        rl_progress.setVisibility(View.GONE);

        if (isSpeaking) {
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
            rl_read_bottom.startAnimation(topAnim);
            rl_read_bottom.setVisibility(View.VISIBLE);
        } else {
            showSystemUI();

            Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
            Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
            rl_bottom.startAnimation(topAnim);

            rl_bottom.setVisibility(View.VISIBLE);

        }
    }

    private void hideReadSetting() {
        isShow = false;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_exit);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(topAnim);
        }

        if (rl_read_bottom.getVisibility() == View.VISIBLE) {
            rl_read_bottom.startAnimation(topAnim);
        }

        rl_bottom.setVisibility(View.GONE);
        rl_read_bottom.setVisibility(View.GONE);

        hideSystemUI();
    }


    private void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }


    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MESSAGE_CHANGEPROGRESS:

                    float progress = (float) msg.obj;
                    setSeekBarProgress(progress);

                    break;

                case CODE111:

                    String result = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(result);
                        int code = json.optInt("status");
                        if (code == 1) {
                            isTCCode = json.optInt("isopen");

                            if (isTCCode == 1) {
                                textViewTC.setText("关闭吐槽");
                            } else {
                                textViewTC.setText("打开吐槽");
                            }

                            /// 保存到本地
                            saveIsOpen(isTCCode);

                            if (isTuChange) {

                                MessageBox.hideWaitDialog();

                                bindBook(chapterInfo, true);

                                pageFactory.currentPage(true);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case CODE222:
                    try {
                        JSONObject json = new JSONObject((String) msg.obj);
                        int status = json.optInt("status");

                        if (status == 1) {
                            int buy = json.optInt("autobuy");
                            if (buy == 1) {
                                changetv_auto_subscribe("setautobuy", 1);
                            } else if (buy == 0) {
                                changetv_auto_subscribe("unsetautobuy", 1);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case CODE333:

                    String str333 = (String) msg.obj;

                    if (!CommFun.isNullOrEmpty(str333)) {

                        String fileName = String.format(Locale.CHINA, "chapter_%d_%d.txt", mUid, mAid);

                        String fullPath = savePath + "/" + fileName;

                        boolean isSuccess = FileUtils.saveFile(str333, savePath, fileName);

                        if (isDebug) {
                            MessageBox.show("保存目录结果：" + isSuccess);
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(str333);
                            String aaa = jsonObject.optString("aid");

                            if (aaa != null && !aaa.equals("null")) {

                                BaseData.setCache("chapterlistJson" + mAid, str333);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        List<DirectoryInfo> directoryInfos = getDirectoryInfo(str333);    ///  目录数据

                        log("从111111来");

                        bindDirectoryInfo(directoryInfos);
                    }

                    break;

                case CODE444:

                    ChapterInfo chapterInfo = getChapterInfo((String) msg.obj);
                    if (chapterInfo != null) {
                        int aid = chapterInfo.getAid();
                        int cid = chapterInfo.getCid();

                        String fileName = String.format(Locale.CHINA, "%d_%d_%d.txt", mUid, mAid, cid);
                        String fullPath = savePath + "/" + fileName;

                        boolean isSuccess = FileUtils.saveFile((String) msg.obj, savePath, fileName);

                        if (isDebug) {
                            MessageBox.show("文章写入结果：" + isSuccess);
                        }

                        BaseData.setCache("chapterJson" + aid + cid, (String) msg.obj);

                    }

                    break;

                case CODE555:

                    MessageBox.hideWaitDialog();

                    ChapterInfo chap = getChapterInfo((String) msg.obj);
                    if (chap != null) {

                        int aid = chap.getAid();
                        int cid = chap.getCid();
                        wordsLen = chap.getWords();

                        String fileName = String.format(Locale.CHINA, "%d_%d_%d.txt", mUid, mAid, cid);
                        String fullPath = savePath + "/" + fileName;

                        /// 保存文件
                        boolean isSuccess = FileUtils.saveFile((String) msg.obj, savePath, fileName);

                        BaseData.setCache("chapterJson" + aid + cid, (String) msg.obj);

                        choose(chap, isOpenBook);

                    }

                    break;

                case CODE666:
                    String sss = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(sss);
                        String string = json.optString("msg");
                        Toast.makeText(ReadActivity.this, string, Toast.LENGTH_SHORT).show();
                        int code = json.optInt("status");
                        if (code == 1) {
                            isTuChange = true;
                            /// 现在的进度赋给原进度
                            mOldProgress = mProgress;
                            getTuCaoChange();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case CODE777:

                    if (!CommFun.isNullOrEmpty(msg.obj)) {
                        ResponseBook responseBook = JsonConvertor.fromJson((String) msg.obj, ResponseBook.class);
                        if (responseBook != null) {
                            final int status = responseBook.getStatus();
                            final String mm = responseBook.getMsg();
                            if (status == 1) {
                                reviewscount(mAid, mCid);
                            }

                            MessageBox.show(mm);
                        }
                    }
                    break;
                case CODE888:

                    CommFunAndroid.setSharedPreferences(READ_BOOK_COMMETLISTCOUNT + mAid + mCid, (String) msg.obj);

                    if (!CommFun.isNullOrEmpty((String) msg.obj)) {

                        List<ViewCount> viewCountList = getViewCountList((String) msg.obj);

                        pageFactory.setViewCountList(viewCountList);

                    }

                    pageFactory.currentPage(true);

                    break;

                case CODE999:

                    MessageBox.hideWaitDialog();

                    if (!CommFun.isNullOrEmpty(msg.obj)) {
                        String str999 = (String) msg.obj;

                        log("下载返回数据-----" + str999);

                        DownloadBookModel downloadBookModel = JsonConvertor.fromJson(str999, DownloadBookModel.class);
                        if (downloadBookModel != null) {
                            final List<ChapterInfo> chapterInfos = downloadBookModel.getData();

                            if (chapterInfos != null) {
                                for (ChapterInfo temp : chapterInfos) {

                                    if (temp != null) {
                                        int aid = temp.getAid();
                                        int cid = temp.getCid();

                                        String chapterJson = JsonConvertor.toJson(temp);

                                        if (!CommFun.isNullOrEmpty(chapterJson)) {

                                           /// 缓存章节内容信息
                                            BaseData.setCache("chapterJson" + aid + cid, chapterJson);

                                            String fileName = String.format(Locale.CHINA, "%d_%d_%d.txt", mUid, mAid, cid);
                                            String fullPath = savePath + "/" + fileName;

                                            boolean isSuccess = FileUtils.saveFile(chapterJson, savePath, fileName);

                                        }
                                    }
                                }

                                 /// 刷新目录信息
                                if (indexFragment != null) {
                                    /// 重新获取一次目录
                                    indexFragment.chapterlist(mAid);

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MessageBox.show("下载成功，共下载" + chapterInfos.size() + "章");

                                    }
                                });

                            }
                        }
                    }
                    break;
            }
        }
    };


    /**
     * 设置进度信息
     *
     * @param progress 进度
     */
    public void showProgress(float progress) {
        if (rl_progress.getVisibility() != View.VISIBLE) {
            rl_progress.setVisibility(View.VISIBLE);
        }

        setProgress(progress);

    }


    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");  // 构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(progress * 100.0);        // format 返回的是字符串
        tv_progress.setText(p + "%");
    }

    /**
     * 显示 进度
     *
     * @param progress 进度
     */
    public void setSeekBarProgress(float progress) {

        ReadActivity.mProgress = progress;
        ReadActivity.tuCaoProgress = progress;

        int progress_int = (int) (progress * 10000);

        log("当前进度-----" + progress_int);

        sb_progress.setProgress(progress_int);

        tv_page_test.setText(String.valueOf(mPage));

        BookMark bookMark = new BookMark();
        bookMark.setPage(mPage);
        bookMark.setProgress(mProgress);

        checkBookMark(bookMark);

        /// 进度为100时，显示下一章
        if (mOldProgress == 1) {
            nextChapter();
        }

    }


    /**
     * 检测是否有书签
     */
    private void checkBookMark(BookMark bookMark) {
        BookMark isBookMark = getBookMarkByPage(bookMark);
        if (isBookMark != null) {
            isExistBookmark = 1;
            img_bookmark.setImageResource(R.mipmap.mo_aotao_reader_icon_book_mark_1);
        } else {
            isExistBookmark = 0;
            img_bookmark.setImageResource(R.mipmap.mo_aotao_reader_icon_book_mark);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intentBroadcast = new Intent(APIModuleReader.ACTION_NAME);
        intentBroadcast.putExtra("type", "stop");
        intentBroadcast.putExtra("savepath", savePath);
        sendBroadcast(intentBroadcast);

        saveProgress();
        openType = 0;//默认打开
        mCid = 0;//加载章节
        mOldProgress = 0;//跳转到指定位置，如果设置，打开后自动跳转到这个位置
    }

    /**
     * 记录阅读进度
     */
    private void saveProgress() {
        ReadCache readCache = new ReadCache();
        readCache.setUid(mUid);
        readCache.setAid(mAid);
        readCache.setCid(currentCid);
        readCache.setProgress(mProgress);
        String readCacheJson = JsonConvertor.toJson(readCache);
        CommFunAndroid.setSharedPreferences(READ_PROGRESSCACHE + mUid + mAid, readCacheJson);
    }


    /**
     * 当前章节内容
     */
    private ChapterInfo mCurrentChapterInfo;

    /**
     * 当前内容
     */
    private String mCurrentPageContent;


    /**
     * 书籍的对象信息
     */
    private BookList bookList = new BookList();


    ChapterInfo chapterInfo;


    /**
     * 绑定图书 并显示
     *
     * @param chapterInfo 图书的对象
     */
    private void bindBook(ChapterInfo chapterInfo, boolean isOpenBook) {
        this.chapterInfo = chapterInfo;

        ///  将isFirst 置为false 不执行onResume(),不重新加载数据

        isFirst = false;

        try {
            if (chapterInfo != null && (chapterInfo.getIsvip()==0 || chapterInfo.getIsmy()==1)) {

                DbUtil.getInstence(this).saveCache(chapterInfo.getCid(), mAid, mUid);

                if (isDebug) {
                    MessageBox.show("绑定图书：" + chapterInfo.getChaptername());
                }

                mCurrentChapterInfo = chapterInfo;  //当前章节
                mCid = mCurrentChapterInfo.getCid();

                int status = chapterInfo.getStatus();
                String msg = chapterInfo.getMsg();

                String content = chapterInfo.getContent();

                if (!CommFun.isNullOrEmpty(content)) {


                    log("获取html-----" + content);

                    int aid = chapterInfo.getAid();   //作品ID
                    int cid = chapterInfo.getCid();   //章节ID

                    bookList.setIsmy(chapterInfo.getIsmy());
                    bookList.setIsvip(chapterInfo.getIsvip());
                    bookList.setBookpath(savePath);
                    bookList.setBookname(chapterInfo.getArticlename());
                    bookList.setChaptername(chapterInfo.getChaptername());
                    bookList.setAid(aid);
                    bookList.setCid(cid);
                    bookList.setNext(chapterInfo.getNext());
                    bookList.setPrevious(chapterInfo.getPrevious());
                    bookList.setOpenBook(true);

                    // 下一章是否为vip 是否购买
                    bookList.setNextvip(chapterInfo.getNextvip());
                    bookList.setNextmy(chapterInfo.getNextmy());

                    // 上一章是否为vip 是否购买
                    bookList.setPremy(chapterInfo.getPremy());
                    bookList.setPrevip(chapterInfo.getPrevip());

                    //// 存在段头和段尾
                    if (content.contains("####{flag:1}####\r\n\r\n####{flag:0}####")) {

                        content = content.replaceAll("\r\n\r\n", "\r\n");

                    } else {

//                        log( "获取html-----");
//                        content = chapterInfo.getContenthtml();
//                        if (content.contains("<br />\n<br />\n")) {
//                            // 添加段落结束标识符
//                            content = content.replaceAll("<br />\n<br />\n", "####{flag:1}####\r\n");
//                        }
//                        if (content.contains("<br />\r\n<br />\r\n")) {
//                            // 添加段落结束标识符
//                            content = content.replaceAll("<br />\r\n<br />\r\n", "####{flag:1}####\r\n");
//
//                        }
                    }

                    if (content.contains("&nbsp;")) {
                        content = content.replaceAll("&nbsp;", "");
                    }

                    /// 按换行符截取
                    String[] contentArray = content.split("\r\n");

                    if (contentArray.length != 0) {
                        StringBuilder stringBuilder = new StringBuilder();

                        ///  加上标题
                        if (!contentArray[0].contains(chapterInfo.getChaptername())) {
                            stringBuilder.append(String.format("\r\n%s\r\n", chapterInfo.getChaptername()));
                        }

                        int paragraph_index = 0;
                        int arrLen = contentArray.length;

                        /// 遍历分段的数据
                        for (int i = 0; i < arrLen; i++) {

                            String str = contentArray[i];

                            if (str.startsWith("####{flag:0}####")) {
                                str = str.replace("####{flag:0}####", "");
                            }

                            if (str.length() > 9 && str.endsWith("<br />")) {
                                str = str.replace("<br />", "####{flag:1}####");
                            }

                            if (str.length() == 8 && str.contains("<br />")) {
                                str = str.replace("<br />", "");
                            }

                            /// 人为添加段落结束符
                            if (!CommFun.isNullOrEmpty(str) && str.endsWith("####{flag:1}####")) {

                                /// 打开吐槽按钮
                                if (isTCCode == 1) {

                                    paragraph_index++;

                                    /// 去除空格
                                    str = str.replaceAll("\\s*", "");

                                    str = str.replace("####{flag:1}####", String.format(Locale.CHINA, "\r\n##{%d}", paragraph_index));

                                    stringBuilder.append(str.trim());

                                } else {

                                    str = str.replace("####{flag:1}####", "\r\n$$$");

                                    stringBuilder.append(str);

                                }
                            }
                        }

                        String notice = chapterInfo.getNotice();

                        if (!CommFun.isNullOrEmpty(notice)) {
                            stringBuilder.append(String.format(Locale.CHINA, "#{}%s", "作者有话说"));
                            stringBuilder.append("\r\n" + notice);
                        }

                        content = stringBuilder.toString();

                    }

                    /// 保存数据
                    bookList.setContent(content);

                    /// 从本地保存获取
                    String jsonStr = CommFunAndroid.getSharedPreferences(READ_BOOK_COMMETLISTCOUNT + aid + cid);

                    List<ViewCount> viewCountList = getViewCountList(jsonStr);

                    bookList.setViewCountList(viewCountList);

                    // 对象转换成json数据 存储
                    String bookListJson = JsonConvertor.toJson(bookList);

                    BaseData.setCache("bookListJson" + aid + cid, bookListJson);

                    log("是否打开-----打开---" + isOpenBook);

                    // 是否打开 true 为打开
                    if (isOpenBook) {

                        log("是否打开-----打开---");

                        if (bookList != null) {

                            if ( (bookList.getIsmy() == 0 && bookList.getIsvip() == 1 ) || (chapterInfo.getIsvip() == 1 &&chapterInfo.getIsmy() == 0)) {

                                showIsBuy(bookList, 0);

                            } else {

                                if (view_default != null) {
                                    view_default.setVisibility(View.GONE);
                                }

                                pageFactory.setViewCountList(viewCountList);

                                pageFactory.openBook(bookList);

                            }
                        }
                    } else {

                        if (isDebug) {
                            MessageBox.show("已缓存" + bookList.getChaptername());
                        }

                        pageFactory.cacheBook(bookList);

                    }

                    closeDrawer();   // 关闭抽屉菜单（目录）

                    checkCacheType();

                } else {
                    MessageBox.show(msg);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 页面显示后再缓存上一章节
        cacheNext(chapterInfo.getPrevious(), chapterInfo.getPremy(), chapterInfo.getPrevip());
        // 缓存下一章节
        cacheNext(chapterInfo.getNext(), chapterInfo.getNextmy(), chapterInfo.getNextvip());

    }

    /**
     * 缓存章节信息
     *
     * @param next 章节id
     */
    private void cacheNext(int next, int ismy, int vip) {
        log("缓存章节:"+next+"---"+ismy+"___"+vip);
        if (next == 0) {
            return;
        }
        if (vip == 1) {
            //如果是vip章节判断是否为自动订阅
            boolean isDing =  Config.getIsDing(this,mUid,mAid);
            if(isDing){
                /// 刷新目录信息
                if (indexFragment != null) {
                    /// 重新获取一次目录
                    indexFragment.chapterlistAutoBuy(mAid);

                }
            }
            if (ismy == 0) {
                return;
            }
        }

        log("进行缓存:"+next);

        String data = BaseData.getCache("chapterJson" + mAid + next);
        if (data == null) {
            // 缓存为 null 读取文件
            String fileName = String.format(Locale.CHINA, "%d_%d_%d.txt", mUid, mAid, next);
            String fullPath = savePath + "/" + fileName;
            data = FileUtils.readFile(fullPath);
        }

        // 读取的文件数据存在，不执行下面从网络获取的操作
        if (data != null && !data.equals("")) {
            return;
        }

        if (mOkHttpClient == null) {
            setOkHttp();
        }

        String sign = CommonUtil.getMd5Sign(mAid, next);   // md5(秘钥#作品ID#章节ID)

        String url = String.format(Locale.CHINA, "%s/chapter.php?aid=%d&cid=%d&uid=%d&sign=%s", SERVICE_URL, mAid, next, mUid, sign);

        log("获取内容=======" + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MessageBox.show("网路错误");
                        MessageBox.hideWaitDialog();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                Message message = mHandler.obtainMessage();
                message.what = CODE444;
                message.obj = str;
                mHandler.sendMessage(message);


            }

        });

    }

    private void checkCacheType() {
        float progress = 0;
        switch (openType) {
            case 1:
                if (mReadCache != null) {
                    if (mReadCache.getCid() == currentCid) {
                        progress = mReadCache.getProgress();
                    }
                }
                break;
            case 2:
                progress = mOldProgress;
                break;
        }

    }


    /**
     * 解析JSON
     *
     * @param data json数据
     */
    private ChapterInfo getChapterInfo(String data) {
        try {
            MessageBox.hideWaitDialog();
            if (!CommFun.isNullOrEmpty(data)) {

                ChapterInfo chapterInfo = JsonConvertor.fromJson(data, ChapterInfo.class);

                return chapterInfo;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 绑定显示数据
     *
     * @param chapterInfo
     * @param isOpenBook
     */
    private void bindChapterInfo(ChapterInfo chapterInfo, boolean isOpenBook) {

        try {
            if (chapterInfo != null && (chapterInfo.getIsvip()==0 || chapterInfo.getIsmy()==1)) {
                int status = chapterInfo.getStatus();
                String msg = chapterInfo.getMsg();

                String content = chapterInfo.getContent();

                if (!CommFun.isNullOrEmpty(content)) {

                    /// 绑定书籍，显示内容
                    choose(chapterInfo, isOpenBook);

////                    bindBook(chapterInfo, isOpenBook);

                    getViewsCount(chapterInfo.getAid(), chapterInfo.getCid());   // 想法数量获取

                } else {
                    MessageBox.show(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 想法数量获取
     *
     * @param aid 文章id
     * @param cid 文章章节 id
     */
    private void getViewsCount(int aid, int cid) {
        reviewscount(aid, cid);

    }


    /**
     * 显示指定选项卡
     */
    public static int tab_position = 0;

    private int bmpW;// 动画图片宽度

    private int screenW = 0;
    private int offset = 0;// 动画图片偏移量

    private ImageView cursor;// 动画图片viewpager_line.jpg

    private View ll_top, ll_tab1, ll_tab2, ll_tab3, ll_tab4;


    private void initTopTabMenu() {

        cursor = (ImageView) this.findViewById(R.id.cursor);

        ll_top = this.findViewById(R.id.ll_top);
        ll_tab1 = this.findViewById(R.id.ll_tab1);
        ll_tab2 = this.findViewById(R.id.ll_tab2);
        ll_tab3 = this.findViewById(R.id.ll_tab3);
        ll_tab4 = this.findViewById(R.id.ll_tab4);

        ll_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pager.setCurrentItem(0);
                setTabImage(0, v);
            }
        });
        ll_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
                setTabImage(1, v);
            }
        });
        ll_tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
                setTabImage(2, v);
            }
        });
        ll_tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(3);
                setTabImage(3, v);
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;// 获取分辨率宽度
        int height1 = dm.heightPixels;

        ViewGroup.LayoutParams para = ll_pop_menu.getLayoutParams();//获取drawerlayout的布局
        para.width = screenW / 5 * 4;//修改宽度
        para.height = height1;//修改高度
        ll_pop_menu.setLayoutParams(para); //设置修改后的布局。

        // screenW = ll_top.getLayoutParams().width;

        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.mo_aotao_reader_viewpager_line).getWidth();// 获取图片宽度

        offset = screenW / 5 * 4 / 4 - bmpW;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 记录当前view（图片切换）
     */
    private View currentView;

    /**
     * 切换图片（background 设置背景：xml->selector）
     *
     * @param v
     */
    private void setView(View v) {

        if (currentView != null) {
            if (currentView.getId() != v.getId()) {
                View imgview = currentView.findViewWithTag("tabimg");
                View textview = currentView.findViewWithTag("tabtext");
                if (imgview != null)
                    imgview.setEnabled(true);
                if (textview != null) {
                    textview.setEnabled(true);
                }
            }
        }
        if (v != null) {
            View imgview = v.findViewWithTag("tabimg");
            View textview = v.findViewWithTag("tabtext");
            if (imgview != null)
                imgview.setEnabled(false);
            if (textview != null) {
                textview.setEnabled(false);
            }
        }
        currentView = v;
    }

    /**
     * 切换动画
     *
     * @param position 即将切换的位置索引
     */
    private void setTabImage(int position, View view) {

        if (cursor != null && view != null) {

            setView(view);

            int one = offset + bmpW;  // 页卡1 -> 页卡2 偏移量
            int two = one * 2;       // 页卡1 -> 页卡3 偏移量


            Animation animation = new TranslateAnimation(one * tab_position, one * position, 0, 0);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);

            tab_position = position;
        }
    }

    /**
     * 滑动viewpager时设置tab(改变图片和字体)
     *
     * @param position
     */
    private void setTab(int position) {

        /// 记录已打开选项卡位置，当跳转到登录界面或者其他界面，显示此界面
        tab_position = position;

        switch (position) {
            case 0:
                setTabImage(position, ll_tab1);
                break;
            case 1:
                setTabImage(position, ll_tab2);
                break;
            case 2:
                setTabImage(position, ll_tab3);
                break;
            case 3:
                setTabImage(position, ll_tab4);
                break;

            default:
                break;
        }
    }


    private DrawerLayout mDrawerlayout;
    private LinearLayout ll_pop_menu;
    private LinearLayout id_drawer;

    private ViewPager pager;

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private void initLeftMenu() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mDrawerlayout = (DrawerLayout) this.findViewById(R.id.id_drawerlayout);
        ll_pop_menu = (LinearLayout) this.findViewById(R.id.ll_pop_menu);
        id_drawer = (LinearLayout) this.findViewById(R.id.id_drawer);

        pager = (ViewPager) this.findViewById(R.id.pager);
        pager.setAdapter(mSectionsPagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Fragment fragment = mSectionsPagerAdapter.getItem(position);
                if (fragment != null) {
                    fragment.onResume();
                }

                setTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setCurrentItem(tab_position);

        mDrawerlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                state = 1;
                hideSystemUI();    //隐藏系统UI
                hideReadSetting(); //隐藏设置菜单
                hideTopBar();
                hideSystemUI();    //隐藏顶部菜单
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                state = 0;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    public static int state = 0;


    public void openDrawer() {
        mDrawerlayout.openDrawer(id_drawer);
    }

    public void closeDrawer() {
        mDrawerlayout.closeDrawer(id_drawer);
    }


    MainTab1Fragment indexFragment;


    /**
     * 从想法中跳转获取数据
     */
    public void loadBookDataFromIdea(int type, int aid, int cid, float progress) {
        openType = type;
        mAid = aid;
        mCid = cid;
        mOldProgress = progress;

        log("------" + mCid);

        closeDrawer();

        getChapter(mCid, true);

    }

    /**
     * 从书签获取数据
     */
    public void loadDataFromShuQian(int type, int uid, int aid, int cid, float progress) {
        openType = type;
        mUid = uid;
        mAid = aid;
        mCid = cid;
        mOldProgress = progress;
        closeDrawer();
        getChapter(cid, true);


    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<String> titles = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            titles.add("目录");
            titles.add("书签");
            titles.add("想法");
            titles.add("书本介绍");

            /// 初始化目录fragment
            indexFragment = new MainTab1Fragment();

        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return indexFragment;
                }
                case 1: {
                    return new MainTab2Fragment();
                }
                case 2: {
                    return new MainTab3Fragment();
                }
                case 3: {
                    return new MainTab4Fragment();
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return super.getPageTitle(position);
            return titles.get(position);
        }
    }


    /**
     * 根据章节ID获取章节内容，如果有缓存，获取缓存,否则从网络中获取
     *
     * @param cid 文章章节
     */
    private void getChapter(final int cid, boolean isOpenBook) {
        try {
            if (cid == 0) {

                MessageBox.hideWaitDialog();

                ConfirmDialog confirmDialog = new ConfirmDialog(this, "系统提示", "加载失败！！", "重试", "返回");
                confirmDialog.setDialogListener(new ConfirmDialog.DialogListener() {
                    @Override
                    public void onCancelClick() {
                        delete(cid);
                        finish();
                    }

                    @Override
                    public void onOkClick() {
                        delete(cid);
                        loadBookData();
                    }
                });

                confirmDialog.show();

                return;
            }


            String chapterJson = BaseData.getCache("chapterJson" + mAid + cid);  /// 读取章节缓存

            if (CommFun.isNullOrEmpty(chapterJson)) {

                /// 缓存为空
                String fileName = String.format(Locale.CHINA, "%d_%d_%d.txt", mUid, mAid, cid);
                String fullPath = savePath + "/" + fileName;
                chapterJson = FileUtils.readFile(fullPath);

                log("执行读取文件------");

            }

            log("读取到的文件======" + chapterJson);

            if (!CommFun.isNullOrEmpty(chapterJson)) {
                ///  数据存在
                ChapterInfo chapterInfo = getChapterInfo(chapterJson);

                if (isDebug) {
                    MessageBox.show("chapterInfo=" + chapterInfo);
                }

                if (chapterInfo != null) {
                    if(chapterInfo.getIsvip() == 1 && chapterInfo.getIsmy()==0){
                        showVipBy(mAid, cid);
                        return;
                    }else {
                        // 本地对象存在,显示本地数据
                        bindChapterInfo(chapterInfo, isOpenBook);
                    }
                } else {
                    if (network_type != -1) {
                        if (isOpenBook) {
                            MessageBox.showWaitDialog(this, "正在读取文章内容...");
                        }

                        // 获取当前章节
                        chapter(cid, isOpenBook);

                    } else {
                        MessageBox.show("手机无网络");
                    }

                }
            } else {
                if (network_type != -1) {
                    if (isOpenBook) {
                        MessageBox.showWaitDialog(this, "正在读取文章内容...");
                    }
                    // 获取当前章节
                    chapter(cid, isOpenBook);

                } else {
                    MessageBox.show("手机无网络");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void delete(int cid) {
        BaseData.setCache("chapterJson" + mAid + cid, null);
        mCid = 0;
        String fileName = String.format(Locale.CHINA, "%d_%d_%d.txt", mUid, mAid, cid);
        String fullPath = savePath + "/" + fileName;
        FileUtils.deletFile(fullPath);

    }


    boolean isOpenBook;

    /**
     * 章节内容,从网络获取,并显示
     *
     * @param cid    章节ID
     * @param isOpen 是否打开   true 打开
     */
    private void chapter(int cid, final boolean isOpen) {

        if (mOkHttpClient == null) {
            setOkHttp();
        }

        String sign = CommonUtil.getMd5Sign(mAid, cid);   // md5(秘钥#作品ID#章节ID)
        String url = String.format(Locale.CHINA, "%s/chapter.php?aid=%d&cid=%d&uid=%d&sign=%s", SERVICE_URL, mAid, cid, mUid, sign);

        log("获取内容===url====" + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MessageBox.show("网路错误");

                        MessageBox.hideWaitDialog();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                ReadActivity.this.isOpenBook = isOpen;

                log("缓存章节==="+str);

                Message message = mHandler.obtainMessage();
                message.obj = str;
                message.what = CODE555;
                mHandler.sendMessage(message);

            }

        });
    }


    /**
     * 选择是否绑定书籍，或者购买vip章节
     *
     * @param chapterInfo
     * @param open
     */
    private void choose(ChapterInfo chapterInfo, boolean open) {
        /// 从对方目录跳转
        if (type == 2) {
            int my = chapterInfo.getIsmy();
            int vip = chapterInfo.getIsvip();

            if (my == 0 && vip == 1) {

                showVipBy(mAid, mCid);

            } else {

                /// 绑定书籍
                bindBook(chapterInfo, open);

                type = 1;
            }

        } else {

            bindBook(chapterInfo, open);

        }
    }

    /**
     * 目录点击事件
     *
     * @param directoryInfo 目录对象
     */
    public void onDirectoryInfoItemClick(DirectoryInfo directoryInfo) {
        buttonBuy.setVisibility(View.GONE);
        log("directoryInfo:" + directoryInfo);
        int isMyVip = directoryInfo.getIsvip();

        /// 是vip,且没有登录，判断是否登录.
        if (isMyVip == 1 && (mUid == 0 || mUid == -1)) {

            MessageBox.show("请登录");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ReadActivity.this.finish();
                }
            }, 1500);

            closeDrawer();

        } else {

            try {
                closeDrawer();

                if (directoryInfo != null) {

                    ReadActivity.mOldProgress = 0;
                    int chapterid = directoryInfo.getChapterid(); //章节ID
                    int chaptertype = directoryInfo.getChaptertype();//该章节是否是卷名0:一般章节 1:卷
                    int isvip = directoryInfo.getIsvip();     // 该章节是否vip:0:免费 1:vip章节
                    int isMy = directoryInfo.getIsmy();       // 我是否已购买 vip
                    String saleprice = directoryInfo.getSaleprice();//vip章节的价格，免费章 节时为0
                    int words = directoryInfo.getWords();    //章节字数

                    if (chaptertype == 0) {

                        if (isvip == 0) {
                            // 免费章节
                            getChapter(chapterid, true);

                        } else if (isvip == 1) {
                            if (isMy == 0) {
                                // 本地存在 该cid
                                boolean hhh = DbUtil.getInstence(ReadActivity.this).isHas(chapterid, mAid, mUid);


                                //Lsrun 判断是否缓存章节内容
                                int lsrun_my = isDownloadByApicloud(mUid,mAid,chapterid);


                                if (hhh) {
                                    // 直接阅读
                                    getChapter(chapterid, true);

                                } else {
                                    // 未购买 提示 购买
                                    showVipBy(mAid, chapterid);

                                }

                            } else if (isMy == 1) {
                                // 直接阅读
                                getChapter(chapterid, true);
                            }

                        }

                    } else {
                        closeDrawer();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 提示需要购买该章节，点击跳转到购买界面
     */

    private void showVipBy(final int aid, final int cid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("该章节为VIP章节，是否付费购买?").setPositiveButton("购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                log("wwwwww我的用户信息===" + mUid);

                Intent intent = new Intent(ReadActivity.this, BuyActivity.class);
                intent.putExtra("aid", aid);
                intent.putExtra("cid", cid);
                intent.putExtra("uid", mUid);
                intent.putExtra("url", SERVICE_URL);

                startActivityForResult(intent, 789);

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (type == 2) {
                    ReadActivity.this.finish();
                }

            }
        }).show();
    }

    public void initDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        } else {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        }
    }


    //改变显示模式
    public void changeDayOrNight() {
        if (mDayOrNight) {
            mDayOrNight = false;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        } else {
            mDayOrNight = true;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        }
        config.setDayOrNight(mDayOrNight);
        pageFactory.setDayOrNight(mDayOrNight);
    }

    /**
     * 上一章
     */
    public void preChapter() {
        mOldProgress = 0;
        try {
            previousCid = mCurrentChapterInfo.getPrevious();

            if (previousCid == 0) {
                MessageBox.show("已到达第一章");

            } else {

                pageFactory.preChapter();
                int indexVip = bookList.getPrevip();
                int indexMy = bookList.getPremy();
                previousCid = bookList.getPrevious();

                boolean bbb = DbUtil.getInstence(this).isHas(previousCid, mAid, mUid);

                boolean isDing = Config.getIsDing(this, mUid, mAid);
                if (isDing) {

                    getChapter(previousCid, true);

                    DbUtil.getInstence(this).save(previousCid, mAid, mUid);

                } else {
                    if (bbb) {
                        getChapter(previousCid, true);
                    } else {
                        if (indexVip == 1) {

                            //Lsrun 判断是否缓存章节内容
                            int lsrun_my = isDownloadByApicloud(mUid,mAid,previousCid);

                            if (indexMy == 1) {
                                getChapter(previousCid, true);
                            } else {
                                showVipBy(mAid, bookList.getPrevious());
                            }
                        } else {
                            getChapter(previousCid, true);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 下一章,判断是否需要vip
     */
    public void nextChapter() {
        mOldProgress = 0;
        try {
            if (mCurrentChapterInfo != null) {
                int next = mCurrentChapterInfo.getNext();
                if (nextCid == 0) {
                    MessageBox.show("已到达最后一章");

                } else {
                    pageFactory.nextChapter();

                    int indexVip = bookList.getNextvip();
                    int indexMy = bookList.getNextmy();

                    nextCid = bookList.getNext();

                    boolean isDing = Config.getIsDing(this, mUid, mAid);

                    log("是否自动订阅====" + isDing);

                    if (isDing) {
                        boolean hhh = DbUtil.getInstence(this).isHas(nextCid, mAid, mUid);
                        log("自动购买余额判断+++" + hhh);
                        if(hhh) {
                            // 自动订阅数据
                            getChapter(nextCid, true);

                            DbUtil.getInstence(this).save(nextCid, mAid, mUid);
                        }else{
                            MessageBox.show("余额不足");
                        }

                    } else {
                        boolean hhh = DbUtil.getInstence(this).isHas(nextCid, mAid, mUid);

                        log("是否自动订阅====" + hhh + "---------" + indexMy);

                        if (hhh) {
                            // 本地提示已购买
                            getChapter(nextCid, true);
                        } else {
                            // 是vip
                            if (indexVip == 1) {

                                //Lsrun 判断是否缓存章节内容
                                int lsrun_my = isDownloadByApicloud(mUid,mAid,nextCid);

                                // 已购买
                                if (indexMy == 1 ) {
                                    getChapter(nextCid, true);
                                } else {
                                    // 未购买
                                    showVipBy(mAid, bookList.getNext());
                                }
                            } else {
                                // 不是vip
                                getChapter(nextCid, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.tv_pre) {
            // 上一章
            preChapter();
        } else if (id == R.id.tv_next) {
            // 下一章
            nextChapter();
        } else if (id == R.id.tv_directory) {
            //目录
            //打开抽屉菜单
            openDrawer();
        } else if (id == R.id.tv_dayornight) {
            changeDayOrNight();
            hideTopBar();
            hideSystemUI();
        } else if (id == R.id.tv_pagemode) {
            hideReadSetting();
            hideTopBar();
            hideSystemUI();
            mPageModeDialog.show();
        } else if (id == R.id.tv_setting) {
            hideReadSetting();
            hideTopBar();
            hideSystemUI();

            mSettingDialog.show();
        } else if (id == R.id.tv_download) {
            // 下载
            hideReadSetting();
            hideTopBar();
            hideSystemUI();

            chapterblock();

        } else if (id == R.id.tv_auto_subscribe) {
            // 自动订阅

            autobuy();

        } else if (id == R.id.tv_buy) {

            // 购买

            hideReadSetting();
            hideTopBar();
            hideSystemUI();

            chapterblock();

        } else if (id == R.id.tv_award) {
            // 打赏
            mPayGiftDialog.show();
            hideSystemUI();
            hideTopBar();
            hideSystemUI();
            hideReadSetting();

        } else if (id == R.id.img_bookmark) {
            // 书签
            int act = addOrRemoveBookMark();
            //添加或移除书签
            switch (act) {
                case 0:
                    img_bookmark.setImageResource(R.mipmap.mo_aotao_reader_icon_book_mark_1);
                    MessageBox.show("添加书签成功");
                    break;
                case 1:
                    img_bookmark.setImageResource(R.mipmap.mo_aotao_reader_icon_book_mark);
                    MessageBox.show("删除书签成功");
                    break;
            }


        } else if (id == R.id.tv_menu) {
            // 菜单

            Intent intentBroadcast = new Intent(APIModuleReader.ACTION_NAME);
            intentBroadcast.putExtra("type", "menu");
            sendBroadcast(intentBroadcast);

        } else if (id == R.id.tv_auto_close) {

            if (isTCCode == 1) {

                changeTuc("delete");


            } else if (isTCCode == 0) {

                changeTuc("add");

            }

        }

    }


    /**
     * 吐槽按钮是否改变，是否点击吐槽按钮
     */
    boolean isTuChange = false;

    /**
     * 改变吐槽的状态
     */
    private void changeTuc(final String str) {

        MessageBox.showWaitDialog(ReadActivity.this, "处理中");

        String url = String.format(Locale.CHINA, "%s/openSwitch.php?uid=%d&act=%s&ajaxapp=1&ajax_request&sign=%s",

                SERVICE_URL, mUid, str, MD5Util.GetMD5Code(String.format(Locale.CHINA, "%s#%d", CommonUtil.PRIVATE_KEY, mUid)));


        log("result-----" + url);

        if (mOkHttpClient == null) {
            setOkHttp();
        }

        mOkHttpClient.newCall(new Request.Builder().get().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message message = mHandler.obtainMessage();
                message.what = CODE666;
                message.obj = result;
                mHandler.sendMessage(message);


            }
        });


    }

    /**
     * 自动订阅按钮更改
     *
     * @param act    操作参数(自动订阅:setautobuy，取消自动订阅:unsetautobuy)
     * @param status 1：成功；0：失败
     */
    private void changetv_auto_subscribe(final String act, final int status) {

        log("执行订阅=====");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tv_auto_subscribe != null) {
                    if (status == 1) {
                        if ("setautobuy".equals(act)) {

                            // 自动订阅
                            ReadActivity.act = "unsetautobuy";

                            tv_auto_subscribe.setText("取消自动订阅");

                            Config.saveDing(ReadActivity.this, mUid, mAid, true);

                        } else if ("unsetautobuy".equals(act)) {
                            // 取消自动订阅

                            ReadActivity.act = "setautobuy";

                            tv_auto_subscribe.setText("自动订阅");

                            /// 不自动订阅
                            Config.saveDing(ReadActivity.this, mUid, mAid, false);

                        } else {
                            tv_auto_subscribe.setText("未登录");
                        }
                    }
                }
            }
        });

    }


    /**
     * 添加书签
     */
    private int addOrRemoveBookMark() {
        int act = 0;
        try {
            List<BookMark> bookMarkList = getBookMarkList();
            //获取一次书签列表
            if (bookMarkList == null) {

                bookMarkList = new ArrayList<>();
            }

            BookMark bookMark = new BookMark();
            bookMark.setUid(mUid);
            bookMark.setAid(mAid);
            bookMark.setCid(currentCid);
            bookMark.setProgress(mProgress);
            bookMark.setPage(mPage);
            bookMark.setContent(mCurrentPageContent);

            if (mCurrentChapterInfo != null) {
                bookMark.setArticlename(mCurrentChapterInfo.getArticlename());
                bookMark.setChapterName(mCurrentChapterInfo.getChaptername());
            }

            boolean isExist = isExistBookMark(bookMark);

            if (!isExist) {
                bookMarkList.add(bookMark);

                String readCacheJson = JsonConvertor.toJson(bookMarkList);
                CommFunAndroid.setSharedPreferences(READ_BOOK_MARK + mUid + mAid, readCacheJson);
            } else {
                act = 1;
                removeBookMark(bookMark);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return act;

    }


    /**
     * 获取书签列表
     *
     * @return
     */
    private List<BookMark> getBookMarkList() {

        String read_book_mark_cache = CommFunAndroid.getSharedPreferences(READ_BOOK_MARK + mUid + mAid);

        try {
            if (!CommFun.isNullOrEmpty(read_book_mark_cache)) {
                List<BookMark> bookMarkList = JsonConvertor.fromJson(read_book_mark_cache, new TypeToken<List<BookMark>>() {
                }.getType());
                return bookMarkList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isExistBookMark(BookMark bookMark) {

        try {
            if (bookMark != null) {
                List<BookMark> bookMarkList = getBookMarkList();
                if (bookMarkList != null && !bookMarkList.isEmpty()) {
                    for (BookMark item :
                            bookMarkList) {
                        if (item != null) {
                            if (CommFun.isEquals(item.getIds(), bookMark.getIds())) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private BookMark getBookMarkByPage(BookMark bookMark) {

        List<BookMark> bookMarkList = getBookMarkList();

        if (bookMarkList != null) {
            for (BookMark item :
                    bookMarkList) {
                if (item != null) {
                    if (item.getPage() == bookMark.getPage() && Math.abs((item.getProgress() * 10000) - (bookMark.getProgress() * 10000)) < 50 && item.getCid() == mCid && item.getAid() == mAid && item.getUid() == mUid) {
                        return bookMark;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 移除书签
     *
     * @param bookMark
     */
    private void removeBookMark(BookMark bookMark) {

        try {
            if (bookMark != null) {
                List<BookMark> bookMarkList = getBookMarkList();

                List<BookMark> bookMarkList_new = new ArrayList<>();

                if (bookMarkList != null && !bookMarkList.isEmpty()) {
                    for (BookMark item :
                            bookMarkList) {
                        if (item != null) {
                            if (!CommFun.isEquals(item.getIds(), bookMark.getIds())) {
                                bookMarkList_new.add(item);
                            }
                        }
                    }
                }
                if (!bookMarkList_new.isEmpty()) {
                    String readCacheJson = JsonConvertor.toJson(bookMarkList_new);
                    CommFunAndroid.setSharedPreferences(READ_BOOK_MARK + mUid + mAid, readCacheJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getToken() {
        if (CommFun.isNullOrEmpty(TOKEN))
            getToken(mUid);

        return TOKEN;
    }

    public static void setToken(String token) {
        TOKEN = token;
    }

    /**
     * 获取token
     *
     * @param uid
     * @return
     */
    public static void getToken(int uid) {

        OkHttpClient mOkHttpClient = new OkHttpClient();

        String url = String.format(Locale.CHINA, "%s/user.php?uid=%d", SERVICE_URL, mUid);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                if (!CommFun.isNullOrEmpty(str)) {

                    try {
                        ArrayMap<String, String> arrayMap = JsonConvertor.fromJson(str, new TypeToken<ArrayMap<String, String>>() {
                        }.getType());
                        if (arrayMap != null) {
                            setToken(arrayMap.get("token"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        });
    }


    /**
     * 添加想法
     *
     * @param content
     * @param paragraphId
     */
    public void sendreview(String content, final int paragraphId) {
        log("想法位置-----" + paragraphId);

        if (mOkHttpClient == null) {
            setOkHttp();
        }

        String sign = CommonUtil.getMd5Sign_sendreview(mAid, mCid, paragraphId);

        String url = String.format(Locale.CHINA, "%s/sendreview.php?aid=%d&cid=%d&uid=%d&did=%d&progress=%d&pcontent=%s&act=newpost&ajaxapp=1&ajax_request=1&sign=%s",
                SERVICE_URL, mAid, mCid, mUid, paragraphId, (int) (mProgress * 100), content, sign);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                Message message = mHandler.obtainMessage();
                message.obj = str;
                message.what = CODE777;
                mHandler.sendMessage(message);
            }

        });
    }

    /**
     * 段落标记点，点击获取想法
     *
     * @param did
     */
    public void getReviews(int did) {

        String str = CommFunAndroid.getSharedPreferences(READ_BOOK_COMMETLIST + mUid + mAid + did);

        reviews(did, 1, 10);

    }

    FlagListPopupwindow popupwindow;

    private void openCommentListPop(int did) {

        log("想法id-------" + did);

        try {
            if (did != 0) {
                popupwindow = new FlagListPopupwindow(ReadActivity.this, mUid, mAid, mCid, did);
                popupwindow.setPopupWindowListener(new FlagListPopupwindow.PopupWindowListener() {
                    @Override
                    public void dismiss() {
                        hideSystemUI();
                    }

                    @Override
                    public void addComment(int did) {
                        openAddCommentPopupwindow(did);
                    }
                });

                popupwindow.updateData(did);

                popupwindow.showPopupWindow(bookpage);

                if (isFastShow()) {

                } else {

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加想法弹窗
     *
     * @param did
     */
    private void openAddCommentPopupwindow(final int did) {
        log("想法弹窗id=====" + did);

        final AddCommentPopupwindow popupwindow = new AddCommentPopupwindow(ReadActivity.this, null);
        popupwindow.setPopupWindowListener(new AddCommentPopupwindow.PopupWindowListener() {

            @Override
            public void sendText(EditText editText, String text) {
                if (!CommFun.isNullOrEmpty(text)) {
                    sendreview(text, did);

                    /// 隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }


                }
            }

            @Override
            public void dismiss() {
                hideSystemUI();
            }
        });

        popupwindow.setOnCloseListener(new AddCommentPopupwindow.OnCloseListener()

        {
            @Override
            public void close() {
                popupwindow.dismiss();
            }
        });

        popupwindow.showPopupWindow(bookpage);
    }

    private static long lastShowTime;


    /**
     * 防止频繁提示相同消息
     *
     * @return
     */
    public synchronized static boolean isFastShow() {
        long time = System.currentTimeMillis();
        if (time - lastShowTime < 2000) {
            return true;
        }
        lastShowTime = time;
        return false;
    }


    /**
     * 想法读取
     *
     * @param did
     * @param page
     * @param pageSize
     */
    public void reviews(final int did, int page, int pageSize) {

        OkHttpClient mOkHttpClient = new OkHttpClient();

        MessageBox.showWaitDialog(this, "数据加载中...");

        String url = String.format(Locale.CHINA, "%s/reviews.php?aid=%d&cid=%d&did=%d&page=%d&pageSize=%d",
                SERVICE_URL, mAid, mCid, did, page, pageSize);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                MessageBox.hideWaitDialog();
                MessageBox.show("想法加载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MessageBox.hideWaitDialog();

                        if (!CommFun.isNullOrEmpty(str)) {
                            CommFunAndroid.setSharedPreferences(READ_BOOK_COMMETLIST + mUid + mAid + did, str);
                        }

                        getCommentModel(did, str);

                    }
                });
            }

        });
    }

    private void getCommentModel(int did, String str) {
        if (!CommFun.isNullOrEmpty(str)) {
            CommentModel commentModel = JsonConvertor.fromJson(str, CommentModel.class);
            if (commentModel != null) {

                final List<CommentInfo> data = commentModel.getData();
                if (data != null && data.size() > 0) {

                }

                openCommentListPop(did);
            }
        }
    }


    /**
     * 想法数量获取
     */
    public void reviewscount(final int aid, final int cid) {
        if (mOkHttpClient == null) {
            setOkHttp();
        }

        String url = String.format(Locale.CHINA, "%s/reviewscount.php?aid=%d&cid=%d", SERVICE_URL, aid, cid);

        log("段落数据-----" + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message message = mHandler.obtainMessage();
                message.what = CODE888;
                message.obj = str;
                mHandler.sendMessage(message);

            }

        });
    }

    /**
     * 获取段落条数
     *
     * @return 段落对象条数
     */
    private List<ViewCount> getViewCountList(String jsonStr) {

        log("段落对象-----" + jsonStr);

        List<ViewCount> viewCountList = null;
        try {
            if (!CommFun.isNullOrEmpty(jsonStr)) {

                ViewCountModel viewCountModel = JsonConvertor.fromJson(jsonStr, ViewCountModel.class);
                if (viewCountModel != null) {
                    viewCountList = viewCountModel.getData();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewCountList;

    }

    /**
     * 操作参数(自动订阅:setautobuy，取消自动订阅:unsetautobuy)
     */
    static String act = "setautobuy";


    /**
     * 自动订阅 取消 与 生成
     */
    public void autobuy() {

        OkHttpClient mOkHttpClient = new OkHttpClient();

        String sign = CommonUtil.getMd5Sign_autobuy(mAid, mUid);

        String url = String.format(Locale.CHINA, "%s/autobuy.php?aid=%d&uid=%d&act=%s&ajaxapp=1&ajax_request=1&sign=%s",
                SERVICE_URL, mAid, mUid, act, sign);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                if (!CommFun.isNullOrEmpty(str)) {
                    ResponseBook responseBook = JsonConvertor.fromJson(str, ResponseBook.class);
                    if (responseBook != null) {
                        final int status = responseBook.getStatus();
                        final String msg = responseBook.getMsg();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (status == 1) {

                                    changetv_auto_subscribe(act, 1);

                                }

                                MessageBox.show(msg);

                            }
                        });
                    }

                }
            }

        });
    }


    /**
     * 打赏
     *
     * @param giftInfo 礼物对象
     */
    public void paygift(GiftInfo giftInfo) {

        if (giftInfo == null) {
            return;
        }

        MessageBox.showWaitDialog(this, "服务器处理中...");

        OkHttpClient mOkHttpClient = new OkHttpClient();

        String sign = CommonUtil.getMd5Sign_paygift(mAid, giftInfo.getRid(), mUid);

        String url = String.format(Locale.CHINA, "%s/paygift.php?id=%d&rid=%d&uid=%d&count=%d&act=post&ajaxapp=1&ajax_request=1&sign=%s",

                SERVICE_URL, mAid, giftInfo.getRid(), mUid, giftInfo.getCount(), sign);


        log("打赏-----" + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                MessageBox.hideWaitDialog();
                MessageBox.show("服务器错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();

                MessageBox.hideWaitDialog();

                if (!CommFun.isNullOrEmpty(str)) {


                    ResponseBook responseBook = JsonConvertor.fromJson(str, ResponseBook.class);
                    if (responseBook != null) {
                        final int status = responseBook.getStatus();
                        final String msg = responseBook.getMsg();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (status == 1) {

                                }

                                MessageBox.show(msg);
                                MessageBox.hideWaitDialog();
                            }
                        });
                    }

                }
            }

        });
    }


    /**
     * 获取下载章节信息
     */
    public void chapterblock() {

        MessageBox.showWaitDialog(this, "数据加载中...");

        if (mOkHttpClient == null) {
            setOkHttp();
        }

        String url = String.format(Locale.CHINA, "%s/chapterblock.php?aid=%d&uid=%d", SERVICE_URL, mAid, mUid);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                MessageBox.hideWaitDialog();
                MessageBox.show("服务器错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();

                log("数据下载返回-----"+str);

                MessageBox.hideWaitDialog();

                if (!CommFun.isNullOrEmpty(str)) {

                    BaseData.setCache(READ_BOOK_CHAPTERBLOCK + mAid + mUid, str);

                    final ChapterBlockModel chapterBlockModel = JsonConvertor.fromJson(str, ChapterBlockModel.class);
                    if (chapterBlockModel != null) {
                        final int status = chapterBlockModel.getStatus();
                        final String msg = chapterBlockModel.getMsg();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (status == 1) {

                                    openDownloadBookDialog(chapterBlockModel);
                                }

                                MessageBox.show(msg);

                            }
                        });
                    }

                }
            }

        });
    }


    ChapterBlockModel mChapterBlockModel;


    /**
     * 打开下载弹窗
     *
     * @param chapterBlockModel 下载的对象
     */
    private void openDownloadBookDialog(ChapterBlockModel chapterBlockModel) {

        mChapterBlockModel = chapterBlockModel;

        mDownloadBookDialog = new DownloadBookDialog(this,chapterBlockModel.getData());

        mDownloadBookDialog.show();

        mDownloadBookDialog.setChapterBlockModel(chapterBlockModel);

        mDownloadBookDialog.setDialogListener(new DownloadBookDialog.DialogListener() {
            @Override
            public void itemClick(ChapterBlockInfo chapterBlockInfo) {

                if (chapterBlockInfo != null) {

                    float account = 0f;
                    //余额
                    if (mChapterBlockModel != null) {
                        // 我的总余额
                        String egold = mChapterBlockModel.getEgold();

                        if (!CommFun.isNullOrEmpty(egold)) {
                            account = Float.parseFloat(egold);
                        }
                    }

                    float sumPrice = chapterBlockInfo.getSumprice();

                    if (sumPrice > account) {

                        openRechargeDialog();

                    } else {

                        chapterblockdownload(chapterBlockInfo);

                    }
                }
            }

            /**
             * 单章集合下载
             * @param list  选中的集合数据
             */
            @Override
            public void onOneDownLoad(List<ChapterBlockInfo> list) {
                down(list);

            }
        });
    }

    /**
     * 单章批量下载
     *
     * @param list 选中的集合数据
     */
    private void down(List<ChapterBlockInfo> list) {
        MessageBox.showWaitDialog(this, "数据加载中...");
        if (mOkHttpClient == null) {
            setOkHttp();
        }

        String sign = CommonUtil.getMd5Sign_autobuy(mAid, mUid);
        StringBuilder sb = new StringBuilder();
        for (ChapterBlockInfo temp : list) {
            sb.append(temp.getCid());
            sb.append(",");
        }

        String resultStr = sb.toString();
        resultStr = resultStr.substring(0, resultStr.length() - 1);

        log("结果-------" + resultStr);

        String url = String.format(Locale.CHINA, "%s/chapterselectdownload.php?aid=%d&uid=%d&orderlist=%s&sign=%s",
                SERVICE_URL, mAid, mUid, resultStr, sign);

        log("单章下载链接-----" + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                MessageBox.hideWaitDialog();
                MessageBox.show("服务器错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message message = mHandler.obtainMessage();
                message.what = CODE999;
                message.obj = str;
                mHandler.sendMessage(message);

            }

        });

    }


    /**
     * 多章下载
     */
    public void chapterblockdownload(final ChapterBlockInfo chapterBlockInfo) {

        MessageBox.showWaitDialog(this, "数据加载中...");

        OkHttpClient mOkHttpClient = new OkHttpClient();

        String sign = CommonUtil.getMd5Sign_autobuy(mAid, mUid);    // md5(秘钥#作品ID#用户ID)

        int fromOrder = chapterBlockInfo.getFromOrder();
        int toOrder = chapterBlockInfo.getToOrder();

        String url = String.format(Locale.CHINA, "%s/chapterblockdownload.php?aid=%d&uid=%d&fromOrder=%d&toOrder=%d&sign=%s",
                SERVICE_URL, mAid, mUid, fromOrder, toOrder, sign);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                MessageBox.hideWaitDialog();
                MessageBox.show("服务器错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Message message = mHandler.obtainMessage();
                message.what = CODE999;
                message.obj = str;
                mHandler.sendMessage(message);

            }

        });
    }


    /**
     * 打开充值页面充值
     */
    private void openRechargeDialog() {
        rechargeDialog = new RechargeDialog(this);
        rechargeDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
            pageFactory.prePage();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
            pageFactory.nextPage();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            /// 保存当前阅读进度到本地数据库
            DbUtil.getInstence(this).saveProgress(mUid, mAid, mCid, mPage, mProgress);

            log("执行保存数据");

            ///  退出当前页面
            ReadActivity.this.finish();

            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 判断APIcloud是否已经下载该章节
     * Lsrun
     * 20180410
     * @param uid
     * @param aid
     * @param cid
     * @return 整型,是否已购买并下载
     */
    public int isDownloadByApicloud(int uid,int aid,int cid){
        //Lsrun 判断是否缓存章节内容
        int lsrun_my = 0;
        String chapterJson = BaseData.getCache("chapterJson" + aid + cid);  /// 读取章节缓存
        if (CommFun.isNullOrEmpty(chapterJson)) {
            /// 缓存为空
            String fileName = String.format(Locale.CHINA, "%d_%d_%d.txt", uid, aid, cid);
            String fullPath = savePath + "/" + fileName;
            chapterJson = FileUtils.readFile(fullPath);

            log("执行读取文件------");

        }
        log("读取到的文件======" + chapterJson);
        if (!CommFun.isNullOrEmpty(chapterJson)) {
            ///  数据存在
            ChapterInfo chapterInfo = getChapterInfo(chapterJson);
            lsrun_my = chapterInfo.getIsmy();
        }
        return lsrun_my;
    }


}
