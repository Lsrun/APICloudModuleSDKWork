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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.adapters.ListViewAdpater_Directory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import base.BaseFragment;
import base.data.BaseData;
import base.model.directory.DirectoryInfo;
import base.model.directory.DirectoryModel;
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
 * 目录
 */
public class MainTab1Fragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.mo_aotao_reader_activity_main_tab1, container, false);

        return view;
    }

    private int page = 1;
    private int pageSize = 10;

    private RelativeLayout relativeLayout;
    private ListView mXListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void initView() {
        super.initView();
        relativeLayout = (RelativeLayout) mContentView.findViewById(R.id.rl_listview);
        mXListView = (ListView) mContentView.findViewById(R.id.xListView);
        swipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_refresh);

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

                chapterlist(mAid);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加载框消失
                        swipeRefreshLayout.setRefreshing(false);

                    }
                },1500);

            }
        });
    }

    private int mAid = 0;
    private int uid = 0;

    @Override
    public void onResume() {
        super.onResume();

        mAid = ReadActivity.mAid;
        uid = ReadActivity.mUid;

        if (getActivity() != null) {

            ///  先获取本地目录数据
            String chapterlistJson = BaseData.getCache("chapterlistJson" + mAid);

            ReadActivity.log( "获取到的数据=====" + chapterlistJson);

            if (CommFun.isNullOrEmpty(chapterlistJson)) {

                chapterlist(mAid);

            } else {

                getDirectoryInfo(chapterlistJson);

            }

        }
    }


    /**
     * 初始化数据
     */
    private void initData(int page, int pageSize) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DirectoryInfo directoryInfo = (DirectoryInfo) parent.getItemAtPosition(position);
        try {
            if (directoryInfo != null) {

                ((ReadActivity) getActivity()).onDirectoryInfoItemClick(directoryInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    OkHttpClient mOkHttpClient = null;


    /**
     * 获取作品目录
     * @param aid  文章id
     */
    public void chapterlist(int aid) {
        mOkHttpClient = new OkHttpClient();

        String url = String.format(Locale.US, "%s/chapterlist.php?aid=%d&uid=%d", ReadActivity.SERVICE_URL, aid, uid);

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

                                BaseData.setCache("chapterlistJson" + mAid, str);

                                getDirectoryInfo(str);

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

    private ListViewAdpater_Directory mAdapter;

    /**
     * 显示目录数据
     * @param chapterlistJson  json数据
     */
    private void getDirectoryInfo(String chapterlistJson) {
        try {
            if (!CommFun.isNullOrEmpty(chapterlistJson)) {
                DirectoryModel directoryModel = JsonConvertor.fromJson(chapterlistJson, DirectoryModel.class);
                if (directoryModel != null) {
                    bindListView(directoryModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 目录对象集合
     */
    List<DirectoryInfo> mDirectoryInfos;

    private static final String TAG = "MainTab1Fragment";

    /**
     * 绑定列表
     *
     * @param directoryModel  数据集合
     */
    private void bindListView(DirectoryModel directoryModel) {
        try {
            if (directoryModel != null) {
                mDirectoryInfos = directoryModel.getData();

                mAdapter = new ListViewAdpater_Directory(getActivity(), mDirectoryInfos);

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
