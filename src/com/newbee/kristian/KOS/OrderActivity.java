package com.newbee.kristian.KOS;

import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.adapters.OrderAdapter;
import com.newbee.kristian.KOS.adapters.StaffAdapter;
import com.newbee.kristian.KOS.models.Order;
import com.newbee.kristian.KOS.models.StaffModel;
import com.newbee.kristian.KOS.models.UserModel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class OrderActivity extends ParentActivity {
	private MenuItem menuItem;
	private PopupWindow pwindo;
	private FrameLayout layout;
	private OrderAdapter adapter;
	private List<com.newbee.kristian.KOS.models.Menu> orders;
	private Order data;
	private ListView listView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		if (ParentActivity.order.getMenus().size() == 0 ){
			Intent i = new Intent(OrderActivity.this, MenuActivity.class);
			startActivity(i);
		}
		
		// set opacity of layout foreground
		this.layout = (FrameLayout) findViewById( R.id.framesplash);
		this.layout.getForeground().setAlpha( 0);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		String psn = ParentActivity.order.getPersons() > 1 ? " Persons" : " Person";
		getActionBar().setSubtitle(ParentActivity.order.getPersons()+psn);
		getActionBar().setTitle("Table "+ParentActivity.order.getTable().tableName);
		
		List<UserModel> users = UserModel.listAll(UserModel.class);
		if (users.size() > 0){
			UserModel user = users.get(0);
			ParentActivity.order.setWaitres(user.userId);
		}
		
		this.listView = (ListView)findViewById(R.id.listView1);
	}
	
	private void displayOrder(){
    	this.orders = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
    	boolean flag = true;
    	if (data.getMenus().size() > 0){
	    	for (int i = 0; i < data.getMenus().size(); i++) {
	    		if (data.getMenus().get(i).amount > 0){
		    		if (flag){
		    			data.getMenus().get(i).orderType = "Dine In";
		    			flag = false;
		    		}else
		    			data.getMenus().get(i).orderType = "";
		    		
		    		data.getMenus().get(i).orderNumber = i;	
		    		this.orders.add(data.getMenus().get(i));
		    		List<com.newbee.kristian.KOS.models.Menu> tmpNas = data.getMenus().get(i).nasi;
		    		for (int j = 0; j < tmpNas.size(); j++) {
						if (tmpNas.get(j).amount > 0)
							this.orders.add(tmpNas.get(j));
					}
	    		}
			}
    	}
    	
    	boolean flag2 = true;
    	if (data.getTakeHome().size() > 0){
	    	for (int i = 0; i < data.getTakeHome().size(); i++) {
	    		if (data.getTakeHome().get(i).amount > 0){
		    		if (flag2){
		    			data.getTakeHome().get(i).orderType = "Take Home";
		    			flag2 = false;
		    		}
		    		else
		    			data.getTakeHome().get(i).orderType = "";
		    			
		    		data.getTakeHome().get(i).orderNumber = i;
		    		this.orders.add(data.getTakeHome().get(i));
		    		List<com.newbee.kristian.KOS.models.Menu> tmpNas2 = data.getTakeHome().get(i).nasi;
		    		for (int j = 0; j < tmpNas2.size(); j++) {
						if (tmpNas2.get(j).amount > 0)
							this.orders.add(tmpNas2.get(j));
					}
	    		}
			}
    	}
    	
    	boolean flag3 = true;
    	if (data.getTambahan().size() > 0){
	    	for (int i = 0; i < data.getTambahan().size(); i++) {
	    		if (data.getTambahan().get(i).amount > 0){
		    		if (flag3){
		    			data.getTambahan().get(i).orderType = "Tambahan";
		    			flag3 = false;
		    		}
		    		else
		    			data.getTambahan().get(i).orderType = "";
		    			
		    		data.getTambahan().get(i).orderNumber = i;
		    		
		    		this.orders.add(data.getTambahan().get(i));
		    		List<com.newbee.kristian.KOS.models.Menu> tmpNas3 = data.getTambahan().get(i).nasi;
		    		for (int j = 0; j < tmpNas3.size(); j++) {
						if (tmpNas3.get(j).amount > 0)
							this.orders.add(tmpNas3.get(j));
					}
	    		}
			}
    	}
    
		this.adapter = new OrderAdapter(this, this.orders);
		this.listView.setAdapter(this.adapter);
	}
	
	// New Order Clicked
	public void newOrder(View view){
		Intent i = new Intent(OrderActivity.this, MenuActivity.class);
		startActivity(i);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		this.data = ParentActivity.order;
		displayOrder();
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	// confirm dialog to cancel order
	        	cancelOrder();

            return true;
        case R.id.changeStaff:
        	pickStaff();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public void onBackPressed(){
		cancelOrder();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parent, menu);
		this.menuItem = (MenuItem) menu.findItem(R.id.changeStaff);
		this.menuItem.setTitle("Served by "+user.displayName());
		return true;
	}
	
	public void cancelOrder(){
		new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Cancel Order")
	        .setMessage("Are you sure to cancel order?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //Stop the activity
	            	Intent i = new Intent(OrderActivity.this, TableActivity.class);
	            	startActivity(i);
	            	OrderActivity.this.finish();    
	            }
	
	        })
	        .setNegativeButton("No", null)
	        .show();
	}
	
	@SuppressWarnings("deprecation")
	public void pickStaff(){
		LayoutInflater inflater = (LayoutInflater)
				getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_table, null);
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.7), (int)(height*0.7), true);
		pWindow.findViewById(R.id.textView1).post(new Runnable() {

		    public void run() {
		    	pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
		    }

		});
		pwindo.setBackgroundDrawable(new BitmapDrawable());
		pwindo.setOutsideTouchable(true);
		pwindo.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				layout.getForeground().setAlpha( 0);
				pwindo.dismiss();
			}
		});
		
		TextView tv = (TextView)pWindow.findViewById(R.id.textView1);
		tv.setText("Pilih Waitress");
		
		List<StaffModel> data = StaffModel.listAll(StaffModel.class);
		StaffAdapter adapter = new StaffAdapter(getApplicationContext(), data);
		ListView lv = (ListView)pWindow.findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		
		this.layout.getForeground().setAlpha( 180);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				StaffModel usr = ((StaffModel) arg1.getTag());
				menuItem.setTitle("Served by "+usr.displayName());
				ParentActivity.order.setWaitres(usr.userId);
				layout.getForeground().setAlpha( 0);
				pwindo.dismiss();
			}
		});
	}

}
