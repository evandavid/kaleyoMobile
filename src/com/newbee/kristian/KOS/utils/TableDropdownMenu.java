package com.newbee.kristian.KOS.utils;

import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.models.Table;

public class TableDropdownMenu {
	private static List<String> menus;
	private static String tableName;
	
	public static List<String> initialize(Table table){
		menus = new ArrayList<String>(table.occupiedCount+2);
		menus.add(0, "VIEW ORDER");
		menus.add(1, "NEW COSTUMER");
		for (int i = 0; i < (table.occupiedCount*2); i = i+2) {
			if (i == 0)
				tableName = table.childs.get(i).tableName;
			else 
				tableName = table.childs.get(i/2).tableName;
			menus.add(i+2, "LINK TABLE "+tableName);
			menus.add(i+3, "MOVE TABLE "+tableName);
		}
		return menus;
	}
}
