package com.cs.wwu.csvirtualtour;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public  class StopGenerator {

	public StopGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static void RequestStop(int stopId) throws MalformedURLException
	{
		StringBuilder builder = new StringBuilder();
		URL  httpGet = new URL("http://strandburg.us/virtour/api/jsonapi.php?stopid=" + stopId);
		
		try {
				InputStream in = httpGet.openStream();
				InputStreamReader content = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(content);
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
		}			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		//stopId 0 is a special case for the main Screen
		if (stopId == 0) {
			ParseMainScreen(builder.toString());
		}
		//All other stops just have a single stop entity
		else {
			ParseSingleStop(builder.toString());
		}
		
	}
	
	private static void ParseMainScreen(String data)
	{
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray stops = jsonObject.getJSONObject("result").getJSONArray("StopList");
			Log.d("StopGenerator", stops.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void ParseSingleStop(String data)
	{
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONObject stop = jsonObject.getJSONObject("result");
			Log.d("StopGenerator", stop.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
