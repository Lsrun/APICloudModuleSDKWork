package com.ysy15350.moduleaotaoreader.adapters;

import android.content.Context;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.model.GiftInfo;

import java.util.List;

import base.ViewHolder;


public class GridViewAdpater_Gift extends CommonAdapter<GiftInfo> {

    public GridViewAdpater_Gift(Context context, List<GiftInfo> list) {
        super(context, list, R.layout.mo_aotao_reader_grid_item_gift);

    }

    @Override
    public void convert(ViewHolder holder, GiftInfo t) {
        try {
            if (t != null) {
                holder.setImageResource(R.id.img_logo, t.getResId())
                        .setText(R.id.tv_title, String.valueOf(t.getValue()))
                        .setText(R.id.tv_content,t.getContent());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
