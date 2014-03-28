package com.newbee.kristian.KOS.utils;

import java.util.Comparator;

import com.newbee.kristian.KOS.models.Menu;

public class MenuComparator implements Comparator<Menu> {
	public int compare(Menu one, Menu two) {
	    return one.codeOrder - two.codeOrder;
	}
}