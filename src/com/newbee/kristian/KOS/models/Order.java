package com.newbee.kristian.KOS.models;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private int persons;
	private Table table;
	private List<Menu> menus = new ArrayList<Menu>();
	private List<Menu> takeHome = new ArrayList<Menu>();
	private List<Menu> tambahan = new ArrayList<Menu>();
	private String waitres;
	
	public int getPersons() {
		return persons;
	}
	
	public void setPersons(int persons) {
		this.persons = persons;
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	public List<Menu> getMenus() {
		return menus;
	}
	
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
	public String getWaitres() {
		return waitres;
	}
	
	public void setWaitres(String waitres) {
		this.waitres = waitres;
	}

	public List<Menu> getTakeHome() {
		return takeHome;
	}

	public void setTakeHome(List<Menu> takeHome) {
		this.takeHome = takeHome;
	}

	public List<Menu> getTambahan() {
		return tambahan;
	}

	public void setTambahan(List<Menu> tambahan) {
		this.tambahan = tambahan;
	}
	
	public void addMenu(Menu menu){
		menus.add(menu);
	}
	
	public void addTambahan(Menu menu){
		tambahan.add(menu);
	}
	
	public void addTakeHome(Menu menu){
		takeHome.add(menu);
	}
}
