package com.newbee.kristian.KOS.models;

import com.google.gson.annotations.SerializedName;

public class MenuPopup {
	
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
}
