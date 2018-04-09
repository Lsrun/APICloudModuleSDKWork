package com.ysy15350.moduleaotaoreader.model;


public class FlagInfo {

    public FlagInfo(int index, float flag_x, float flag_y, int location_current_page,
                    int location_total, int line, String content, int range,float progress) {
        this.index = index;
        this.flag_x = flag_x;
        this.flag_y = flag_y;
        this.location_current_page = location_current_page;
        this.location_total = location_total;
        this.line = line;
        this.content = content;
        this.range = range;
        this.progress=progress;
    }


    private float progress;
    private int index;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    /**
     * x
     */
    private float flag_x;

    /**
     * y
     */
    private float flag_y;

    /**
     * 行数
     */
    private int line;

    /**
     * 距离当前页第一个文字位置（字数）
     */
    private int location_current_page;

    /**
     * 距离当前文章第一个文字位置（字数）
     */
    private int location_total;

    /**
     * 当前行文本内容
     */
    private String content;

    /**
     * 点击范围
     */
    private int range;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getFlag_x() {
        return flag_x;
    }

    public void setFlag_x(float flag_x) {
        this.flag_x = flag_x;
    }

    public float getFlag_y() {
        return flag_y;
    }

    public void setFlag_y(float flag_y) {
        this.flag_y = flag_y;
    }

    public int getLocation_current_page() {
        return location_current_page;
    }

    public void setLocation_current_page(int location_current_page) {
        this.location_current_page = location_current_page;
    }

    public int getLocation_total() {
        return location_total;
    }

    public void setLocation_total(int location_total) {
        this.location_total = location_total;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "flag_x:" + flag_x + ",flag_y:" + flag_y + ",location_current_page:" + location_current_page + "location_total:" + location_total + ",line:" + line + ",content:" + content + ",range" + range;
    }
}
