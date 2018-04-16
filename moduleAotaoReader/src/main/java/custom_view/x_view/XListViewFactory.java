package custom_view.x_view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.ysy15350.moduleaotaoreader.R;

import base.ViewHolder;
import common.CommFunAndroid;

/**
 * Created by yangshiyou on 2017/11/9.
 */

public class XListViewFactory implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private static XListViewFactory xListViewFactory;


    public static synchronized XListViewFactory getInstance() {
        return xListViewFactory;
    }

    public static synchronized XListViewFactory createPageFactory(Context context) {
        if (xListViewFactory == null) {
            xListViewFactory = new XListViewFactory(context);
        }
        return xListViewFactory;
    }

    private ViewHolder mHolder;
    private Context mContext;
    private View rl_listview;
    private XListView mXListView;

    private int page = 1;
    private int pageSize = 10;

    private XListViewFactory(Context context) {
        mContext = context;
    }

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
                    mXListView.setRefreshTime(timeStr);

                    mXListView.setAdapter(mAdapter);

                } else {
                    mAdapter.notifyDataSetChanged();


                }

                mXListView.stopRefresh();
                mXListView.stopLoadMore();


                //

                // // listview布局动画
                // if (mContext != null) {
                // LayoutAnimationController lac = new LayoutAnimationController(
                // AnimationUtils.loadAnimation(mContext, R.anim.zoom_in));
                // lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
                // xListView.setLayoutAnimation(lac);
                // xListView.startLayoutAnimation();
                // }
            } else {
                hideXListView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showXListView() {

        mHolder.setVisibility_GONE(R.id.ll_nodata)
                .setVisibility_VISIBLE(R.id.xListView)
        ;
    }

    public void hideXListView() {

        mHolder.setVisibility_GONE(R.id.xListView)
                .setVisibility_VISIBLE(R.id.ll_nodata);

    }


    public void setRl_listview(View view) {

        try {
            if (view != null && mContext != null) {
                this.rl_listview = view;

                mHolder = ViewHolder.get(mContext, view);

                XListView xListView = (XListView) view.findViewById(R.id.xListView);
                initXListView(xListView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initXListView(XListView xListView) {
        this.mXListView = xListView;

        this.mXListView.setXListViewListener(this);
        this.mXListView.setOnItemClickListener(this);

    }


    public void setPullLoadEnable(boolean enable) {
        if (this.mXListView != null)
            this.mXListView.setPullLoadEnable(enable);


    }

    public void setPullRefreshEnable(boolean enable) {
        if (this.mXListView != null)
            mXListView.setPullRefreshEnable(enable);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (xListViewFactoryListener != null) {
            xListViewFactoryListener.onItemClick(adapterView, view, i, l);
        }

    }

    @Override
    public void onRefresh() {
        if (xListViewFactoryListener != null) {
            xListViewFactoryListener.onRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (xListViewFactoryListener != null) {
            xListViewFactoryListener.onLoadMore();
        }
    }

    private XListViewFactoryListener xListViewFactoryListener;

    public void setxListViewFactoryListener(XListViewFactoryListener listener) {
        this.xListViewFactoryListener = listener;
    }

    public interface XListViewFactoryListener {

        public void onRefresh();

        public void onLoadMore();

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l);
    }
}
