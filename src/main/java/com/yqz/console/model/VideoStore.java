package com.yqz.console.model;

import java.util.Date;

public class VideoStore {
	private Long id;

	private Long vsId;

	private Integer segSeq;

	private Integer segSize;

	private String segLocalPath;

	private Integer vMilliseconds;

	private Integer aMilliseconds;

	private Date createTime;

	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVsId() {
		return vsId;
	}

	public void setVsId(Long vsId) {
		this.vsId = vsId;
	}

	public Integer getSegSeq() {
		return segSeq;
	}

	public void setSegSeq(Integer segSeq) {
		this.segSeq = segSeq;
	}

	public Integer getSegSize() {
		return segSize;
	}

	public void setSegSize(Integer segSize) {
		this.segSize = segSize;
	}

	public String getSegLocalPath() {
		return segLocalPath;
	}

	public void setSegLocalPath(String segLocalPath) {
		this.segLocalPath = segLocalPath == null ? null : segLocalPath.trim();
	}

	public Integer getvMilliseconds() {
		return vMilliseconds;
	}

	public void setvMilliseconds(Integer vMilliseconds) {
		this.vMilliseconds = vMilliseconds;
	}

	public Integer getaMilliseconds() {
		return aMilliseconds;
	}

	public void setaMilliseconds(Integer aMilliseconds) {
		this.aMilliseconds = aMilliseconds;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}