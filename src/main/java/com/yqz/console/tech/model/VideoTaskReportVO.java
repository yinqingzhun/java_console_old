package com.yqz.console.tech.model;

import java.util.ArrayList;
import java.util.List;

public class VideoTaskReportVO {
	private Integer taskId;
	private Integer workDuration;
	private Integer retryCount;
	private String log;
	private List<VideoStore> storeList = new ArrayList<>();

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getWorkDuration() {
		return workDuration;
	}

	public void setWorkDuration(Integer workDuration) {
		this.workDuration = workDuration;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public List<VideoStore> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<VideoStore> storeList) {
		this.storeList = storeList;
	}
}