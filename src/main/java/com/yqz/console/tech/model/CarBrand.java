package com.yqz.console.tech.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarBrand {

	private String brandName;
	private List<CarSerial> list = new ArrayList<>();
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<CarSerial> getList() {
		return list;
	}

	public void setList(List<CarSerial> list) {
		this.list = list;
	}

}
