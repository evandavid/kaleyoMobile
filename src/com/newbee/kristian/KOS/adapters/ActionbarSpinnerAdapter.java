package com.newbee.kristian.KOS.adapters;

import com.newbee.kristian.KOS.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ActionbarSpinnerAdapter extends BaseAdapter {
	 
    private TextView subtitle, title;
    private String[] spinnerNavItem;
    private Context context;
    @SuppressWarnings("unused")
	private ActionBar actionbar;
    private int pos;
 
    public ActionbarSpinnerAdapter(Context context, ActionBar ab, int pos) {
        this.spinnerNavItem = new String[]{
        	"DEFAULT",
        	"ALL TABLES",
        	"AVAILABLE TABLE",
        	"BUSY TABLE"
        };
        this.actionbar = ab;
        this.context = context;
        this.pos = pos;
    }
 
    @Override
    public int getCount() {
        return spinnerNavItem.length;
    }
 
    @Override
    public Object getItem(int index) {
        return spinnerNavItem[index];
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) { 
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);
        }
         
        subtitle = (TextView) convertView.findViewById(R.id.txtTitle);
        subtitle.setText(spinnerNavItem[pos+1]);
        return convertView;
    }
     
 
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);
        }
        
        subtitle = (TextView) convertView.findViewById(R.id.txtTitle);
        subtitle.setText(spinnerNavItem[position]);
        //set margin and hide title
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        params.setMargins(0,10,0,10);
        subtitle.setLayoutParams(params);
        title = (TextView) convertView.findViewById(R.id.title);
        title.setVisibility(View.GONE);
        return convertView;
    }
 
}