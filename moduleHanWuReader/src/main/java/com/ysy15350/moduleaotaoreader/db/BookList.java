package com.ysy15350.moduleaotaoreader.db;

import com.ysy15350.moduleaotaoreader.model.ChapterInfo;
import com.ysy15350.moduleaotaoreader.model.ViewCount;

import java.io.Serializable;
import java.util.List;

/**
 * 书籍对象
 */
public class BookList implements Serializable {
    private int id;
    private boolean isOpenBook;

    /**
     *  0:免费 1:vip章节
     */
    private int isvip;

    /**
     * 0:未购买 1:已经购买
     */
    private int ismy;
    private String bookname;
    private String chaptername;
    private int aid;
    private int cid;
    private int previous;
    private int next;

    /**
     * 下一张vip
     */
    private int nextvip;

    /**
     * 下一张是否购买
     */
    private int nextmy;
    /**
     * 上一章是否购买
     */
    private int premy;
    /**
     * 上一章是否需要购买
     */
    private int previp;

    private String bookpath;
    private long begin;
    private String charset;
    private String content;
    private List<ViewCount> viewCountList;     //段落想法数量
    private List<ChapterInfo> chapterInfoList;  //当前章节前后两章

    public int getPremy() {
        return premy;
    }

    public void setPremy(int premy) {
        this.premy = premy;
    }

    public int getPrevip() {
        return previp;
    }

    public void setPrevip(int previp) {
        this.previp = previp;
    }

    public int getNextvip() {
        return nextvip;
    }

    public void setNextvip(int nextvip) {
        this.nextvip = nextvip;
    }

    public int getNextmy() {
        return nextmy;
    }

    public void setNextmy(int nextmy) {
        this.nextmy = nextmy;
    }

    public boolean isOpenBook() {
        return isOpenBook;
    }

    public void setOpenBook(boolean openBook) {
        isOpenBook = openBook;
    }

    public int getIsvip() {
        return isvip;
    }

    public void setIsvip(int isvip) {
        this.isvip = isvip;
    }

    public int getIsmy() {
        return ismy;
    }

    public void setIsmy(int ismy) {
        this.ismy = ismy;
    }

    public String getBookname() {
        return this.bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
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

    public String getBookpath() {
        return this.bookpath;
    }

    public void setBookpath(String bookpath) {
        this.bookpath = bookpath;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ViewCount> getViewCountList() {
        return viewCountList;
    }

    public void setViewCountList(List<ViewCount> viewCountList) {
        this.viewCountList = viewCountList;
    }

    public List<ChapterInfo> getChapterInfoList() {
        return chapterInfoList;
    }

    public void setChapterInfoList(List<ChapterInfo> chapterInfoList) {
        this.chapterInfoList = chapterInfoList;
    }
}
