package com.ysy15350.moduleaotaoreader.adapters;

import android.content.Context;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.model.RechargeInfo;

import java.util.List;

import base.ViewHolder;

/**
 * 礼物
 *
 * @author yangshiyou
 */
public class GridViewAdpater_RechargeInfo extends CommonAdapter<RechargeInfo> {


    public GridViewAdpater_RechargeInfo(Context context, List<RechargeInfo> list) {
        super(context, list, R.layout.mo_aotao_reader_grid_item_recharge_info);

    }

    @Override
    public void convert(ViewHolder holder, RechargeInfo t) {

        /**
         * isvip : 0
         * isall : 0
         * sumprice : 0
         * discount : 0
         * fromOrder : 0
         * toOrder : 14
         * chapterlist
         **/
        try {
            if (t != null) {
                holder.setText(R.id.tv_title, t.getValue() + "元")
                        .setText(R.id.tv_content, t.getContent());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
