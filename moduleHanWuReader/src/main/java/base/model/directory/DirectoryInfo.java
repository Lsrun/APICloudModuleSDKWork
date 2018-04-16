package base.model.directory;

import java.io.Serializable;

/**
 * Created by yangshiyou on 2017/11/9.
 */

public class DirectoryInfo implements Serializable {


    /**
     * chapterid : 72142
     * chaptertype : 1
     * chaptername : 第一卷
     * isvip : 0
     * saleprice : 0
     * postdate : 20170106165625
     * lastupdate : 20170106165625
     * words : 0
     */

    private int chapterid;
    private int chaptertype;
    private String chaptername;
    private int isvip;
    private String saleprice;
    private String postdate;
    private String lastupdate;
    private int words;
    private int ismy;

    public int getIsmy() {
        return ismy;
    }

    public void setIsmy(int ismy) {
        this.ismy = ismy;
    }

    public int getChapterid() {
        return chapterid;
    }

    public void setChapterid(int chapterid) {
        this.chapterid = chapterid;
    }

    public int getChaptertype() {
        return chaptertype;
    }

    public void setChaptertype(int chaptertype) {
        this.chaptertype = chaptertype;
    }

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }

    public int getIsvip() {
        return isvip;
    }

    public void setIsvip(int isvip) {
        this.isvip = isvip;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }
}
