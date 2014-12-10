package com.cs.wwu.csvirtualtour;

public class Globals {
	static private Stop[] Stops;
	static private Map[] Maps;
	
	static private boolean firstRun = true;

	static public Stop[] getStops() {
		return Stops;
	}

	static public void setStops(Stop[] stops) {
		Stops = stops;
	}

	public static Map[] getMaps() {
		return Maps;
	}

	public static void setMaps(Map[] maps) {
		Maps = maps;
	}

	public static boolean isFirstRun() {
		return firstRun;
	}

	public static void setFirstRun(boolean firstRun) {
		Globals.firstRun = firstRun;
	}

}
