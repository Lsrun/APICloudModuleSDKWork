package com.ysy15350.moduleaotaoreader.adapters;

import android.content.Context;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.model.CommentInfo;

import java.util.List;

import base.ViewHolder;
import common.CommFun;

/**
 * 阅读内，想法列表，弹窗
 */
public class ListViewAdpater_CommentInfo extends CommonAdapter<CommentInfo> {


    public ListViewAdpater_CommentInfo(Context context, List<CommentInfo> list) {
        super(context, list, R.layout.mo_aotao_reader_list_item_comment);


    }

    @Override
    public void convert(ViewHolder holder, CommentInfo t) {
        if (t != null) {

            String posttime = t.getPosttime();

            String timeStr = CommFun.stampToDateStr(posttime, "yyyy-MM-dd HH:mm:ss");

            holder
                    .setImageURL(R.id.img_head, t.getImage())
                    .setText(R.id.tv_nickname, t.getPoster())
                    .setText(R.id.tv_content, t.getPosttext())
                    .setText(R.id.tv_time, timeStr)
            ;
        }
    }


}
