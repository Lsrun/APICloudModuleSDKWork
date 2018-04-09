package base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import common.CommFunAndroid;
import common.message.MessageBox;

/**
 * Created by yangshiyou on 2017/10/30.
 */

public abstract class BaseFragment extends Fragment implements IView {
    private boolean injected = false;

    //这个activity就是MainActivity
    public Activity mActivity;

    /**
     * 控件ViewGroup
     */
    protected View mContentView;

    protected ViewHolder mHolder;

    /**
     * 界面标题
     */
    protected String mTitle = "";

    /**
     * 是否需要登录
     */
    boolean mNeedLogin = false;


    // Fragment被创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();// 获取所在的activity对象
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;

        mContentView = initFragmentView(inflater, container);

        //mContentView = (ViewGroup) x.view().inject(this, inflater, container);

        if (getActivity() != null)
            mHolder = ViewHolder.get(getActivity(), mContentView);

        init();

        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            //x.view().inject(this, this.getView());
        }
    }

    /**
     * 初始化布局, 子类必须实现
     */
    public abstract View initFragmentView(LayoutInflater inflater, ViewGroup container);


    /**
     * 初始化，1：initView；2：readCahce；3：loadData；4：bindData
     */
    protected void init() {

        initView();

        initData();

        readCahce();

        loadData();

        bindData();
    }

    /**
     * 初始化，1：initView；2：readCahce；3：loadData；4：bindData
     *
     * @param context
     * @param contentView
     * @param title
     * @param isNeedLogin
     */
    public void init(Context context, View contentView, String title, boolean isNeedLogin) {

        mTitle = title;
        mNeedLogin = isNeedLogin;


        initView();

        initData();

        readCahce();

        loadData();

        bindData();
    }


    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        // 填充状态栏
        //CommFunAndroid.fullScreenStatuBar(this);
    }

    @Override
    public void readCahce() {
    }

    @Override
    public void loadData() {
    }

    @Override
    public void bindData() {

    }

    protected boolean isLogin() {


        return false;
    }

    @Override
    public void showMsg(String msg) {
        if (CommFunAndroid.isNullOrEmpty(msg))
            return;
        MessageBox.show(msg);
    }

    @Override
    public void showWaitDialog(String msg) {
        if (CommFunAndroid.isNullOrEmpty(msg))
            return;
        //CommFunMessage.showWaitDialog(this, msg);
    }

    @Override
    public void hideWaitDialog() {

        //CommFunMessage.hideWaitDialog();
    }


    @Override
    public void setViewText(int id, CharSequence text) {
        if (mHolder != null)
            mHolder.setText(id, text);
    }

    @Override
    public String getViewText(int id) {
        if (mHolder != null)
            return mHolder.getViewText(id);
        return "";
    }

    @Override
    public void setTextColor(int id, int color) {
        if (mHolder != null)
            mHolder.setTextColor(id, color);
    }

    @Override
    public void setBackgroundColor(int id, int color) {
        if (mHolder != null)
            mHolder.setBackgroundColor(id, color);
    }

    @Override
    public void setVisibility_GONE(int id) {
        if (mHolder != null)
            mHolder.setVisibility_GONE(id);
    }

    @Override
    public void setVisibility_VISIBLE(int id) {
        if (mHolder != null)
            mHolder.setVisibility_VISIBLE(id);

    }


}
