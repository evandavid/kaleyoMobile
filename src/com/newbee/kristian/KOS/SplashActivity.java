package com.newbee.kristian.KOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.models.ApiCategories;
import com.newbee.kristian.KOS.models.CategoryModel;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.UserModel;

import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;

public class SplashActivity extends ParentActivity {
	private static int SPLASH_TIME_OUT = 100;
	private FrameLayout layout;
	private boolean isConnected = false;
	private PopupWindow pwindo;
	private EditText form;
	private String ip;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// set opacity of layout foreground
		this.layout = (FrameLayout) findViewById( R.id.framesplash);
		this.layout.getForeground().setAlpha( 0);
		
		//check connection first
		this.isConnected = conn.isConnected();
		if (!this.isConnected){
			Toast.makeText(this,"You are not connected, please connect to Bebek Kaleyo WIFI first.", 
	                Toast.LENGTH_LONG).show();
			this.finish();
		}else{
			if (super.hasIP){
				new Thread(new Runnable() {
					public void run() {
						tryConnection();
					} 		
				}).start();
			}else
				initiatePopupWindow();
		}
	}
	
	@SuppressLint("NewApi")
	private void initiatePopupWindow() {
		// We need to get the instance of the LayoutInflater
		LayoutInflater inflater = (LayoutInflater)
				getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		final View pWindow = inflater.inflate(R.layout.popup_server, null);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		
		pwindo = new PopupWindow(pWindow, (int)(width*0.85), ViewGroup.LayoutParams.WRAP_CONTENT, true);
		new Handler().postDelayed(new Runnable(){

		    public void run() {
		    	pwindo.showAtLocation(pWindow, Gravity.CENTER, 0, 0);
		    }

		}, 100L);
		
		Button 	 btn  = (Button)pWindow.findViewById(R.id.button1);
		form = (EditText)pWindow.findViewById(R.id.server);
		
		if (super.url != null)
			form.setText(super.ip);
		else
			form.setText("95.85.12.73");
		btn.setOnClickListener(btnClicked);
		this.layout.getForeground().setAlpha( 180);
	}
	
	OnClickListener btnClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ip = form.getText().toString();
			Server s = Server.findById(Server.class, (long) 1);
			if (s == null){
				Server ss = new Server(getApplicationContext(), ip);
				ss.save();
				checkIP();
			}else{
				s.ip = ip;
				s.save();
				checkIP();
			}
			
			progress.setTitle("");
			progress.setMessage("Checking server connection, please wait..");
			progress.show();
			new Thread(new Runnable() {
				public void run() {
					tryConnection();
				} 		
			}).start();
		}
	};
	
	public void tryConnection(){
		try {
			Server server = Server.findById(Server.class, (long) 1);
			InputStream source = conn.doGetConnect(server.url()+"categories");
			
			System.out.println("hehe "+server.url()+"categories");
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			System.out.println("hehe "+reader);
			ApiCategories response = gson.fromJson(reader, ApiCategories.class);
			
			if (response.code.equals("OK")) {
				//get all categories
				List<CategoryModel> categories = CategoryModel.listAll(CategoryModel.class);
				//if categories has changed reinsert into database
				if (categories.size() != response.results.size()){
					CategoryModel.deleteAll(CategoryModel.class);
					for (int i = 0; i < response.results.size(); i++) {
						CategoryModel cat = new CategoryModel(
								this,response.results.get(i).category.toString());
						cat.save();
					}
				}
			
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
    
    final Runnable updateGagal = new Runnable() {
        public void run() {
            gagal();
        }
    };
    
    public void gagal(){
    	progress.hide();
    	Toast.makeText(this,"Failed connecting to server, change server IP or try again.", 
                Toast.LENGTH_LONG).show();
    	initiatePopupWindow();
    }
    
    public void sukses(){
    	final Intent i;
    	List<UserModel> users = UserModel.listAll(UserModel.class);
    	if (users.size() == 0)
    		i = new Intent(SplashActivity.this, LoginActivity.class);
    	else
    		i = new Intent(SplashActivity.this, TableActivity.class);
    	
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	startActivity(i);	 
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
