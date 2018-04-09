package com.ysy15350.moduleaotaoreader.adapters;

import android.content.Context;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.model.BookComment;

import java.util.List;

import base.ViewHolder;
import common.CommFun;

/**
 * 我的想法
 *
 * @author yangshiyou
 */
public class ListViewAdpater_MyBookComment extends CommonAdapter<BookComment> {


    public ListViewAdpater_MyBookComment(Context context, List<BookComment> list) {
        super(context, list, R.layout.mo_aotao_reader_list_item_my_book_comment);


    }

    @Override
    public void convert(ViewHolder holder, BookComment t) {
        if (t != null) {
            holder
                    .setText(R.id.tv_title, String.format("【%s】 %s", t.getArticlename(), t.getChaptername()))
                    .setText(R.id.tv_time, String.format("%s", CommFun.stampToDateStr(t.getPosttime(), "yyyy-MM-dd hh:mm:ss")))
                    .setText(R.id.tv_progress, String.format("%d%%", t.getProgress()))
                    .setText(R.id.tv_content, String.format("想法：%s", t.getPosttext()));

        }
    }
}
