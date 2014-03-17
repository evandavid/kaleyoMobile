package com.newbee.kristian.KOS.models;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ApiCategories {
	public List<Category> results;
	
	@SerializedName("code")
    public String code;

}
