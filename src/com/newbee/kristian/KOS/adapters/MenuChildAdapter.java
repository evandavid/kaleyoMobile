package com.newbee.kristian.KOS.adapters;

import java.util.List;

import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.MenuPopup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuChildAdapter extends BaseAdapter {
    
    private Context activity;
    private List<MenuPopup> data;
    private LayoutInflater inflater=null;
	public String username, type;
	public int posisi;
//	private Menu menus;
    
    public MenuChildAdapter(Context context, List<MenuPopup> d) {
        this.activity = context;
        this.data = d;
//        this.menus = menu;
 
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
	            vi = inflater.inflate(R.layout.item_menu_table, null);
	    if(data.get(position) != null){
	    	TextView menu = (TextView)vi.findViewById(R.id.textView1);
	    	menu.setText(data.get(position).menuItemName);
			vi.setTag(data.get(position));
			
			LinearLayout ll = (LinearLayout)vi.findViewById(R.id.box);
	    	if (data.get(position).soldOut.equals("1")){
	    		ll.setBackgroundColor(Color.parseColor("#b23f01"));
	    		menu.setTextColor(Color.parseColor("#ffffff"));
	    	}
			

        }else {
        }
        return vi;
    }
    
    
}
