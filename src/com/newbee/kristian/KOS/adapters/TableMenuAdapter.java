package com.newbee.kristian.KOS.adapters;

import java.util.List;

import com.newbee.kristian.KOS.MoveOrLinkTableActivity;
import com.newbee.kristian.KOS.OrderActivity;
import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.Order;
import com.newbee.kristian.KOS.models.Table;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class TableMenuAdapter extends BaseAdapter {
    
    private Context context;
    private List<String[]> data;
    private Table table;
    private LayoutInflater inflater=null;
    private PopupWindow pwindo;
    private PopupWindow parentwindo;
    private FrameLayout layout;
	public String username, type;
	public int posisi;
    
    public TableMenuAdapter(Context context, List<String[]> d, FrameLayout layout, PopupWindow parent, Table table) {
        this.context = context;
        this.table = table;
        this.data = d;
        this.layout = layout;
        this.parentwindo = parent;
        
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	    	menu.setText(data.get(position)[0]);
	    	
	    	vi.setBackgroundColor(Color.parseColor("#ffffff"));
	    	//new order
	    	if (data.get(position)[1].equals("new"))
	    		newOrder(vi, position);
	    	//view order
	    	else if (data.get(position)[1].contains("view"))
	    		viewOrder(vi, position);
	    	else if (data.get(position)[1].contains("link"))
	    		linkTable(vi, position);
	    	else if (data.get(position)[1].contains("move"))
	    		moveTable(vi, position);
        }else {
        }
        return vi;
    }
    
    private void linkTable(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int table_pos = Integer.parseInt(data.get(position)[1].split("_")[1]);
				Table tbl = table.childs.get(table_pos);
				ParentActivity.editTable = tbl;
				ParentActivity.isMove = false;
				ParentActivity.occupiedCount = table.occupiedCount;
				goToMoveOrLink();
			}
		});
    }
    
    private void moveTable(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int table_pos = Integer.parseInt(data.get(position)[1].split("_")[1]);
				Table tbl = table.childs.get(table_pos);
				ParentActivity.editTable = tbl;
				ParentActivity.occupiedCount = table.occupiedCount;
				ParentActivity.isMove = true;
				goToMoveOrLink();
			}
		});
    }
    
    private void goToMoveOrLink(){
    	try {
			parentwindo.dismiss();
		} catch (Exception e) {}
    	Intent i = new Intent(context, MoveOrLinkTableActivity.class);
    	context.startActivity(i);	 
    }
    
    private void viewOrder(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int table_pos = Integer.parseInt(data.get(position)[1].split("_")[1]);
				Table tbl = table.childs.get(table_pos);
				ParentActivity.editTable = tbl;
				OrderActivity.tambahan = true;
				OrderActivity.fromtambahan = true;
				goToOrder();
			}
		});
    }
    
    private void goToOrder(){
    	try {
			parentwindo.dismiss();
		} catch (Exception e) {}
    	Intent i = new Intent(context,OrderActivity.class);
    	context.startActivity(i);	 
    }
    
    private void newOrder(View vi, final int position){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				person(position);
			}
		});
    }
    
    @SuppressWarnings("deprecation")
	private void person(final int position){
    	
    	try {parentwindo.dismiss();	} catch (Exception e) {}
    	
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_persons, null);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		final int width = metrics.widthPixels;		
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.8), ViewGroup.LayoutParams.WRAP_CONTENT, true);
		
		try {
			pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	    	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		} catch (Exception e) {
			pWindow.post(new Runnable() {
			    public void run() {
			    	pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
			    	InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			    	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
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
		
		Button btn_dismiss = (Button)pWindow.findViewById(R.id.button2);
		btn_dismiss.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layout.getForeground().setAlpha( 0);
				pwindo.dismiss();
				((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
		});
		
		
		Button btn = (Button)pWindow.findViewById(R.id.button1);
		TextView tv = (TextView)pWindow.findViewById(R.id.textView1);
		final EditText et = (EditText)pWindow.findViewById(R.id.server);
		
		btn.setText("Continue");
		tv.setText("Table "+table.childs.get(table.occupiedCount).tableName+" - Person");
		et.setHint("Person");
		
		btn.setOnClickListener(new View.OnClickListener() {
			// person sets, continue to waitress lists
			@Override
			public void onClick(View v) {
				if (!et.getText().toString().equals("")){
					ParentActivity.order = new Order();
					ParentActivity.order.persons = (Integer.parseInt(et.getText().toString()));
					ParentActivity.order.table = (table.childs.get(table.occupiedCount));

					Intent i = new Intent(context.getApplicationContext(), OrderActivity.class);
			    	context.startActivity(i);	
			    	pwindo.dismiss();
				}
			}
		});
	}
}
