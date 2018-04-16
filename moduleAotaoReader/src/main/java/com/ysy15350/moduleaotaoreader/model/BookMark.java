package com.ysy15350.moduleaotaoreader.model;

/**
 * Created by yangshiyou on 2017/11/22.
 */

/**
 * 书签
 */
public class BookMark {

    private int uid;

    private int aid;

    private int cid;

    private float progress;

    private int page;

    private String articlename;

    private String chapterName;

    private String content;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getArticlename() {
        return articlename;
    }

    public void setArticlename(String articlename) {
        this.articlename = articlename;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 用户id+图书ID+章节ID
     *
     * @return
     */
    public String getIds() {
        return String.format("%d%d%d", this.getUid(), this.getAid(), this.getCid()) + this.getProgress();
    }

}
