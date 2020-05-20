package com.yqz.console.tech;

import com.yqz.console.tech.model.BrandItem;

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
