package com.newbee.kristian.KOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.models.ApiUser;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.StaffModel;
import com.newbee.kristian.KOS.models.User;
import com.newbee.kristian.KOS.models.UserModel;
import com.newbee.kristian.KOS.utils.Connection;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private TextView usernameBox, passwordBox;
	private String username, password;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initialize();
	}

	private void initialize(){
		usernameBox = (TextView)findViewById(R.id.username);
		passwordBox = (TextView)findViewById(R.id.password);
		progress = new ProgressDialog(this);
		progress.setTitle("");
		progress.setMessage("Loging in, please wait..");
	}
	
	public void loginClicked(View view){
		username = usernameBox.getText().toString();
		password = passwordBox.getText().toString();
		progress.show();
		new Thread(new Runnable() {
			public void run() {
				doLogin();
			} 		
		}).start();
	}
	
	public void doLogin(){
		try {
			Server server = Server.findById(Server.class, (long) 1);
			Connection conn = new Connection(getApplicationContext());
			InputStream source = conn.doGetConnect(server.url()+"login?username="+username+"&password="+password);
			
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			ApiUser response = gson.fromJson(reader, ApiUser.class);

			if (response.code.equals("OK")) {
				// set count
				User user = response.results.get(response.results.size()-1);	
				UserModel u = new UserModel(getApplicationContext(), user.userId, user.mobilePassword, user.firstName,
					      user.lastName, user.cardId, user.role, user.companyId, user.branchId, user.synch);
				u.save();
				
				for (int i = 0; i < response.staffs.size(); i++) {
					User usertmp = response.staffs.get(i);
					StaffModel us = new StaffModel(getApplicationContext(), usertmp.userId, usertmp.mobilePassword, usertmp.firstName,
						      usertmp.lastName, usertmp.cardId, usertmp.role, usertmp.companyId, usertmp.branchId, usertmp.synch);
					us.save();
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
    	Toast.makeText(this,"Failed to loggin, check username and password.", 
                Toast.LENGTH_LONG).show();
    }
    
    public void sukses(){
    	progress.hide();
    	Toast.makeText(this,"Sukses.", 
                Toast.LENGTH_LONG).show();
    	final Intent i = new Intent(LoginActivity.this, TableActivity.class);
    	
    	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	startActivity(i);	 
                finish();
            }
        }, 0);
    }

}
