package com.newbee.kristian.KOS.models;

import com.google.gson.annotations.SerializedName;

public class SoldOutDetail {
	@SerializedName("detail_id")
	public String detailId;
	
	@SerializedName("item_name")
	public String itemName;
	
	@SerializedName("sold_out")
	public String soldOut;
}
