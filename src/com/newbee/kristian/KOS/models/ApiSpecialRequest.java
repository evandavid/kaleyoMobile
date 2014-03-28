package com.newbee.kristian.KOS.models;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ApiSpecialRequest {
	public List<SpecialRequest> results;
	
	@SerializedName("code")
    public String code;

}
