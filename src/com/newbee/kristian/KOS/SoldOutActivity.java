package com.newbee.kristian.KOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.adapters.SoldOutAdapter;
import com.newbee.kristian.KOS.models.ApiSoldOut;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.SoldOut;

import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class SoldOutActivity extends ParentActivity {
	private final Handler myHandler = new Handler();
	private ActionBar actionBar;
	private ListView lviw;
	private SoldOutAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sold_out);
		
		lviw = (ListView)findViewById(R.id.listView1);
		
		init_actionbar();
		progress.setMessage("Downloading data...");
		progress.show();
		
		new Thread(new Runnable() {
			public void run() {
				getSoldOut();
			} 		
		}).start();
	}
	
	private void init_actionbar(){
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Sold Out Menu");
	}
	
	private void getSoldOut(){
		try {
			Server server = Server.findById(Server.class, (long) 1);			
			InputStream source = conn.doGetConnect(server.url()+"sold_out");
			Reader reader = new InputStreamReader(source);
			
			Gson gson = new Gson();
			ApiSoldOut response = gson.fromJson(reader, ApiSoldOut.class);
			
			if (response.code.equals("OK")){
				ParentActivity.soldOut = response.results;
				myHandler.post(successSoldOut);
			}
			else
				myHandler.post(failedSoldOut);

		} catch (Exception e) {
			myHandler.post(failedSoldOut);
		}
	}
	
	final Runnable successSoldOut = new Runnable() {
        public void run() {
            processData();
        }
    };
    
    private void processData(){
    	List<String> categories = new ArrayList<String>();
    	List<String> counter = new ArrayList<String>();
    	List<SoldOut> soldOut = ParentActivity.soldOut;
    	int x = 0;
    	
    	for (int i = 0; i < ParentActivity.soldOut.size(); i++) {
			if (categories.indexOf(soldOut.get(i).category) < 0){
				categories.add(soldOut.get(i).category);
				counter.add("1");
			}
			int tmp = Integer.parseInt(counter.get(categories.indexOf(soldOut.get(i).category)));
			if(soldOut.get(i).hasDetail.equals("1")){
				ParentActivity.soldOut.get(i).hasDetailPos = x;
				x = x+1;
			}
			ParentActivity.soldOut.get(i).position = tmp;
			counter.set(categories.indexOf(soldOut.get(i).category), String.valueOf(tmp+1));
		}
    	
    	try { progress.hide();} catch (Exception e) {}
    	adapter = new SoldOutAdapter(this, ParentActivity.soldOut);
    	lviw.setAdapter(adapter);
    }
    
    final Runnable failedSoldOut = new Runnable() {
        public void run() {
            gagalSoldOut();
        }
    };
    
    private void gagalSoldOut(){
    	try { progress.hide();} catch (Exception e) {}
    	Toast.makeText(this, "Failed to downlad sold out data, please try again", Toast.LENGTH_LONG).show();
    	finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	onBackPressed();
            return true;
        case R.id.done_btn:
        	saveSoldOut();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void saveSoldOut(){
    	progress.setMessage("Saving data...");
		progress.show();
		
		new Thread(new Runnable() {
			public void run() {
				postSoldOut();
			} 		
		}).start();
    }
    
    private void postSoldOut(){
		try {
			Server server = Server.findById(Server.class, (long) 1);
			List<String[]> data = new ArrayList<String[]>();
			Gson gson = new Gson();
			data.add(new String[]{"sold_outs", gson.toJson(ParentActivity.soldOut).toString()});
			data.add(new String[]{"updated_by", user.userId});
			InputStream source = conn.doPostConnect(server.url()+"sold_outs", data);
			if (source != null) 
				myHandler.post(broadcastSuksesSave);
			else
				myHandler.post(broadcastGagalSave);

		} catch (Exception e) {
			myHandler.post(broadcastGagalSave);
		}
	}
	
	final Runnable broadcastSuksesSave = new Runnable() {
        public void run() {
            suksesBroadcast("sold out");
        }
    };
    
    final Runnable broadcastGagalSave = new Runnable() {
        public void run() {
            gagalBroadcast("sold out");
        }
    };
    
    private void gagalBroadcast(String txt){
    	try {progress.hide();} catch (Exception e) {}
		Toast.makeText(getApplicationContext(), "Failed to save "+txt+", please try again.", Toast.LENGTH_SHORT).show();
    }
    
    private void suksesBroadcast(String txt){
    	try {progress.hide();} catch (Exception e) {}
		Toast.makeText(getApplicationContext(), "Success to save "+txt+" message.", Toast.LENGTH_SHORT).show();
		finish();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sold_out, menu);
		return true;
	}

}
