package com.ysy15350.moduleaotaoreader.model;

import java.util.List;

/**
 * Created by yangshiyou on 2017/11/27.
 */

public class ViewCountModel {

    /**
     * status : 1
     * msg :
     * cid : 20143
     * data : [{"did":"2","progress":"0","count":"12"},{"did":"26","progress":"80","count":"3"},{"did":"4","progress":"60","count":"4"}]
     */

    private int status;
    private String msg;
    private String cid;
    private List<ViewCount> data;

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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<ViewCount> getData() {
        return data;
    }

    public void setData(List<ViewCount> data) {
        this.data = data;
    }


}
