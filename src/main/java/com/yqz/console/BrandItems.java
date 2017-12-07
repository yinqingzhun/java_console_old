package com.yqz.console;

import com.yqz.console.model.BrandItem;

public class BrandItems {
	private BrandItem[] branditems;

	public BrandItem[] getBranditems() {
		if (branditems == null)
			branditems = new BrandItem[0];
		return branditems;
	}

	public void setBranditems(BrandItem[] branditems) {
		this.branditems = branditems;
	}
}
