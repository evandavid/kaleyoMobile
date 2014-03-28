package com.newbee.kristian.KOS.adapters;

import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.SoldOut;
import com.newbee.kristian.KOS.models.SoldOutDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SoldOutAdapter extends BaseAdapter {
    
    private Context activity;
    private List<SoldOut> data;
    private LayoutInflater inflater=null;
    public List<List<CheckBox>> detailsCheck;
	public String username, type;
	public int posisi;
    
    public SoldOutAdapter(Context context, List<SoldOut> d) {
        this.activity = context;
        this.data = d;
        this.detailsCheck = new ArrayList<List<CheckBox>>(data.size());
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
	            vi = inflater.inflate(R.layout.item_soldout, null);
	    if(data.get(position) != null){
	    	RelativeLayout headerBox = (RelativeLayout)vi.findViewById(R.id.headerBox);
	    	LinearLayout detailBox = (LinearLayout)vi.findViewById(R.id.detailBox);
	    	
	    	TextView header = (TextView)vi.findViewById(R.id.header);
	    	TextView parentText = (TextView)vi.findViewById(R.id.parent);
	    	final CheckBox parentCheck = (CheckBox)vi.findViewById(R.id.checkParent);
	    	
	    	header.setText(data.get(position).category);
	    	if (data.get(position).position == 1)
	    		headerBox.setVisibility(View.VISIBLE);
	    	else
	    		headerBox.setVisibility(View.GONE);
	    	
	    	parentText.setText(data.get(position).itemName);
	    	
	    	parentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					data.get(position).soldOut = isChecked ? "1" : "0";
				}
			});
	    	
	    	//details
	    	if (data.get(position).hasDetail.equals("1")){
	    		detailBox.removeAllViews();
	    		List<CheckBox> tmpChx = new ArrayList<CheckBox>();
	    		for (int i = 0; i < data.get(position).details.size(); i++) {
					final List<SoldOutDetail> sd = data.get(position).details;
					View view = View.inflate(activity, R.layout.item_detail_soldout, null);
					TextView detailText = (TextView)view.findViewById(R.id.detail);
			    	CheckBox detailCheck = (CheckBox)view.findViewById(R.id.detailCheck);
			    	detailText.setText(sd.get(i).itemName);
			    	
			    	final int ii = i;
			    	detailCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							sd.get(ii).soldOut = isChecked ? "1" : "0";
						}
					});
			    	
			    	tmpChx.add(detailCheck);
			    	detailBox.addView(view);
			    	// set checked parent
			    	if (sd.get(i).soldOut.equals("0"))
			    		detailCheck.setChecked(false);
			    	else
			    		detailCheck.setChecked(true);
				}
	    		detailsCheck.add(data.get(position).hasDetailPos, tmpChx);
	    	}else
	    		detailBox.removeAllViews();
	    	
	    	if (data.get(position).soldOut.equals("0"))
	    		parentCheck.setChecked(false);
	    	else
	    		parentCheck.setChecked(true);
        }else {
        }
        return vi;
    }
    
    
}
