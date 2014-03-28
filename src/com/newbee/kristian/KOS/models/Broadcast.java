package com.newbee.kristian.KOS.models;

import android.content.Context;
import com.orm.SugarRecord;

public class Broadcast extends SugarRecord<Broadcast>{
	public String text;
	public String ids;
	public String createdBy;
	public String user;
	
	public Broadcast(Context arg0) {
		super(arg0);
	}
	
	public Broadcast(Context arg0, ApiBroadcast api, String user) {
		super(arg0);
		this.text = api.text;
		this.ids = api.ids;
		this.user = user;
		this.createdBy = api.createdBy;
	}
}
