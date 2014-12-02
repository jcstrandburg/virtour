package com.cs.wwu.csvirtualtour;

import java.net.MalformedURLException;

import android.os.AsyncTask;

public class StopRetrievalTask extends AsyncTask<Integer, Void, Stop[]> {

	private OnTaskCompleted listener;
	public StopRetrievalTask(OnTaskCompleted listener){
		this.listener = listener;
	}
	@Override
	protected Stop[] doInBackground(Integer... params) {
		
		Stop[] stops = null;
		try {
			stops = StopGenerator.RequestStop((int)params[0]);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stops;
	}
	
	@Override
	protected void onPostExecute(Stop[] Result){
		
		listener.onTaskCompleted(Result);
	}
	


}
