package com.newbee.kristian.KOS.models;
import java.util.List;

import com.google.gson.annotations.SerializedName;
public class ApiUser {
	public List<User> results;
	public List<User> staffs;
	
	@SerializedName("code")
    public String code;
}
