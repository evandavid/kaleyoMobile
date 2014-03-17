package com.newbee.kristian.KOS.models;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ApiTables {
	public List<Table> results;
	
	@SerializedName("code")
    public String code;
	
	@SerializedName("total_results_count")
    public String totalResultCount;
	
	@SerializedName("results_count")
    public String resultCount;
}
