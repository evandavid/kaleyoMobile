package com.newbee.kristian.KOS.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Table {
	public static String OCCUPIED = "0";
	public static String AVAILABLE = "1";
	public static String RESERVED = "2";
	public static String DONE = "3";
	
	public List<Table> childs;
	
	@SerializedName("table_id")
    public String tableId;
	
	@SerializedName("table_name")
    public String tableName;
	
	@SerializedName("table_status")
    public String tableStatus;
	
	@SerializedName("floor_id")
    public String floorId;
	
	@SerializedName("room_id")
    public String roomId;
	
	@SerializedName("branch_id")
    public String branchId;
	
	@SerializedName("occupied_count")
	public int occupiedCount;
}
