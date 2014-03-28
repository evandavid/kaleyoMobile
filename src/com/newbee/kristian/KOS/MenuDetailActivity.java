package com.newbee.kristian.KOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.adapters.MenuAdapter;
import com.newbee.kristian.KOS.models.ApiMenu;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MenuDetailActivity extends ParentActivity {
	private String category, url;
	private ListView listview; 
	private List<com.newbee.kristian.KOS.models.Menu> listMenu;
	private List<com.newbee.kristian.KOS.models.Menu> listNasi;
	private MenuAdapter adapter;
	private FrameLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.layout = (FrameLayout) findViewById( R.id.framesplash);
		this.layout.getForeground().setAlpha( 0);
		
		this.category = getIntent().getStringExtra("category");
		this.listview = (ListView)findViewById(R.id.listView1);
		this.url = super.url+"menu?category="+this.category;
		
		progress.setMessage("Getting menu from server, please wait");
		progress.show();
		// Get Tables
		new Thread(new Runnable() {
			public void run() {
				getMenus();
			} 		
		}).start();
	}
		
	public void getMenus(){
		try {
			InputStream source = conn.doGetConnect(this.url);
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			ApiMenu response = gson.fromJson(reader, ApiMenu.class);

			if (response.code.equals("OK")) {
				// set count
				this.listMenu = response.results;
				myHandler.post(updateSukses);
			}else
				myHandler.post(updateGagal);

		} catch (Exception e) {
			myHandler.post(updateGagal);
		}
	}
	
	private final Handler myHandler = new Handler();
    final Runnable updateSukses = new Runnable() {
        public void run() {
            sukses();
        }
    };
    
    final Runnable updateSukses2 = new Runnable() {
        public void run() {
            sukses2();
        }
    };
    
    final Runnable updateGagal = new Runnable() {
        public void run() {
            gagal();
        }
    };
    
    public void gagal(){
    	progress.hide();
    }
    
    public void sukses(){
    	new Thread(new Runnable() {
			public void run() {
				getNasi();
			} 		
		}).start();
    }
    
    public void sukses2(){
    	progress.hide();

		adapter = new MenuAdapter(this, this.listMenu, this.listNasi, category,layout, progress);
		listview.setAdapter(adapter);
    }
    
    public void getNasi(){
		try {
			InputStream source = conn.doGetConnect(super.url+"menu?category=nasi");
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			ApiMenu response = gson.fromJson(reader, ApiMenu.class);

			if (response.code.equals("OK")) {
				// set count
				this.listNasi = response.results;
				myHandler.post(updateSukses2);
			}else
				myHandler.post(updateGagal);

		} catch (Exception e) {
			myHandler.post(updateGagal);
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		MenuActivity.marqueeSubtitle();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
