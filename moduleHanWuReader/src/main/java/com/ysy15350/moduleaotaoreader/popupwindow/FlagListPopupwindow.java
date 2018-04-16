package com.ysy15350.moduleaotaoreader.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.adapters.ListViewAdpater_CommentInfo;
import com.ysy15350.moduleaotaoreader.adapters.ListViewAdpater_Directory;
import com.ysy15350.moduleaotaoreader.model.CommentInfo;
import com.ysy15350.moduleaotaoreader.model.CommentModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import base.ViewHolder;
import base.model.directory.DirectoryInfo;
import common.CommFun;
import common.CommFunAndroid;
import common.string.JsonConvertor;
import custom_view.x_view.XListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by yangshiyou on 2017/11/2.
 */

public class FlagListPopupwindow extends PopupWindow implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private static final String TAG = "FlagListPopupwindow";

    private Activity mContext;

    private View mContentView;

    protected ViewHolder mHolder;


    private ListView mXListView;


    private int mUid, mAid, mCid, mDid;


    public FlagListPopupwindow(final Activity context, int uid, int aid, int cid, int did) {

        mContext = context;

        mUid = uid;
        mAid = aid;
        mCid = cid;
        mDid = did;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.mo_aotao_reader_pop_flag_list, null);
        mHolder = ViewHolder.get(mContext, mContentView);

        init();
        initView();// 初始化按钮事件

        loadData();

    }

    private void init() {
        mContentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        int h = mContext.getWindowManager().getDefaultDisplay().getHeight();
        int w = mContext.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(mContentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
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
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.app_pop);
    }

    private void initView() {
        mXListView = (ListView) mContentView.findViewById(R.id.xListView);

        if (mXListView != null) {
            mXListView.setDivider(new ColorDrawable(mContext.getResources().getColor(R.color.devider_color))); // 设置间距颜色
            mXListView.setDividerHeight(CommFunAndroid.dip2px(0)); // 设置间距高度(此必须设置在setDivider（）之后，否则无效果)

            mXListView.setOnItemClickListener(this);

        }

        View ll_add_comment = mHolder.getView(R.id.ll_add_comment);
        if (ll_add_comment != null) {
            ll_add_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.addComment(mDid);
                        dismiss();
                    }
                }
            });
        }

    }

    public void updateData(int did) {
        mDid = did;
        getReviews(did);
    }


    public void showPopupWindow(View parent) {
        showPopupWindow(parent, 0, 0);
    }

    public void showPopupWindow(View parent, int x, int y) {
        if (!this.isShowing()) {
            showAtLocation(parent, Gravity.CENTER, x, y);

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

    private void loadData() {
        getReviews(mDid);
    }

    /**
     * 段落标记点，点击获取想法
     *
     * @param did
     */
    public void getReviews(int did) {
        String str = CommFunAndroid.getSharedPreferences(ReadActivity.READ_BOOK_COMMETLIST + mUid + mAid + did);
        getCommentModel(str);
        reviews(did, 1, 10);
    }


    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    private ListViewAdpater_CommentInfo mAdapter;

    private void getCommentModel(String str) {

        List<CommentInfo> data = null;

        if (!CommFun.isNullOrEmpty(str)) {

            CommentModel commentModel = JsonConvertor.fromJson(str, CommentModel.class);

            if (commentModel != null) {

                data = commentModel.getData();

            }
        }

        bindListView(data);
    }

    /**
     * 绑定列表
     */
    private void bindListView(List<CommentInfo> data) {

        try {
            if (data != null && data.size() > 0) {
                mHolder.setVisibility_GONE(R.id.ll_nodata).setVisibility_VISIBLE(R.id.xListView);
                mAdapter = new ListViewAdpater_CommentInfo(mContext, data);
                bindListView(mAdapter);
            } else {
                mHolder.setVisibility_VISIBLE(R.id.ll_nodata).setVisibility_GONE(R.id.xListView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int page = 1;
    private int pageSize = 10;


    /**
     * 绑定下拉列表
     */
    public void bindListView(BaseAdapter mAdapter) {
        // TODO Auto-generated method stub
        try {
            if (mXListView != null && mAdapter != null) {

                int count = mAdapter.getCount();
                if (count == 0) {
                    hideXListView();

                } else {
                    showXListView();
                }

                if (page == 1) {

                    String timeStr = CommFunAndroid.getDateString("yyyy-MM-dd HH:mm:ss");

                    mXListView.setAdapter(mAdapter);

                } else {
                    mAdapter.notifyDataSetChanged();


                }


            } else {
                hideXListView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showXListView() {

        mHolder.setVisibility_GONE(R.id.ll_nodata).setVisibility_VISIBLE(R.id.xListView);
    }

    public void hideXListView() {

        mHolder.setVisibility_GONE(R.id.xListView).setVisibility_VISIBLE(R.id.ll_nodata);

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

        String url = String.format(Locale.CHINA, "%s/reviews.php?aid=%d&cid=%d&did=%d&page=%d&pageSize=%d",
                ReadActivity.SERVICE_URL, mAid, mCid, did, page, pageSize);

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
                    CommFunAndroid.setSharedPreferences(ReadActivity.READ_BOOK_COMMETLIST + mUid + mAid + did, str);
                }

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getCommentModel(str);
                    }
                });
            }

        });
    }


    private PopupWindowListener mListener;

    public void setPopupWindowListener(PopupWindowListener listener) {
        mListener = listener;
    }

    public interface PopupWindowListener {
        void dismiss();

        void addComment(int did);
    }
}
