package com.ysy15350.moduleaotaoreader.model;

import java.util.List;

/**
 * Created by yangshiyou on 2017/11/24.
 */

public class CommentModel {


    /**
     * status : 1
     * msg : 
     * totalCount : 0
     * currentPage : 1
     * pageSize : 10
     * totalPages : 0
     * data : [{"image":"http://106.14.4.243/avatar.php?uid=17013","posterid":"17013","poster":"小道","posttext":"给钱了","posttime":"1511516005"}]
     * progress : 12
     */

    private int status;
    private String msg;
    private int totalCount;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private String progress;
    private List<CommentInfo> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public List<CommentInfo> getData() {
        return data;
    }

    public void setData(List<CommentInfo> data) {
        this.data = data;
    }

}
