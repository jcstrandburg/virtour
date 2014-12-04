package com.cs.wwu.csvirtualtour;

public class Stop implements Comparable<Stop> {
	
	private String stopName;
	private int stopID;
	private int stopMapID;
	private int stopOrder;
	private float stopPositionX;
	private float stopPositionY;
	private String stopQRIdentifier;
	private String stopContent;
	private String stopRoomNumber;
	
	public String getStopName() {
		return stopName;
	}
	public int getStopID() {
		return stopID;
	}
	public int getStopMapID() {
		return stopMapID;
	}
	public int getStopOrder() {
		return stopOrder;
	}
	public float getStopPositionX() {
		return stopPositionX;
	}
	public float getStopPositionY() {
		return stopPositionY;
	}
	public String getStopQRIdentifier() {
		return stopQRIdentifier;
	}
	public String getStopContent() {
		return stopContent;
	}
	
	public String getStopRooNumber() {
		return stopRoomNumber;
	}
	public Stop(String StopName, int StopID, int StopOrder, float StopPositionX, float StopPositionY, String StopQRIdentifier, String StopContent, int StopMapId, String StopRoomNumber) {
		// TODO Auto-generated constructor stub
		this.stopName = StopName;
		this.stopID = StopID;
		this.stopPositionX = StopPositionX;
		this.stopPositionY = StopPositionY;
		this.stopQRIdentifier = StopQRIdentifier;
		this.stopContent = StopContent;
		this.stopOrder = StopOrder;
		this.stopMapID = StopMapId;
		this.stopRoomNumber = StopRoomNumber;
	}
	@Override
	public int compareTo(Stop another) {
		
		int compareOrder = another.getStopOrder();
		return this.stopOrder - compareOrder;
	}

}
