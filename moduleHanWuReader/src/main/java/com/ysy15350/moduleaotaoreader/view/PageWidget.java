package com.ysy15350.moduleaotaoreader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

import com.ysy15350.moduleaotaoreader.Config;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.model.FlagInfo;
import com.ysy15350.moduleaotaoreader.model.ParagraphInfo;
import com.ysy15350.moduleaotaoreader.util.PageFactory;
import com.ysy15350.moduleaotaoreader.view.animation.AnimationProvider;
import com.ysy15350.moduleaotaoreader.view.animation.CoverAnimation;
import com.ysy15350.moduleaotaoreader.view.animation.NoneAnimation;
import com.ysy15350.moduleaotaoreader.view.animation.SimulationAnimation;
import com.ysy15350.moduleaotaoreader.view.animation.SlideAnimation;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;


public class PageWidget extends TextView {


    private final static String TAG = "BookPageWidget";
    private int mScreenWidth = 0; // 屏幕宽
    private int mScreenHeight = 0; // 屏幕高
    private Context mContext;

    //是否移动了
    private Boolean isMove = false;
    //是否翻到下一页
    private Boolean isNext = false;
    //是否取消翻页
    private Boolean cancelPage = false;
    //是否没下一页或者上一页
    private Boolean noNext = false;
    private int downX = 0;
    private int downY = 0;

    private int moveX = 0;
    private int moveY = 0;

    private final int TRIGGER_LONGPRESS_TIME_THRESHOLD = 300;    // 触发长按事件的时间阈值
    private final int TRIGGER_LONGPRESS_DISTANCE_THRESHOLD = 10; // 触发长按事件的位移阈值

    private boolean isLongPress = false;               // 是否发触了长按事件
    private boolean isLongPressTouchActionUp = false;  // 长按事件结束后，标记该次事件
    private boolean isVibrator = false;                // 是否触发过长按震动

    private Vibrator mVibrator;//震动


    //翻页动画是否在执行
    private Boolean isRuning = false;

    Bitmap mCurPageBitmap = null; // 当前页
    Bitmap mNextPageBitmap = null;
    private AnimationProvider mAnimationProvider;

    Scroller mScroller;
    private int mBgColor = 0xFFCEC29C;
    private TouchListener mTouchListener;

    public PageWidget(Context context) {
        this(context, null);
    }

    public PageWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPage();
        mScroller = new Scroller(getContext(), new LinearInterpolator());
        mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);

        init();
    }


    private void init() {

        mVibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);

    }

    private void initPage() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);      //android:LargeHeap=true  use in  manifest application
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
    }


    public void setPageMode(int pageMode) {
        switch (pageMode) {
            case Config.PAGE_MODE_SIMULATION:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case Config.PAGE_MODE_COVER:
                mAnimationProvider = new CoverAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case Config.PAGE_MODE_SLIDE:
                mAnimationProvider = new SlideAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case Config.PAGE_MODE_NONE:
                mAnimationProvider = new NoneAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            default:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
        }
    }

    float mFlag_x = 0, mFlag_y = 0;

    public void setFlagLocation(float flag_x, float flag_y) {
        mFlag_x = flag_x;
        mFlag_y = flag_y;
    }

    List<FlagInfo> flagInfoList = new ArrayList<>();
    List<ParagraphInfo> paragraphInfoList = new ArrayList<>();

    /**
     * 添加标记点
     *
     * @param flagInfo
     */
    public void addFlagLocation(FlagInfo flagInfo) {
        flagInfoList.add(flagInfo);
    }

    public void clearFlagLocation() {
        flagInfoList = new ArrayList<>();
    }

    public void addParagraphInfo(ParagraphInfo paragraphInfo) {
        paragraphInfoList.add(paragraphInfo);
    }

    public void clearParagraphInfo() {
        paragraphInfoList = new ArrayList<>();
    }


    /**
     * 是否点击标记点
     *
     * @param x 点击 x
     * @param y 点击 y
     */
    private List<FlagInfo> isClickFlag(float x, float y) {

        int progress = (int) (ReadActivity.tuCaoProgress * 10000);

        List<FlagInfo> arr = new ArrayList<>();

        try {
            if (flagInfoList != null && !flagInfoList.isEmpty()) {
                for (FlagInfo flagInfo : flagInfoList) {
                    if (flagInfo != null) {
                        int range = flagInfo.getRange();
                        // 点击范围
                        float flag_x = flagInfo.getFlag_x();
                        float flag_y = flagInfo.getFlag_y();

                        int len = Math.abs((int) Math.sqrt(((x - flag_x) * (x - flag_x) + (y - flag_y) * (y - flag_y))));

                        int saveProgress= (int) (flagInfo.getProgress()*10000);

                        if (len <= range && saveProgress== progress) {
                            arr.add(flagInfo);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arr;
    }


    private ParagraphInfo isLongClickParagraph(float x, float y) {
        try {
            if (paragraphInfoList != null && !paragraphInfoList.isEmpty()) {
                for (ParagraphInfo paragraphInfo : paragraphInfoList) {
                    if (paragraphInfo != null) {

                        float startX = paragraphInfo.getIndexX();
                        float startY = paragraphInfo.getIndexY();

                        int len = Math.abs((int) Math.sqrt(((x - startX) * (x - startX) + (y - startY) * (y - startY))));

                        /// 两点之间的距离大于吐槽按钮的距离
                        if (len > 30) {
                            return paragraphInfo;
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getCurPage() {
        return mCurPageBitmap;
    }

    public Bitmap getNextPage() {
        return mNextPageBitmap;
    }

    public void setBgColor(int color) {
        mBgColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(mBgColor);

        if (isRuning) {
            mAnimationProvider.drawMove(canvas);
        } else {
            mAnimationProvider.drawStatic(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (PageFactory.getStatus() == PageFactory.Status.OPENING) {
            return true;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        long eventTime = System.currentTimeMillis();

        mAnimationProvider.setTouchPoint(x, y);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            downY = (int) event.getY();
            moveX = 0;
            moveY = 0;
            isMove = false;
//            cancelPage = false;
            noNext = false;
            isNext = false;
            isRuning = false;

            isLongPress = false;
            isVibrator = false;
            isLongPressTouchActionUp = false;


            mAnimationProvider.setStartPoint(downX, downY);
            abortAnimation();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            // 判断是否触发长按事件
            if (event.getEventTime() - event.getDownTime() >= TRIGGER_LONGPRESS_TIME_THRESHOLD
                    && Math.abs(event.getX() - downX) < TRIGGER_LONGPRESS_DISTANCE_THRESHOLD
                    && Math.abs(event.getY() - downY) < TRIGGER_LONGPRESS_DISTANCE_THRESHOLD) {

                isLongPress = true;
                isLongPressTouchActionUp = false;
                Log.d(TAG, "onTouchEvent: 长按事件" + downX + "," + downY);

                // 每次触发长按时，震动提示一次
                if (!isVibrator) {
                    mVibrator.vibrate(30);
                    isVibrator = true;
                }
            }


            final int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            //判断是否移动了
            if (!isMove) {
                isMove = Math.abs(downX - x) > slop || Math.abs(downY - y) > slop;
            }

            if (isMove) {
                isMove = true;
                if (moveX == 0 && moveY == 0) {
                    /// 判断翻得是上一页还是下一页
                    if (x - downX > 0) {
                        isNext = false;
                    } else {
                        isNext = true;
                    }
                    cancelPage = false;
                    if (isNext) {
                        Boolean isNext = mTouchListener.nextPage();
//                        calcCornerXY(downX,mScreenHeight);
                        mAnimationProvider.setDirection(AnimationProvider.Direction.next);

                        if (!isNext) {
                            noNext = true;
                            return true;
                        }
                    } else {
                        Boolean isPre = mTouchListener.prePage();

                        mAnimationProvider.setDirection(AnimationProvider.Direction.pre);

                        if (!isPre) {
                            noNext = true;
                            return true;
                        }
                    }

                } else {
                    //判断是否取消翻页
                    if (isNext) {
                        if (x - moveX > 0) {
                            cancelPage = true;
                            mAnimationProvider.setCancel(true);
                        } else {
                            cancelPage = false;
                            mAnimationProvider.setCancel(false);
                        }
                    } else {
                        if (x - moveX < 0) {
                            mAnimationProvider.setCancel(true);
                            cancelPage = true;
                        } else {
                            mAnimationProvider.setCancel(false);
                            cancelPage = false;
                        }
                    }

                }

                moveX = x;
                moveY = y;
                isRuning = true;
                this.postInvalidate();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (!isMove) {
                cancelPage = false;

                List<FlagInfo> flagInfoList = isClickFlag(downX, downY);

                ReadActivity.log( "附近吐槽长度----" + flagInfoList.size());

                if (flagInfoList.size() != 0) {

                    if (mTouchListener != null) {
                        mTouchListener.flagClick(flagInfoList);
                    }

                    return true;

                } else {

                    if (isLongPress) {

                        ParagraphInfo paragraphInfo = isLongClickParagraph(downX, downY);

                        if (paragraphInfo != null) {
                            if (mTouchListener != null) {
                                mTouchListener.paragraphLongClick(paragraphInfo);
                            }
                        }

                        isLongPress = false;

                        return true;
                    } else if (downX > mScreenWidth / 5 && downX < mScreenWidth * 4 / 5 && downY > mScreenHeight / 3 && downY < mScreenHeight * 2 / 3) {

                        ///  是否点击了中间
                        if (mTouchListener != null) {
                            mTouchListener.center();
                        }

                        return true;

                    } else if (downX > mScreenWidth / 5 && downX < mScreenWidth * 4 / 5 && downY > mScreenHeight * 4 / 5 && downY < mScreenHeight) {
                        // 是否点击了底部
                        if (mTouchListener != null) {
                            mTouchListener.bottom();
                        }
                        return true;
                    } else if (x < mScreenWidth / 3) {
                        isNext = false;
                    } else {
                        isNext = true;
                    }

                    if (isNext) {
                        Boolean isNext = mTouchListener.nextPage();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                        if (!isNext) {
                            return true;
                        }

                    } else {
                        Boolean isPre = mTouchListener.prePage();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.pre);
                        if (!isPre) {
                            return true;
                        }
                    }
                }
            }

            if (cancelPage && mTouchListener != null) {
                mTouchListener.cancel();
            }

            if (!noNext) {
                isRuning = true;
                mAnimationProvider.startAnimation(mScroller);
                this.postInvalidate();
            }
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            mAnimationProvider.setTouchPoint(x, y);
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                isRuning = false;
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            mAnimationProvider.setTouchPoint(mScroller.getFinalX(), mScroller.getFinalY());
            postInvalidate();
        }
    }

    public boolean isRunning() {
        return isRuning;
    }

    public void setTouchListener(TouchListener mTouchListener) {
        this.mTouchListener = mTouchListener;
    }

    public void clearList() {
        flagInfoList.clear();

    }

    public interface TouchListener {
        void center();

        void flagClick(List<FlagInfo> flagInfoList);

        void paragraphLongClick(ParagraphInfo paragraphInfo);

        void bottom();

        Boolean prePage();

        Boolean nextPage();

        void cancel();
    }


}
