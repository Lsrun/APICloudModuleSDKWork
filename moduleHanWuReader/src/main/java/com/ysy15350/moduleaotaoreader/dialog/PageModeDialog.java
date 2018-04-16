package com.ysy15350.moduleaotaoreader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ysy15350.moduleaotaoreader.Config;
import com.ysy15350.moduleaotaoreader.R;


public class PageModeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private View conentView;

    TextView tv_simulation;
    TextView tv_cover;
    TextView tv_slide;
    TextView tv_none;

    private Config config;
    private PageModeListener pageModeListener;

    private PageModeDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public PageModeDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public PageModeDialog(Context context, int themeResId) {
        super(context, themeResId);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        conentView = inflater.inflate(R.layout.mo_aotao_reader_dialog_pagemode, null);

        initView();

        config = Config.getInstance();
        selectPageMode(config.getPageMode());

//        WindowManager m = getWindow().getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.width = d.getWidth();
        //getWindow().setAttributes(p);


        //this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));
        //this.setCanceledOnTouchOutside(false);

        // 解决圆角黑边
        // getWindow().setBackgroundDrawable(new BitmapDrawable());
        // 或者

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(conentView);
    }

    private void initView() {
        tv_simulation = (TextView) conentView.findViewById(R.id.tv_simulation);
        tv_cover = (TextView) conentView.findViewById(R.id.tv_cover);
        tv_slide = (TextView) conentView.findViewById(R.id.tv_slide);
        tv_none = (TextView) conentView.findViewById(R.id.tv_none);

        tv_simulation.setOnClickListener(this);
        tv_cover.setOnClickListener(this);
        tv_slide.setOnClickListener(this);
        tv_none.setOnClickListener(this);
    }


    // 设置翻页
    public void setPageMode(int pageMode) {
        config.setPageMode(pageMode);
        if (pageModeListener != null) {
            pageModeListener.changePageMode(pageMode);
        }
    }

    //选择怕翻页
    public void selectPageMode(int pageMode) {
        if (pageMode == Config.PAGE_MODE_SIMULATION) {
            setTextViewSelect(tv_simulation, true);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_COVER) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, true);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_SLIDE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, true);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_NONE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, true);
        }
    }

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.mo_aotao_reader_button_select_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.read_dialog_button_select));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.mo_aotao_reader_button_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    public void setPageModeListener(PageModeListener pageModeListener) {
        this.pageModeListener = pageModeListener;
    }

    public interface PageModeListener {
        void changePageMode(int pageMode);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.tv_simulation) {
            selectPageMode(Config.PAGE_MODE_SIMULATION);
            setPageMode(Config.PAGE_MODE_SIMULATION);
        } else if (id == R.id.tv_cover) {
            selectPageMode(Config.PAGE_MODE_COVER);
            setPageMode(Config.PAGE_MODE_COVER);
        } else if (id == R.id.tv_slide) {
            selectPageMode(Config.PAGE_MODE_SLIDE);
            setPageMode(Config.PAGE_MODE_SLIDE);
        } else if (id == R.id.tv_none) {
            selectPageMode(Config.PAGE_MODE_NONE);
            setPageMode(Config.PAGE_MODE_NONE);
        }


    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }
}
