package com.cs.wwu.csvirtualtour;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class MapRetrievalTask extends AsyncTask<Void, Void, Map[]> {

	private static final String MAP_URL = "http://sw.cs.wwu.edu/~vut3/virtualtour/maps/list.php";
	private OnTaskCompleted listener;
	
	public MapRetrievalTask(MainActivity listener){
		this.listener = listener;
	}
	@Override
	protected Map[] doInBackground(Void... params)  {

		try {
			return GetMaps();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Map[] GetMaps() throws MalformedURLException {
		Map[] returned = null;
		StringBuilder builder = new StringBuilder();
		URL httpGet;
		httpGet = new URL(MAP_URL);
		
		
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
		
		String data = builder.toString();
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(data);
			JSONArray maps = jsonObject.getJSONArray("result");
			returned = new Map[maps.length()];
			
			for (int i = 0; i < maps.length(); i++)
			{
				JSONObject map = maps.getJSONObject(i);
				returned[i] = new Map(map.getInt("id"), map.getString("url"),map.getString("desc"), map.getInt("ordering"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returned;
	}
	
	protected void onPostExecute(Map[] Result){
		
		Arrays.sort(Result);
		Globals.setMaps(Result);
		listener.onTaskCompleted(Result);
		
	}
	

}
