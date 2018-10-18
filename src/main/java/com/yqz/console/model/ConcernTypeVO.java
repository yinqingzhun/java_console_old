package com.yqz.console.model;

public class ConcernTypeVO {
	
	private int type;
	
	private int objId;
	

	public ConcernTypeVO() {
		super();
	}
	

	public ConcernTypeVO(int type, int objId) {
		super();
		this.type = type;
		this.objId = objId;
	}



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getObjId() {
		return objId;
	}

	public void setObjId(int objId) {
		this.objId = objId;
	}

	@Override
	public String toString() {
		return "ConcernTypeVO [type=" + type + ", objId=" + objId + "]";
	}
	
	
}
