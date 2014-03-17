package com.newbee.kristian.KOS.models;

import android.content.Context;

import com.orm.SugarRecord;

public class CategoryModel extends SugarRecord<CategoryModel>{
	private String category;
	
	public CategoryModel(Context arg0) {
		super(arg0);
	}

	public CategoryModel(Context arg0, String cat){
	    super(arg0);
	    this.category = cat;
	}
	
	public String getCategory(){
		return this.category;
	}
}
