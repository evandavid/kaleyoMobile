package com.newbee.kristian.KOS.models;

import com.google.gson.annotations.SerializedName;

public class SpecialRequest {
	@SerializedName("request_name")
	public String requestName;
	
	@SerializedName("show_quantity")
	public String showQuantity;
	
	@SerializedName("request_summary")
	public String requestSummary;
	
	@SerializedName("request_no")
	public String requestNo;
	
	@SerializedName("amount")
	public String amount;
	
	@SerializedName("to_save")
	public String toSave;
	
	public boolean isNote = false;
	
	public SpecialRequest(SpecialRequest sr){
		this.requestName = sr.requestName;
		this.showQuantity = sr.showQuantity;
		this.requestNo = sr.requestNo;
		this.requestSummary = sr.requestSummary;
		this.amount = sr.amount;
		this.isNote = sr.isNote;
		this.toSave = sr.toSave;
	}
	
	public SpecialRequest(String sr) {
		this.requestName = sr;
		this.isNote = true;
		this.showQuantity = "0";
	}

}
