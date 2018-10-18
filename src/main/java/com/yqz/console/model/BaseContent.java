package com.yqz.console.model;

/**
 * 基础内容信息
 * @author liuchao
 * @date 2017年11月20日 下午1:51:14
 */
public class BaseContent {
	
	/**
	 * 用户id
	 */
	private int uid;
	
	/**
	 * 用户类型
	 */
	private int utype;
	
	/**
	 * 内容id
	 */
	private int cid;
	
	/**
	 * 内容类型
	 */
	private int ctype;

	/**
	 * 操作类型(1:添加、2:删除、3:修改)
	 */
	private int oprtype;
	
	/**
	 * 发布时间
	 */
	private String posttime;
	
	/**
	 * 车系id
	 */
	private String seriesid;
	
	/**
	 * 车型id
	 */
	private String specid;
	
	/**
	 * 城市id
	 */
	private String cityid;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getUtype() {
		return utype;
	}

	public void setUtype(int utype) {
		this.utype = utype;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getCtype() {
		return ctype;
	}

	public void setCtype(int ctype) {
		this.ctype = ctype;
	}

	public int getOprtype() {
		return oprtype;
	}

	public void setOprtype(int oprtype) {
		this.oprtype = oprtype;
	}

	public String getPosttime() {
		return posttime;
	}

	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}

	public String getSeriesid() {
		return seriesid;
	}

	public void setSeriesid(String seriesid) {
		this.seriesid = seriesid;
	}

	public String getSpecid() {
		return specid;
	}

	public void setSpecid(String specid) {
		this.specid = specid;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	@Override
	public String toString() {
		return "BaseContent [uid=" + uid + ", utype=" + utype + ", cid=" + cid + ", ctype=" + ctype + ", oprtype="
				+ oprtype + ", posttime=" + posttime + ", seriesid=" + seriesid + ", specid=" + specid + ", cityid="
				+ cityid + "]";
	}
	
	
	
}
