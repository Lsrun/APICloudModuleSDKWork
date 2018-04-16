package com.ysy15350.moduleaotaoreader.util;

import java.util.List;


public class TRPage {
    private int aid;
    private int cid;
    private int previous;
    private int next;
    private long pageIndex;
    private long pageTotal;
    private long begin;
    private long end;
    private long bookLenth;

    private List<String> lines;

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

    public long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getBookLenth() {
        return bookLenth;
    }

    public void setBookLenth(long bookLenth) {
        this.bookLenth = bookLenth;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLineToString() {
        String text = "";
        if (lines != null) {
            for (String line : lines) {
                text += line;
            }
        }
        return text;
    }


    public void setLines(List<String> lines) {
        this.lines = lines;
    }


}
