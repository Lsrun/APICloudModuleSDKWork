package com.ysy15350.moduleaotaoreader.adapters;

import android.content.Context;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.model.ChapterBlockInfo;

import java.util.List;
import java.util.Locale;

import base.ViewHolder;

/**
 * 章节下载
 */
public class GridViewAdpater_DownloadInfo extends CommonAdapter<ChapterBlockInfo> {

    private int code;


    public GridViewAdpater_DownloadInfo(Context context, List<ChapterBlockInfo> list, int code) {
        super(context, list, R.layout.mo_aotao_reader_grid_item_download_info);
        this.code = code;

    }

    @Override
    public void convert(ViewHolder holder, ChapterBlockInfo t) {
        try {
            if (t != null) {

                if (code == 1) {
//
//                    holder.setVisibility_GONE(R.id.tv_discount);
//                    holder.setText(R.id.tv_title, t.getBtnName());
//                    if(t.isCheck()){
//                        holder.getView(R.id.ll_main).setBackgroundResource(R.drawable.mo_aotao_reader_shape_download_info_grid_item_selected);
//                    }else{
//                        holder.getView(R.id.ll_main).setBackgroundResource(R.drawable.mo_aotao_reader_shape_download_info_grid_item);
//                    }

                } else if (code == 2) {

                    if (t.getDiscount() == 0) {
                        holder.setVisibility_GONE(R.id.tv_discount);
                    } else {
                        holder.setVisibility_VISIBLE(R.id.tv_discount);
                    }

                    holder.setText(R.id.tv_title, String.format(Locale.CHINA, t.getBtnName() + "下载 (%d-%d) 章", t.getFromOrder(), t.getToOrder()))
                            .setText(R.id.tv_discount, String.format(Locale.CHINA, "%.1f折", t.getDiscount()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
