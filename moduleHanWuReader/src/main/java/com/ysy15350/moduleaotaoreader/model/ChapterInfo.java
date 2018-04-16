package com.ysy15350.moduleaotaoreader.model;


public class ChapterInfo {
    private int status;
    private int isvip;  //0:免费 1:vip章节
    private int ismy;   //0:未购买 1:已经购买
    private String msg;
    private int aid;
    private int cid;
    private int previous;
    private int next;

    private int nextvip;
    private int nextmy;

    private int chaptertype;
    private String articlename;
    private String chaptername;
    private String content;
    private String contenthtml;
    private String summary;
    private int words;
    private String notice;

    private int premy;
    private int previp;

    public String getContenthtml() {
        return contenthtml;
    }

    public void setContenthtml(String contenthtml) {
        this.contenthtml = contenthtml;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public int getChaptertype() {
        return chaptertype;
    }

    public void setChaptertype(int chaptertype) {
        this.chaptertype = chaptertype;
    }

    public String getArticlename() {
        return articlename;
    }

    public void setArticlename(String articlename) {
        this.articlename = articlename;
    }

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
