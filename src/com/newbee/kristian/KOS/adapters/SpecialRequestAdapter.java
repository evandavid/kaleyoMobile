package com.newbee.kristian.KOS.adapters;

import java.util.List;

import com.newbee.kristian.KOS.OrderActivity;
import com.newbee.kristian.KOS.ParentActivity;
import com.newbee.kristian.KOS.R;
import com.newbee.kristian.KOS.models.Menu;
import com.newbee.kristian.KOS.models.SpecialRequest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class SpecialRequestAdapter extends BaseAdapter {
    
    private Context activity;
    private List<SpecialRequest> data;
    private PopupWindow pwindo;
    private PopupWindow parentWindo;
    private LayoutInflater inflater=null;
    private FrameLayout layout;
    private ListView lview;
	public String username, type;
	public int orderPos;
    
    public SpecialRequestAdapter(Context context, List<SpecialRequest> d, FrameLayout layout, int orderPos,
    		ListView lview, PopupWindow parentWindow) {
        this.activity = context;
        this.data = d;
        this.parentWindo = parentWindow;
        this.lview = lview;
        this.orderPos = orderPos;
        this.layout = layout;
 
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
	    	menu.setText(data.get(position).requestName);
			vi.setTag(data.get(position));
			
			// show quantity
			if (data.get(position).isNote){
				setNote(position, vi);
			}else{
				if (data.get(position).showQuantity.equals("1"))
					setAmount(position, vi);
				else
					setSpecialReq(position, vi);
			}
		}else {
        }
        return vi;
    }
    
    private void setNote(final int position, View vi){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {parentWindo.dismiss();} catch (Exception e) {}
				layout.getForeground().setAlpha( 180);
				showNoteForm(position);
			}
    	
    	});
    }
    
    private void setSpecialReq(final int position, View vi){
    	vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SpecialRequest newReq = new SpecialRequest(data.get(position));
				newReq.toSave = newReq.requestSummary;
		    	Menu tmp = ParentActivity.order.menus.get(orderPos);
				tmp.specialRequest.add(newReq);
				OrderActivity.sortData();
				OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
				lview.setAdapter(adapter);
				try {parentWindo.dismiss();} catch (Exception e) {}
				layout.getForeground().setAlpha( 0);
			}
		});    	
    }
    
    private void setAmount(final int position, View vi){
    	vi.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				try {parentWindo.dismiss();} catch (Exception e) {}
				layout.getForeground().setAlpha( 180);
				showAmountForm(position);
			}
		});
    }
    
    @SuppressWarnings("deprecation")
	private void showNoteForm(final int position){
    	LayoutInflater inflater = (LayoutInflater)
				activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_void, null);
		
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;		
		pwindo = new PopupWindow(pWindow,
				(int)(width*0.8), ViewGroup.LayoutParams.WRAP_CONTENT, true);
		try {
			pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
			((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		} catch (Exception e) {
			pWindow.post(new Runnable() {
			    public void run() {
			    	pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
			    	((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			    }
			});
		}
		

		Button btn_dismiss = (Button)pWindow.findViewById(R.id.button2);
		btn_dismiss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
				layout.getForeground().setAlpha( 0);
				pwindo.dismiss();
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
		tv.setText("Special Request Note");
		
		et.setHint("note");
		Menu tmp = ParentActivity.order.menus.get(orderPos);
		System.out.println("nasi "+tmp.specialNotePos);
		if (tmp.specialNotePos > 0){
			et.setText(tmp.specialRequest.get(tmp.specialNotePos-1).requestSummary);
		}
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!et.getText().toString().equals("")){
					Menu tmp = ParentActivity.order.menus.get(orderPos);
					if (tmp.specialNotePos < 1){
						SpecialRequest newReq = new SpecialRequest(data.get(position));
						newReq.requestSummary = et.getText().toString();
						newReq.toSave = newReq.requestSummary;
						tmp.specialRequest.add(newReq);
						tmp.specialNotePos = tmp.specialRequest.indexOf(newReq)+1;
					}else{
						tmp.specialRequest.get(tmp.specialNotePos-1).requestSummary = et.getText().toString();
						tmp.specialRequest.get(tmp.specialNotePos-1).toSave = et.getText().toString();
					}
					OrderActivity.sortData();
					OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
					lview.setAdapter(adapter);
					pwindo.dismiss();
					layout.getForeground().setAlpha( 0);
				}else{
					Toast.makeText(activity, "Cannot blank.", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
    
    @SuppressWarnings("deprecation")
	private void showAmountForm(final int position){
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
		tv.setText(data.get(position).requestName);
		et.setHint("number of orders");
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!et.getText().toString().equals("")){
					SpecialRequest newReq = new SpecialRequest(data.get(position));
					newReq.amount = et.getText().toString();
					newReq.toSave = newReq.amount+"-"+newReq.requestSummary;
					Menu tmp = ParentActivity.order.menus.get(orderPos);
					tmp.specialRequest.add(newReq);
					OrderActivity.sortData();
					OrderAdapter adapter = new OrderAdapter(((Activity)activity), ParentActivity.displayOrder.menus, layout, lview);
					lview.setAdapter(adapter);
					pwindo.dismiss();
					layout.getForeground().setAlpha( 0);
				}
			}
		});
    }
}
