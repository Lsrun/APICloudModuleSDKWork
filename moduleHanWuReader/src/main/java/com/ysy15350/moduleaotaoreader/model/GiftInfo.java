package com.ysy15350.moduleaotaoreader.model;


public class GiftInfo {

    private int rid;//打赏类型ID(取值范围:2，3，4，5，6，7)
    private int count;//礼物数量
    private int value;//
    private String content;
    private int resId;//图片ID

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
