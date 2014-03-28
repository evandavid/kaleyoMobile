package com.newbee.kristian.KOS.utils;

import java.util.ArrayList;
import java.util.List;

import com.newbee.kristian.KOS.models.Table;

public class TableDropdownMenu {
	private static List<String[]> menus;
	private static String tableName;
	private static int childPos;
	
	public static List<String[]> initialize(Table table){
		menus = new ArrayList<String[]>(table.occupiedCount+1);
		menus.add(0, new String[]{"NEW COSTUMER", "new"});
		for (int i = 0; i < (table.occupiedCount*3); i = i+3) {
			if (i == 0){
				tableName = table.childs.get(i).tableName;
				childPos = i;
			}
			else{ 
				tableName = table.childs.get(i/3).tableName;
				childPos = i/3;
			}
			menus.add(i+1, new String[]{"VIEW ORDER TABLE "+tableName, "view_"+childPos});
			menus.add(i+2, new String[]{"LINK TABLE "+tableName, "link_"+childPos});
			menus.add(i+3, new String[]{"MOVE TABLE "+tableName, "move_"+childPos});
		}
		return menus;
	}
}
