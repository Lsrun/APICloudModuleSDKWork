package com.ysy15350.moduleaotaoreader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ysy15350.moduleaotaoreader.Config;
import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.util.BrightnessUtil;
import com.ysy15350.moduleaotaoreader.util.DisplayUtils;
import com.ysy15350.moduleaotaoreader.util.PageFactory;
import com.ysy15350.moduleaotaoreader.view.CircleImageView;

import common.message.MessageBox;


public class SettingDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private View conentView;


    // TextView tv_dark;

    SeekBar sb_brightness;

    // TextView tv_bright;

    TextView tv_xitong;

    TextView tv_subtract;

    TextView tv_size;

    TextView tv_add;

    TextView tv_qihei;

    TextView tv_default;

    CircleImageView iv_bg_default;

    CircleImageView iv_bg1;

    CircleImageView iv_bg2;

    CircleImageView iv_bg3;

    CircleImageView iv_bg4;

    TextView tv_size_default;

    TextView tv_fzxinghei;

    TextView tv_fzkatong;

    TextView tv_bysong;

    TextView tv_simsun;

    CircleImageView iv_font_default;
    CircleImageView iv_font_1;
    CircleImageView iv_font_2;
    CircleImageView iv_font_3;
    CircleImageView iv_font_4;

    CircleImageView iv_line_default;
    CircleImageView iv_line_1;
    CircleImageView iv_line_2;
    CircleImageView iv_line_3;
    CircleImageView iv_line_4;


    private Config config;
    private Boolean isSystem;
    private SettingListener mSettingListener;
    /**
     * 最小的字号
     */
    private int FONT_SIZE_MIN;
    /**
     * 最大的字号
     */
    private int FONT_SIZE_MAX;
    /**
     * 当前字号
     */
    private int currentFontSize;

    private SettingDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public SettingDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, themeResId);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);

        /// setContentView(R.layout.dialog_setting);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        conentView = inflater.inflate(R.layout.mo_aotao_reader_dialog_setting, null);

        // 初始化View注入
        initView();

        // 解决圆角黑边
        // getWindow().setBackgroundDrawable(new BitmapDrawable());
        // 或者
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(conentView);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(R.dimen.reading_min_text_size);

        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(R.dimen.reading_max_text_size);

        config = Config.getInstance();

        //初始化亮度
        isSystem = config.isSystemLight();
        setTextViewSelect(tv_xitong, isSystem);
        setBrightness(config.getLight());

        //初始化字体大小
        currentFontSize = (int) config.getFontSize();
        tv_size.setText(String.valueOf(currentFontSize));

        //初始化字体
        tv_default.setTypeface(config.getTypeface(Config.FONTTYPE_DEFAULT));
        tv_qihei.setTypeface(config.getTypeface(Config.FONTTYPE_QIHEI));
//        tv_fzxinghei.setTypeface(config.getTypeface(Config.FONTTYPE_FZXINGHEI));
        tv_fzkatong.setTypeface(config.getTypeface(Config.FONTTYPE_FZKATONG));
        tv_bysong.setTypeface(config.getTypeface(Config.FONTTYPE_BYSONG));
        tv_simsun.setTypeface(config.getTypeface(Config.FONTTYPE_SIMSUN));
//        tv_xinshou.setTypeface(config.getTypeface(Config.FONTTYPE_XINSHOU));
//        tv_wawa.setTypeface(config.getTypeface(Config.FONTTYPE_WAWA));
        selectTypeface(config.getTypefacePath());

        selectBg(config.getBookBgType());

        int lightNum = getSystemLight();
        sb_brightness.setProgress(lightNum);

        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 0) {
                    changeBright(false, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /**
     * 获取系统亮度
     *
     * @return 返回的亮度
     */
    private int getSystemLight() {
        int num = 0;
        try {
            num = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return num;
    }

    private void initView() {
        //tv_dark = (TextView) conentView.findViewById(R.id.tv_dark);

        sb_brightness = (SeekBar) conentView.findViewById(R.id.sb_brightness);

        //tv_bright = (TextView) conentView.findViewById(R.id.tv_bright);

        tv_xitong = (TextView) conentView.findViewById(R.id.tv_xitong);

        tv_subtract = (TextView) conentView.findViewById(R.id.tv_subtract);

        tv_size = (TextView) conentView.findViewById(R.id.tv_size);

        tv_add = (TextView) conentView.findViewById(R.id.tv_add);

        tv_qihei = (TextView) conentView.findViewById(R.id.tv_qihei);

        tv_default = (TextView) conentView.findViewById(R.id.tv_default);

        iv_bg_default = (CircleImageView) conentView.findViewById(R.id.iv_bg_default);

        iv_bg1 = (CircleImageView) conentView.findViewById(R.id.iv_bg_1);

        iv_bg2 = (CircleImageView) conentView.findViewById(R.id.iv_bg_2);

        iv_bg3 = (CircleImageView) conentView.findViewById(R.id.iv_bg_3);

        iv_bg4 = (CircleImageView) conentView.findViewById(R.id.iv_bg_4);

        tv_size_default = (TextView) conentView.findViewById(R.id.tv_size_default);

        tv_fzxinghei = (TextView) conentView.findViewById(R.id.tv_fzxinghei);

        tv_fzkatong = (TextView) conentView.findViewById(R.id.tv_fzkatong);

        tv_bysong = (TextView) conentView.findViewById(R.id.tv_bysong);

        tv_simsun = (TextView) conentView.findViewById(R.id.tv_simsun);


        iv_font_default = (CircleImageView) conentView.findViewById(R.id.iv_font_default);
        iv_font_1 = (CircleImageView) conentView.findViewById(R.id.iv_font_1);
        iv_font_2 = (CircleImageView) conentView.findViewById(R.id.iv_font_2);
        iv_font_3 = (CircleImageView) conentView.findViewById(R.id.iv_font_3);
        iv_font_4 = (CircleImageView) conentView.findViewById(R.id.iv_font_4);

        iv_line_default = (CircleImageView) conentView.findViewById(R.id.iv_line_default);
        iv_line_1 = (CircleImageView) conentView.findViewById(R.id.iv_line_1);
        iv_line_2 = (CircleImageView) conentView.findViewById(R.id.iv_line_2);
        iv_line_3 = (CircleImageView) conentView.findViewById(R.id.iv_line_3);
        iv_line_4 = (CircleImageView) conentView.findViewById(R.id.iv_line_4);


        //-----------
        //tv_dark.setOnClickListener(this);

        sb_brightness.setOnClickListener(this);

        //tv_bright.setOnClickListener(this);

        tv_xitong.setOnClickListener(this);

        tv_subtract.setOnClickListener(this);

        tv_size.setOnClickListener(this);

        tv_add.setOnClickListener(this);

        tv_qihei.setOnClickListener(this);

        tv_default.setOnClickListener(this);

        iv_bg_default.setOnClickListener(this);

        iv_bg1.setOnClickListener(this);

        iv_bg2.setOnClickListener(this);

        iv_bg3.setOnClickListener(this);

        iv_bg4.setOnClickListener(this);

        tv_size_default.setOnClickListener(this);

        tv_fzxinghei.setOnClickListener(this);

        tv_fzkatong.setOnClickListener(this);

        tv_bysong.setOnClickListener(this);

        tv_simsun.setOnClickListener(this);

        iv_font_default.setOnClickListener(this);
        iv_font_1.setOnClickListener(this);
        iv_font_2.setOnClickListener(this);
        iv_font_3.setOnClickListener(this);
        iv_font_4.setOnClickListener(this);

        iv_line_default.setOnClickListener(this);
        iv_line_1.setOnClickListener(this);
        iv_line_2.setOnClickListener(this);
        iv_line_3.setOnClickListener(this);
        iv_line_4.setOnClickListener(this);

    }

    //选择背景
    private void selectTextColorBg(int type) {
        switch (type) {
            case Config.TEXT_COLOR_DEFAULT:
                iv_font_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_font_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.TEXT_COLOR_1:
                iv_font_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_font_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.TEXT_COLOR_2:
                iv_font_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_font_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.TEXT_COLOR_3:
                iv_font_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_font_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.TEXT_COLOR_4:
                iv_font_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_font_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                break;
        }
    }


    //设置字体
    public void setTextColor(int type) {
        config.setTextColor(type);
        if (mSettingListener != null) {
            mSettingListener.changeTextColor(type);
        }
    }


    //选择背景
    private void selectStyleBG(int type) {
        switch (type) {
            case Config.LINE_SPACE_DEFAULT:
                iv_line_default.setBorderColor(getContext().getResources().getColor(R.color.reader_border_selected));
                iv_line_1.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_2.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_3.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_4.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));

//                iv_line_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_line_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));

                break;
            case Config.LINE_SPACE_1:
                iv_line_default.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_1.setBorderColor(getContext().getResources().getColor(R.color.reader_border_selected));
                iv_line_2.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_3.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_4.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));

//                iv_line_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_line_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));

                break;
            case Config.LINE_SPACE_2:
                iv_line_default.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_1.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_2.setBorderColor(getContext().getResources().getColor(R.color.reader_border_selected));
                iv_line_3.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_4.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));

//                iv_line_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_line_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));

                break;
            case Config.LINE_SPACE_3:
                iv_line_default.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_1.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_2.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_3.setBorderColor(getContext().getResources().getColor(R.color.reader_border_selected));
                iv_line_4.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));

//                iv_line_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
//                iv_line_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));

                break;
            case Config.LINE_SPACE_4:
                iv_line_default.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_1.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_2.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_3.setBorderColor(getContext().getResources().getColor(R.color.reader_border_default));
                iv_line_4.setBorderColor(getContext().getResources().getColor(R.color.reader_border_selected));

//                iv_line_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_line_4.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));

                break;
        }
    }

    //设置字体
    public void setLineSpaceStyle(int type) {
        config.setLineSpaceStyle(type);
        if (mSettingListener != null) {
            mSettingListener.changeLineSpaceStyle(type);
        }
    }


    //选择背景
    private void selectBg(int type) {
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_1:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_2:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_3:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_4:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                break;
        }
    }

    //设置字体
    public void setBookBg(int type) {
        config.setBookBg(type);
        if (mSettingListener != null) {
            mSettingListener.changeBookBg(type);
        }
    }


    //选择字体
    private void selectTypeface(String typeface) {
        if (typeface.equals(Config.FONTTYPE_DEFAULT)) {
            setTextViewSelect(tv_default, true);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
            setTextViewSelect(tv_simsun, false);

        } else if (typeface.equals(Config.FONTTYPE_QIHEI)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, true);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
            setTextViewSelect(tv_simsun, false);

        } else if (typeface.equals(Config.FONTTYPE_FZXINGHEI)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, true);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
            setTextViewSelect(tv_simsun, false);
            ;
        } else if (typeface.equals(Config.FONTTYPE_FZKATONG)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, true);
            setTextViewSelect(tv_bysong, false);
            setTextViewSelect(tv_simsun, false);

        } else if (typeface.equals(Config.FONTTYPE_BYSONG)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, true);
            setTextViewSelect(tv_simsun, false);

        }else if(typeface.equals(Config.FONTTYPE_SIMSUN)){
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
            setTextViewSelect(tv_simsun, true);
        }
    }

    //设置字体
    public void setTypeface(String typeface) {
        config.setTypeface(typeface);
        Typeface tface = config.getTypeface(typeface);
        if (mSettingListener != null) {
            mSettingListener.changeTypeFace(tface);
        }
    }

    //设置亮度
    public void setBrightness(float brightness) {
        sb_brightness.setProgress((int) (brightness * 100));
    }

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.mo_aotao_reader_button_select_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.reader_border_selected));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.mo_aotao_reader_button_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    public Boolean isShow() {
        return isShowing();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_xitong) {
            isSystem = !isSystem;
            changeBright(isSystem, sb_brightness.getProgress());
        } else if (id == R.id.tv_subtract) {
            subtractFontSize();
        } else if (id == R.id.tv_add) {
            addFontSize();
        } else if (id == R.id.tv_size_default) {
            defaultFontSize();
        } else if (id == R.id.tv_qihei) {
            selectTypeface(Config.FONTTYPE_QIHEI);
            setTypeface(Config.FONTTYPE_QIHEI);
        } else if (id == R.id.tv_fzxinghei) {
            selectTypeface(Config.FONTTYPE_FZXINGHEI);
            setTypeface(Config.FONTTYPE_FZXINGHEI);
        } else if (id == R.id.tv_fzkatong) {
            selectTypeface(Config.FONTTYPE_FZKATONG);
            setTypeface(Config.FONTTYPE_FZKATONG);
        } else if (id == R.id.tv_bysong) {
            selectTypeface(Config.FONTTYPE_BYSONG);
            setTypeface(Config.FONTTYPE_BYSONG);
        } else if (id == R.id.tv_default) {
            selectTypeface(Config.FONTTYPE_DEFAULT);
            setTypeface(Config.FONTTYPE_DEFAULT);
        } else if (id == R.id.iv_bg_default) {
            setBookBg(Config.BOOK_BG_DEFAULT);
            selectBg(Config.BOOK_BG_DEFAULT);
        } else if (id == R.id.iv_bg_1) {
            setBookBg(Config.BOOK_BG_1);
            selectBg(Config.BOOK_BG_1);
        } else if (id == R.id.iv_bg_2) {
            setBookBg(Config.BOOK_BG_2);
            selectBg(Config.BOOK_BG_2);
        } else if (id == R.id.iv_bg_3) {
            setBookBg(Config.BOOK_BG_3);
            selectBg(Config.BOOK_BG_3);
        } else if (id == R.id.iv_bg_4) {
            setBookBg(Config.BOOK_BG_4);
            selectBg(Config.BOOK_BG_4);
        } else if (id == R.id.iv_line_default) {
            setLineSpaceStyle(Config.LINE_SPACE_DEFAULT);
            selectStyleBG(Config.LINE_SPACE_DEFAULT);
        } else if (id == R.id.iv_line_1) {
            setLineSpaceStyle(Config.LINE_SPACE_1);
            selectStyleBG(Config.LINE_SPACE_1);
        } else if (id == R.id.iv_line_2) {
            setLineSpaceStyle(Config.LINE_SPACE_2);
            selectStyleBG(Config.LINE_SPACE_2);
        } else if (id == R.id.iv_line_3) {
            setLineSpaceStyle(Config.LINE_SPACE_3);
            selectStyleBG(Config.LINE_SPACE_3);
        } else if (id == R.id.iv_line_4) {
            setLineSpaceStyle(Config.LINE_SPACE_4);
            selectStyleBG(Config.LINE_SPACE_4);
        } else if (id == R.id.iv_font_default) {
            setTextColor(Config.TEXT_COLOR_DEFAULT);
            selectTextColorBg(Config.TEXT_COLOR_DEFAULT);
        } else if (id == R.id.iv_font_1) {
            setTextColor(Config.TEXT_COLOR_1);
            selectTextColorBg(Config.TEXT_COLOR_1);
        } else if (id == R.id.iv_font_2) {
            setTextColor(Config.TEXT_COLOR_2);
            selectTextColorBg(Config.TEXT_COLOR_2);
        } else if (id == R.id.iv_font_3) {
            setTextColor(Config.TEXT_COLOR_3);
            selectTextColorBg(Config.TEXT_COLOR_3);
        } else if (id == R.id.iv_font_4) {
            setTextColor(Config.TEXT_COLOR_4);
            selectTextColorBg(Config.TEXT_COLOR_4);
        }else if(id == R.id.tv_simsun){
            selectTypeface(Config.FONTTYPE_SIMSUN);
            setTypeface(Config.FONTTYPE_SIMSUN);
        }

    }


    /**
     *   变大书本字号，当前字号比最大的字号小就加一
     */
    private void addFontSize() {
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1;
            tv_size.setText(String.valueOf(currentFontSize));
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    private void defaultFontSize() {
        currentFontSize = (int) getContext().getResources().getDimension(R.dimen.reading_default_text_size);
        tv_size.setText(String.valueOf(currentFontSize));
        config.setFontSize(currentFontSize);
        if (mSettingListener != null) {
            mSettingListener.changeFontSize(currentFontSize);
        }
    }

    /**
     * 变小书本字体
     */
    private void subtractFontSize() {
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1;
            tv_size.setText(String.valueOf(currentFontSize));
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    /**
     * 改变系统亮度
     *
     * @param isSystem
     * @param brightness
     */
    public void changeBright(Boolean isSystem, int brightness) {
        float light = (float) (brightness / 100.0);
        setTextViewSelect(tv_xitong, isSystem);
        config.setSystemLight(isSystem);
        config.setLight(light);
        if (mSettingListener != null) {
            mSettingListener.changeSystemBright(isSystem, light);
        }
    }

    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    public interface SettingListener {
        void changeSystemBright(Boolean isSystem, float brightness);

        void changeFontSize(int fontSize);

        void changeTypeFace(Typeface typeface);

        void changeBookBg(int type);

        void changeLineSpaceStyle(int type);

        void changeTextColor(int type);
    }
}
