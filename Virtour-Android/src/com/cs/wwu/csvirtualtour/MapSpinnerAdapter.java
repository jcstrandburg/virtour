package com.cs.wwu.csvirtualtour;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MapSpinnerAdapter extends ArrayAdapter<Map> {

	private Context context;
	
	private Map[] values;
	
	public MapSpinnerAdapter(Context context, int textViewResourceId,
			Map[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.values = objects;
		
	}
	
	public int getCount() {
		return values.length;
	}
	
	public Map getItem(int position) {
		return values[position];
	}
	
	public long getItemID(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(values[position].getMapDescription());
		
		return label;
		
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(values[position].getMapDescription());
		
		return label;
	}

}
