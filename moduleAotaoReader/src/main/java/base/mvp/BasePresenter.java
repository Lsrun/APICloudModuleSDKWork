package base.mvp;

import android.content.Context;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


public class BasePresenter<T> {

    protected Reference<T> mViewRef;// View接口类型的弱引用，简而言之，reference就是一个地址

    protected T mView;

    protected Context mContext;// 上下文


    public BasePresenter() {

    }

    public BasePresenter(Context context) {
        mContext = context;
    }

    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);// 建立关联
        mView = getView();
    }

    protected T getView() {
        return mViewRef.get();// 使用get() 获取软引用所引用的对象
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
