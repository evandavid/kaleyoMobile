package com.newbee.kristian.KOS.adapters;

import java.util.List;

import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.StaffModel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StaffAdapter extends BaseAdapter {
    
    private Context activity;
    private List<StaffModel> data;
    private LayoutInflater inflater=null;
	public String username, type;
	public int posisi;
    
    public StaffAdapter(Context context, List<StaffModel> d) {
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
	    	vi.setBackgroundColor(Color.parseColor("#ffffff"));
	    	TextView menu = (TextView)vi.findViewById(R.id.textView1);
	    	menu.setText(data.get(position).firstName+" "+data.get(position).lastName);
			vi.setTag(data.get(position));
        }else {
        }
        return vi;
    }
    
    
}
