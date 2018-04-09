package com.ysy15350.moduleaotaoreader.model;

import java.lang.ref.WeakReference;

/**
 * 缓存对象
 */

public class Cache {

    private int aid;
    private int cid;
    private int previous;
    private int next;

    private long size;

    private WeakReference<char[]> data;

    public WeakReference<char[]> getData() {
        return data;
    }

    public void setData(WeakReference<char[]> data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
