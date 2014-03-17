package com.newbee.kristian.KOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.newbee.kristian.KOS.adapters.ActionbarSpinnerAdapter;
import com.newbee.kristian.KOS.adapters.MejaAdapter;
import com.newbee.kristian.KOS.models.ApiTables;
import com.newbee.kristian.KOS.models.Server;
import com.newbee.kristian.KOS.models.StaffModel;
import com.newbee.kristian.KOS.models.Table;
import com.newbee.kristian.KOS.models.User;
import com.newbee.kristian.KOS.models.UserModel;

import android.os.Bundle;
import android.os.Handler;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("NewApi")
public class TableActivity extends ParentActivity implements ActionBar.OnNavigationListener {
	// action bar
    public ActionBar actionBar;
    public List<Table> listTables, masterTables, tmpTables = new ArrayList<Table>();
    public LinearLayout footerBox;
    public ArrayList<Button> btnArr;
    private static String[] TABLE_CONDITION = new String[] {"tables", "available_table", "busy_table"};
    private ActionbarSpinnerAdapter adapter;
    private int tableCount;
    private SlidingMenu menu;
    private GridView gridView;
    private int firstRow, lastRow, count;
    private MejaAdapter tbAdapter;
    @SuppressWarnings("unused")
	private String[] data, tmpdata, master;
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
		if (!user.role.equals(User.OPERATOR))
			init_slidemenu();
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
			
			System.out.println("xx "+server.url()+this.table_list);
			
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(source);
			ApiTables response = gson.fromJson(reader, ApiTables.class);
			System.out.println("hoho "+response.code);
			if (response.code.equals("OK")) {
				System.out.println("hoho "+response.code);
				// set count
				this.tableCount = Integer.parseInt(response.resultCount);
				System.out.println("hoho "+tableCount);
//				init_button_footer(tableCount);
				this.masterTables = response.results;
				System.out.println("hoho "+tableCount);
			
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
//		firstRow = 1;
//		lastRow = 20;
		this.tmpTables = this.masterTables;
		this.listTables = new ArrayList<Table>(lastRow-firstRow+1);
    	int x = 0;
    	for (int j = 0; j < this.tmpTables.size(); j++) {
			if (((Integer.parseInt(this.tmpTables.get(j).tableName))>= firstRow) && ( ((Integer.parseInt(this.tmpTables.get(j).tableName)))<= lastRow)){
				this.listTables.add(x, this.tmpTables.get(j));
				x++;
    		}
		}
		tbAdapter = new MejaAdapter(this, this.listTables, firstRow, lastRow, layout, progress);
		gridView.setAdapter(tbAdapter);
    }
	
	public void init_slidemenu(){
		// configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        menu.setShadowDrawable(R.drawable.sliding_shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slidingmenu);
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
				bt.setBackground(getResources().getDrawable(
						R.drawable.cab_background_top_kaleyostyle));
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
		if (!user.role.equals(User.OPERATOR))
			actionBar.setDisplayHomeAsUpEnabled(true);
        // Hide the action bar title
        actionBar.setDisplayShowTitleEnabled(false);
        // Enabling Spinner dropdown navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);     
        // title drop down adapter
        adapter = new ActionbarSpinnerAdapter(this, actionBar, subtitle_pos);
        // assigning the spinner navigation     
        actionBar.setListNavigationCallbacks(adapter, this);
        actionBar.setSelectedNavigationItem(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (!user.role.equals(User.OPERATOR))
			getMenuInflater().inflate(R.menu.table, menu);
		else
			getMenuInflater().inflate(R.menu.table_operator, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
//		// SET CHOOSEN TABLE
		if (synthetic) {
            synthetic = false;
            actionBar.setSelectedNavigationItem(0);
		}else{
			System.out.println("kampret "+arg0);
			progress.setMessage("Get tables, please wait..");
			progress.show();
			Intent i = new Intent(TableActivity.this, TableActivity.class);
	    	i.putExtra("table", TABLE_CONDITION[arg0-1]);
	    	i.putExtra("pos", String.valueOf(arg0-1));
	    	i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    	startActivity(i);	 
//	    	if (arg0 == 0)
	    	finish();
		}
		return false;
	}
	
	private android.view.View.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	
        	firstRow = (v.getId()*20) + 1;
        	lastRow = (firstRow + 19);
        	
        	Intent i = new Intent(TableActivity.this, TableActivity.class);
	    	i.putExtra("firstrow", String.valueOf(firstRow));
	    	i.putExtra("lastrow", String.valueOf(lastRow));
	    	i.putExtra("index", String.valueOf(v.getId()));
	    	i.putExtra("table", table_list);
	    	i.putExtra("pos", String.valueOf(subtitle_pos));
	    	i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    	startActivity(i);	 
//	    	if (arg0 == 0)
	    	finish();
        }
    };
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.menu.toggle();
            return true;
        case R.id.action_refresh:
        	Intent i = new Intent(TableActivity.this, TableActivity.class);
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
    	if (!user.role.equals(User.OPERATOR)){
	        if ( menu.isMenuShowing()) {
	            menu.toggle();
	        }
	        else {
	            super.onBackPressed();
	        }
    	}else
    		super.onBackPressed();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (!user.role.equals(User.OPERATOR)){
	        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	            this.menu.toggle();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
    	}else
    		return super.onKeyDown(keyCode, event);
    }
    
    public void doLogout(View view){
    	progress.setMessage("Loging out..");
    	progress.show();
    	UserModel.deleteAll(UserModel.class);
    	StaffModel.deleteAll(StaffModel.class);
    	Intent i = new Intent(TableActivity.this, LoginActivity.class);
    	startActivity(i);
    	finish();
    	progress.hide();
    }

}
