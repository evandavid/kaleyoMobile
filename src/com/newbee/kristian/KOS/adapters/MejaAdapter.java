package com.newbee.kristian.KOS.adapters;

import com.newbee.kristian.KOS.OrderActivity;
import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

import com.newbee.kristian.KOS.models.Order;
import com.newbee.kristian.KOS.models.StaffModel;
import com.newbee.kristian.KOS.models.Table;
import com.newbee.kristian.KOS.utils.TableDropdownMenu;

public class MejaAdapter extends BaseAdapter {
	private Context context;
	private List<Table> mobileValues;
	private PopupWindow pwindo;
	private FrameLayout layout;
	int first, last;
 
	public MejaAdapter(Context context, List<Table> mobileValues, int first, int last, FrameLayout layout, ProgressDialog progress) {
		this.context = context;
		this.layout = layout;
		this.mobileValues = mobileValues;
		this.last = last;
		this.first = first;
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
			table.setText(mobileValues.get(position).tableName);
			if (mobileValues.get(position).tableStatus.equals(Table.OCCUPIED)){
				box.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.bg_busy));
			}else{
				if (mobileValues.get(position).tableStatus.equals(Table.DONE))
					box.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.bg_pending));
			}
			
			box.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					initiatePopupWindow(position, v);
				}
			});
			
 
		} else {
			gridView = (View) convertView;
		}
 
		return gridView;
	}
 
	@Override
	public int getCount() {
		return mobileValues.size();
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initiatePopupWindow(int position, View vi) {
		// We need to get the instance of the LayoutInflater
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		if (mobileValues.get(position).tableStatus.equals(Table.AVAILABLE)){
			person(position, vi);
		}else if (mobileValues.get(position).tableStatus.equals(Table.OCCUPIED)){
			final View pWindow = inflater.inflate(R.layout.popup_table, null);
			
			TextView tv = (TextView)pWindow.findViewById(R.id.textView1);
			tv.setText("Table "+mobileValues.get(position).tableName);
			
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			int width = metrics.widthPixels;		
			int height = metrics.heightPixels;
			
			List<String[]> data = TableDropdownMenu.initialize(mobileValues.get(position));
			int dp = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 50, metrics );
			int dps = data.size() * dp + dp;
			
			int heightl = dps > (int)(height*0.68) ? (int)(height*0.68) : ViewGroup.LayoutParams.WRAP_CONTENT;
			pwindo = new PopupWindow(pWindow,
					(int)(width*0.7), (int)(heightl), true);
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
					((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
			
			
			TableMenuAdapter adapter = new TableMenuAdapter(context, data, layout, pwindo, mobileValues.get(position));
			ListView lv = (ListView)pWindow.findViewById(R.id.listView1);
			lv.setAdapter(adapter);
			this.layout.getForeground().setAlpha( 180);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void person(final int position, final View vi){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_persons, null);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		final int width = metrics.widthPixels;		
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.8), ViewGroup.LayoutParams.WRAP_CONTENT, true);
		
		try {
			pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
			((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

		} catch (Exception e) {
			pWindow.post(new Runnable() {

			    public void run() {
			    	pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
					((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			    }

			});
		}
		
		
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
		
		Button btn_dismiss = (Button)pWindow.findViewById(R.id.button2);
		btn_dismiss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
				layout.getForeground().setAlpha( 0);
				pwindo.dismiss();
			}
		});
		
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
		
		btn.setText("Continue");
		tv.setText("Table "+mobileValues.get(position).tableName+" - Person");
		et.setHint("Person");
		
		btn.setOnClickListener(new View.OnClickListener() {
			// person sets, continue to waitress lists
			@Override
			public void onClick(View v) {
				if (!et.getText().toString().equals("")){
					ParentActivity.order = new Order();
					((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
					ParentActivity.order.persons = (Integer.parseInt(et.getText().toString()));
					ParentActivity.order.table = mobileValues.get(position).childs.get(mobileValues.get(position).occupiedCount);
					OrderActivity.tambahan = false;
					Intent i = new Intent(context.getApplicationContext(), OrderActivity.class);
			    	context.startActivity(i);	
			    	pwindo.dismiss();
				}
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void pickStaff(int position){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_table, null);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.7), (int)(height*0.7), true);
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
		StaffAdapter adapter = new StaffAdapter(context.getApplicationContext(), data);
		ListView lv = (ListView)pWindow.findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		
		this.layout.getForeground().setAlpha( 180);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ParentActivity.order.waitres = (arg1.getTag().toString());
				Intent i = new Intent(context.getApplicationContext(), OrderActivity.class);
		    	context.startActivity(i);	 
			}
		});
	}

}
