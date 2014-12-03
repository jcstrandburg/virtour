package com.cs.wwu.csvirtualtour;

public class Map implements Comparable<Map> {

	private int mapId;
	private int mapOrder;
	private String mapUrl;
	private String mapDescription;
	
	public int getMapId() {
		return mapId;
	}
	public int getMapOrder() {
		return mapOrder;
	}
	public String getMapUrl() {
		return mapUrl;
	}
	public String getMapDescription() {
		return mapDescription;
	}
	
	public Map(int id, String url, String description, int Order) {
		this.mapId = id;
		this.mapOrder = Order;
		this.mapUrl = url;
		this.mapDescription = description;
	
	}
	@Override
	public int compareTo(Map another) {
		
		return this.mapOrder - another.getMapOrder();
	}

}
