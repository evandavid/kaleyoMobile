package com.newbee.kristian.KOS.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SoldOut {
	@SerializedName("item_id")
	public String itemId;
	
	@SerializedName("item_name")
	public String itemName;
	
	@SerializedName("category")
	public String category;
	
	@SerializedName("sold_out")
	public String soldOut;
	
	@SerializedName("has_detail")
	public String hasDetail;
	
	public List<SoldOutDetail> details;
	
	public int position, hasDetailPos;
}
