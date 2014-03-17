package com.newbee.kristian.KOS.models;

import android.content.Context;
import com.orm.SugarRecord;

public class Server extends SugarRecord<Server>{
	public String ip;
	private static String NAMESPACE = "kaleyo/v1/";

	public Server(Context arg0) {
		super(arg0);
	}
	
	public Server(Context arg0, String ip){
	    super(arg0);
	    this.ip = ip;
	}
	
	public String url() {
		String 
	  		url = "http://"+this.ip+"/"+Server.NAMESPACE;
	  	return url;
	}

}
