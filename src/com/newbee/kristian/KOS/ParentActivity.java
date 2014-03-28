package com.newbee.kristian.KOS;

import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.models.Order;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.SoldOut;
import com.newbee.kristian.KOS.models.SpecialRequest;
import com.newbee.kristian.KOS.models.Table;
import com.newbee.kristian.KOS.models.UserModel;
import com.newbee.kristian.KOS.utils.Connection;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;

public class ParentActivity extends Activity {
	protected String url, ip= "";
	protected boolean hasIP = false;
	protected Connection conn;
	protected UserModel user;
	public static ProgressDialog progress;
	public static boolean isMove = true;
	public static Table editTable, targetTable =  null;
	public static int occupiedCount = 0;
	public static Order order;
	public static Order displayOrder;
	public static List<SoldOut> soldOut;
	public static List<SpecialRequest> specialRequest = new ArrayList<SpecialRequest>();
	private Server server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parent);
		
		checkIP();
		progress = new ProgressDialog(this);
		progress.setTitle("");
		conn = new Connection(this);
		List<UserModel> users = UserModel.listAll(UserModel.class);
		if (users.size() > 0)
			user = users.get(0);
		
	}
	
	protected void checkIP(){
		this.server = Server.findById(Server.class, (long) 1);
		if (this.server != null){
			this.url = this.server.url();
			this.ip = this.server.ip;
			this.hasIP = true;
		}
	}

}
