package com.ysy15350.moduleaotaoreader.model;


public class ParagraphInfo {

    private int id;

    private int index;

    private int flag;

    private int length;

    private float start_y;

    private float end_y;

    private String content;

    private int viewsCount;  //想法数量

    private int indexX;

    private int indexY;

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


    public float getStart_y() {
        return start_y;
    }

    public void setStart_y(float start_y) {
        this.start_y = start_y;
    }

    public float getEnd_y() {
        return end_y;
    }

    public void setEnd_y(float end_y) {
        this.end_y = end_y;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }
}
