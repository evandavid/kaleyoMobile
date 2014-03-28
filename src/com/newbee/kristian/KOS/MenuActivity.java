package com.newbee.kristian.KOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.models.ApiPromo;
import com.newbee.kristian.KOS.models.CategoryModel;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.utils.Connection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MenuActivity extends TabActivity {
	private List<TabSpec> tabspec;
	public ActionBar actionBar;
	public static boolean dineIn = true;
	public static Activity act;
	public static String promo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dineIn = true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);// create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        act = this;
       
        
        List<CategoryModel> categories = CategoryModel.listAll(CategoryModel.class);
        this.tabspec = new ArrayList<TabHost.TabSpec>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
        	TabSpec tmp = tabHost.newTabSpec("Tab "+(i+1));
        	tmp.setIndicator(categories.get(i).getCategory());
            Intent intent = new Intent(this, MenuDetailActivity.class);
            intent.putExtra("category", categories.get(i).getCategory());
            intent.putExtra("table", "1");
            tmp.setContent(intent);
        	tabHost.addTab(tmp);
			tabspec.add(i, tmp);
			TextView x = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	        x.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
	        x.setTypeface(null, Typeface.BOLD);
		}
        
        getActionBar().setSubtitle(null);
        // Get Tables
 		new Thread(new Runnable() {
 			public void run() {
 				getPromo();
 			} 		
 		}).start();
	}
	
	public void getPromo(){
		try {
			Server server = Server.findById(Server.class, (long) 1);
			Connection conn = new Connection(this);
			InputStream source = conn.doGetConnect(server.url()+"promos");
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			ApiPromo response = gson.fromJson(reader, ApiPromo.class);

			if (response.code.equals("OK")) {
				// set promo
				promo = "";
				for (int i = 0; i < 5; i++) {
					promo = promo + "     " + response.promo;
				}
				myHandler.post(updateSukses);
			}
		} catch (Exception e) {
		}
	}
	
	private final Handler myHandler = new Handler();
    final Runnable updateSukses = new Runnable() {
        public void run() {
            sukses();
        }
    };
    
    private void sukses(){
    	getActionBar().setSubtitle(promo);
    }
	
	@SuppressLint("NewApi")
	private void actionbar_init(){
		
		getActionBar().setTitle("Table "+ParentActivity.order.table.tableName);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		marqueeSubtitle();
	}
	
	public static void marqueeSubtitle(){
		int subtitleId = Resources.getSystem().
				getIdentifier("action_bar_subtitle", "id", "android");

		TextView mAppSubtitle=(TextView)act.getWindow().getDecorView().findViewById(subtitleId);

		mAppSubtitle.setEllipsize(TruncateAt.MARQUEE);

	    mAppSubtitle.setFocusable(true);

	    mAppSubtitle.setFocusableInTouchMode(true);

	    mAppSubtitle.requestFocus();

	    mAppSubtitle.setSingleLine(true);

	    mAppSubtitle.setSelected(true);

	    mAppSubtitle.setMarqueeRepeatLimit(-1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu_toogle:
	        if(dineIn){
	            item.setTitle("Take Home");
	            dineIn=false;
	        }else{
	            item.setTitle("Dine In");
	            dineIn=true;
	        }
	        return true;
	    case android.R.id.home:
	    	onBackPressed();
	    case R.id.done_btn:
	    	onBackPressed();
	    default:
            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public void backHeader(View v){
		onBackPressed();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		actionbar_init();
		dineIn = true;
		new Thread(new Runnable() {
 			public void run() {
 				getPromo();
 			} 		
 		}).start();
	}

}
