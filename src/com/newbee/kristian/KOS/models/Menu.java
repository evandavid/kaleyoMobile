package com.newbee.kristian.KOS.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Menu {
	
	public Menu(Menu menu) {
		popups = menu.popups;
		menuItemId = menu.menuItemId;
		menuItemName = menu.menuItemName;
		menuId = menu.menuId;
		popupMenuId = menu.popupMenuId;
		soldOutItem = menu.soldOutItem;
		hasPopup = menu.hasPopup;
		itemId = menu.itemId;
		soldOut = menu.soldOut;
		bagian = menu.bagian;
		amount = menu.amount;
		amountTmp = menu.amountTmp;
		nasi = menu.nasi;
		orderType = menu.orderType;
		orderNumber = menu.orderNumber;
	}

	public List<MenuPopup> popups;
	
	@SerializedName("menu_item_id")
    public String menuItemId;
	
	@SerializedName("menu_item_name")
    public String menuItemName;
	
	@SerializedName("menu_id")
    public String menuId;
	
	@SerializedName("popup_menu_id")
    public String popupMenuId;
	
	@SerializedName("sold_out_item")
    public String soldOutItem;
	
	@SerializedName("has_popup")
    public String hasPopup;
	
	@SerializedName("item_id")
    public String itemId;
	
	@SerializedName("sold_out")
    public String soldOut;
	
	public String bagian;
	
	public int amount = 0;
	
	public int amountTmp = 0;
	
	public List<Menu> nasi;
	
	public String orderType;
	
	public int orderNumber;
	
}
