package com.newbee.kristian.KOS.models;

import com.google.gson.annotations.SerializedName;

public class User {
	public static String OPERATOR = "0";
	public static String SUPERVISOR = "1";
	public static String MANAGER = "2";
	public static String ADMIN = "3";
	
	@SerializedName("user_id")
	public String userId;
	
	@SerializedName("mobile_password")
	public String mobilePassword;
	
	@SerializedName("first_name")
	public String firstName;
	
	@SerializedName("last_name")
	public String lastName;
	
	@SerializedName("card_id")
	public String cardId;
	
	@SerializedName("status")
	public String role;
	
	@SerializedName("company_id")
	public String companyId;

	@SerializedName("branch_id")
	public String branchId;
	
	@SerializedName("synch")
	public String synch;
}
