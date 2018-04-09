package com.ysy15350.moduleaotaoreader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioGroup;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.adapters.GridViewAdpater_Gift;
import com.ysy15350.moduleaotaoreader.model.GiftInfo;

import java.util.ArrayList;
import java.util.List;

import base.ViewHolder;
import common.message.MessageBox;


/**
 * 打赏弹框
 */
public class PayGiftDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private View conentView;
    private ViewHolder mHolder;


    private PayGiftDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public PayGiftDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public PayGiftDialog(Context context, int themeResId) {
        super(context, themeResId);

        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        conentView = inflater.inflate(R.layout.mo_aotao_reader_dialog_paygift, null);
        mHolder = ViewHolder.get(mContext, conentView);

        initView();

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(conentView);
    }

    GridView gv_gift;

    GiftInfo mSelectGiftInfo;

    private void initView() {
        mHolder.getView(R.id.btn_close).setOnClickListener(this);

        mHolder.getView(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectGiftInfo != null) {
                    if (mListener != null) {
                        mListener.payGift(mSelectGiftInfo);
                        dismiss();
                    }

                } else {
                    MessageBox.show("请选择礼物");
                }
            }
        });

        gv_gift = mHolder.getView(R.id.gv_gift);

        List<GiftInfo> list = getGiftInfoList();

        GridViewAdpater_Gift viewAdpaterGift = new GridViewAdpater_Gift(mContext, list);
        gv_gift.setAdapter(viewAdpaterGift);

        gv_gift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GiftInfo giftInfo = (GiftInfo) parent.getItemAtPosition(position);
                mSelectGiftInfo = giftInfo;

                if (giftInfo != null) {
                    mHolder.setText(R.id.btn_ok, "打赏" + giftInfo.getContent());
                }
            }
        });


        RadioGroup radioGroup = mHolder.getView(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (mSelectGiftInfo != null) {
                    if (i == R.id.rb1) {
                        mSelectGiftInfo.setCount(1);

                    } else if (i == R.id.rb2) {
                        mSelectGiftInfo.setCount(2);
                    } else if (i == R.id.rb3) {
                        mSelectGiftInfo.setCount(3);
                    } else if (i == R.id.rb4) {
                        mSelectGiftInfo.setCount(4);
                    } else if (i == R.id.rb5) {
                        mSelectGiftInfo.setCount(5);

                    }
                }

            }
        });

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_close) {
            dismiss();
        }


    }

    @Override
    public void show() {
        super.show();

        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }


    private List<GiftInfo> getGiftInfoList() {
        List<GiftInfo> giftInfos = new ArrayList<>();
        GiftInfo giftInfo1 = new GiftInfo();
        giftInfo1.setRid(101);
        giftInfo1.setValue(111);
        giftInfo1.setContent("吃狗粮");
        giftInfo1.setResId(R.drawable.icon_reward1);
        giftInfo1.setCount(1);

        giftInfos.add(giftInfo1);

        GiftInfo giftInfo2 = new GiftInfo();
        giftInfo2.setRid(102);
        giftInfo2.setValue(250);
        giftInfo2.setContent("扔拖鞋");
        giftInfo2.setResId(R.drawable.icon_reward2);
        giftInfo2.setCount(1);

        giftInfos.add(giftInfo2);


        GiftInfo giftInfo3 = new GiftInfo();
        giftInfo3.setRid(103);
        giftInfo3.setValue(666);
        giftInfo3.setContent("吃口瓜");
        giftInfo3.setResId(R.drawable.icon_reward3);
        giftInfo3.setCount(1);

        giftInfos.add(giftInfo3);


        GiftInfo giftInfo4 = new GiftInfo();
        giftInfo4.setRid(104);
        giftInfo4.setValue(999);
        giftInfo4.setContent("丢肥皂");
        giftInfo4.setResId(R.drawable.icon_reward4);
        giftInfo4.setCount(1);

        giftInfos.add(giftInfo4);


        GiftInfo giftInfo5 = new GiftInfo();
        giftInfo5.setRid(105);
        giftInfo5.setValue(1000);
        giftInfo5.setContent("寒武扶仙");
        giftInfo5.setResId(R.drawable.icon_reward5);
        giftInfo5.setCount(1);

        giftInfos.add(giftInfo5);


        GiftInfo giftInfo6 = new GiftInfo();
        giftInfo6.setRid(106);
        giftInfo6.setValue(2000);
        giftInfo6.setContent("寒武奇虾");
        giftInfo6.setResId(R.drawable.icon_reward6);
        giftInfo6.setCount(1);

        giftInfos.add(giftInfo6);

        GiftInfo giftInfo7 = new GiftInfo();
        giftInfo7.setRid(107);
        giftInfo7.setValue(5000);
        giftInfo7.setContent("寒武笔石");
        giftInfo7.setResId(R.drawable.icon_reward7);
        giftInfo7.setCount(1);

        giftInfos.add(giftInfo7);

        GiftInfo giftInfo8 = new GiftInfo();
        giftInfo8.setRid(108);
        giftInfo8.setValue(10000);
        giftInfo8.setContent("寒武恐龙");
        giftInfo8.setResId(R.drawable.icon_reward8);
        giftInfo8.setCount(1);

        giftInfos.add(giftInfo8);

        return giftInfos;
    }

    private DialogListener mListener;

    public void setDialogListener(DialogListener listener) {
        mListener = listener;
    }

    public interface DialogListener {
        void payGift(GiftInfo giftInfo);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mSelectGiftInfo = null;
        mHolder.setText(R.id.btn_ok, "打赏");
    }
}
