package com.newbee.kristian.KOS.adapters;

import java.util.List;

import com.newbee.kristian.KOS.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TableMenuAdapter extends BaseAdapter {
    
    private Context activity;
    private List<String> data;
    private LayoutInflater inflater=null;
	public String username, type;
	public int posisi;
    
    public TableMenuAdapter(Context context, List<String> d) {
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
	            vi = inflater.inflate(R.layout.item_menu_table, null);
	    if(data.get(position) != null){
	    	TextView menu = (TextView)vi.findViewById(R.id.textView1);
	    	menu.setText(data.get(position));
	    	
        }else {
        }
        return vi;
    }
    
    
}
