package com.ysy15350.moduleaotaoreader.model;

import java.util.List;


public class ChapterBlockModel {

    private int status;
    private String msg;
    private String uid;
    private String uname;
    private String egold;
    private String monthlimit;
    private int uviplevel;
    private String ismonth;

    private List<ChapterBlockInfo> data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEgold() {
        return egold;
    }

    public void setEgold(String egold) {
        this.egold = egold;
    }

    public String getMonthlimit() {
        return monthlimit;
    }

    public void setMonthlimit(String monthlimit) {
        this.monthlimit = monthlimit;
    }

    public int getUviplevel() {
        return uviplevel;
    }

    public void setUviplevel(int uviplevel) {
        this.uviplevel = uviplevel;
    }

    public String getIsmonth() {
        return ismonth;
    }

    public void setIsmonth(String ismonth) {
        this.ismonth = ismonth;
    }

    public List<ChapterBlockInfo> getData() {
        return data;
    }

    public void setData(List<ChapterBlockInfo> data) {
        this.data = data;
    }

}
