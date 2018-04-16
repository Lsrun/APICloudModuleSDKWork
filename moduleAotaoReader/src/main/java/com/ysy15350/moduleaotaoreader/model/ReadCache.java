package com.ysy15350.moduleaotaoreader.model;

/**
 * Created by yangshiyou on 2017/11/22.
 */

/**
 * 阅读记录
 */
public class ReadCache {

    private int uid;

    private int aid;

    private int cid;

    private float progress;

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
}
