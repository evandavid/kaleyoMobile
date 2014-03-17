package com.newbee.kristian.KOS.adapters;

import java.util.List;

import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.Menu;
import com.newbee.kristian.KOS.utils.FlowLayout;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {
    
    private Context activity;
    private List<Menu> data;
    private LayoutInflater inflater=null;
    private TextView header;
	public String username, type;
	public int posisi;
    
    public OrderAdapter(Context context, List<Menu> d) {
        this.activity = context;
        this.data = d;
 
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
	    	RelativeLayout itemBox = (RelativeLayout)vi.findViewById(R.id.itemBox);
	    	
//	    	create detail
	    	TextView tt = new TextView(activity);
	    	tt.setBackgroundColor(Color.parseColor("#ffcc33"));
	    	tt.setTextSize(10);
	    	tt.setPadding(3, 3, 3, 3);
	    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    	params.setMargins(0, 0, 5, 0);
	    	tt.setLayoutParams(params);
//	    	end create detail
	    	
	    	//set data
	    	Menu tmp = data.get(position);
	    	if (tmp != null){
		    	number.setText(String.valueOf(tmp.amount));
		    	if (tmp.bagian != null)
		    		menu.setText(tmp.menuItemName+" ["+tmp.bagian+"]");
		    	else
		    		menu.setText(tmp.menuItemName);
		    	
		    	if (tmp.orderType == null){
		    		header = (TextView)vi.findViewById(R.id.header);
		    		header.setVisibility(View.GONE);
		    	}else{
			    	if (!tmp.orderType.equals("")){
			    		header.setText(tmp.orderType);
			    		header = (TextView)vi.findViewById(R.id.header);
			    		header.setVisibility(View.VISIBLE);
			    	}else{
			    		header = (TextView)vi.findViewById(R.id.header);
			    		header.setVisibility(View.GONE);
			    	}
		    	}
//		    	System.out.println("jemuran "+tmp.orderType);
	    	}	
	    	
//	    	menu.setText(data.get(position).menuItemName);
//	    	
//	    	LinearLayout ll = (LinearLayout)vi.findViewById(R.id.box);
//	    	if (data.get(position).soldOut.equals("1")){
//	    		ll.setBackgroundColor(Color.parseColor("#b23f01"));
//	    		menu.setTextColor(Color.parseColor("#ffffff"));
//	    	}
//			vi.setOnLongClickListener(longlistener);
        }else {
        }
        return vi;
    }
    
    
}
