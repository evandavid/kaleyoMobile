package com.newbee.kristian.KOS.models;

import java.util.ArrayList;
import java.util.List;

public class Order {
	public int persons;
	public Table table;
	public String waitres, waitres_name;
	public String note;
	public String code;
	public String orderId;
	public int total = 0;
	public List<Menu> menus = new ArrayList<Menu>();
	public static int DINEIN_CODE = 1000;
	public static int TAMBAHAN_CODE = 3000;
	public static int TAKEAWAY_CODE = 2000;
	
	public void addOrder(Menu menu){
		menus.add(menu);
	}
	
	public Order(Order order){
		this.persons = order.persons;
		this.table = order.table;
		this.waitres = order.waitres;
		this.menus = order.menus;
		this.note = order.note;
		this.waitres_name = order.waitres_name;
		this.orderId = order.orderId;
		this.total = order.total;
	}

	public Order() {
	}
}
