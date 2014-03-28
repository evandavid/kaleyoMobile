package com.newbee.kristian.KOS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.adapters.OrderAdapter;
import com.newbee.kristian.KOS.adapters.StaffAdapter;
import com.newbee.kristian.KOS.models.Order;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.StaffModel;
import com.newbee.kristian.KOS.models.UserModel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class OrderActivity extends ParentActivity {
	private MenuItem menuItem;
	private PopupWindow pwindo;
	private FrameLayout layout;
	private OrderAdapter adapter;
	private ListView listView;
	public static TextView priceValue, ppnValue, totalValue;
	public static int price, ppn, total = 0;
	public static int universalSort, dineInSort, takeAwaySort, tambahanSort;
	public static boolean tambahan = false;
	public static boolean fromtambahan = false;
	public Order tmpOrder;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		
		if (!fromtambahan){
			Intent i = new Intent(OrderActivity.this, MenuActivity.class);
			startActivity(i);
		}
		
		// set opacity of layout foreground
		this.layout = (FrameLayout) findViewById( R.id.framesplash);
		this.layout.getForeground().setAlpha( 0);
		
		this.listView = (ListView)findViewById(R.id.listView1);
		
		if (fromtambahan){
			progress.setMessage("Downloading data, please wait..");
			progress.show();
			new Thread(new Runnable() {
				public void run() {
					getOrders();
				} 		
			}).start();
			
		}else{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			String psn = ParentActivity.order.persons > 1 ? " Persons" : " Person";
			getActionBar().setSubtitle(ParentActivity.order.persons+psn);
			
			getActionBar().setTitle("Table "+ParentActivity.order.table.tableName);
			
			List<UserModel> users = UserModel.listAll(UserModel.class);
			if (users.size() > 0){
				UserModel user = users.get(0);
				ParentActivity.order.waitres = (user.userId);
				ParentActivity.order.waitres_name = (user.firstName+" "+user.lastName);
			}
		}
	}
	
	public void getOrders(){
		try {
			Server server = Server.findById(Server.class, (long) 1);
			InputStream source = conn.doGetConnect(server.url()+"get_order?table_id="+ParentActivity.editTable.tableId);
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			Order response = gson.fromJson(reader, Order.class);
			if (response.code.equals("OK")) {
				ParentActivity.order = response;
			
				myHandler.post(updateSukses);
			}else{
				myHandler.post(updateGagal);
			}
		} catch (Exception e) {
			myHandler.post(updateGagal);
		}
	}

    final Runnable updateSukses = new Runnable() {
        public void run() {
            sukses();
        }
    };
    
    final Runnable updateGagal = new Runnable() {
        public void run() {
            gagal();
        }
    };
    
    public void gagal(){
    	progress.hide();
    	Toast.makeText(this, "Failed to download data, please try again", Toast.LENGTH_LONG).show();
    	finish();
    }
    
    public void sukses(){
    	progress.hide();
    	fromtambahan = false;
    	getActionBar().setDisplayHomeAsUpEnabled(true);
		String psn = ParentActivity.order.persons > 1 ? " Persons" : " Person";
		getActionBar().setSubtitle(ParentActivity.order.persons+psn);
		getActionBar().setTitle("Table "+ParentActivity.order.table.tableName);
		try {
			this.menuItem.setTitle("Served by "+ParentActivity.order.waitres_name);
		} catch (Exception e) {	}
		
		displayOrder();
    }
	
	private void displayOrder(){
		// orders
    	tmpOrder = new Order(ParentActivity.order);
    	ParentActivity.order.menus = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
    	addOrders(tmpOrder.menus);
    	
    	displayPrice();
    	
		this.adapter = new OrderAdapter(this, ParentActivity.displayOrder.menus, this.layout, this.listView);
		this.listView.setAdapter(this.adapter);
	}
	
	private void addOrders(List<com.newbee.kristian.KOS.models.Menu> data){

    	if (data.size() > 0){
	    	for (int i = 0; i < data.size(); i++) {
	    		if (data.get(i).amount > 0){
	    			
	    			// asign tmp menu
	    			com.newbee.kristian.KOS.models.Menu parentMenu = new com.newbee.kristian.KOS.models.Menu(data.get(i));
	    			//clear nasi
	    			parentMenu.nasi = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
	    			if (parentMenu.dineIn){
	    				if (parentMenu.tambahan){
	    					parentMenu.codeOrder = Order.TAMBAHAN_CODE;
	    					parentMenu.orderType = "Tambahan";
	    				}else{
	    					parentMenu.codeOrder = Order.DINEIN_CODE;
	    					parentMenu.orderType = "Dine In";
	    				}
	    			}else{
	    				parentMenu.codeOrder = Order.TAKEAWAY_CODE;
	    				parentMenu.orderType = "Take Away";
	    			}
	    			
		    		// set sortable list
		    		if (parentMenu.dineIn){
		    			if (parentMenu.tambahan){
		    				
		    				tambahanSort = tambahanSort + 1;
		    				parentMenu.localSort = tambahanSort;
		    				
		    				parentMenu.dineIn = true;
		    				parentMenu.tambahan = true;
		    			}else{
		    				dineInSort = dineInSort + 1;
		    				parentMenu.localSort = dineInSort;
		    				parentMenu.dineIn = true;
		    				parentMenu.tambahan = false;
		    			}
		    		}else if (!parentMenu.dineIn){
		    			takeAwaySort = takeAwaySort + 1;
		    			parentMenu.localSort = takeAwaySort;
		    			
		    		}
		    		
		    		ParentActivity.order.addOrder(parentMenu);
		    		
		    		List<com.newbee.kristian.KOS.models.Menu> tmpNas3 = data.get(i).nasi;
		    		if (tmpNas3 != null){
			    		for (int j = 0; j < tmpNas3.size(); j++) {
							if (tmpNas3.get(j).amount > 0){
								com.newbee.kristian.KOS.models.Menu childMenu = new com.newbee.kristian.KOS.models.Menu(tmpNas3.get(j));

								childMenu.codeOrder = parentMenu.codeOrder;
		    					childMenu.orderType = parentMenu.orderType;
		    					childMenu.dineIn = parentMenu.dineIn;
		    					childMenu.tambahan = parentMenu.tambahan;
		    					
		    					// set sortable list
		    					if (childMenu.dineIn){
		    		    			if (childMenu.tambahan){
		    		    				tambahanSort = tambahanSort + 1;
		    		    				childMenu.localSort = tambahanSort;
		    		    			}else{
		    		    				dineInSort = dineInSort + 1;
		    		    				childMenu.localSort = dineInSort;
		    		    			}
		    		    		}else{
		    		    			takeAwaySort = takeAwaySort + 1;
		    		    			childMenu.localSort = takeAwaySort;
		    		    		}
								
								ParentActivity.order.addOrder(childMenu);
								price = price + (Integer.parseInt(tmpNas3.get(j).salesPrice)* tmpNas3.get(j).amount);
							}
						}
		    		}
		    		
		    		// child with nasi
		    		if (parentMenu.hasPopup.equals("1")){
			    		for (int k = 0; k < parentMenu.popups.size(); k++) {
			    			
			    			List<com.newbee.kristian.KOS.models.Menu> tmpNas2 = parentMenu.popups.get(k).nasi;
				    		if (tmpNas2 != null){
					    		for (int j = 0; j < tmpNas2.size(); j++) {
									if (tmpNas2.get(j).amount > 0){
										com.newbee.kristian.KOS.models.Menu childMenu = new com.newbee.kristian.KOS.models.Menu(tmpNas2.get(j));
	
										childMenu.codeOrder = parentMenu.codeOrder;
				    					childMenu.orderType = parentMenu.orderType;
				    					childMenu.dineIn = parentMenu.dineIn;
				    					childMenu.tambahan = parentMenu.tambahan; 
				    					
				    					// set sortable list
				    					if (childMenu.dineIn){
				    		    			if (childMenu.tambahan){
				    		    				tambahanSort = tambahanSort + 1;
				    		    				childMenu.localSort = tambahanSort;
				    		    			}else{
				    		    				dineInSort = dineInSort + 1;
				    		    				childMenu.localSort = dineInSort;
				    		    			}
				    		    		}else{
				    		    			takeAwaySort = takeAwaySort + 1;
				    		    			childMenu.localSort = takeAwaySort;
				    		    		}
										
										ParentActivity.order.addOrder(childMenu);
										price = price + (Integer.parseInt(tmpNas2.get(j).salesPrice)* tmpNas2.get(j).amount);
									}
								}
					    		parentMenu.popups.get(k).nasi = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
				    		}
						}
	    			}
		    		if (data.get(i).voids.equals("0"))
		    			price = price + (Integer.parseInt(data.get(i).salesPrice) * data.get(i).amount);
	    		}
			}
    	}
    	
    	sortData();
	}
	
	public static void sortData(){
		Collections.sort(ParentActivity.order.menus, 
				new com.newbee.kristian.KOS.utils.MenuComparator());
		List<com.newbee.kristian.KOS.models.Menu> tmp = ParentActivity.order.menus;
		List<com.newbee.kristian.KOS.models.Menu> tmpDine = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		List<com.newbee.kristian.KOS.models.Menu> tmpTake = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		List<com.newbee.kristian.KOS.models.Menu> tmpTam = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		for (int i = 0; i < tmp.size(); i++) {
			tmp.get(i).universalSort = i + 1;
			if (tmp.get(i).dineIn){
				if (tmp.get(i).tambahan)
					tmpTam.add(tmp.get(i));
				else
					tmpDine.add(tmp.get(i));
			}else if (!tmp.get(i).dineIn)
				tmpTake.add(tmp.get(i));
		}
		
		localSort(tmpDine);
		localSort(tmpTam);
		localSort(tmpTake);
		
		Collections.sort(tmpDine, 
				new com.newbee.kristian.KOS.utils.LocalMenuComparator());
		Collections.sort(tmpTake, 
				new com.newbee.kristian.KOS.utils.LocalMenuComparator());
		Collections.sort(tmpTam, 
				new com.newbee.kristian.KOS.utils.LocalMenuComparator());
		ParentActivity.order.menus = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		ParentActivity.order.menus.addAll(tmpDine);
		ParentActivity.order.menus.addAll(tmpTake);
		ParentActivity.order.menus.addAll(tmpTam);
		
		ParentActivity.displayOrder = new Order(ParentActivity.order);
		ParentActivity.displayOrder.menus = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		rearangeOrder();
	}
	
	// for display purpose
	private static void rearangeOrder(){
		List<com.newbee.kristian.KOS.models.Menu> tmps = ParentActivity.order.menus;
		for (int i = 0; i < tmps.size(); i++) {
			if (tmps.get(i).voids.equals("0")){
				com.newbee.kristian.KOS.models.Menu m = new com.newbee.kristian.KOS.models.Menu(tmps.get(i));
				m.position = i;
				m.codeOrder = tmps.get(i).codeOrder;
				ParentActivity.displayOrder.addOrder(m);
			}
		}
		
		List<com.newbee.kristian.KOS.models.Menu> tmp = ParentActivity.displayOrder.menus;
		List<com.newbee.kristian.KOS.models.Menu> tmpDine = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		List<com.newbee.kristian.KOS.models.Menu> tmpTake = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		List<com.newbee.kristian.KOS.models.Menu> tmpTam = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		for (int i = 0; i < tmp.size(); i++) {
			tmp.get(i).universalSort = i + 1;
			if (tmp.get(i).dineIn){
				if (tmp.get(i).tambahan)
					tmpTam.add(tmp.get(i));
				else
					tmpDine.add(tmp.get(i));
			}else if (!tmp.get(i).dineIn)
				tmpTake.add(tmp.get(i));
		}
		
		localSort(tmpDine);
		localSort(tmpTake);
		localSort(tmpTam);
		
		Collections.sort(tmpDine, 
				new com.newbee.kristian.KOS.utils.LocalMenuComparator());
		Collections.sort(tmpTake, 
				new com.newbee.kristian.KOS.utils.LocalMenuComparator());
		Collections.sort(tmpTam, 
				new com.newbee.kristian.KOS.utils.LocalMenuComparator());
		ParentActivity.displayOrder.menus = new ArrayList<com.newbee.kristian.KOS.models.Menu>();
		ParentActivity.displayOrder.menus.addAll(tmpDine);
		ParentActivity.displayOrder.menus.addAll(tmpTake);
		ParentActivity.displayOrder.menus.addAll(tmpTam);
	}
	
	public static void localSort(List<com.newbee.kristian.KOS.models.Menu> tmp){
		for (int i = 0; i < tmp.size(); i++) {
			tmp.get(i).localSort = i + 1;
		}
	}
	
	public static void displayPrice(){
		if (price >= 0){
			ppn = ((price*10)/100);
			total = ppn + price;

			DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
			symbols.setCurrencySymbol(""); // Don't use null.
			formatter.setDecimalFormatSymbols(symbols);

			String priceCurrency = formatter.format(price);
			String ppnCurrency = formatter.format(ppn);
			String totalCurrency = formatter.format(total);
			
			priceValue.setText("Rp. "+priceCurrency);
			ppnValue.setText("Rp. "+ppnCurrency);
			totalValue.setText("Rp. "+totalCurrency);
		}
	}
	
	// New Order Clicked
	public void newOrder(View view){
		Intent i = new Intent(OrderActivity.this, MenuActivity.class);
		startActivity(i);
	}
	
	//done Order clicked
	public void doneOrder(View view){
//		Gson n = new Gson();
//		File f=new File("/mnt/shared/adb/files.json");
//		try {
//			FileOutputStream fos = new FileOutputStream(f);
//			fos.write(n.toJson(ParentActivity.order).getBytes());
//			fos.close();
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_menu_save)
        .setTitle("Done Order")
        .setMessage("Are you sure to save order?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if (ParentActivity.displayOrder.menus.size() > 0){
	            	progress.setMessage("Saving order, please wait...");
	            	progress.show();
	            	new Thread(new Runnable() {
	        			public void run() {
	        				saveOrder();
	        			} 		
	        		}).start();
            	}else
            		Toast.makeText(getApplicationContext(), "Order cannot blank.", Toast.LENGTH_LONG).show();
            }
        })
        .setNegativeButton("No", null)
        .show();
	}
	
	public void saveOrder(){
		try {
			Server server = Server.findById(Server.class, (long) 1);
			Gson gson = new Gson();
			List<String[]> data = new ArrayList<String[]>();
			String ord = gson.toJson(ParentActivity.order).toString();
			
			data.add(new String[]{"orders", ord});
			String url = OrderActivity.tambahan ? "update_orders" : "orders";
			InputStream source = conn.doPostConnect(server.url()+url, data);

			if (source != null) 
				myHandler.post(updateSuksesSave);
			else
				myHandler.post(updateGagalSave);

		} catch (Exception e) {
			myHandler.post(updateGagalSave);
		}
	}
	
	private final Handler myHandler = new Handler();
    final Runnable updateSuksesSave = new Runnable() {
        public void run() {
            suksesSave();
        }
    };
    
    final Runnable updateGagalSave = new Runnable() {
        public void run() {
            gagalSave();
        }
    };
    
    public void suksesSave(){
    	try {progress.dismiss();} catch (Exception e) {}
    	Toast.makeText(this, "Saving order successfully.", Toast.LENGTH_LONG).show();
    	Intent i = new Intent(OrderActivity.this, TableActivity.class);
    	startActivity(i);
    	this.finish();  
    }
    
    public void gagalSave(){
    	try {progress.dismiss();} catch (Exception e) {}
    	Toast.makeText(this, "Failed to save order, please try again.", Toast.LENGTH_LONG).show();
    }
	
	@Override
	protected void onResume(){
		super.onResume();

		OrderActivity.priceValue = (TextView)findViewById(R.id.priceValue);
		OrderActivity.ppnValue = (TextView)findViewById(R.id.ppnValue);
		OrderActivity.totalValue = (TextView)findViewById(R.id.totalValue);
		
		price = 0;
		ppn = 0;
		total = 0;

		universalSort = 0;
		takeAwaySort = 0;
		dineInSort = 0;
		tambahanSort = 0;
		if (!fromtambahan){
			displayOrder();
			Gson n = new Gson();
			File f=new File("/mnt/shared/adb/files.json");
			try {
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(n.toJson(ParentActivity.order).getBytes());
				fos.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
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
				(int)(width*0.7), (int)(height*0.65), true);
		try {
			pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
		} catch (Exception e) {
			pWindow.post(new Runnable() {

			    public void run() {
			    	pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
			    }

			});
		}
		
		Button btn_dismiss = (Button)pWindow.findViewById(R.id.button2);
		btn_dismiss.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layout.getForeground().setAlpha( 0);
				pwindo.dismiss();
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
				ParentActivity.order.waitres = (usr.userId);
				ParentActivity.order.waitres_name = (usr.firstName+" "+usr.lastName);
				layout.getForeground().setAlpha( 0);
				pwindo.dismiss();
			}
		});
	}

}
