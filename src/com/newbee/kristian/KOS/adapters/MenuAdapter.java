package com.newbee.kristian.KOS.adapters;

import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.MenuActivity;
import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.Menu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

@SuppressLint("NewApi")
public class MenuAdapter extends BaseAdapter {
    
    private Context activity;
    private List<Menu> data;
    private LayoutInflater inflater=null;
    private PopupWindow pwindo;
	@SuppressWarnings("unused")
	private int pos;
	public String username, type, category;
	public int posisi;
	public FrameLayout layout;
	public List<Menu> nas;
	public ProgressDialog progress;
	public boolean[] llFlag;
	public Menu menuTmp;
    
    public MenuAdapter(Context context, List<Menu> d, List<Menu> nasi, String cat, FrameLayout layout, ProgressDialog progress) {
        this.activity = context;
        this.data = d;
        this.nas = nasi;
        this.layout = layout;
        this.progress = progress;
        this.category = cat;
 
        llFlag = new boolean[data.size()];
        for (int i = 0; i < llFlag.length; i++) {
			llFlag[i] = true;
		}
        
        for (int i = 0; i < data.size(); i++) {
        	data.get(i).nasi = new ArrayList<Menu>();
			data.get(i).nasi.clear();
			for (int j = 0; j < nas.size(); j++) {
				Menu nn = new Menu(nas.get(j));
				data.get(i).nasi.add(nn);
			}
		}
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
    
    @SuppressLint("DefaultLocale")
	public View getView(final int position, View convertView, ViewGroup parent) {
    	View vi=convertView;
        if(convertView==null)
	            vi = inflater.inflate(R.layout.item_menu_table, null);
	    if(data.get(position) != null){
	    	final TextView menu = (TextView)vi.findViewById(R.id.textView1);
	    	int tmp1 = data.get(position).amountTmp;
	    	menu.setText(tmp1+". "+data.get(position).menuItemName);
	    	
	    	final RelativeLayout ll = (RelativeLayout)vi.findViewById(R.id.box);
	    	final LinearLayout childBox = (LinearLayout)vi.findViewById(R.id.childBox);
	    	TextView tvsoldout = (TextView)vi.findViewById(R.id.textView2);
	    	
	    	if (data.get(position).soldOut.equals("1")){
	    		ll.setBackgroundColor(Color.parseColor("#9d9d9d"));
	    		menu.setTextColor(Color.parseColor("#ffffff"));
	    		tvsoldout.setVisibility(View.VISIBLE);
	    	}else{
	    		if (data.get(position).hasPopup.equals("1"))
	    			ll.setBackgroundColor(Color.parseColor("#d34b01"));
	    		else{
	    			ll.setBackgroundColor(Color.parseColor("#ffcc33"));
	    			if (category.toLowerCase().contains("ayam")){
		    			addNasi(position, vi, false);
		    			llFlag[position] = false;
	    			}
	    		}
	    		menu.setTextColor(Color.parseColor("#333333"));
	    		tvsoldout.setVisibility(View.GONE);
	    		
	    	}
	    	
	    	if (category.toLowerCase().contains("ayam")){
		    	if (llFlag[position] == true){
		    		childBox.removeAllViews();
		    	}else{
		    		if (data.get(position).soldOut.equals("0")){
				    	if (data.get(position).hasPopup.equals("1")){
				    		pickChild(position, vi, menu);
				    	}
				    	else{
				    		addNasi(position, vi, false);
				    	}
			    	}else{
			    		childBox.removeAllViews();
			    	}
		    	}
	    	}
	    	
	    	
	    	final View vv = vi;
			ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (data.get(position).soldOut.equals("0")){
						llFlag[position] = false;
						if (data.get(position).hasPopup.equals("1")){
							if (data.get(position).soldOut.equals("0")){
						    	if (data.get(position).hasPopup.equals("1")){
						    		pickChild(position, vv, menu);
						    	}
						    	else{
						    		addNasi(position, vv, false);
						    	}
					    	}else{
					    		childBox.removeAllViews();
					    	}
						}else{
							data.get(position).bagian = null;
							menuTmp = data.get(position);
							pickJumlah(position, false, 0, false, menu, null, data.get(position).menuItemName, null);
						}
					}
				}
			});
        }else {
        }
        return vi;
    }
    
    public void pickChild(final int position, View v, final TextView parent){
    	LinearLayout vi = (LinearLayout)v.findViewById(R.id.childBox);
    	
    	vi.removeAllViews();
    	for (int i = 0; i < data.get(position).popups.size(); i++) {
    		
			final TextView tv = new TextView(activity);
			if (data.get(position).popups.get(i).soldOut.equals("1")){
				tv.setBackgroundColor(Color.parseColor("#cccccc"));
			}else{
				tv.setBackgroundColor(Color.parseColor("#ffcc33"));
			}
			int tmp2 = data.get(position).popups.get(i).amountTmp;
			tv.setText(tmp2+". "+data.get(position).popups.get(i).menuItemName);
			

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			float scale = activity.getResources().getDisplayMetrics().density;
			int leftDp = (int) (30*scale + 0.5f);
			int TopDp = (int) (10*scale + 0.5f);
			
			
            tv.setPadding(leftDp, TopDp, 0, TopDp);
	    	tv.setLayoutParams(params);
	    	vi.addView(tv);
	    	
	    	//divider
	    	LinearLayout div = new LinearLayout(activity);
	    	LinearLayout.LayoutParams div_params = new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT, 1);
	    	div.setBackgroundColor(Color.parseColor("#9d9d9d"));
	    	div.setLayoutParams(div_params);
	    	vi.addView(div);
	    	
	    	final int ii = i;
	    	tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (data.get(position).popups.get(ii).soldOut.equals("0")){
						data.get(position).bagian = data.get(position).popups.get(ii).menuItemName;
						Menu tmp = new Menu(data.get(position));
						menuTmp = tmp;
						pickJumlah(position, true, ii, false, tv, parent, data.get(position).popups.get(ii).menuItemName, data.get(position).menuItemName);
					}
				}
			});
	    	
	    	// add nasi
	    	addNasi(position, v, true);
		}

    }
    
    public void addNasi(final int position, View v, boolean hasChild){
 
    	LinearLayout vi = (LinearLayout)v.findViewById(R.id.childBox);
    	if (!hasChild)
    		vi.removeAllViews();
    	for (int j = 0; j < data.get(position).nasi.size(); j++) {
    	    TextView tvs = new TextView(activity);
			if (data.get(position).nasi.get(j).soldOut.equals("1")){
				tvs.setBackgroundColor(Color.parseColor("#cccccc"));
			}else{
				tvs.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
			
			int tmp3 = data.get(position).nasi.get(j).amountTmp;
			tvs.setText(tmp3+". "+data.get(position).nasi.get(j).menuItemName);

			LinearLayout.LayoutParams paramsd = new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			float scales = activity.getResources().getDisplayMetrics().density;
			int leftDps = (int) (60*scales + 0.5f);
			int TopDps = (int) (10*scales + 0.5f);
			
			
            tvs.setPadding(leftDps, TopDps, 0, TopDps);
	    	tvs.setLayoutParams(paramsd);
	    	vi.addView(tvs);
	    	
	    	//divider
	    	LinearLayout divd = new LinearLayout(activity);
	    	LinearLayout.LayoutParams div_paramdd = new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT, 1);
	    	divd.setBackgroundColor(Color.parseColor("#9d9d9d"));
	    	divd.setLayoutParams(div_paramdd);
	    	vi.addView(divd);
	    	
	    	final int jj = j;
	    	final TextView tvs2 = tvs;
	    	tvs.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (data.get(position).nasi.get(jj).soldOut.equals("0")){
//						menuTmp = data.get(position).nasi.get(jj);
//						menuTmp.bagian = null;
						pickJumlah(position, false, jj, true, tvs2, null, data.get(position).nasi.get(jj).menuItemName, null);
					}
				}
			});
		}
    }
    
    @SuppressWarnings("deprecation")
	public void pickJumlah(final int position, boolean hasPopup, final int childpos, final boolean isNasi, 
    		final TextView textview, final TextView parentview, final String menuName, final String parentName){
    	LayoutInflater inflater = (LayoutInflater)
				activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_persons, null);
		
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;		
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.8), ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pWindow.findViewById(R.id.button1).post(new Runnable() {

		    public void run() {
		    	pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
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
		if (hasPopup)
			tv.setText("Amount "+data.get(position).menuItemName+" - "+data.get(position).popups.get(childpos).menuItemName);
		else{
			if (isNasi)
				tv.setText("Amount "+data.get(position).nasi.get(childpos).menuItemName);
			else
				tv.setText("Amount "+data.get(position).menuItemName);
			
		}
		et.setHint("number of orders");
		
		btn.setOnClickListener(new View.OnClickListener() {
			// person sets, continue to waitress lists
			@Override
			public void onClick(View v) {
				if (!et.getText().toString().equals("")){
//					Menu m = data.get(position);
					
					
					if (isNasi){
						int tmpJml = Integer.parseInt(et.getText().toString());
						data.get(position).nasi.get(childpos).amountTmp = tmpJml;
						data.get(position).nasi.get(childpos).amount = tmpJml;
						textview.setText(tmpJml+". "+menuName);
					}else{
						
						menuTmp.amount = Integer.parseInt(et.getText().toString());
						if (MenuActivity.dineIn)
							ParentActivity.order.addMenu(menuTmp);
						else
							ParentActivity.order.addTakeHome(menuTmp);
						
						if (parentview != null){
							int jml = data.get(position).popups.get(childpos).amountTmp;
							int tmpJml = Integer.parseInt(et.getText().toString());
							data.get(position).popups.get(childpos).amountTmp = tmpJml;
							
							int jmlParent = data.get(position).amountTmp;
							int tmpParent = jmlParent + tmpJml - jml;
							data.get(position).amountTmp = tmpParent;
							parentview.setText(tmpParent+". "+parentName);
							textview.setText(tmpJml+". "+menuName);
						}else {
							int tmpJml = Integer.parseInt(et.getText().toString());
							data.get(position).amountTmp = tmpJml;
							textview.setText(tmpJml+". "+menuName);
						}
					}
					
					pwindo.dismiss();
					layout.getForeground().setAlpha( 0);
//					((Activity) activity).onBackPressed();
//					pickNasi(position);
				}
			}
		});
    }
    
    
    
}
