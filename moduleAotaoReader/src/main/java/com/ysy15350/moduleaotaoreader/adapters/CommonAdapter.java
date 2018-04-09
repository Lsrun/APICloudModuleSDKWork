package com.ysy15350.moduleaotaoreader.adapters;

/**
 * Created by yangshiyou on 2017/7/10.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ysy15350.moduleaotaoreader.ReadActivity;

import java.util.List;

import base.ViewHolder;


/**
 * 公共适配器，指定数据源，数据源的数据类型即可,根据需求调用不同构造方法，传入对应参数,在convert 方法中对item控件赋值
 *
 * @param <T>
 * @author Administrator
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected ViewHolder mViewHolder;
    private LayoutInflater mInflater;
    private List<T> mList;
    private int mlayoutId;

    protected ViewGroup mParent;


    /**
     * 公共适配器构造方法
     *
     * @param context  上下文
     * @param list     数据源
     * @param layoutId 指定item布局文件id
     */
    public CommonAdapter(Context context, List<T> list, int layoutId) {

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
        this.mlayoutId = layoutId;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ReadActivity.log("是否执行到此-------121212--------");

        try {
            mParent = parent;
            ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mlayoutId, position);

            mViewHolder = holder;

            ReadActivity.log("是否执行到此-------4444444--------");

            convert(holder, getItem(position));

            return holder.getmConvertView();

        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;

    }

    /**
     * 对控件赋值:
     * <p>
     * holder.setTag(R.id.tv_1, t); holder.setText(R.id.tv_1,t.getName());
     *
     * @param holder
     * @param t
     */
    public abstract void convert(ViewHolder holder, T t);


    /**
     * 回收bitmap
     */
    public void destoryImageView() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

