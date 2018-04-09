package base.mvp;

import android.os.Bundle;

import base.BaseActivity;


public abstract class MVPBaseActivity<V, T extends BasePresenter<V>> extends BaseActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();

        mPresenter.attachView((V) this);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        getWindow().setBackgroundDrawable(null);// android的默认背景是不是为空。

        mPresenter.detachView();

        System.gc();
    }

    protected abstract T createPresenter();

}
