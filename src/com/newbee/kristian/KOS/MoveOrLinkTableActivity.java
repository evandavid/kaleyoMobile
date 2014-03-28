package com.newbee.kristian.KOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.newbee.kristian.KOS.adapters.MoveOrLinkMejaAdapter;
import com.newbee.kristian.KOS.models.ApiTables;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.Table;
import com.newbee.kristian.KOS.models.User;

import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;

public class MoveOrLinkTableActivity extends ParentActivity implements ActionBar.OnNavigationListener{

	public ActionBar actionBar;
    public List<Table> listTables, masterTables, tmpTables = new ArrayList<Table>();
    public LinearLayout footerBox;
    public ArrayList<Button> btnArr;
    public PopupWindow pwindos;
    public boolean isActive;
    private static String[] TABLE_CONDITION = new String[] {"tables", "available_table", "busy_table"};
    private int tableCount;
    private GridView gridView;
    private int firstRow, lastRow, count;
    private MoveOrLinkMejaAdapter tbAdapter;
    private String table_list = TABLE_CONDITION[0];
    private boolean synthetic = true;
    private int subtitle_pos, index_ = 0;
    private FrameLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		
		// set opacity of layout foreground
		this.layout = (FrameLayout) findViewById( R.id.framesplash);
		this.layout.getForeground().setAlpha( 0);
		
		String check = getIntent().getStringExtra("table");
		if (check != null){
			this.table_list = check;
			this.subtitle_pos = Integer.parseInt(getIntent().getStringExtra("pos"));
		}else{
			this.table_list = "tables";
			this.subtitle_pos = 0;
		}
		
		String first_ = getIntent().getStringExtra("firstrow");
		if (first_ != null){
			firstRow = Integer.parseInt(first_);
			lastRow = Integer.parseInt(getIntent().getStringExtra("lastrow"));
			index_ = Integer.parseInt(getIntent().getStringExtra("index"));
		}else{
			firstRow = 1;
			lastRow = 20;
			index_ = 0;
		}
		
		actionbar_init();
		table_init();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override 
	public void onPause(){
		super.onPause();
	}
	
	@Override 
	public void onStop(){
		super.onStop();
	}
	
	public void table_init(){
		progress.setMessage("Get tables, please wait..");
		progress.show();
		
		// Get Tables
		new Thread(new Runnable() {
			public void run() {
				getTables();
			} 		
		}).start();
	}
	
	public void getTables(){
		tmpTables = null;
		listTables = null;
		masterTables = null;
		try {
			Server server = Server.findById(Server.class, (long) 1);
			InputStream source = conn.doGetConnect(server.url()+this.table_list);
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			ApiTables response = gson.fromJson(reader, ApiTables.class);

			if (response.code.equals("OK")) {
				// set count
				this.tableCount = Integer.parseInt(response.resultCount);
				//init_button_footer(tableCount);
				this.masterTables = response.results;
			
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
    }
    
    public void sukses(){
    	progress.hide();
    	//dummy data
		gridView = (GridView) findViewById(R.id.gridView1);
		init_button_footer(tableCount);

		this.tmpTables = this.masterTables;
		this.listTables = new ArrayList<Table>(lastRow-firstRow+1);
    	int x = 0;
    	for (int j = 0; j < this.tmpTables.size(); j++) {
			if (((Integer.parseInt(this.tmpTables.get(j).tableName))>= firstRow) && ( ((Integer.parseInt(this.tmpTables.get(j).tableName)))<= lastRow)){
				this.listTables.add(x, this.tmpTables.get(j));
				x++;
    		}
		}
		tbAdapter = new MoveOrLinkMejaAdapter(this, this.listTables, firstRow, lastRow, user, progress);
		gridView.setAdapter(tbAdapter);
    }
	
	public void init_button_footer(int tbl){
		footerBox = (LinearLayout)findViewById(R.id.linearLayout1);
		btnArr = new ArrayList<Button>();
		this.count = (int) Math.ceil(tbl/20.0);
		int front, back = 0;
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		ll.weight = 1f;
		for (int i = 0; i < count; i++) {
			front = back == 0 ? 1 : back+1;
			back = (front + 19);
			Button bt = new Button(this);
			bt.setText(String.valueOf(front)+"-"+String.valueOf(back));
			bt.setPadding(3, 0, 3, 0);
			bt.setId(i);
			bt.setLayoutParams(ll);
			if (i == index_)
				bt.setBackgroundResource(R.drawable.cab_background_top_kaleyostyle);
			else
				bt.setBackgroundColor(Color.parseColor("#f2f2f2"));
			bt.setTextSize(12);
			footerBox.addView(bt);
			btnArr.add(bt);
			bt.setOnClickListener(btn_listener);
			
			// add divider
			if (i != (count-1)) {
				LinearLayout div = new LinearLayout(this);
				LinearLayout.LayoutParams lll = new LinearLayout.LayoutParams(2, LayoutParams.MATCH_PARENT);
				div.setBackgroundColor(Color.GRAY);
				lll.setMargins(0, 7, 0, 7);
				div.setLayoutParams(lll);
				footerBox.addView(div);
			}
		}
	}
	
	private void actionbar_init(){
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        if (ParentActivity.isMove)
        	actionBar.setTitle("Move Table "+ParentActivity.editTable.tableName);
        else
        	actionBar.setTitle("Link Table "+ParentActivity.editTable.tableName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (!user.role.equals(User.OPERATOR))
			getMenuInflater().inflate(R.menu.table, menu);
		else
			getMenuInflater().inflate(R.menu.table, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// SET CHOOSEN TABLE
		if (synthetic) {
            synthetic = false;
            actionBar.setSelectedNavigationItem(0);
		}else{
			progress.setMessage("Get tables, please wait..");
			progress.show();
			Intent i = new Intent(MoveOrLinkTableActivity.this, MoveOrLinkTableActivity.class);
	    	i.putExtra("table", TABLE_CONDITION[arg0-1]);
	    	i.putExtra("pos", String.valueOf(arg0-1));
	    	i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    	startActivity(i);	 

	    	finish();
		}
		return false;
	}
	
	private android.view.View.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	
        	firstRow = (v.getId()*20) + 1;
        	lastRow = (firstRow + 19);
        	
        	Intent i = new Intent(MoveOrLinkTableActivity.this, MoveOrLinkTableActivity.class);
	    	i.putExtra("firstrow", String.valueOf(firstRow));
	    	i.putExtra("lastrow", String.valueOf(lastRow));
	    	i.putExtra("index", String.valueOf(v.getId()));
	    	i.putExtra("table", table_list);
	    	i.putExtra("pos", String.valueOf(subtitle_pos));
	    	i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    	startActivity(i);	 

	    	finish();
        }
    };
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            return true;
        case R.id.action_refresh:
        	Intent i = new Intent(MoveOrLinkTableActivity.this, MoveOrLinkTableActivity.class);
	    	i.putExtra("firstrow", String.valueOf(firstRow));
	    	i.putExtra("lastrow", String.valueOf(lastRow));
	    	i.putExtra("index", String.valueOf(index_));
	    	i.putExtra("table", table_list);
	    	i.putExtra("pos", String.valueOf(subtitle_pos));
	    	i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    	startActivity(i);
	    	finish();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (!user.role.equals(User.OPERATOR)){
	        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
    	}else
    		return super.onKeyDown(keyCode, event);
    }

}
