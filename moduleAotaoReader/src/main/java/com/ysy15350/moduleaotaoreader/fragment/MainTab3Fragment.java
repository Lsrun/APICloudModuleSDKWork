package com.ysy15350.moduleaotaoreader.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.adapters.ListViewAdpater_MyBookComment;
import com.ysy15350.moduleaotaoreader.model.BookComment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import base.BaseFragment;
import base.data.BaseData;
import base.model.directory.DirectoryInfo;
import common.CommFun;
import common.CommFunAndroid;
import common.string.JsonConvertor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 想法
 */
public class MainTab3Fragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.mo_aotao_reader_activity_main_tab3, container, false);
        return view;
    }

    private int page = 1;
    private int pageSize = 30;

    private RelativeLayout rl_listview;
    private LinearLayout linearLayout;
    private ListView mXListView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void initView() {
        super.initView();
        rl_listview = (RelativeLayout) mContentView.findViewById(R.id.rl_listview);
        linearLayout = (LinearLayout) mContentView.findViewById(R.id.ll_nodata);
        mXListView = (ListView) mContentView.findViewById(R.id.xListView);
        swipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_refresh);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                    android.R.color.holo_red_light, android.R.color.holo_green_dark);
        }

        if (mXListView != null) {
            mXListView.setDivider(new ColorDrawable(getResources().getColor(R.color.devider_color))); // 设置间距颜色
            mXListView.setDividerHeight(CommFunAndroid.dip2px(1)); // 设置间距高度(此必须设置在setDivider（）之后，否则无效果)
            mXListView.setOnItemClickListener(this);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initData(page, pageSize);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);

            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(page, pageSize);
            }
        });
    }

    private int mAid = 0;
    private int mCid = 0;
    private int mUid = 0;

    @Override
    public void onResume() {
        super.onResume();
        mAid = ReadActivity.mAid;
        mUid = ReadActivity.mUid;
        mCid = ReadActivity.mCid;

        if (getActivity() != null) {
            String myCommentlistJson = BaseData.getCache("myCommentlistJson" + mUid);
            if (CommFun.isNullOrEmpty(myCommentlistJson)) {
                myreviews();
            } else {
                getBookCommentList(myCommentlistJson);
            }

            page = 1;
            initData(page, pageSize);
        }
    }

    private void initData(int page, int pageSize) {
        myreviews();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookComment bookComment = (BookComment) parent.getItemAtPosition(position);
        try {
            if (bookComment != null) {
                int aid = CommFun.toInt32(bookComment.getAid(), 0);
                int cid = CommFun.toInt32(bookComment.getCid(), 0);
                float du = bookComment.getProgress();

                ///  设置进度
                float progress = 0.01f * du;

                ((ReadActivity) getActivity()).loadBookDataFromIdea(2, aid, cid, progress);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    OkHttpClient mOkHttpClient = null;


    /**
     * 我的想法
     */
    private void myreviews() {
        mOkHttpClient = new OkHttpClient();

        String url = String.format(Locale.CHINA, "%s/myreviews.php?aid=%d&uid=%d", ReadActivity.SERVICE_URL, mAid, mUid);

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

                ReadActivity.log( str);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!CommFun.isNullOrEmpty(str)) {
                                BaseData.setCache("myCommentlistJson" + mUid, str);
                                getBookCommentList(str);
                            }

                        }
                    });
                }
            }

        });

    }

    /**
     * 当前章节
     */
    private DirectoryInfo mCurrentDirectoryInfo;

    private ListViewAdpater_MyBookComment mAdapter;


    private void getBookCommentList(String myCommentlistJson) {
        try {
            if (!CommFun.isNullOrEmpty(myCommentlistJson)) {
                MyCommentModel myCommentModel = JsonConvertor.fromJson(myCommentlistJson, MyCommentModel.class);
                if (myCommentModel != null) {
                    List<BookComment> bookCommentList = myCommentModel.getData();
                    bindListView(bookCommentList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定列表
     *
     * @param bookCommentList 列表集合
     */
    private void bindListView(List<BookComment> bookCommentList) {

        try {
            if (bookCommentList != null) {
                mAdapter = new ListViewAdpater_MyBookComment(getActivity(), bookCommentList);
                bindListView(mAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定下拉列表
     */
    public void bindListView(BaseAdapter mAdapter) {

        try {
            if (mXListView != null && mAdapter != null) {
                int count = mAdapter.getCount();
                if (count == 0) {
                    hideXListView();

                } else {
                    showXListView();
                }

                if (page == 1) {
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

    private class MyCommentModel {
        private int status;
        private String msg;

        private List<BookComment> data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<BookComment> getData() {
            return data;
        }

        public void setData(List<BookComment> data) {
            this.data = data;
        }
    }

}