package com.newbee.kristian.KOS.adapters;

import java.util.List;

import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.Menu;
import com.newbee.kristian.KOS.models.SpecialRequest;
import com.newbee.kristian.KOS.models.User;
import com.newbee.kristian.KOS.models.UserModel;
import com.newbee.kristian.KOS.utils.FlowLayout;
import com.newbee.kristian.KOS.utils.OrderSetting;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class OrderAdapter extends BaseAdapter {
    
    private Context activity;
    private List<Menu> data;
    private LayoutInflater inflater=null;
    private TextView header;
	public String username, type;
	public int posisi;
	public PopupWindow pwindo;
	public FrameLayout layout;
	public UserModel user;
	public ListView lview;
	public String[][] setting_dineIn = new String[][]{
		{"Edit", "edit"},
		{"Special Request", "special"},
		{"Take Away", "dine"},
		{"Delete", "delete"}
	};
	public String[][] setting_takeHome = new String[][]{
		{"Edit", "edit"},
		{"Special Request", "special"},
		{"Note", "note"},
		{"Dine In", "dine"},
		{"Delete", "delete"}
	};
	
	public String[][] setting_dineIn_user = new String[][]{
		{"Edit", "edit"},
		{"Special Request", "special"},
		{"Take Away", "dine"}
	};
	
	public String[][] setting_takeHome_user = new String[][]{
		{"Edit", "edit"},
		{"Special Request", "special"},
		{"Note", "note"},
		{"Dine In", "dine"},
	};
	
	public String[][] setting_takeHome2 = new String[][]{
		{"Edit", "edit"},
		{"Special Request", "special"},
		{"Dine In", "dine"},
		{"Delete", "delete"}
	};
	
	public String[][] setting_takeHome_user2 = new String[][]{
		{"Edit", "edit"},
		{"Special Request", "special"},
		{"Dine In", "dine"},
	};
	
	public String[][] setting_tambahan = new String[][]{
		{"Edit", "edit"},
		{"Delete", "delete"}
	};
	
	public String[][] setting_tambahan_user = new String[][]{
		{"Edit", "edit"}
	};
    
    public OrderAdapter(Context context, List<Menu> d, FrameLayout layout, ListView lv) {
        this.activity = context;
        this.data = d;
        this.layout = layout;
        this.lview = lv;
 
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
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
	            vi = inflater.inflate(R.layout.item_order, null);
	    if(data.get(position) != null){
	    	
	    	TextView menu = (TextView)vi.findViewById(R.id.menu);
	    	TextView number = (TextView)vi.findViewById(R.id.numbers);
	    	header = (TextView)vi.findViewById(R.id.header);
	    	FlowLayout detailBox = (FlowLayout)vi.findViewById(R.id.detailBox);
	    	@SuppressWarnings("unused")
			RelativeLayout itemBox = (RelativeLayout)vi.findViewById(R.id.itemBox);
	    	
	    	//create detail
	    	if (data.get(position).specialRequest.size() > 0){
	    		detailBox.removeAllViews();
	    		for (int i = 0; i < data.get(position).specialRequest.size(); i++) {
					SpecialRequest sr = data.get(position).specialRequest.get(i);
					TextView tt = new TextView(activity);
			    	tt.setBackgroundColor(Color.parseColor("#ffcc33"));
			    	tt.setTextSize(10);
			    	tt.setPadding(3, 3, 3, 3);
			    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
			                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.setMargins(0, 0, 5, 0);
			    	tt.setLayoutParams(params);
			    	if (sr.showQuantity.equals("1"))
			    		tt.setText(sr.amount+" "+sr.requestName);
			    	else
			    		tt.setText(sr.requestName);
			    	detailBox.addView(tt);
			    	//end create detail
				}    	
	    		
		    	// add note to data
		    	if (!data.get(position).dineIn && data.get(position).localSort == 1 && ParentActivity.order.note != null){
		    		TextView tts = new TextView(activity);
			    	tts.setBackgroundColor(Color.parseColor("#ffcc33"));
			    	tts.setTextSize(10);
			    	tts.setPadding(3, 3, 3, 3);
			    	RelativeLayout.LayoutParams paramss = new RelativeLayout.LayoutParams(
			                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	paramss.setMargins(0, 0, 5, 0);
			    	tts.setLayoutParams(paramss);
			    	tts.setText(ParentActivity.order.note);
			    	detailBox.addView(tts);
		    	}
		    	
		    	TextView tt = new TextView(activity);
		    	tt.setBackgroundColor(Color.parseColor("#ffffff"));
		    	tt.setTextSize(10);
		    	tt.setPadding(3, 3, 3, 3);
		    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    	params.setMargins(0, 0, 5, 0);
		    	tt.setLayoutParams(params);
		    	tt.setText("");
		    	detailBox.addView(tt);
	    	}else{
	    		detailBox.removeAllViews();
	    		if (!data.get(position).dineIn && data.get(position).localSort == 1 && ParentActivity.order.note != null){
		    		TextView tts = new TextView(activity);
			    	tts.setBackgroundColor(Color.parseColor("#ffcc33"));
			    	tts.setTextSize(10);
			    	tts.setPadding(3, 3, 3, 3);
			    	RelativeLayout.LayoutParams paramss = new RelativeLayout.LayoutParams(
			                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	paramss.setMargins(0, 0, 5, 0);
			    	tts.setLayoutParams(paramss);
			    	tts.setText(ParentActivity.order.note);
			    	detailBox.addView(tts);
		    	}
	    	}
	    	//TODO hapus details
	    	
	    	
	    	//set data
	    	Menu tmp = data.get(position);
	    	if (tmp != null){
		    	number.setText(String.valueOf(tmp.amount));
		    	if (tmp.bagian != null)
		    		menu.setText(tmp.menuItemName+" ["+tmp.bagian+"]");
		    	else
		    		menu.setText(tmp.menuItemName);
		    	
		    	if (tmp.localSort > 1){
		    		header = (TextView)vi.findViewById(R.id.header);
		    		header.setVisibility(View.GONE);
		    	}else{
			    	if (tmp.orderType != null){
			    		header.setText(tmp.orderType);
			    		header = (TextView)vi.findViewById(R.id.header);
			    		header.setVisibility(View.VISIBLE);
			    	}else{
			    		header = (TextView)vi.findViewById(R.id.header);
			    		header.setVisibility(View.GONE);
			    	}
		    	}

	    	}	
	    	
	    	//setting button
	    	LinearLayout btnSetting = (LinearLayout)vi.findViewById(R.id.editBox);
	    	btnSetting.setClickable(true);
	    	btnSetting.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					layout.getForeground().setAlpha( 180);
					initiatePopupSetting(position, v);
				}
			});
	    	
	    	Button btn = (Button)vi.findViewById(R.id.edit);
	    	btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					layout.getForeground().setAlpha( 180);
					initiatePopupSetting(position, v);
				}
			});
	    	
        }else {
        }
        return vi;
    }
    
    @SuppressWarnings("deprecation")
	private void initiatePopupSetting(int position, final View v){
    	// We need to get the instance of the LayoutInflater
		LayoutInflater inflater = (LayoutInflater)
				activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		
		final View pWindow = inflater.inflate(R.layout.item_order_setting, null);
		
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;		
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.5), ViewGroup.LayoutParams.WRAP_CONTENT, true);
		try {
			pwindo.showAtLocation(pWindow, Gravity.RIGHT, 20, 0);
		} catch (Exception e) {
			pWindow.post(new Runnable() {
			    public void run() {
			    	pwindo.showAtLocation(pWindow, Gravity.RIGHT, 20, 0);
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
		List<UserModel> users = UserModel.listAll(UserModel.class);
		
		if (users.size() > 0)
			user = users.get(0);
		String[][] tmp;
		if(data.get(position).saved){
			if (user.role.equals(User.OPERATOR))
				tmp = setting_tambahan_user;
			else
				tmp = setting_tambahan;
		}else{
			if (data.get(position).dineIn){
				if (user.role.equals(User.OPERATOR))
					tmp = setting_dineIn_user;
				else
					tmp = setting_dineIn;
			}else{
				if (data.get(position).localSort == 1){
					if (user.role.equals(User.OPERATOR))
						tmp = setting_takeHome_user;
					else
						tmp = setting_takeHome;
				}else{
					if (user.role.equals(User.OPERATOR))
						tmp = setting_takeHome_user2;
					else
						tmp = setting_takeHome2;
				}
			}
		}
		OrderSetting os = new OrderSetting(tmp, data.get(position).position);

		OrderSettingAdapter adapter = new OrderSettingAdapter(activity, os, lview, layout, pwindo);
		ListView lv = (ListView)pWindow.findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		
		this.layout.getForeground().setAlpha( 180);
    }
    
    
}
