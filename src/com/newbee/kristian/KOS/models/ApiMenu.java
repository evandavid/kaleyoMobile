package com.newbee.kristian.KOS.models;
import java.util.List;

import com.google.gson.annotations.SerializedName;
public class ApiMenu {
	public List<Menu> results;
	
	@SerializedName("code")
    public String code;
}
