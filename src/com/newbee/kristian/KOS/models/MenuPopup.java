package com.newbee.kristian.KOS.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MenuPopup {
	
	public MenuPopup(MenuPopup menu) {
		menuItemId = menu.menuItemId;
		menuItemName = menu.menuItemName;
		menuId = menu.menuId;
		popupMenuId = menu.popupMenuId;
		soldOutItem = menu.soldOutItem;
		soldOut = menu.soldOut;
		amount = menu.amount;
		amountTmp = menu.amountTmp;
		
		if (menu.nasi != null){
			nasi = new ArrayList<Menu>();
			for (int i = 0; i < menu.nasi.size(); i++) {
				nasi.add(new Menu(menu.nasi.get(i)));
			}
		}
		
	}

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
	
	@SerializedName("sold_out")
    public String soldOut;
	
	public int amountTmp = 0;
	public int amount = 0;
	
	public List<Menu> nasi = new ArrayList<Menu>();
}
