package com.newbee.kristian.KOS.models;

import android.content.Context;
import com.orm.SugarRecord;

public class UserModel extends SugarRecord<UserModel>{
	public String userId;
	public String mobilePassword;
	public String firstName;
	public String lastName;
	public String cardId;
	public String role;
	public String companyId;
	public String branchId;
	public String synch;

	public UserModel(Context arg0) {
		super(arg0);
	}
	
	public UserModel(Context arg0, String userId, String mobilePassword, String firstName,
			String lastName, String cardId, String role, String companyId, String branchId, String synch){
	    super(arg0);
	    this.userId = userId;
	    this.mobilePassword = mobilePassword;
	    this.firstName = firstName;
	    this.lastName = lastName;
	    this.cardId = cardId;
	    this.role = role;
	    this.companyId = companyId;
	    this.branchId = branchId;
	    this.synch = synch;
	}
	
	public String displayName(){
		return this.firstName+" "+this.lastName;
	}
	

}
