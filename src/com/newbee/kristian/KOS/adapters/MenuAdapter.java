package com.newbee.kristian.KOS.adapters;

import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.MenuActivity;
import com.newbee.kristian.KOS.OrderActivity;
import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.Menu;
import com.newbee.kristian.KOS.models.MenuPopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

@SuppressLint({ "NewApi", "DefaultLocale" })
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
	public Menu menuTmp;
    
    public MenuAdapter(Context context, List<Menu> d, List<Menu> nasi, String cat, FrameLayout layout, ProgressDialog progress) {
        this.activity = context;
        this.data = d;
        this.nas = nasi;
        this.layout = layout;
        this.progress = progress;
        this.category = cat;
        
        for (int i = 0; i < data.size(); i++) {
        	if (data.get(i).hasPopup.equals("1")){
        		for (int j = 0; j < data.get(i).popups.size(); j++) {
        			
        			data.get(i).popups.get(j).nasi = new ArrayList<Menu>();
        			data.get(i).popups.get(j).nasi.clear();
					for (int k = 0; k < nas.size(); k++) {
						Menu nn = new Menu(nas.get(k));
						data.get(i).popups.get(j).nasi.add(nn);
					}
				}
        	}else{
	        	data.get(i).nasi = new ArrayList<Menu>();
				data.get(i).nasi.clear();
				for (int j = 0; j < nas.size(); j++) {
					Menu nn = new Menu(nas.get(j));
					data.get(i).nasi.add(nn);
				}
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
	    	final ImageView img = (ImageView)vi.findViewById(R.id.imageView1);
	    	
	    	if (data.get(position).soldOut.equals("1")){
	    		ll.setBackgroundColor(Color.parseColor("#9d9d9d"));
	    		menu.setTextColor(Color.parseColor("#ffffff"));
	    		tvsoldout.setVisibility(View.VISIBLE);
	    		img.setVisibility(View.GONE);
	    	}else{
	    		if (data.get(position).hasPopup.equals("1")){
	    			ll.setBackgroundColor(Color.parseColor("#d34b01"));
	    			menu.setTextColor(Color.parseColor("#ffffff"));}
	    		else{
	    			ll.setBackgroundColor(Color.parseColor("#ffcc33"));
	    			menu.setTextColor(Color.parseColor("#333333"));
	    			if (category.toLowerCase().contains("ayam")){
//		    			addNasi(position, vi, false, 0);
	    			}
	    		}
	    		tvsoldout.setVisibility(View.GONE);
	    		img.setVisibility(View.VISIBLE);
	    	}
	    	
	    	if (category.toLowerCase().contains("ayam")){
	    		if (!data.get(position).isExpand){
	    			if (data.get(position).hasPopup.equals("1"))
	    				childBox.removeAllViews();
//	    			else
//			    		addNasi(position, vi, false, 0);
				}else{
		    		if (data.get(position).soldOut.equals("0")){
				    	if (data.get(position).hasPopup.equals("1")){
				    		pickChild(position, vi, menu);
				    	}
				    	else{
				    		addNasi(position, vi, false, 0);
				    	}
			    	}else{
			    		childBox.removeAllViews();
			    	}
				}
	    	}else{
	    		img.setVisibility(View.GONE);
	    		if (!data.get(position).isExpand){
		    		childBox.removeAllViews();
		    	}else{
		    		if (data.get(position).soldOut.equals("0")){
				    	if (data.get(position).hasPopup.equals("1"))
				    		pickChild(position, vi, menu);
				    	else
				    		childBox.removeAllViews();
			    	}else{
			    		childBox.removeAllViews();
			    	}
		    	}
	    	}
	    	
	    	
	    	final View vv = vi;
	    	
	    	// double click 
	    	img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//colapse or not
					if (data.get(position).isExpand){
						data.get(position).isExpand = false;
						childBox.removeAllViews();
						img.setImageResource(R.drawable.ic_action_expand);
					}else{
						data.get(position).isExpand = true;
						img.setImageResource(R.drawable.ic_action_collapse);
						if (data.get(position).soldOut.equals("0")){
							if (data.get(position).hasPopup.equals("1")){
								if (data.get(position).soldOut.equals("0")){
							    	if (data.get(position).hasPopup.equals("1")){
							    		pickChild(position, vv, menu);
							    	}
							    	else{
							    		addNasi(position, vv, false, 0);
							    	}
						    	}else{
						    		childBox.removeAllViews();
						    	}
							}else{
								addNasi(position, vv, false, 0);
							}
						}
					}
				}
			});
	    	
	    	
			ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (data.get(position).soldOut.equals("0")){
						if (data.get(position).hasPopup.equals("1")){
						}else{
							data.get(position).bagian = null;
							menuTmp = data.get(position);
							pickJumlah(position, false, 0, false, menu, null, data.get(position).menuItemName, null, 0);
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
				if (category.toLowerCase().contains("ayam"))
					tv.setBackgroundColor(Color.parseColor("#ffcc33"));
				else
					tv.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			int tmp2 = data.get(position).popups.get(i).amountTmp;
			tv.setText(tmp2+". "+data.get(position).popups.get(i).menuItemName);
			

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			float scale = activity.getResources().getDisplayMetrics().density;
			int leftDp = (int) (20*scale + 0.5f);
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
						for (int j = 0; j < tmp.popups.size(); j++) {
							for (int k = 0; k < tmp.popups.get(j).nasi.size(); k++) {
								tmp.popups.get(j).nasi.get(k).amount = 0;
								tmp.popups.get(j).nasi.get(k).amountTmp = 0;
							}
						}
						
						menuTmp = tmp;
						pickJumlah(position, true, ii, false, tv, parent, data.get(position).popups.get(ii).menuItemName, data.get(position).menuItemName, 0);
					}
				}
			});
	    	
	    	// add nasi
	    	if (category.toLowerCase().contains("ayam"))
	    		addNasi(position, v, true, i);
		}

    }
    
    public void addNasi(final int position, View v, boolean hasChild, final int childPos){
 
    	LinearLayout vi = (LinearLayout)v.findViewById(R.id.childBox);    	
    	if (!hasChild){
    		vi.removeAllViews();
        	LinearLayout vi2 = new LinearLayout(activity);
        	LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        	vi2.setLayoutParams(paramsx);
        	vi2.setOrientation(LinearLayout.HORIZONTAL);
        	vi.addView(vi2);
    		for (int j = 0; j < data.get(position).nasi.size(); j++) {
    			
    			TextView tvs = new TextView(activity);
    			if (data.get(position).nasi.get(j).soldOut.equals("1")){
    				tvs.setBackgroundColor(Color.parseColor("#cccccc"));
    			}else{
    				tvs.setBackgroundColor(Color.parseColor("#FFFFFF"));
    			}
    			
    			int tmp3 = data.get(position).nasi.get(j).amountTmp;
    			String[] m = data.get(position).nasi.get(j).menuItemName.split(" ");
    			String ds;
    			if (m.length > 2){
    				ds = m[0].substring(0,1)+m[1].substring(0,1)+m[2];
    			}else{
    				ds = m[0].substring(0,1)+m[1].substring(0,1);
    			}
    			tvs.setText(tmp3+". "+ds);

    			LinearLayout.LayoutParams paramsd = new LinearLayout.LayoutParams(
    	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
    			
    			float scales = activity.getResources().getDisplayMetrics().density;
    			int leftDps = (int) (20*scales + 0.5f);
    			int TopDps = (int) (10*scales + 0.5f);
    			
    			
    			if (j == 0)
    				tvs.setPadding(leftDps, TopDps, 0, TopDps);
    			else
    				tvs.setPadding(TopDps, TopDps, 0, TopDps);
    	    	tvs.setLayoutParams(paramsd);
    	    	vi2.addView(tvs);
    	    	
    	    	//divider
    	    	LinearLayout divd = new LinearLayout(activity);
    	    	LinearLayout.LayoutParams div_paramdd = new LinearLayout.LayoutParams(
    	                1, LayoutParams.MATCH_PARENT);
    	    	divd.setBackgroundColor(Color.parseColor("#9d9d9d"));
    	    	divd.setLayoutParams(div_paramdd);
    	    	vi2.addView(divd);
    	    	
    	    	final int jj = j;
    	    	final TextView tvs2 = tvs;
    	    	tvs.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					if (data.get(position).nasi.get(jj).soldOut.equals("0")){
    						pickJumlah(position, false, jj, true, tvs2, null, data.get(position).nasi.get(jj).menuItemName, null, jj);
    					}
    				}
    			});
    		}
    	}else{
    		final MenuPopup child = data.get(position).popups.get(childPos);
    		LinearLayout vi2 = new LinearLayout(activity);
        	LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        	vi2.setLayoutParams(paramsx);
        	vi2.setOrientation(LinearLayout.HORIZONTAL);
        	vi.addView(vi2);
    		for (int j = 0; j < child.nasi.size(); j++) {
        	    TextView tvs = new TextView(activity);
    			if (child.nasi.get(j).soldOut.equals("1")){
    				tvs.setBackgroundColor(Color.parseColor("#cccccc"));
    			}else{
    				tvs.setBackgroundColor(Color.parseColor("#FFFFFF"));
    			}
    			
    			int tmp3 = child.nasi.get(j).amountTmp;
    			String[] m = child.nasi.get(j).menuItemName.split(" ");
    			String ds;
    			if (m.length > 2){
    				ds = m[0].substring(0,1)+m[1].substring(0,1)+m[2];
    			}else{
    				ds = m[0].substring(0,1)+m[1].substring(0,1);
    			}
    			tvs.setText(tmp3+". "+ds);

    			LinearLayout.LayoutParams paramsd = new LinearLayout.LayoutParams(
    	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
    			
    			float scales = activity.getResources().getDisplayMetrics().density;
    			int leftDps = (int) (30*scales + 0.5f);
    			int TopDps = (int) (10*scales + 0.5f);
    			
    			if (j == 0)
    				tvs.setPadding(leftDps, TopDps, 0, TopDps);
    			else
    				tvs.setPadding(TopDps, TopDps, 0, TopDps);
    	    	tvs.setLayoutParams(paramsd);
    	    	vi2.addView(tvs);
    	    	
    	    	//divider
    	    	LinearLayout divd = new LinearLayout(activity);
    	    	LinearLayout.LayoutParams div_paramdd = new LinearLayout.LayoutParams(1, 
    	                LayoutParams.MATCH_PARENT);
    	    	divd.setBackgroundColor(Color.parseColor("#9d9d9d"));
    	    	divd.setLayoutParams(div_paramdd);
    	    	vi2.addView(divd);
    	    	
    	    	final int jj = j;
    	    	final TextView tvs2 = tvs;
    	    	tvs.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					if (data.get(position).popups.get(childPos).nasi.get(jj).soldOut.equals("0")){
    						pickJumlah(position, true, childPos, true, tvs2, null, child.nasi.get(jj).menuItemName, null, jj);
    					}
    				}
    			});
    		}
    	}
    	
    }
    
    @SuppressWarnings("deprecation")
	public void pickJumlah(final int position, final boolean hasPopup, final int childpos, final boolean isNasi, 
    		final TextView textview, final TextView parentview, final String menuName, final String parentName,
    		final int nasPos){
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
		this.layout.getForeground().setAlpha( 180);
		
		Button btn = (Button)pWindow.findViewById(R.id.button1);
		TextView tv = (TextView)pWindow.findViewById(R.id.textView1);
		final EditText et = (EditText)pWindow.findViewById(R.id.server);
		
		btn.setText("Continue");
		if (hasPopup){
			if (isNasi)
				tv.setText("Amount "+data.get(position).popups.get(childpos).nasi.get(nasPos).menuItemName);
			else
				tv.setText("Amount "+data.get(position).menuItemName+" - "+data.get(position).popups.get(childpos).menuItemName);
		}
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

					if (isNasi){
						if (hasPopup){
							if (data.get(position).popups.get(childpos).amount > 0){
								int tmpJml = Integer.parseInt(et.getText().toString());
								menuTmp.popups.get(childpos).nasi.get(nasPos).amountTmp = tmpJml;
								menuTmp.popups.get(childpos).nasi.get(nasPos).amount = tmpJml;
								data.get(position).popups.get(childpos).nasi.get(nasPos).amountTmp = tmpJml;
								data.get(position).popups.get(childpos).nasi.get(nasPos).amount = tmpJml;
								String[] m = menuName.split(" ");
				    			String ds;
				    			if (m.length > 2){
				    				ds = m[0].substring(0,1)+m[1].substring(0,1)+m[2];
				    			}else{
				    				ds = m[0].substring(0,1)+m[1].substring(0,1);
				    			}
								textview.setText(tmpJml+". "+ds);
							}else{
								Toast.makeText(activity.getApplicationContext(), "Pilih menu terlebih dahulu, atau pilih nasi melalui tab nasi.", Toast.LENGTH_LONG).show();
							}
						}else{
							if (data.get(position).amount > 0){
								int tmpJml = Integer.parseInt(et.getText().toString());
								data.get(position).nasi.get(childpos).amountTmp = tmpJml;
								data.get(position).nasi.get(childpos).amount = tmpJml;
								
								menuTmp.nasi.get(childpos).amountTmp = tmpJml;
								menuTmp.nasi.get(childpos).amount = tmpJml;
								String[] m = menuName.split(" ");
				    			String ds;
				    			if (m.length > 2){
				    				ds = m[0].substring(0,1)+m[1].substring(0,1)+m[2];
				    			}else{
				    				ds = m[0].substring(0,1)+m[1].substring(0,1);
				    			}
								textview.setText(tmpJml+". "+ds);
							}else{
								Toast.makeText(activity.getApplicationContext(), "Pilih menu terlebih dahulu, atau pilih nasi melalui tab nasi.", Toast.LENGTH_LONG).show();
							}
						}
					}else{
						
						menuTmp.amount = Integer.parseInt(et.getText().toString());
						menuTmp.dineIn = MenuActivity.dineIn;
						menuTmp.tambahan = MenuActivity.dineIn ? OrderActivity.tambahan : false;
						
						ParentActivity.order.addOrder(menuTmp);
						
						if (hasPopup){
							int jml = data.get(position).popups.get(childpos).amountTmp;
							int tmpJml = Integer.parseInt(et.getText().toString());
							data.get(position).popups.get(childpos).amountTmp = tmpJml;
							data.get(position).popups.get(childpos).amount = tmpJml;
							
							menuTmp.popups.get(childpos).amountTmp = tmpJml;
							menuTmp.popups.get(childpos).amount = tmpJml;
							
							int jmlParent = data.get(position).amountTmp;
							int tmpParent = jmlParent + tmpJml - jml;
							data.get(position).amountTmp = tmpParent;
							menuTmp.amountTmp = tmpParent;
							parentview.setText(tmpParent+". "+parentName);
							textview.setText(tmpJml+". "+menuName);
						}else {
							int tmpJml = Integer.parseInt(et.getText().toString());
							data.get(position).amountTmp = tmpJml;
							data.get(position).amount = tmpJml;
							menuTmp.amountTmp = tmpJml;
							menuTmp.amount = tmpJml;
							textview.setText(tmpJml+". "+menuName);
						}
					}
					
					pwindo.dismiss();
					layout.getForeground().setAlpha( 0);

				}
			}
		});
    }
    
    
    
}
