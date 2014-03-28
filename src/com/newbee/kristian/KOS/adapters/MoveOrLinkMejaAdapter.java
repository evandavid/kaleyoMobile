package com.newbee.kristian.KOS.adapters;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.TableActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.Table;
import com.newbee.kristian.KOS.models.UserModel;
import com.newbee.kristian.KOS.utils.Connection;

@SuppressLint("DefaultLocale")
public class MoveOrLinkMejaAdapter extends BaseAdapter {
	private Context context;
	private List<Table> data;
	private ProgressDialog progress;
	private String mv;
	private UserModel user;
	int first, last;
 
	public MoveOrLinkMejaAdapter(Context context, List<Table> mobileValues, int first, int last, UserModel user, ProgressDialog progress) {
		this.context = context;
		this.user = user;
		this.data = mobileValues;
		this.last = last;
		this.first = first;
		this.progress = progress;
	}
 
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
 
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View gridView;
 
		if (convertView == null) {
 
			gridView = new View(context);
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.meja_layout, null);
 
			// set value into textview
			TextView table = (TextView) gridView.findViewById(R.id.meja);
			RelativeLayout box = (RelativeLayout) gridView.findViewById(R.id.meja_box);
			table.setText(data.get(position).tableName);
			if (data.get(position).tableStatus.equals(Table.OCCUPIED)){
				box.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.bg_busy));
			}else{
				if (data.get(position).tableStatus.equals(Table.DONE))
					box.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.bg_pending));
			}
			
			box.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					processTable(position);
				}
			});
			
 
		} else {
			gridView = (View) convertView;
		}
 
		return gridView;
	}
	
	private void processTable(final int position){
		final Table target = data.get(position).childs.get(data.get(position).occupiedCount);
		mv = ParentActivity.isMove ? "Move " : "Link ";
		new AlertDialog.Builder(context)
	        .setIcon(android.R.drawable.ic_menu_save)
	        .setTitle(mv+" Table "+ParentActivity.editTable.tableName)
	        .setMessage("Are you sure to "+mv.toLowerCase()+"table "+ParentActivity.editTable.tableName+" to table "+target.tableName)
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	ParentActivity.targetTable = target;
	            	progress.setMessage("Processing data. Please wait..");
	            	progress.show();
	            	new Thread(new Runnable() {
	   	     			public void run() {
	   	     					saveTable();
	   	     				} 		
	   	            }).start();
	            }
	
	        })
	        .setNegativeButton("No", null)
	        .show();
	}
	
	private void saveTable(){
		try {
			Server server = Server.findById(Server.class, (long) 1);
			List<String[]> data = new ArrayList<String[]>();
			Gson gson = new Gson();
			
			data.add(new String[]{"from", gson.toJson(ParentActivity.editTable).toString()});
			data.add(new String[]{"target", gson.toJson(ParentActivity.targetTable).toString()});
			data.add(new String[]{"occupied", String.valueOf(ParentActivity.occupiedCount)});
			
			data.add(new String[]{"updated_by", user.userId});
			Connection conn = new Connection(context);
			
			String link = ParentActivity.isMove ? "move_table" : "link_table";
			InputStream source = conn.doPostConnect(server.url()+link, data);

			if (source != null) 
				myHandler.post(tableSuksesSave);
			else
				myHandler.post(tableGagalSave);

		} catch (Exception e) {
			myHandler.post(tableGagalSave);
		}
	}
	
	final Handler myHandler = new Handler();
	final Runnable tableSuksesSave = new Runnable() {
        public void run() {
            suksesTable();
        }
    };
    
    final Runnable tableGagalSave = new Runnable() {
        public void run() {
            gagalTable();
        }
    };
    
    private void gagalTable(){
    	try {progress.hide();} catch (Exception e) {}
		Toast.makeText(context, "Failed to "+mv.toLowerCase()+"table, please try again.", Toast.LENGTH_SHORT).show();
    }
    
    private void suksesTable(){
    	try {progress.hide();} catch (Exception e) {}
		Toast.makeText(context, "Success to "+mv.toLowerCase()+"table.", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(context, TableActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	context.startActivity(i);
    	((Activity) context).finish();    
    }
 
	@Override
	public int getCount() {
		return data.size();
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}

}
