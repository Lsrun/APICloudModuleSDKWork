package com.ysy15350.moduleaotaoreader.model;

import java.util.List;


public class ChapterBlockInfo {

    private boolean isCheck;
    private int isvip;
    private int isall;
    private float sumprice;
    private float discount;
    private int fromOrder;
    private int toOrder;
    private String content;
    private int cid;

    private List<ChapterListBlockInfo> chapterlist;
    private String btnName;

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsvip() {
        return isvip;
    }

    public void setIsvip(int isvip) {
        this.isvip = isvip;
    }

    public int getIsall() {
        return isall;
    }

    public void setIsall(int isall) {
        this.isall = isall;
    }

    public float getSumprice() {
        return sumprice;
    }

    public void setSumprice(float sumprice) {
        this.sumprice = sumprice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getFromOrder() {
        return fromOrder;
    }

    public void setFromOrder(int fromOrder) {
        this.fromOrder = fromOrder;
    }

    public int getToOrder() {
        return toOrder;
    }

    public void setToOrder(int toOrder) {
        this.toOrder = toOrder;
    }

    public List<ChapterListBlockInfo> getChapterlist() {
        return chapterlist;
    }

    public void setChapterlist(List<ChapterListBlockInfo> chapterlist) {
        this.chapterlist = chapterlist;
    }

}
