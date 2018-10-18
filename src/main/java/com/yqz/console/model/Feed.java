package com.yqz.console.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * feed流
 * @author liuchao
 * @date 2017/12/07
 **/
 
public class Feed {
   private Boolean enabled;
    /**
     * 作者id(目前只有原创的文章、视频、直播有作者id)
     */
    private int authorid;

    /**
     * 作者类型
     */
    private int authortype;

    /**
     * 内容id
     */
    @JsonProperty("Objid")
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
    /**
     * 车系id
     */
    private int seriesid;
    private  String name;

 public Boolean getEnabled() {
  return enabled;
 }

 public void setEnabled(Boolean enabled) {
  this.enabled = enabled;
 }

 public int getAuthorid() {
  return authorid;
 }

 public void setAuthorid(int authorid) {
  this.authorid = authorid;
 }

 public int getAuthortype() {
  return authortype;
 }

 public void setAuthortype(int authortype) {
  this.authortype = authortype;
 }

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

 public int getSeriesid() {
  return seriesid;
 }

 public void setSeriesid(int seriesid) {
  this.seriesid = seriesid;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }
}
