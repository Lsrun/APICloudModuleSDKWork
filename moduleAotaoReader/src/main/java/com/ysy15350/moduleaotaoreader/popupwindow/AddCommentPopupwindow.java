package com.ysy15350.moduleaotaoreader.popupwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.model.FlagInfo;

import common.CommFunAndroid;
import common.message.MessageBox;


/**
 * Created by yangshiyou on 2017/11/2.
 */

public class AddCommentPopupwindow extends PopupWindow {

    private Activity mContext;

    private View conentView;

    FlagInfo mFlagInfo;

    private TextView tv_test;
    private EditText et_content;
    private View btn_send;
    TextView tvCount;
    ImageButton imageButtonClose;


    public AddCommentPopupwindow(final Activity context, FlagInfo flagInfo) {

        mContext = context;

        mFlagInfo = flagInfo;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.mo_aotao_reader_pop_add_comment, null);

        init();

        initView();// 初始化按钮事件

    }

    @SuppressLint("WrongConstant")
    private void init() {
        conentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        int h = mContext.getWindowManager().getDefaultDisplay().getHeight();
        int w = mContext.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        // this.setBackgroundDrawable(dw);

        //设置透明：http://2960629.blog.51cto.com/2950629/742499
//
//        半透明<Button android:background="#e0000000" />
//                透明<Button android:background="#00000000" />

        //getBackground().setAlpha(0);//可实现透明

        //防止PopupWindow被软件盘挡住
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.app_pop);
    }

    private void initView() {

        et_content = (EditText) conentView.findViewById(R.id.et_content);
        if (et_content != null) {
            //设置回车发送
            et_content.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                        if (mListener != null) {
                            mListener.sendText(et_content,et_content.getText().toString().trim());
                        }

                        dismiss();
                    }

                    return false;
                }
            });
        }

        btn_send = conentView.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.sendText(et_content,et_content.getText().toString().trim());
                    dismiss();
                }
            }
        });

        tvCount = (TextView) conentView.findViewById(R.id.tv_tent_count);

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = et_content.getText().toString().length();
                tvCount.setText(String.valueOf(len));

            }
        });

        imageButtonClose = (ImageButton) conentView.findViewById(R.id.ll_close);
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCloseListener != null) {
                    onCloseListener.close();
                }

            }
        });
    }

    private OnCloseListener onCloseListener;

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public interface OnCloseListener {
        void close();
    }


    public void showPopupWindow(View parent) {
        showPopupWindow(parent, 0, 0);
    }

    public void showPopupWindow(View parent, int x, int y) {
        if (!this.isShowing()) {
            showAtLocation(parent, Gravity.BOTTOM, x, y);

        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mListener != null) {
            mListener.dismiss();
        }

    }

    private PopupWindowListener mListener;

    public void setPopupWindowListener(PopupWindowListener listener) {
        mListener = listener;
    }

    public interface PopupWindowListener {
        void sendText(EditText editText,String text);

        void dismiss();
    }
}
