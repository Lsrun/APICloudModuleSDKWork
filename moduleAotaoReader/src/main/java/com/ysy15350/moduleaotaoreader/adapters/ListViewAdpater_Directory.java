package com.ysy15350.moduleaotaoreader.adapters;

import android.content.Context;
import android.util.Log;

import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.db.DbUtil;
import com.ysy15350.moduleaotaoreader.util.PageFactory;

import java.io.File;
import java.util.List;
import java.util.Locale;

import base.ViewHolder;
import base.model.directory.DirectoryInfo;


public class ListViewAdpater_Directory extends CommonAdapter<DirectoryInfo> {


    public ListViewAdpater_Directory(Context context, List<DirectoryInfo> list) {
        super(context, list, R.layout.mo_aotao_reader_list_item_directory);

    }


    @Override
    public void convert(ViewHolder holder, DirectoryInfo t) {
        ReadActivity.log( "执行到此===目录=====");

        if (t != null) {
            int aid = ReadActivity.mAid;
            int cid = t.getChapterid();
            int uid = ReadActivity.mUid;

            int isCache = PageFactory.mBookUtil.isCache(aid, cid);

            int isVip = t.getIsvip();
            int isMy = t.getIsmy();

            boolean cache = (isCache > 0);

            try {
                if (isCache <= 0) {

                    String fileName = String.format(Locale.CHINA,"%d_%d_%d.txt", ReadActivity.mUid, aid, cid);

                    String fullPath = ReadActivity.savePath + "/" + fileName;

                    File file = new File(fullPath);
                    if (file != null && file.exists()) {
                        cache = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 显示章节标题
            holder.setText(R.id.tv_chaptername, t.getChaptername());

            if (isVip == 1) {
                holder.setVisibility_VISIBLE(R.id.img_vip);
            } else {
                holder.setVisibility_INVISIBLE(R.id.img_vip);
            }

            boolean hh = DbUtil.getInstence(mContext).isHas(cid, aid, uid);
            if (hh) {
                holder.setText(R.id.tv_isCache, "已购买 " + (cache ? "已下载" : ""));
            } else {
                if (isVip == 1 && isMy == 1) {
                    holder.setText(R.id.tv_isCache, "已购买 " + (cache ? "已下载" : ""));
                } else {
                    holder.setText(R.id.tv_isCache, (cache ? "已下载" : ""));
                }
            }

        }
    }
}
