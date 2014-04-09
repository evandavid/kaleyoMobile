package com.newbee.kristian.KOS.adapters;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.OrderActivity;
import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.ApiSpecialRequest;
import com.newbee.kristian.KOS.models.Menu;
import com.newbee.kristian.KOS.models.Order;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.SpecialRequest;
import com.newbee.kristian.KOS.utils.Connection;
import com.newbee.kristian.KOS.utils.OrderSetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class OrderSettingAdapter extends BaseAdapter {
    
    private Context activity;
    private String[][] data;
    private OrderSetting objData;
    private LayoutInflater inflater=null;
    private ListView lview;
	public String username, type;
	public int posisi;
	public FrameLayout layout;
	public PopupWindow pwindo;
	public int universalSo = 0;
	private int orderPos = 0;
    
    public OrderSettingAdapter(Context context, OrderSetting obj, ListView lv, 
    		FrameLayout layout, PopupWindow pwindo) {
        this.activity = context;
        this.objData = obj;
        this.lview = lv;
        this.pwindo = pwindo;
        this.layout = layout;
        this.data = objData.lists;
        
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	View vi=convertView;
        if(convertView==null)
	            vi = inflater.inflate(R.layout.item_menu_table, null);
	    if(data[position] != null){
	    	TextView menu = (TextView)vi.findViewById(R.id.textView1);
	    	menu.setText(data[position][0]);

	    	//change dineIn or takeAway
	    	if (data[position][1].equals("dine"))
	    		changeOrderType(vi, position);
	    	//special order
	    	else if (data[position][1].equals("special"))
	    		specialOrder(vi, position);
	    	//delete order, SV only
	    	else if (data[position][1].equals("delete"))
	    		deleteMenuOrder(vi, position);
	    	//edit amount
	    	else if (data[position][1].equals("edit"))
	    		editMenuOrder(vi, position);
	    	//edit amount
	    	else if (data[position][1].equals("note"))
	    		noteMenuOrder(vi, position);
	    	
        }else {
        }
        return vi;
    }
    
    private void noteMenuOrder(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {pwindo.dismiss();} catch (Exception e) {}
				noteOrderClicked(position);
			}
		});
    }
    
    @SuppressWarnings("deprecation")
   	private void noteOrderClicked(final int position){
       	LayoutInflater inflater = (LayoutInflater)
   				activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
   		final View pWindow = inflater.inflate(R.layout.popup_void, null);
   		
   		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
   		int width = metrics.widthPixels;		
   		pwindo = new PopupWindow(pWindow,
   				(int)(width*0.8), ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
   		this.layout.getForeground().setAlpha( 180);
   		
   		Button btn = (Button)pWindow.findViewById(R.id.button1);
   		TextView tv = (TextView)pWindow.findViewById(R.id.textView1);
   		final EditText et = (EditText)pWindow.findViewById(R.id.server);
   		
   		btn.setText("Continue");
   		tv.setText("Note Take Away Order");
   		
   		et.setHint("add Note");
   		et.setText(ParentActivity.order.note == null ? "" : ParentActivity.order.note);
   		
   		btn.setOnClickListener(new View.OnClickListener() {
   			@Override
   			public void onClick(View v) {
   				if (!et.getText().toString().equals("")){
   					//set note
   	            	ParentActivity.order.note = et.getText().toString();
   	            	OrderActivity.sortData();
   	            	OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
   	        		lview.setAdapter(adapter);
   	        		try {pwindo.dismiss();} catch (Exception e) {}
   				}else{
   					ParentActivity.order.note = null;
   	            	OrderActivity.sortData();
   	            	OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
   	        		lview.setAdapter(adapter);
   	        		try {pwindo.dismiss();} catch (Exception e) {}
   				}
   			}
   		});
    }
    
    private void editMenuOrder(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {pwindo.dismiss();} catch (Exception e) {}
				editOrderClicked(position);
			}
		});
    }
    
    @SuppressWarnings("deprecation")
   	private void editOrderClicked(final int position){
       	LayoutInflater inflater = (LayoutInflater)
   				activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
   		final View pWindow = inflater.inflate(R.layout.popup_persons, null);
   		
   		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
   		int width = metrics.widthPixels;		
   		pwindo = new PopupWindow(pWindow,
   				(int)(width*0.8), ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
   		this.layout.getForeground().setAlpha( 180);
   		
   		Button btn = (Button)pWindow.findViewById(R.id.button1);
   		TextView tv = (TextView)pWindow.findViewById(R.id.textView1);
   		final EditText et = (EditText)pWindow.findViewById(R.id.server);
   		
   		btn.setText("Continue");
   		if (ParentActivity.order.menus.get(objData.position).hasPopup.equals("1"))
   			tv.setText("Edit Amount "+ParentActivity.order.menus.get(objData.position).menuItemName+" ["+ParentActivity.order.menus.get(objData.position).bagian+"]");
   		else
   			tv.setText("Edit Amount "+ParentActivity.order.menus.get(objData.position).menuItemName);
   		
   		et.setHint("edit amount");
   		
   		btn.setOnClickListener(new View.OnClickListener() {
   			@Override
   			public void onClick(View v) {
   				if (!et.getText().toString().equals("")){
   					//setprice
   	            	OrderActivity.price = OrderActivity.price - 
   	            			((ParentActivity.order.menus.get(objData.position).amount) 
   	            			* Integer.parseInt(ParentActivity.order.menus.get(objData.position).salesPrice)) +
   	            			((Integer.parseInt(et.getText().toString())) 
   	            			* Integer.parseInt(ParentActivity.order.menus.get(objData.position).salesPrice));
   	            	OrderActivity.displayPrice();
   	                //Edit data
   	            	Menu tmpMen = ParentActivity.order.menus.get(objData.position);
   	            	int tmpAmount = Integer.parseInt(et.getText().toString());
   	            	// if has popup
   	            	if (tmpMen.hasPopup.equals("1")){
   	            		for (int i = 0; i < tmpMen.popups.size(); i++) {
							if (tmpMen.bagian.equals(tmpMen.popups.get(i).menuItemName)){
								tmpMen.popups.get(i).amount = tmpAmount;
								tmpMen.popups.get(i).amountTmp = tmpAmount;
							}
						}
   	            	}
   	            	tmpMen.amount = tmpAmount;
   	            	tmpMen.amountTmp = tmpAmount;
   	            	
   	            	OrderActivity.sortData();
   	            	OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
   	        		lview.setAdapter(adapter);
   	        		try {pwindo.dismiss();} catch (Exception e) {}
   				}else{
   					Toast.makeText(activity, "Cannot blank.", Toast.LENGTH_SHORT).show();
   				}
   			}
   		});
    }
    
    private void deleteMenuOrder(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteOrderClicked(position);
			}
		});
    }
    
    private void deleteOrderClicked(final int position){
		new AlertDialog.Builder(activity)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Delete Menu Order")
	        .setMessage("Are you sure to delete this menu order? "
	        		+ParentActivity.order.menus.get(objData.position).menuItemName)
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	        		try {pwindo.dismiss();} catch (Exception e) {}
	        		if (ParentActivity.order.menus.get(objData.position).saved)
	        			voidNote(position);
	        		else{
	        			OrderActivity.price = OrderActivity.price - 
		            			((ParentActivity.order.menus.get(objData.position).amount) 
		            			* Integer.parseInt(ParentActivity.order.menus.get(objData.position).salesPrice));
		            	OrderActivity.displayPrice();
		                //Delete order Activity
		            	ParentActivity.order.menus.remove(objData.position);
		            	OrderActivity.sortData();
		            	OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
		        		lview.setAdapter(adapter);
	        		}
	            }
	
	        })
	        .setNegativeButton("No", null)
	        .show();
    }
    
    @SuppressWarnings("deprecation")
	private void voidNote(final int position){
    	LayoutInflater inflater = (LayoutInflater)
				activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_void, null);
		
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;		
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.8), ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
		this.layout.getForeground().setAlpha( 180);
		
		Button btn = (Button)pWindow.findViewById(R.id.button1);
		TextView tv = (TextView)pWindow.findViewById(R.id.textView1);
		final EditText et = (EditText)pWindow.findViewById(R.id.server);
		
		btn.setText("Continue");
		tv.setText("Void Note");
		
		et.setHint("void note");
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!et.getText().toString().equals("")){
					//setprice
	            	OrderActivity.price = OrderActivity.price - 
	            			((ParentActivity.order.menus.get(objData.position).amount) 
	            			* Integer.parseInt(ParentActivity.order.menus.get(objData.position).salesPrice));
	            	OrderActivity.displayPrice();
	                //Delete order Activity
	            	ParentActivity.order.menus.get(objData.position).voids = "1";
	            	ParentActivity.order.menus.get(objData.position).voidNote = et.getText().toString();
	            	OrderActivity.sortData();
	            	OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
	        		lview.setAdapter(adapter);
	        		try {pwindo.dismiss();} catch (Exception e) {}
				}else{
					Toast.makeText(activity, "Cannot blank.", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
    
    public void specialOrder(View vi,final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParentActivity.progress = new ProgressDialog(activity);
				ParentActivity.progress.setTitle("");
		    	ParentActivity.progress.setMessage("Downloading special order, please wait..");
		    	ParentActivity.progress.show();
		    	orderPos = objData.position;
		    	// check special order is already download?
		    	if (ParentActivity.specialRequest.size() == 0){
			    	new Thread(new Runnable() {
						public void run() {
							getSpecial();
						} 		
					}).start();
		    	}else{
		    		ParentActivity.progress.dismiss();
		    		specialOrderSuccess();
		    	}
			}
		});
    	
    }
    
    public void getSpecial(){
    	try {
			Server server = Server.findById(Server.class, (long) 1);
			Connection conn = new Connection(activity);
			InputStream source = conn.doGetConnect(server.url()+"special_menu");
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			ApiSpecialRequest response = gson.fromJson(reader, ApiSpecialRequest.class);

			if (response.code.equals("OK")) {
				// set data
				ParentActivity.specialRequest = response.results;
				SpecialRequest sr = new SpecialRequest("Other");
				ParentActivity.specialRequest.add(sr);
				myHandler.post(updateSukses);
			}else
				myHandler.post(updateGagal);

		} catch (Exception e) {
			myHandler.post(updateGagal);
		}
    }
    
    private Handler myHandler = new Handler();
    private Runnable updateSukses = new Runnable() {
		@Override
		public void run() {
			specialOrderSuccess();	
		}
	};
	
	private Runnable updateGagal = new Runnable() {
		@Override
		public void run() {
			specialOrderFailed();
		}
	};
	
	private void specialOrderSuccess(){
		// hide previous popup window
		try{pwindo.dismiss();} catch (Exception e) {}
		ParentActivity.progress.dismiss();	
		displaySpecialOrder();
	}
	
	@SuppressWarnings("deprecation")
	public void displaySpecialOrder(){
		LayoutInflater inflater = (LayoutInflater)
				activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		
		final View pWindow = inflater.inflate(R.layout.popup_table, null);
		
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.7), (int)(height*0.65), true);
		
		//displayed
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
				((Activity) activity).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
		tv.setText("Special Request");
		
		SpecialRequestAdapter adapter = new SpecialRequestAdapter(activity, ParentActivity.specialRequest,
				layout, orderPos, lview, pwindo);
		ListView lv = (ListView)pWindow.findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		
		this.layout.getForeground().setAlpha( 180);
	}
	
	private void specialOrderFailed(){
		ParentActivity.progress.dismiss();
		Toast.makeText(activity,
				"Failed to get special order data, please try again..", 
				Toast.LENGTH_LONG).show();
	}
    
    public void changeOrderType(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (data[position][0].contains("Dine In") || data[position][0].contains("Tambahan")){
					
					Menu tmp = ParentActivity.order.menus.get(objData.position);
					
					if (!OrderActivity.tambahan){
						tmp.tambahan = false;
						tmp.orderType = "Dine In";
						tmp.codeOrder = Order.DINEIN_CODE;
					}else{
						tmp.tambahan = true; 
						tmp.orderType = "Tambahan";
						tmp.codeOrder = Order.TAMBAHAN_CODE;
					}
					tmp.dineIn = true;

					tmp.localSort = OrderActivity.takeAwaySort + 1 ;							
				}else{
					Menu tmp = ParentActivity.order.menus.get(objData.position);	
					tmp.orderType = "Take Away";
					tmp.dineIn = false;
					tmp.tambahan = false;
					tmp.codeOrder = Order.TAKEAWAY_CODE;
					tmp.localSort = OrderActivity.dineInSort + 1 ;
				}
				OrderActivity.sortData();
				hidePopup();
			}
			
		});
    }
    
    public void hidePopup(){
    	OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, this.layout, this.lview);
		lview.setAdapter(adapter);
		try {pwindo.dismiss();} catch (Exception e) {}
    }
}
