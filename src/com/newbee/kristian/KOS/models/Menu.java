package com.newbee.kristian.KOS.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Menu {
	
	public Menu(Menu menu) {
		popups = menu.popups;
		if (menu.popups != null){
			popups = new ArrayList<MenuPopup>();
			for (int i = 0; i < menu.popups.size(); i++) {
				popups.add(new MenuPopup(menu.popups.get(i)));
			}
		}
		
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
		
		if (menu.nasi != null){
			nasi = new ArrayList<Menu>();
			for (int i = 0; i < menu.nasi.size(); i++) {
				nasi.add(new Menu(menu.nasi.get(i)));
			}
		}
		
		orderType = menu.orderType;
		orderNumber = menu.orderNumber;
		salesPrice = menu.salesPrice;
		dineIn = menu.dineIn;
		tambahan = menu.tambahan;
		position = menu.position;
		isNasi = menu.isNasi;
		universalSort = menu.universalSort;
		localSort = menu.localSort;
		nasiPos = menu.nasiPos;
		voids = menu.voids;
		voidNote = menu.voidNote;
		productId = menu.productId;
		category = menu.category;
		saved = menu.saved;
		order_item_no = menu.order_item_no;
		isExpand = menu.isExpand;
		specialNotePos = menu.specialNotePos;
		
		if (menu.specialRequest != null){
			specialRequest = new ArrayList<SpecialRequest>();
			for (int i = 0; i < menu.specialRequest.size(); i++) {
				specialRequest.add(new SpecialRequest(menu.specialRequest.get(i)));
			}
		}
	}

	public List<MenuPopup> popups;
	
	@SerializedName("menu_item_id")
    public String menuItemId;
	
	@SerializedName("sales_price")
    public String salesPrice;
	
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
	
	@SerializedName("void")
    public String voids;
	
	@SerializedName("void_note")
    public String voidNote;
	
	@SerializedName("product_id")
    public String productId;
	
	@SerializedName("category")
	public String category;
	
	@SerializedName("saved")
	public boolean saved = false;
	
	@SerializedName("order_item_no")
	public int order_item_no;
	
	public String bagian;
	
	@SerializedName("amount")
	public int amount = 0;
	
	@SerializedName("amountTmp")
	public int amountTmp = 0;
	
	public List<Menu> nasi;
	
	public String orderType;
	
	public int orderNumber;
	
	public boolean dineIn;
	public boolean tambahan;
	
	public int position, nasiPos;
	public boolean isNasi;
	
	public boolean isExpand = false;
	public int specialNotePos = -1;
	
	public int universalSort, localSort, codeOrder;
	
	public List<SpecialRequest> specialRequest = 
			new ArrayList<SpecialRequest>();
	
}
