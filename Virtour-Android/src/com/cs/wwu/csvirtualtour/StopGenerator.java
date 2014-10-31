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


public class StopGenerator {

	private static final String STOP_URL = "http://strandburg.us/virtour/api/jsonapi.php?stopid=";
	public StopGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static Stop[] RequestStop(int stopId) throws MalformedURLException
	{
		StringBuilder builder = new StringBuilder();
		URL  httpGet = new URL(STOP_URL + stopId);
		
		
		//Log.d("StopGenerator", "Got here");
		try {
				InputStream in = httpGet.openStream();
				//Log.d("StopGenerator","Got herrre");
				InputStreamReader content = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(content);
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
		}			
		} catch (Exception e) {
			Log.d("StopGenerator","" + e.getMessage());
		}
		//stopId 0 is a special case for the main Screen
		if (stopId == 0) {
			return ParseMainScreen(builder.toString());
		}
		//All other stops just have a single stop entity
		else {
			return ParseSingleStop(builder.toString());
		}
		
	}
	
	private static Stop[] ParseMainScreen(String data)
	{
		Stop[] returned;
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray stops = jsonObject.getJSONObject("result").getJSONArray("StopList");
			//Log.d("StopGenerator", stops.toString());
			
			returned = new Stop[stops.length()];
			
			for (int i = 0; i < stops.length() ; i++){
				JSONObject stop = stops.getJSONObject(i);
				returned[i] = new Stop(stop.getString("StopName"),stop.getInt("StopID"),-1,-1,stop.getString("StopQRIdentifier"),"null");
			}
			return returned;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("StopGenerator",e.getMessage());
		}
		return null;
	}
	
	private static Stop[] ParseSingleStop(String data)
	{
		Stop[] returned = new Stop[1];
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONObject stop = jsonObject.getJSONObject("result");
			//Log.d("StopGenerator", stop.toString());
			
			returned[0] = new Stop(stop.getString("StopName"),stop.getInt("StopID"),-1,-1,stop.getString("StopQRIdentifier"),stop.getString("StopContent"));
		} catch (JSONException e) {
			Log.d("StopGenerator","" + e.getMessage());
		}
		return returned;
	}
}
