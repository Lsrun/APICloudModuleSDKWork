package com.ysy15350.moduleaotaoreader.adapters;

import android.content.Context;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.model.BookMark;

import java.util.List;

import base.ViewHolder;

/**
 * 余额明细
 *
 * @author yangshiyou
 */
public class ListViewAdpater_BookMark extends CommonAdapter<BookMark> {


    public ListViewAdpater_BookMark(Context context, List<BookMark> list) {
        super(context, list, R.layout.mo_aotao_reader_list_item_book_mark);


    }

    @Override
    public void convert(ViewHolder holder, BookMark t) {
        if (t != null) {
            holder.setText(R.id.tv_title, t.getChapterName())
                    .setText(R.id.tv_progress, String.format("%.1f%%", t.getProgress() * 100))
                    .setText(R.id.tv_content, t.getContent())
                    .setText(R.id.tv_page, t.getPage() + " 页");

        }
    }
}
