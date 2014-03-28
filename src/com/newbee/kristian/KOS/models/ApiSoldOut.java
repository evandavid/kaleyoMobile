package com.newbee.kristian.KOS.models;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ApiSoldOut {
	public List<SoldOut> results;
	
	@SerializedName("code")
    public String code;

}
