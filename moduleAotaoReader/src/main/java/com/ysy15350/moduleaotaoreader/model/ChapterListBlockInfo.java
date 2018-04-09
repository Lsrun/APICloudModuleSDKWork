package com.ysy15350.moduleaotaoreader.model;


public class ChapterListBlockInfo {

    private String cid;
    private String chapterorder;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getChapterorder() {
        return chapterorder;
    }

    public void setChapterorder(String chapterorder) {
        this.chapterorder = chapterorder;
    }
}