package com.cs.wwu.csvirtualtour;

public class Stop {
	
	private String stopName;
	private int stopID;
	private float stopPositionX;
	private float stopPositionY;
	private String stopQRIdentifier;
	private String stopContent;
	
	public String getStopName() {
		return stopName;
	}
	public int getStopID() {
		return stopID;
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
	public Stop(String StopName, int StopID, float StopPositionX, float StopPositionY, String StopQRIdentifier, String StopContent) {
		// TODO Auto-generated constructor stub
		this.stopName = StopName;
		this.stopID = StopID;
		this.stopPositionX = StopPositionX;
		this.stopPositionY = StopPositionY;
		this.stopQRIdentifier = StopQRIdentifier;
		this.stopContent = StopContent;
	}

}
