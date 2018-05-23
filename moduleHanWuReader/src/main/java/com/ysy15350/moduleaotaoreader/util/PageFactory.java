package com.ysy15350.moduleaotaoreader.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.WindowManager;
import android.widget.Toast;

import com.ysy15350.moduleaotaoreader.Config;
import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.db.BookList;
import com.ysy15350.moduleaotaoreader.db.DbUtil;
import com.ysy15350.moduleaotaoreader.dialog.SettingDialog;
import com.ysy15350.moduleaotaoreader.model.FlagInfo;
import com.ysy15350.moduleaotaoreader.model.ParagraphInfo;
import com.ysy15350.moduleaotaoreader.model.ViewCount;
import com.ysy15350.moduleaotaoreader.view.PageWidget;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import base.data.BaseData;
import common.CommFun;
import common.message.MessageBox;
import common.string.JsonConvertor;


public class PageFactory {

    private static final String TAG = "PageFactory";
    private static PageFactory pageFactory;

    private static Status mStatus = Status.OPENING;
    private static Status mCacheStatus = Status.OPENING;
    private Context mContext;
    private Config config;

    //当前是否为第一页
    private boolean m_isfirstPage;
    //当前是否为最后一页
    private boolean m_islastPage;

    //书本widget
    private PageWidget mBookPageWidget;


    //页面宽
    private int mWidth;

    //页面高
    private int mHeight;

    //文字字体大小
    private float m_fontSize;

    //时间格式
    private SimpleDateFormat sdf;

    //时间
    private String date;

    //进度格式
    private DecimalFormat df;
    //电池边界宽度
    private float mBorderWidth;

    // 上下与边缘的距离
    private float marginHeight;

    // 左右与边缘的距离
    private float measureMarginWidth;

    // 左右与边缘的距离
    private float marginWidth;

    //状态栏距离底部高度
    private float statusMarginBottom;

    // 行间距
    private float lineSpace;

    //段间距
    private float paragraphSpace;

    //字体
    private Typeface typeface;

    //文字画笔
    private Paint mPaint;

    //加载画笔
    private Paint waitPaint;

    //段落画笔
    private Paint paragraphPaint;

    //文字颜色
    private int m_textColor = Color.rgb(50, 65, 78);

    //文字颜色
    private int m_commentcounttextColor = Color.rgb(255, 255, 255);

    // 绘制内容的宽
    private float mVisibleHeight;

    // 绘制内容的宽
    private float mVisibleWidth;

    // 每页可以显示的行数
    private int mLineCount;

    private Intent batteryInfoIntent;

    //电池画笔
    private Paint mBatterryPaint;

    //章节标题（内容）画笔
    private Paint mChapterTitlePaint;

    //评论数字画笔
    private Paint mCommentCountPaint;

    //电池字体大小
    private float mChapterTitleFontSize;

    //电池字体大小
    private float mBatterryFontSize;

    //电池电量百分比
    private float mBatteryPercentage;

    //电池外边框
    private RectF rect1 = new RectF();

    //电池内边框
    private RectF rect2 = new RectF();

    //背景图片
    private Bitmap m_book_bg = null;

    public static BookUtil mBookUtil;

    private PageEvent mPageEvent;

    /**
     * 当前阅读界面对象
     */
    private TRPage currentPage;
    private TRPage prePage;
    private TRPage nextPage;
    private TRPage cancelPage;

    private int mPageIndex = 0;


    //书本名字
    private String bookName = "";
    //章节名称
    private String chapterName = "";

    private List<ViewCount> mViewCountList;

    private BookList bookList;

    private BookList bookListCache;

    private int mAid;
    private int mCid;
    private int mPrevious;
    private int mNext;

    private BookList bookListTemp;

    //书本章节
    private int currentCharter = 0;

    //当前电量
    private int level = 0;

    //现在的进度
    private float currentProgress;

    private BookTask bookTask;

    private BookPageTask cacheTask;

    ContentValues values = new ContentValues();


    public static synchronized PageFactory getInstance() {
        return pageFactory;
    }

    public static synchronized PageFactory createPageFactory(Context context) {
        if (pageFactory == null) {
            pageFactory = new PageFactory(context);
        }
        return pageFactory;
    }

    private PageFactory(Context context) {
        mContext = context.getApplicationContext();
        mBookUtil = new BookUtil();
        config = Config.getInstance();

        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mWidth = metric.widthPixels;
        mHeight = metric.heightPixels;

        sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);    //HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new java.util.Date());
        df = new DecimalFormat("#0.0");

        marginWidth = mContext.getResources().getDimension(R.dimen.readingMarginWidth);
        marginHeight = mContext.getResources().getDimension(R.dimen.readingMarginHeight);
        statusMarginBottom = mContext.getResources().getDimension(R.dimen.reading_status_margin_bottom);
        lineSpace = context.getResources().getDimension(R.dimen.reading_line_spacing);
        paragraphSpace = context.getResources().getDimension(R.dimen.reading_paragraph_spacing);
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - marginHeight * 2;

        typeface = config.getTypeface();

        m_fontSize = config.getFontSize();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(m_fontSize);// 字体大小
        mPaint.setColor(m_textColor);// 字体颜色
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        waitPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        waitPaint.setTextSize(mContext.getResources().getDimension(R.dimen.reading_max_text_size));// 字体大小
        waitPaint.setColor(m_textColor);// 字体颜色
        waitPaint.setTypeface(typeface);
        waitPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        paragraphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        paragraphPaint.setAntiAlias(true);                       //设置画笔为无锯齿
        paragraphPaint.setColor(Color.parseColor("#ffb6ca"));                    //设置画笔颜色
        paragraphPaint.setStrokeWidth((float) 3.0);              //线宽
        paragraphPaint.setStyle(Paint.Style.FILL_AND_STROKE);                   //空心效果


        calculateLineCount();

        mBorderWidth = mContext.getResources().getDimension(R.dimen.reading_board_battery_border_width);
        mBatterryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatterryFontSize = CommonUtil.sp2px(context, 12);
        mBatterryPaint.setTextSize(mBatterryFontSize);
        mBatterryPaint.setTypeface(typeface);
        mBatterryPaint.setTextAlign(Paint.Align.LEFT);
        mBatterryPaint.setColor(m_textColor);

        mChapterTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChapterTitleFontSize = m_fontSize + 8;
        mChapterTitlePaint.setTextSize(mChapterTitleFontSize);
        mChapterTitlePaint.setTypeface(typeface);
        mChapterTitlePaint.setTextAlign(Paint.Align.LEFT);
        mChapterTitlePaint.setColor(m_textColor);

        mCommentCountPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCommentCountPaint.setTextSize(CommonUtil.sp2px(context, 10));
        mCommentCountPaint.setTypeface(typeface);
        mCommentCountPaint.setTextAlign(Paint.Align.LEFT);
        mCommentCountPaint.setColor(m_commentcounttextColor);


        batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//注册广播,随时获取到电池电量信息

        initBg(config.getDayOrNight());
        measureMarginWidth();

    }


    public enum Status {
        OPENING,
        FINISH,
        FAIL,
    }


    private void measureMarginWidth() {
        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = marginWidth + width / 2;


    }


    /**
     * 初始化背景
     *
     * @param isNight
     */
    private void initBg(Boolean isNight) {
        if (isNight) {
            // 设置背景
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.BLACK);
            setBgBitmap(bitmap);
            //设置字体颜色
            setM_textColor(Color.rgb(128, 128, 128));
            setBookPageBg(Color.BLACK);
        } else {
            //设置背景
            setBookBg(config.getBookBgType());
        }
    }

    public void setBookPageBg(int color) {
        if (mBookPageWidget != null) {
            mBookPageWidget.setBgColor(color);
        }
    }


    //更新电量
    public void updateBattery(int mLevel) {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            if (level != mLevel) {
                level = mLevel;
                currentPage(false);
            }
        }
    }

    public void updateTime() {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            String mDate = sdf.format(new java.util.Date());
            if (date != mDate) {
                date = mDate;
                currentPage(false);
            }
        }
    }


    /**
     * 改变进度
     *
     * @param progress 进度
     */
    public void changeProgress(float progress) {
        long begin = (long) (mBookUtil.getBookLen(bookList.getAid(), bookList.getCid()) * progress);

        currentPage = getPageForBegin(begin);

        currentPage(true);

    }


    /**
     * 改变进度
     *
     * @param begin
     */
    public void changeChapter(long begin) {
        currentPage = getPageForBegin(begin);
        currentPage(true);
    }

    public void changeLineSpace(float lineSpace) {
        this.lineSpace = lineSpace;
        calculateLineCount();
        measureMarginWidth();
        //currentPage = getPageByPageIndex(mPageIndex);
        currentPage(true);
    }


    /**
     * 改变字体大小
     *
     * @param fontSize 字体大小
     */
    public void changeFontSize(int fontSize) {
        this.m_fontSize = fontSize;
        mPaint.setTextSize(m_fontSize);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(currentPage.getBegin());
        currentPage(true);

    }

    /**
     * 改变字体
     *
     * @param typeface 字体类型
     */
    public void changeTypeface(Typeface typeface) {
        this.typeface = typeface;
        mPaint.setTypeface(typeface);
        mBatterryPaint.setTypeface(typeface);
        calculateLineCount();
        measureMarginWidth();
        //currentPage = getPageByPageIndex(mPageIndex);
        currentPage(true);
    }

    /**
     * 改变字体颜色
     *
     * @param type
     */
    public void changeTextColor(int type) {
        setTextColor(type);
        currentPage(false);
    }

    public void changeLineSpaceStyle(int type) {
        setLineSpaceStyle(type);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(currentPage.getBegin());
        currentPage(true);

    }

    //改变背景
    public void changeBookBg(int type) {
        setBookBg(type);
        currentPage(false);
    }

    //设置日间或者夜间模式
    public void setDayOrNight(Boolean isNgiht) {
        initBg(isNgiht);
        currentPage(false);
    }

    //设置字体颜色
    public void setTextColor(int type) {
        int color = 0;
        switch (type) {
            case Config.TEXT_COLOR_DEFAULT:
                color = mContext.getResources().getColor(R.color.read_font_default);
                break;
            case Config.TEXT_COLOR_1:
                color = mContext.getResources().getColor(R.color.read_font_1);
                break;
            case Config.TEXT_COLOR_2:
                color = mContext.getResources().getColor(R.color.read_font_2);
                break;
            case Config.TEXT_COLOR_3:
                color = mContext.getResources().getColor(R.color.read_font_3);
                break;
            case Config.TEXT_COLOR_4:
                color = mContext.getResources().getColor(R.color.read_font_4);
                break;
        }

        //设置字体颜色
        setM_textColor(color);
    }

    //设置行间距
    public void setLineSpaceStyle(int type) {
        switch (type) {
            case Config.LINE_SPACE_DEFAULT:
                lineSpace = mContext.getResources().getDimension(R.dimen.reading_line_spacing1);
                break;
            case Config.LINE_SPACE_1:
                lineSpace = mContext.getResources().getDimension(R.dimen.reading_line_spacing2);
                break;
            case Config.LINE_SPACE_2:
                lineSpace = mContext.getResources().getDimension(R.dimen.reading_line_spacing3);
                break;
            case Config.LINE_SPACE_3:
                lineSpace = mContext.getResources().getDimension(R.dimen.reading_line_spacing4);
                break;
            case Config.LINE_SPACE_4:
                lineSpace = mContext.getResources().getDimension(R.dimen.reading_line_spacing);
                break;
        }
    }

    //设置页面的背景
    public void setBookBg(int type) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int color = 0;
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                canvas = null;
                bitmap.recycle();
                if (getBgBitmap() != null) {
                    getBgBitmap().recycle();
                }
                bitmap = BitmapUtil.decodeSampledBitmapFromResource(
                        mContext.getResources(), R.drawable.mo_aotao_reader_paper, mWidth, mHeight);
                color = mContext.getResources().getColor(R.color.read_font_default);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_default));
                break;
            case Config.BOOK_BG_1:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_1));
                color = mContext.getResources().getColor(R.color.read_font_1);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_1));
                break;
            case Config.BOOK_BG_2:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_2));
                color = mContext.getResources().getColor(R.color.read_font_2);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_2));
                break;
            case Config.BOOK_BG_3:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_3));
                color = mContext.getResources().getColor(R.color.read_font_3);
                if (mBookPageWidget != null) {
                    mBookPageWidget.setBgColor(mContext.getResources().getColor(R.color.read_bg_3));
                }
                break;
            case Config.BOOK_BG_4:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_4));
                color = mContext.getResources().getColor(R.color.read_font_4);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_4));
                break;
        }

        setBgBitmap(bitmap);

        //设置字体颜色
        //Lsrun 设置背景时,不更改字体颜色
//        setM_textColor(color);
    }

    /**
     * 可显示的行数
     */
    private void calculateLineCount() {
        mLineCount = (int) (mVisibleHeight / (m_fontSize + lineSpace));
    }

    //是否是第一页
    public boolean isfirstPage() {
        return m_isfirstPage;
    }

    //是否是最后一页
    public boolean islastPage() {
        return m_islastPage;
    }


    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }

    //设置页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    //设置文字颜色
    public void setM_textColor(int m_textColor) {
        this.m_textColor = m_textColor;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.m_textColor;
    }

    //获取文字大小
    public float getFontSize() {
        return this.m_fontSize;
    }


    /**
     * 上一章
     */
    public void preChapter() {

//
//        if (mBookUtil.getDirectoryList().size() > 0) {
//            int num = currentCharter;
//            if (num == 0) {
//                num = getCurrentCharter();
//            }
//            num--;
//            if (num >= 0) {
//                long bookLen = mBookUtil.getBookLen();
//                int pageSize = (int) (bookLen / templengthAvg);
//                long begin = (long) ((pageSize - 1) * templengthAvg);//mBookUtil.getDirectoryList().get(num).getBookCatalogueStartPos();
//                currentPage = getPageForBegin(begin);
//                currentPage(true);
//                currentCharter = num;
//            }
//        }
//
    }


    /**
     * 下一章
     */
    public void nextChapter() {

    }


    /**
     * 获取当前的章节
     *
     * @return
     */
    public int getCurrentCharter() {
        return 0;
    }

    private void drawStatus(Bitmap bitmap) {
        String status = "";
        switch (mStatus) {
            case OPENING:
                status = "正在加载...";
                break;
            case FAIL:
                status = "加载失败！";
                break;
        }

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        waitPaint.setColor(getTextColor());
        waitPaint.setTextAlign(Paint.Align.CENTER);

        Rect targetRect = new Rect(0, 0, mWidth, mHeight);

        Paint.FontMetricsInt fontMetrics = waitPaint.getFontMetricsInt();

        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;

        waitPaint.setTextAlign(Paint.Align.CENTER);

        c.drawText(status, targetRect.centerX(), baseline, waitPaint);

        mBookPageWidget.postInvalidate();
    }


    /**
     * 文章总字数
     */
    public int total_length = 0;

    public static int templengthMax;

    public static double templengthAvg;

    /**
     * 平均数
     */
    public int templength, templengthMin, templengthTotal, templengthCount;


    /**
     * 段落 x 的偏移量
     */
    private float flag_x = 0;
    /**
     * 段落 y的偏移量
     */
    private float flag_y = 0;

    /**
     * 圆的半径
     */
    private int r = 20;

    /**
     * 坐标
     */
    Point point = new Point();

    private float lastX = 0;


    float indexLineLen = 0;

    /**
     * 绘制 界面
     *
     * @param bitmap
     * @param m_lines
     * @param updateCharter
     * @param index
     */
    public void onDraw(Bitmap bitmap, List<String> m_lines, Boolean updateCharter, int index) {

        if (index == 1) {

            BookList bookList = getBookListFromCache(currentPage.getAid(), currentPage.getCid());
            if (bookList != null) {
                this.bookList = bookList;
            }

            if (bookList != null) {
                chapterName = bookList.getChaptername();
                bookName = bookList.getBookname();
            }
        }

        //// 更新数据库进度
        if (currentPage != null && mTRPageSparseArray != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    values.put("begin", currentPage.getBegin());

                }
            }.start();
        }


        Canvas c = new Canvas(bitmap);
        /// 绘制背景
        c.drawBitmap(getBgBitmap(), 0, 0, null);

        // 标题画笔
        mChapterTitlePaint.setTextSize(getFontSize() + 4);

        mPaint.setTextSize(getFontSize());
        mPaint.setColor(getTextColor());

        mBatterryPaint.setColor(getTextColor());

        if (m_lines.size() == 0) {
            return;
        }

        StringBuilder pageContentStringBuilder = new StringBuilder();

        /// 文档长度
        int length = 0;

        // 当前页文字数量

        if (m_lines.size() > 0) {

            // 行间距高度
            float y = marginHeight;

            int indexLine = 0;   // 行索引

            StringBuilder stringBuilder = new StringBuilder();

            float temp_y = 0;
            float temp_x = 0;

            int strLen = m_lines.size();

            /// 循环绘制每一行数据
            for (int d = 0; d < strLen; d++) {
                
                String strLine = m_lines.get(d);

                /// 绘制时，去除段落结束标记
                String drawTextStr = disposeStr(strLine);

                pageContentStringBuilder.append(drawTextStr);

                ReadActivity.log("-------" + drawTextStr + "段长度-------" + drawTextStr.length());

                if (!drawTextStr.equals("")) {

                    ///  y 坐标偏移量 为 字体的高度 + 间隔的高度
                    y += m_fontSize + lineSpace;

                    c.drawText(drawTextStr, measureMarginWidth, y, mPaint);

                    indexLine++;

                    length += strLine.length();

                    stringBuilder.append(strLine);

                    if (temp_y == 0) {
                        temp_y = y;
                    }

                    temp_x = mPaint.measureText(strLine);

                }

                String upSs = "";

                if (d > 0) {
                    upSs = m_lines.get(d - 1);
                    /// 上一行的长度
                    indexLineLen = mPaint.measureText(upSs);
                }

                if (panDuanPic(d, upSs, strLine)) {

                    /// 绘制段落结束的图标,吐槽
                    flag_x = indexLineLen;

                    flag_y = y - m_fontSize - lineSpace - 4;

                    /// 获取吐槽的位置
                    String json = strLine.substring(strLine.indexOf("{") + 1, strLine.indexOf("}")).trim();

                    ReadActivity.log("截取的字符串------" + json);

                    /// 吐槽的位置
                    int count = checkViewCount(Integer.parseInt(json));

                    if (count > -1) {
                        int width = 40;
                        int height = 40;
                        String text;

                        if (count == 0) {

                            Bitmap bitmap1 = BitmapUtil.decodeSampledBitmapFromResource(mContext.getResources(), R.mipmap.mo_aotao_reader_icon_edit_gray, mWidth, mHeight);

                            point.x = (int) (flag_x + width);
                            point.y = (int) (flag_y - height);

                            c.drawBitmap(bitmap1, point.x, point.y, mCommentCountPaint);

                        } else {

                            if (count > 0 && count < 100) {
                                text = String.valueOf(count);
                            } else {
                                text = "99+";
                            }

                            // 绘制圆形
                            RectF r2 = new RectF();                           //RectF对象
                            r2.left = flag_x + width;                         //左边
                            r2.top = flag_y - height / 2;                     //上边
                            r2.right = r2.left + 2 * r;                        //右边
                            r2.bottom = r2.top + 2 * r;                               //下边

                            c.drawOval(r2, paragraphPaint);        // 绘制圆
                            c.drawText(text, r2.left + r / 2, r2.top + r + 6, mCommentCountPaint);  /// 绘制文字信息

                            point.x = (int) (r2.left + r / 2);
                            point.y = (int) (r2.top + r + 6);

                        }
                    }


                    if (index == 1) {

                        ///  吐槽进度信息
                        float progress = (float) (currentPage.getBegin() * 1.0 / mBookUtil.getBookLen(currentPage.getAid(), currentPage.getCid()));

                        ReadActivity.log("绘制当前进度信息-----" + ((int) (progress * 10000)));

                        FlagInfo flagInfo = new FlagInfo(Integer.parseInt(json),
                                point.x, point.y,
                                length, total_length,
                                indexLine, strLine, 80, progress);

                        mBookPageWidget.addFlagLocation(flagInfo);

                        ParagraphInfo info = new ParagraphInfo();

                        info.setIndexX(point.x);
                        info.setIndexY(point.y);

                        info.setContent(stringBuilder.toString());

                        mBookPageWidget.addParagraphInfo(info);

                    }

                    stringBuilder = new StringBuilder();

                }

                temp_x = mPaint.measureText(strLine);   // 记录段落位置

            }

            if (currentPage.getBegin() == 0) {
                // 如果是开始页面，归0
                templength = 0;
                templengthMin = 0;
                templengthTotal = 0;
                templengthCount = 0;
            }

            templength = length;

            if (templengthMax == 0) {
                templengthMax = length;
            }
            if (templengthMin == 0) {
                templengthMin = length;
            }

            if (length > templengthMax) {
                templengthMax = length;
            }

            if (length < templengthMin) {
                templengthMin = length;
            }

            templengthTotal += length; // 记录总长度

            templengthCount++; // 记录长度次数

            total_length += length;

        }

        // Lu 2018/4/6 模块页面时间不变的问题
        sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);    //HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new java.util.Date());

        // 画进度及时间，时间宽度
        int dateWith = (int) (mBatterryPaint.measureText(date) + mBorderWidth);

        //  进度
        float fPercent = (float) (currentPage.getBegin() * 1.0 / mBookUtil.getBookLen(currentPage.getAid(), currentPage.getCid()));

        currentProgress = fPercent;

        int lenghth_1 = 0;

        //最接近真实的页面字数；
        if (templengthCount <= 3) {

            lenghth_1 = templengthMax;

        } else {

            int length_a = templengthTotal - templengthMax - templengthMin;
            lenghth_1 = (int) (length_a / (templengthCount - 2));

            // 去掉一个最高数，去掉一个最低数，计算平均数

            if (Math.abs(templengthMax - lenghth_1) > 100) {

                lenghth_1 = (int) templengthAvg;

            } else {
                templengthAvg = lenghth_1;
            }

        }

        try {

            int page = 0;

            if (lenghth_1 != 0)
                page = (int) (currentPage.getBegin() / lenghth_1);

            if (page == 0) {
                //排除第二页文字大于第一页数据，结果为0的情况

                long abs = Math.abs(currentPage.getBegin() - lenghth_1);

                if (abs < 50) {
                    page = 1;
                }
            }

            if (mPageEvent != null) {
                mPageEvent.changeProgress(fPercent, mPageIndex);

                String pageContent = pageContentStringBuilder.toString();
                mPageEvent.changeContent(pageContent);

            }

        } catch (Exception ex) {

        }

        String strPercent = df.format(fPercent * 100) + "%";
        int nPercentWidth = (int) mBatterryPaint.measureText("999.9%") + 1;

        c.drawText(strPercent, mWidth - nPercentWidth, mHeight - statusMarginBottom, mBatterryPaint);

        /// x y为坐标值

        c.drawText(date, marginWidth, mHeight - statusMarginBottom, mBatterryPaint);

        // 画电池
        level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 100);
        mBatteryPercentage = (float) level / scale;
        float rect1Left = marginWidth + dateWith + statusMarginBottom;//电池外框left位置
        //画电池外框
        float width = CommonUtil.convertDpToPixel(mContext, 20) - mBorderWidth;
        float height = CommonUtil.convertDpToPixel(mContext, 10);
        rect1.set(rect1Left, mHeight - height - statusMarginBottom, rect1Left + width, mHeight - statusMarginBottom);
        rect2.set(rect1Left + mBorderWidth, mHeight - height + mBorderWidth - statusMarginBottom, rect1Left + width - mBorderWidth, mHeight - mBorderWidth - statusMarginBottom);
        c.save(Canvas.ALL_SAVE_FLAG);
        c.clipRect(rect2, Region.Op.DIFFERENCE);
        c.drawRect(rect1, mBatterryPaint);
        c.restore();
        //画电量部分
        rect2.left += mBorderWidth;
        rect2.right -= mBorderWidth;
        rect2.right = rect2.left + rect2.width() * mBatteryPercentage;
        rect2.top += mBorderWidth;
        rect2.bottom -= mBorderWidth;
        c.drawRect(rect2, mBatterryPaint);
        //画电池头
        int poleHeight = (int) CommonUtil.convertDpToPixel(mContext, 10) / 2;
        rect2.left = rect1.right;
        rect2.top = rect2.top + poleHeight / 4;
        rect2.right = rect1.right + mBorderWidth;
        rect2.bottom = rect2.bottom - poleHeight / 4;
        c.drawRect(rect2, mBatterryPaint);
        //画书名
        c.drawText(CommonUtil.subString(bookName, 12), marginWidth, statusMarginBottom + mBatterryFontSize, mBatterryPaint);

        if (chapterName != null) {
            int nChaterWidth = (int) mBatterryPaint.measureText(chapterName) + 1;
            // 画章
            c.drawText(chapterName, mWidth - marginWidth - nChaterWidth, statusMarginBottom + mBatterryFontSize, mBatterryPaint);
        }

        mBookPageWidget.postInvalidate();
    }


    /**
     * 判断是否绘制吐槽图标
     *
     * @param strLine 数据
     * @return false or true
     */
    private boolean panDuanPic(int p, String upSS, String strLine) {
        // 行数
        if (p > 0) {
            if ((strLine.contains("##{"))) {
                return true;
            }
        }

        return false;

    }


    /**
     * 处理字符串
     *
     * @param drawTextStr 字符串数据 ，去除 {}
     * @return 处理好的字符串
     */
    private String disposeStr(String drawTextStr) {
        String result;
        if (drawTextStr.contains("##{")) {

            int lastLen = drawTextStr.lastIndexOf("}");

            String ss = drawTextStr.substring(0, lastLen + 1);

            Log.i("result", "长度------" + lastLen + "截取-----" + ss);
            if(config.getTypefacePath() == "font/simsun.ttf") {
                result = drawTextStr.replace(ss, "\u3000\u3000");
            }else{
                result = drawTextStr.replace(ss, "\u3000\u3000\u3000\u3000");
            }
//            result = drawTextStr.replace(ss, "\u0020\u0020");

        } else {

            if (drawTextStr.startsWith("$$$")) {
//                result = drawTextStr.replace("$$$", "\u3000");
//                result = drawTextStr.replace("$$$", "\u0020");
                if(config.getTypefacePath() == "font/simsun.ttf"){
                    result = drawTextStr.replace("$$$", "");
                }else{
                    result = drawTextStr.replace("$$$", "\u3000");
                }
            } else {
                if(drawTextStr.startsWith("    ")){
                    result = drawTextStr.replace("    ", "\u3000\u3000");
                }else {
                    result = drawTextStr;
                }
            }
        }

        return result;

    }


    /**
     * 检测是否有段落想法，返回想法数量
     */
    private int checkViewCount(int index) {
        try {
            if (mViewCountList != null) {
                for (ViewCount viewCount : mViewCountList) {
                    if (viewCount != null) {
                        int did = viewCount.getDid();
                        int count = viewCount.getCount();
                        if (did == index) {
                            return count;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    SparseArray<SparseArray<TRPage>> mTRPageSparseArrayAll = new SparseArray<>();

    SparseArray<TRPage> mTRPageSparseArray = new SparseArray<>();

    int mPageTotal = 0;


    /**
     * 绘制当前页面
     *
     * @param updateChapter 是否绘制当前页面
     */
    public void currentPage(Boolean updateChapter) {

        ReadActivity.log("是否绘制界面数据------");

        mBookPageWidget.clearParagraphInfo();
        mBookPageWidget.clearFlagLocation();

        if (currentPage == null) {
            return;
        }

        /// 绘制第一章
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), updateChapter, 0);

        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), updateChapter, 1);

    }


    /**
     * 向前翻页
     */
    public void prePage() {
        /// 是第一页
        if (currentPage.getBegin() <= 0 && currentPage.getPrevious() <= 0) {
            //Log.e(TAG, "当前是第一页");
            if (!m_isfirstPage) {
                Toast.makeText(mContext, "当前是第一页", Toast.LENGTH_SHORT).show();
            }
            m_isfirstPage = true;
            return;
        } else {
            m_isfirstPage = false;
        }

        /// 开始的长度 小于或者 等于  1

        if (currentPage.getBegin() <= 1) {

            ReadActivity.log("是否判断购买vip-----");

            int previous = currentPage.getPrevious();

            int indexMy = bookList.getPremy();
            int vip = bookList.getPrevip();

            // 获取上一章节的cid
            int nextCid = bookList.getPrevious();
            int aid = ReadActivity.mAid;
            int uid = ReadActivity.mUid;

            boolean hhh = DbUtil.getInstence(mContext).isHas(nextCid, aid, uid);

            //Lsrun 判断APIcloud是否已经下载 2018410
            ReadActivity lsrun_readActivity = new ReadActivity();
            int lsrun_ismy = lsrun_readActivity.isDownloadByApicloud(uid,aid,nextCid);

            if (hhh) {
                // 该章节在本地数据库中已购买

            } else {
                if (indexMy == 0 && vip == 1) {
                    if (Config.getIsDing(mContext, uid, aid)) {
                        // 向前翻页 前一页面为vip页面且没有购买  自动购买
                        if (mPageEvent != null) {
                            mPageEvent.onDing(bookList, 1);
                        }

                    } else {
                        // 向前翻页 前一页面为vip页面且没有购买  提示购买
                        if (mPageEvent != null) {
                            mPageEvent.onButtonBuy(true, 1);
                            mPageEvent.isBuy(bookList, 1);
                        }
                        return;
                    }
                } else {
                    if (mPageEvent != null) {
                        mPageEvent.onButtonBuy(false, 1);
                    }
                }

            }

            if (previous > 0) {
                if (mPageEvent != null) {
                    mPageEvent.changeChapter(currentPage.getAid(), previous, 1);
                }

                int isCache = mBookUtil.isCache(currentPage.getAid(), previous);

                if (isCache <= 0) {

                    //缓存无数据，通知加载数据
                    if (mPageEvent != null) {
                        mPageEvent.getChapterFromWeb(previous, false);
                    }

                    return;
                }
            }
        }

        ReadActivity.log("向前翻页-----");

        cancelPage = currentPage;
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true, 0);
        currentPage = getPrePage();
        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true, 1);

    }


    /**
     * 向后翻页
     */
    public void nextPage() {

        int next = currentPage.getNext();

        if (currentPage.getEnd() >= mBookUtil.getBookLen() && currentPage.getNext() <= 0) {
            /// 已经是最后一页了，不绘制界面,直接返回
            if (!m_islastPage) {
                Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }

            m_islastPage = true;

            return;

        } else {
            m_islastPage = false;
        }

        if (currentPage.getEnd() + 1 >= mBookUtil.getBookLen()) {

            int my = bookList.getNextmy();
            int vip = bookList.getNextvip();

            ReadActivity.log("---我的vip-------" + my + "-----书籍vip--------" + vip);

            /// 下一章是vip但 没有购买 提示购买
            int nextCid = bookList.getNext();

            int aid = ReadActivity.mAid;
            int uid = ReadActivity.mUid;

            boolean hhh = DbUtil.getInstence(mContext).isHas(nextCid, aid, uid);

            ReadActivity.log("======" + hhh);

            //Lsrun 判断APIcloud是否已经下载 2018410
            ReadActivity lsrun_readActivity = new ReadActivity();
            int lsrun_ismy = lsrun_readActivity.isDownloadByApicloud(uid,aid,nextCid);

            if (hhh ) {
                // 下一章节已在本地存在，直接阅读
            } else {
                if (my == 0 && vip == 1) {

                    // 连续订阅，不需要弹出 按钮
                    if (Config.getIsDing(mContext, uid, aid)) {
                        if (mPageEvent != null) {
                            mPageEvent.onDing(bookList, 2);
                        }

                    } else {
                        // 弹出 购买的按钮
                        if (mPageEvent != null) {
                            mPageEvent.onButtonBuy(true, 2);
                            mPageEvent.isBuy(bookList, 2);
                        }

                        return;
                    }
                } else {
                    if (mPageEvent != null) {
                        mPageEvent.onButtonBuy(false, 2);
                    }
                }
            }


            ReadActivity.log("加载下级页面------");

            if (next > 0) {
                /// 下一章节id 不为0 ，缓存下一章
                if (mPageEvent != null) {
                    mPageEvent.changeChapter(currentPage.getAid(), next, 2);

                }

                int isCache = mBookUtil.isCache(currentPage.getAid(), next);

                ReadActivity.log("是否有数据缓存======" + isCache);

                if (isCache <= 0) {
                    // 缓存无数据，通知加载数据
                    if (mPageEvent != null) {
                        mPageEvent.getChapterFromWeb(next, false);
                    }

                    return;
                }
            } else if (next == 0) {
                return;
            }
        }

        ReadActivity.log("加载下级页面绘制界面------");

        /// 绘制界面
        cancelPage = currentPage;
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true, 0);
        prePage = currentPage;
        currentPage = getNextPage();
        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true, 1);


    }

    /**
     * 取消翻页
     */
    public void cancelPage() {
        currentPage = cancelPage;
    }

    private BookList getBookListFromCache(int aid, int cid) {

        String bookListJson = BaseData.getCache("bookListJson" + aid + cid);//读取章节缓存
        if (!CommFun.isNullOrEmpty(bookListJson)) {

            BookList model = JsonConvertor.fromJson(bookListJson, BookList.class);
            return model;
        }


        return null;
    }


    /**
     * 向前翻页
     *
     * @return
     */
    public TRPage getPrePage() {
        mBookUtil.setPostition(currentPage.getBegin());

        TRPage trPage = new TRPage();

        if (currentPage.getBegin() <= 1) {
            ///  需要切换章节

            int previous = currentPage.getPrevious();
            if (previous > 0) {
                int isCache = mBookUtil.isCache(currentPage.getAid(), currentPage.getPrevious());
                if (isCache > 0) {
                    //已缓存

                    BookList bookList_change = getBookListFromCache(currentPage.getAid(), currentPage.getPrevious());
                    bookList = bookList_change;

                    mBookUtil.setUniqueId(currentPage.getAid(), currentPage.getPrevious());
                    mBookUtil.setPostition(mBookUtil.getBookLen(currentPage.getAid(), currentPage.getPrevious()));

                    trPage.setAid(currentPage.getAid());
                    trPage.setCid(currentPage.getPrevious());
                    trPage.setPrevious(bookList_change.getPrevious());
                    trPage.setNext(currentPage.getCid());

                }
            }
        } else {

            mBookUtil.setUniqueId(currentPage.getAid(), currentPage.getCid());
            mBookUtil.setPostition(currentPage.getBegin());

            trPage.setAid(currentPage.getAid());
            trPage.setCid(currentPage.getCid());
            trPage.setPrevious(currentPage.getPrevious());
            trPage.setNext(currentPage.getNext());

        }

        trPage.setEnd(mBookUtil.getPosition() - 1);

        trPage.setLines(getPreLines());

        trPage.setBegin(mBookUtil.getPosition() + 1);

        return trPage;
    }

    /**
     * 向后翻页
     *
     * @return
     */
    public TRPage getNextPage() {
        mBookUtil.setPostition(currentPage.getEnd());

        TRPage trPage = new TRPage();

        if (currentPage.getEnd() + 1 >= mBookUtil.getBookLen(currentPage.getAid(), currentPage.getCid())) {
            //最后一页

            int next = currentPage.getNext();
            //获取是否有下一章

            if (next > 0) {
                //如果有下一章

                int isCache = mBookUtil.isCache(currentPage.getAid(), next);//获取是否有缓存
                if (isCache > 0) {
                    //如果有缓存

                    BookList bookList_change = getBookListFromCache(currentPage.getAid(), currentPage.getNext()); //获取书本信息

                    bookList = bookList_change;

                    mBookUtil.setUniqueId(currentPage.getAid(), currentPage.getNext());
                    mBookUtil.setPostition(1);

                    trPage.setBegin(1);

                    trPage.setAid(currentPage.getAid());
                    trPage.setCid(currentPage.getNext());
                    trPage.setPrevious(currentPage.getCid());

                    if (bookList_change != null) {
                        // 当前章节的下一章cid
                        trPage.setNext(bookList_change.getNext());
                    }

                } else {
                    //通知网络下载
                    if (mPageEvent != null) {
                        mPageEvent.getChapterFromWeb(next, true);
                    }
                }
            }
        } else {

            mBookUtil.setUniqueId(currentPage.getAid(), currentPage.getCid());
            mBookUtil.setPostition(currentPage.getEnd());


            trPage.setBegin(currentPage.getEnd() + 1);

            trPage.setAid(currentPage.getAid());
            trPage.setCid(currentPage.getCid());
            trPage.setPrevious(currentPage.getPrevious());
            trPage.setNext(currentPage.getNext());

        }

        trPage.setLines(getNextLines());

        trPage.setEnd(mBookUtil.getPosition());

        return trPage;
    }


    /**
     * 向前翻页
     *
     * @return
     */
    public List<String> getPreLines() {
        List<String> lines = new ArrayList<>();
        float width = 0;
        String line = "";

        char[] par = mBookUtil.preLine();
        while (par != null) {
            List<String> preLines = new ArrayList<>();
            for (int i = 0; i < par.length; i++) {
                char word = par[i];
                float widthChar = mPaint.measureText(word + "");
                width += widthChar;
                if (width > mVisibleWidth) {
                    width = widthChar;
                    preLines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }
            if (!line.isEmpty()) {
                preLines.add(line);
            }

            lines.addAll(0, preLines);

            if (lines.size() >= mLineCount) {
                break;
            }
            width = 0;
            line = "";
            par = mBookUtil.preLine();
        }

        List<String> reLines = new ArrayList<>();
        int num = 0;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (reLines.size() < mLineCount) {
                reLines.add(0, lines.get(i));
            } else {
                num = num + lines.get(i).length();
            }
        }

        if (num > 0) {
            if (mBookUtil.getPosition() > 0) {
                mBookUtil.setPostition(mBookUtil.getPosition() + num + 2);
            } else {
                mBookUtil.setPostition(mBookUtil.getPosition() + num);
            }
        }

        return reLines;
    }


    public List<String> getNextLines() {
        List<String> linesList = new ArrayList<>();
        float width = 0;
        float height = 0;
        String line = "";

        while (mBookUtil.next(true) != -1) {
            char word = (char) mBookUtil.next(false);

            // 判断是否换行

            if ((String.valueOf(word)).equals("\r") && (String.valueOf((char) mBookUtil.next(true))).equals("\n")) {

                mBookUtil.next(false);

                if (!line.isEmpty()) {

                    /// 保存数据到集合
                    linesList.add(line);
                    line = "";
                    width = 0;

                    if (linesList.size() == mLineCount) {
                        break;
                    }
                }
            } else {
                float widthChar = mPaint.measureText(String.valueOf(word));
                width += widthChar;
                if (width > mVisibleWidth) {
                    if(line.startsWith("$$$")){
                        if( (width-widthChar*1) > mVisibleWidth){
                            width = widthChar;
                            linesList.add(line);
                            line = String.valueOf(word);
                        }else{
                            line += String.valueOf(word);
                        }
                    }else{
                        width = widthChar;
                        linesList.add(line);
                        line = String.valueOf(word);
                    }

                } else {

                    line += String.valueOf(word);

                }


            }

            if (linesList.size() == mLineCount) {
                if (!line.isEmpty()) {
                    mBookUtil.setPostition(mBookUtil.getPosition() - 1);
                }
                break;
            }
        }

        if (!line.isEmpty() && linesList.size() < mLineCount) {
            linesList.add(line);
        }

        return linesList;

    }


    public TRPage getPageForBegin(long begin) {
        TRPage trPage = new TRPage();
        trPage.setAid(bookList.getAid());
        trPage.setCid(bookList.getCid());
        trPage.setPrevious(bookList.getPrevious());
        trPage.setNext(bookList.getNext());

        /// 调整字号大小修改
        trPage.setBegin(begin);

        mBookUtil.setUniqueId(bookList.getAid(), bookList.getCid());
        mBookUtil.setPostition(begin - 1);

        long bookLen = mBookUtil.getBookLen(bookList.getAid(), bookList.getCid());

        ReadActivity.log("字符的长度-------" + bookLen);

        trPage.setBookLenth(bookLen);

        trPage.setLines(getNextLines());
        trPage.setEnd(mBookUtil.getPosition());

        if (mPageEvent != null) {
            mPageEvent.changeChapter(bookList.getAid(), bookList.getCid(), 0);
        }

        return trPage;
    }


    public TRPage getTRPage(TRPage trPage) {
        int aid = trPage.getAid();
        int cid = trPage.getCid();
        long position = trPage.getEnd();
        TRPage trPageNew = null;
        int isCache = mBookUtil.isCache(aid, cid);
        if (isCache == 0) {
            return null;
        }
        mBookUtil.setPostition(position);
        List<String> lines = new ArrayList<>();
        int page = 0;

        while (true) {
            lines = new ArrayList<>();
            float width = 0;
            float height = 0;

            String line = "";
            //循环取内容（每一个字）
            while (mBookUtil.next(true) != -1) {

                char word = (char) mBookUtil.next(false);
                //判断是否换行
                if ((String.valueOf(word)).equals("\r") && (String.valueOf((char) mBookUtil.next(true))).equals("\n")) {

                    mBookUtil.next(false);

                    if (!line.isEmpty()) {

                        lines.add(line);

                        line = "";
                        width = 0;

                        if (lines.size() == mLineCount) {
                            break;
                        }
                    }
                } else {

                    float widthChar = mPaint.measureText(String.valueOf(word));

                    width += widthChar;

                    if (width > mVisibleWidth) {
                        width = widthChar;

                        lines.add(line);

                        line = String.valueOf(word);

                    } else {
                        line += word;
                    }
                }

                if (lines.size() == mLineCount) {
                    if (!line.isEmpty()) {
                        mBookUtil.setPostition(mBookUtil.getPosition() - 1);
                    }
                    break;
                }
            }//获取一篇内容end

            if (!line.isEmpty() && lines.size() < mLineCount) {
                lines.add(line);
            }

            if (lines.size() != 0) {
                trPage = new TRPage();
                trPage.setLines(lines);
                trPage.setPageIndex(page);
                trPage.setEnd(mBookUtil.getPosition());

                break;

            } else {
                break;
            }

        }


        return trPage;
    }


    /**
     * 读取文字内容
     *
     * @param aid
     * @param cid
     * @return
     */
    private SparseArray<TRPage> getTRPage(int aid, int cid) {
        int isCache = mBookUtil.isCache(aid, cid);

        if (isCache == 0) {
            return null;
        }
        mBookUtil.setPostition(0);
        SparseArray<TRPage> array = new SparseArray<>();
        List<String> lines = new ArrayList<>();
        int page = 0;
        while (true) {

            lines = new ArrayList<>();

            float width = 0;
            float height = 0;
            String line = "";
            //循环取内容（每一个字）
            while (mBookUtil.next(true) != -1) {

                char word = (char) mBookUtil.next(false);
                //判断是否换行
                if ((word + "").equals("\r") && (((char) mBookUtil.next(true)) + "").equals("\n")) {

                    mBookUtil.next(false);

                    if (!line.isEmpty()) {

                        lines.add(line);

                        line = "";
                        width = 0;

                        if (lines.size() == mLineCount) {
                            break;
                        }
                    }
                } else {

                    float widthChar = mPaint.measureText(String.valueOf(word));

                    width += widthChar;

                    if (width > mVisibleWidth) {
                        width = widthChar;

                        lines.add(line);

                        line = String.valueOf(word);

                    } else {
                        line += String.valueOf(word);
                    }
                }

                if (lines.size() == mLineCount) {
                    if (!line.isEmpty()) {
                        mBookUtil.setPostition(mBookUtil.getPosition() - 1);
                    }
                    break;
                }
            }
            //获取一篇内容end

            if (!line.isEmpty() && lines.size() < mLineCount) {
                lines.add(line);
            }


            if (lines.size() != 0) {

                for (String str : lines) {

                }

                TRPage trPage = new TRPage();
                trPage.setLines(lines);
                trPage.setPageIndex(page);
                trPage.setEnd(mBookUtil.getPosition());

                array.put(page, trPage);

                page++;

            } else {
                break;
            }


            long bookLen = mBookUtil.getBookLen(aid, cid);

            if (mBookUtil.getPosition() >= bookLen) {
                break;
            }


        }

        return array;
    }


    public void setPageWidget(PageWidget mBookPageWidget) {
        this.mBookPageWidget = mBookPageWidget;
    }

    public void setViewCountList(List<ViewCount> viewCountList) {
        mViewCountList = viewCountList;
    }

    public void setPageEvent(PageEvent pageEvent) {
        this.mPageEvent = pageEvent;
    }

    public interface PageEvent {
        void changeProgress(float progress, int page);

        void changeContent(String content);

        void changeChapter(int aid, int cid, int code);

        void firstChapter();

        void lastChapter();

        void getChapterFromWeb(int cid, boolean isBack);

        void isBuy(BookList bookList, int code); //询问是否购买

        void onButtonBuy(boolean buy, int code);

        /**
         * 自动订阅章节
         */
        void onDing(BookList bookList, int code);

    }

    public void clear() {
        currentCharter = 0;
        bookName = "";
        bookList = null;
        mBookPageWidget = null;
        mPageEvent = null;
        cancelPage = null;
        prePage = null;
        currentPage = null;
    }


    public static Status getStatus() {
        return mStatus;
    }


    /**
     * 打开书本
     *
     * @throws IOException
     */
    public void openBook(BookList bookList) throws IOException {

        if (ReadActivity.isDebug) {
            MessageBox.show("打开章节" + bookList.getChaptername());
        }

        // 清空数据
        currentCharter = 0;

        initBg(config.getDayOrNight());

        this.bookList = bookList;

        mStatus = Status.OPENING;

        drawStatus(mBookPageWidget.getCurPage());
        drawStatus(mBookPageWidget.getNextPage());

        if (bookTask != null && bookTask.getStatus() != AsyncTask.Status.FINISHED) {
            bookTask.cancel(true);
        }

        bookTask = new BookTask();
        bookTask.execute(bookList.getBegin());

    }

    private void bindBook(BookList bookList) {
        mAid = bookList.getAid();
        mCid = bookList.getCid();
        mPrevious = bookList.getPrevious();
        mNext = bookList.getNext();
        bookName = bookList.getBookname();
        chapterName = bookList.getChaptername();
        mViewCountList = bookList.getViewCountList();

    }


    private class BookTask extends AsyncTask<Long, Void, Boolean> {
        private long begin = 0;

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (isCancelled()) {
                return;
            }

            if (result) {
                PageFactory.mStatus = PageFactory.Status.FINISH;

                mBookUtil.setUniqueId(bookList.getAid(), bookList.getCid());

                /// 获取阅读记录的进度
                float progress = ReadActivity.mOldProgress;

                ReadActivity.log("------" + progress);

                ///  根据进度获取从哪儿开始
                begin = (long) (mBookUtil.getBookLen() * progress);

                currentPage = getPageForBegin(begin);

                if (mBookPageWidget != null) {
                    currentPage(true);
                }

            } else {
                PageFactory.mStatus = PageFactory.Status.FAIL;
                drawStatus(mBookPageWidget.getCurPage());
                drawStatus(mBookPageWidget.getNextPage());
                Toast.makeText(mContext, "打开书本失败！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            begin = params[0];
            try {
                mBookUtil.openBook(bookList);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }

    public void cacheBook(BookList bookList) {
        ReadActivity.log("执行缓存书籍-------");

        bookListCache = bookList;

        mCacheStatus = Status.OPENING;
        if (cacheTask != null && cacheTask.getStatus() != AsyncTask.Status.FINISHED) {
            cacheTask.cancel(true);
        }

        cacheTask = new BookPageTask();
        cacheTask.execute(bookList.getBegin());
    }


    private class BookPageTask extends AsyncTask<Long, Void, Boolean> {
        private long begin = 0;

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (isCancelled()) {
                return;
            }
            if (result) {
                PageFactory.mCacheStatus = PageFactory.Status.FINISH;

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            try {
                if (bookListCache != null) {
                    mBookUtil.cacheBook(bookListCache);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
