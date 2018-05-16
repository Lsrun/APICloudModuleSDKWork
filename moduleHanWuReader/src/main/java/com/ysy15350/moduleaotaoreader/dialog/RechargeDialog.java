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

import com.google.gson.Gson;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.ysy15350.moduleaotaoreader.APIModuleReader;
import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.adapters.GridViewAdpater_RechargeInfo;
import com.ysy15350.moduleaotaoreader.model.RechargeInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.ViewHolder;


/**
 * 充值界面弹窗
 */
public class RechargeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private View conentView;
    private ViewHolder mHolder;


    private RechargeDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public RechargeDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public RechargeDialog(Context context, int themeResId) {

        super(context, themeResId);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        conentView = inflater.inflate(R.layout.mo_aotao_reader_dialog_recharge, null);
        mHolder = ViewHolder.get(mContext, conentView);

        initView();

        // 或者
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(conentView);
    }

    GridView gv_recharge;

    RechargeInfo mSelectRechargeInfo;

    private void initView() {
        mHolder.getView(R.id.btn_close).setOnClickListener(this);

        gv_recharge = mHolder.getView(R.id.gv_recharge);

        List<RechargeInfo> list = getRechargeInfoList();

        GridViewAdpater_RechargeInfo viewAdpaterGift = new GridViewAdpater_RechargeInfo(mContext, list);

        gv_recharge.setAdapter(viewAdpaterGift);
        gv_recharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RechargeInfo giftInfo = (RechargeInfo) parent.getItemAtPosition(position);
                mSelectRechargeInfo = giftInfo;

                Gson gson=new Gson();

                UZModule uzModule=new APIModuleReader();

                String json=gson.toJson(giftInfo);

                try {
                    uzModule.sendEventToHtml5("czAction",new JSONObject(json));
                } catch (JSONException e) {
                    e.printStackTrace();
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
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }


    private List<RechargeInfo> getRechargeInfoList() {
        List<RechargeInfo> giftInfos = new ArrayList<>();
        RechargeInfo giftInfo1 = new RechargeInfo();
        giftInfo1.setId(1);
        giftInfo1.setValue(18);
        giftInfo1.setContent("180虫币");
        giftInfo1.setResId(R.mipmap.mo_aotao_reader_icon_gift1);
        giftInfo1.setCount(1);

        giftInfos.add(giftInfo1);

        RechargeInfo giftInfo2 = new RechargeInfo();
        giftInfo2.setId(2);
        giftInfo2.setValue(50);
        giftInfo2.setContent("500虫币");
        giftInfo2.setResId(R.mipmap.mo_aotao_reader_icon_gift2);
        giftInfo2.setCount(1);

        giftInfos.add(giftInfo2);


        RechargeInfo giftInfo3 = new RechargeInfo();
        giftInfo3.setId(3);
        giftInfo3.setValue(100);
        giftInfo3.setContent("1000虫币");
        giftInfo3.setResId(R.mipmap.mo_aotao_reader_icon_gift3);
        giftInfo3.setCount(1);

        giftInfos.add(giftInfo3);


        RechargeInfo giftInfo4 = new RechargeInfo();
        giftInfo4.setId(4);
        giftInfo4.setValue(200);
        giftInfo4.setContent("2000虫币");
        giftInfo4.setResId(R.mipmap.mo_aotao_reader_icon_gift4);
        giftInfo4.setCount(1);

        giftInfos.add(giftInfo4);
//
//
//        RechargeInfo giftInfo5 = new RechargeInfo();
//        giftInfo5.setId(6);
//        giftInfo5.setValue(120);
//        giftInfo5.setContent("120虫币");
//        giftInfo5.setResId(R.mipmap.mo_aotao_reader_icon_gift5);
//        giftInfo5.setCount(1);
//
//        giftInfos.add(giftInfo5);
//
//        RechargeInfo giftInfo6 = new RechargeInfo();
//        giftInfo6.setId(7);
//        giftInfo6.setValue(200);
//        giftInfo6.setContent("200虫币");
//        giftInfo6.setResId(R.mipmap.mo_aotao_reader_icon_gift6);
//        giftInfo6.setCount(1);
//
//        giftInfos.add(giftInfo6);


        return giftInfos;

    }


    private DialogListener mListener;

    public void setDialogListener(DialogListener listener) {
        mListener = listener;
    }

    public interface DialogListener {
        void payGift(RechargeInfo giftInfo);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mSelectRechargeInfo = null;
        mHolder.setText(R.id.btn_ok, "打赏");
    }
}
