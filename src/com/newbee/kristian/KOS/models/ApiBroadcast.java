package com.newbee.kristian.KOS.models;

import com.google.gson.annotations.SerializedName;

public class ApiBroadcast {
	@SerializedName("code")
	public String code;
	
	@SerializedName("text")
	public String text;
	
	@SerializedName("ids")
	public String ids;
	
	@SerializedName("created_by")
	public String createdBy;
}
