package com.ysy15350.moduleaotaoreader.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.adapters.ListViewAdpater_BookMark;
import com.ysy15350.moduleaotaoreader.model.BookMark;

import java.util.List;

import base.BaseFragment;
import common.CommFun;
import common.CommFunAndroid;
import common.string.JsonConvertor;
import custom_view.x_view.XListView;
import okhttp3.OkHttpClient;


/**
 * 书签
 */
public class MainTab2Fragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.mo_aotao_reader_activity_main_tab2, container, false);
        return view;
    }

    private int page = 1, pageSize = 10;

    private View rl_listview;
    private ListView mXListView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void initView() {
        super.initView();
        rl_listview = mContentView.findViewById(R.id.rl_listview);
        mXListView = (ListView) mContentView.findViewById(R.id.xListView);
        swipeRefreshLayout= (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_refresh);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_orange_light,
                    android.R.color.holo_red_light,android.R.color.holo_green_dark);
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
                },1500);

            }
        });

    }

    private int mUid = 0;
    private int mAid = 0;


    @Override
    public void onResume() {
        super.onResume();

        mUid = ReadActivity.mUid;
        mAid = ReadActivity.mAid;

        page = 1;
        initData(page, pageSize);
    }

    private void initData(int page, int pageSize) {
        List<BookMark> bookMarkList = getBookMarkList();
        bindListView(bookMarkList);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookMark bookMark = (BookMark) parent.getItemAtPosition(position);
        try {
            if (bookMark != null) {

                ((ReadActivity) getActivity()).loadDataFromShuQian(2,bookMark.getUid(),bookMark.getAid(),bookMark.getCid(),bookMark.getProgress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    OkHttpClient mOkHttpClient = null;

    private static final String SERVICE_URL = "http://106.14.4.243/riku/reader";


    /**
     * 获取书签列表
     *
     * @return
     */
    private List<BookMark> getBookMarkList() {
        String read_book_mark_cache = CommFunAndroid.getSharedPreferences(ReadActivity.READ_BOOK_MARK + mUid + mAid);
        try {
            if (!CommFun.isNullOrEmpty(read_book_mark_cache)) {
                List<BookMark> bookMarkList = JsonConvertor.fromJson(read_book_mark_cache, new TypeToken<List<BookMark>>() {
                }.getType());
                return bookMarkList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private ListViewAdpater_BookMark mAdapter;


    private void bindListView(List<BookMark> bookMarkList) {
        try {
            if (bookMarkList != null) {
                mAdapter = new ListViewAdpater_BookMark(getActivity(), bookMarkList);
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


}