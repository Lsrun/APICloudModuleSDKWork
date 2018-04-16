package base.mvp;

import android.os.Bundle;

import base.BaseFragment;


/**
 * 通过这个基类的生命周期控制与Presenter的关系
 *
 * @param <V>
 * @param <T>
 * @author yangshiyou
 */
public abstract class MVPBaseFragment<V, T extends BasePresenter<V>> extends BaseFragment {

    protected T mPresenter;//


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();

        mPresenter.attachView((V) this);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mPresenter.detachView();
        //mViewCommon.release();
    }

    protected abstract T createPresenter();


}
