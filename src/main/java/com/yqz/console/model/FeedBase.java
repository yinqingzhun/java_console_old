package com.yqz.console.model;

import java.util.ArrayList;
import java.util.List;

public class FeedBase {

    public FeedBase(int objid, int objtype, String publishtime) {
        this.objid = objid;
        this.objtype = objtype;
        this.publishtime = publishtime;
    }

    /**
     * 内容id
     */
    private int objid;

    /**
     * 内容类型
     */
    private int objtype;

    /**
     * 发布时间
     */
    private String publishtime;

    /**
     * 关注维度
     */
    private List<ConcernTypeVO> concerntype = new ArrayList<ConcernTypeVO>();

    public int getObjid() {
        return objid;
    }

    public void setObjid(int objid) {
        this.objid = objid;
    }

    public int getObjtype() {
        return objtype;
    }

    public void setObjtype(int objtype) {
        this.objtype = objtype;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public List<ConcernTypeVO> getConcerntype() {
        return concerntype;
    }

    public void setConcerntype(List<ConcernTypeVO> concerntype) {
        this.concerntype = concerntype;
    }

     
}