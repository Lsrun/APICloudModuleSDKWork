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

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.adapters.GridViewAdpater_DownloadInfo;
import com.ysy15350.moduleaotaoreader.model.ChapterBlockInfo;
import com.ysy15350.moduleaotaoreader.model.ChapterBlockModel;
import com.ysy15350.moduleaotaoreader.model.ChapterListBlockInfo;

import java.util.ArrayList;
import java.util.List;

import base.ViewHolder;
import common.CommFun;
import common.message.MessageBox;

/**
 * 下载弹窗
 */
public class DownloadBookDialog extends Dialog {

    private Context mContext;
    private View conentView;
    private ViewHolder mHolder;
    List<ChapterBlockInfo> listData;
    private boolean isInsufficient = false;//判断是否余额不足 true 余额不足 false 余额充足


    private DownloadBookDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public DownloadBookDialog(Context context, List<ChapterBlockInfo> listData) {
        this(context, R.style.setting_dialog);
        mContext = context;
        this.listData = listData;
    }

    public DownloadBookDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        conentView = inflater.inflate(R.layout.mo_aotao_reader_dialog_download_book, null);
        mHolder = ViewHolder.get(mContext, conentView);


        initView();

        /// getWindow().setBackgroundDrawable(new BitmapDrawable());
        // 或者
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(conentView);
    }

    private GridView gv_gift;

    private ChapterBlockInfo mSelectChapterBlockInfo;

    private void initView() {
        mHolder.getView(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /// 下载按钮点击事件
        mHolder.getView(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectChapterBlockInfo != null) {

                    if (mListener != null) {
                        if(isInsufficient){
                            MessageBox.show("余额不足,请及时充值!");
                            MessageBox.hideWaitDialog();
                        }else {
                            mListener.itemClick(mSelectChapterBlockInfo);
                        }
                    }
                } else {
                    MessageBox.show("请选择下载章节");
                }

                dismiss();


            }
        });


        gv_gift = mHolder.getView(R.id.gv_gift);

        gv_gift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (selectView != null) {
                    selectView.findViewById(R.id.ll_main).setBackgroundResource(R.drawable.mo_aotao_reader_shape_download_info_grid_item);
                }

                view.findViewById(R.id.ll_main).setBackgroundResource(R.drawable.mo_aotao_reader_shape_download_info_grid_item_selected);

                selectView = view;

                ChapterBlockInfo chapterBlockInfo = (ChapterBlockInfo) parent.getItemAtPosition(position);

                mSelectChapterBlockInfo = chapterBlockInfo;

                if (chapterBlockInfo != null) {
                    checkAccount(chapterBlockInfo);

                }
            }
        });

    }

    private List<ChapterListBlockInfo> getListChoose() {
        List<ChapterListBlockInfo> list = new ArrayList<>();
        for (ChapterListBlockInfo temp : oneListData) {
            if (temp.isCheck()) {
                list.add(temp);
            }
        }
        return list;
    }


    private View selectView;

    private void checkAccount(ChapterBlockInfo chapterBlockInfo) {
        if (chapterBlockInfo != null) {

            int payChapterCount = chapterBlockInfo.getToOrder() - chapterBlockInfo.getFromOrder();
            payChapterCount = payChapterCount < 0 ? 0 : payChapterCount;

            mHolder.setText(R.id.tv_payChapterCount, payChapterCount + " 章")
                    .setText(R.id.tv_sumprice, chapterBlockInfo.getSumprice() + "");

            float account = 0f;

            /// 余额
            if (mChapterBlockModel != null) {
                /// 我的总余额
                String egold = mChapterBlockModel.getEgold();

                if (!CommFun.isNullOrEmpty(egold)) {
                    account = Float.parseFloat(egold);
                }
            }

            /// 需要的总金额
            if (chapterBlockInfo.getSumprice() > account) {
                mHolder.setText(R.id.btn_ok, "余额不足，点击充值");
                isInsufficient = true;
            } else {
                mHolder.setText(R.id.btn_ok, "下载");
                isInsufficient = false;
            }

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

    private ChapterBlockModel mChapterBlockModel;

    public void setChapterBlockModel(ChapterBlockModel chapterBlockModel) {
        mChapterBlockModel = chapterBlockModel;
        bindData();

    }


    /**
     * 批量下载
     */
    private void bindData() {
        GridViewAdpater_DownloadInfo viewAdpaterGift = new GridViewAdpater_DownloadInfo(mContext, listData, 2);
        gv_gift.setAdapter(viewAdpaterGift);

        mHolder.setText(R.id.tv_egold, mChapterBlockModel.getEgold());

    }


    List<ChapterListBlockInfo> oneListData = new ArrayList<>();


    /**
     * 显示所有免费下载章节
     */
    private void bindOneList() {
        GridViewAdpater_DownloadInfo viewAdpaterGift = new GridViewAdpater_DownloadInfo(mContext, listData, 1);
        gv_gift.setAdapter(viewAdpaterGift);


    }

    private DialogListener mListener;

    public void setDialogListener(DialogListener listener) {
        mListener = listener;
    }

    public interface DialogListener {
        void itemClick(ChapterBlockInfo chapterBlockInfo);

        void onOneDownLoad(List<ChapterBlockInfo> list);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
