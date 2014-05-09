package com.cs.wwu.csvirtualtour;

public class Stop {
	
	private String stopName;
	private int stopID;
	private int stopPositionX;
	private int stopPositionY;
	private String stopQRIdentifier;
	private String stopContent;
	
	public String getStopName() {
		return stopName;
	}
	public int getStopID() {
		return stopID;
	}
	public int getStopPositionX() {
		return stopPositionX;
	}
	public int getStopPositionY() {
		return stopPositionY;
	}
	public String getStopQRIdentifier() {
		return stopQRIdentifier;
	}
	public String getStopContent() {
		return stopContent;
	}
	public Stop(String StopName, int StopID, int StopPositionX, int StopPositionY, String StopQRIdentifier, String StopContent) {
		// TODO Auto-generated constructor stub
		this.stopName = StopName;
		this.stopID = StopID;
		this.stopPositionX = StopPositionX;
		this.stopPositionY = StopPositionY;
		this.stopQRIdentifier = StopQRIdentifier;
		this.stopContent = StopContent;
	}

}
