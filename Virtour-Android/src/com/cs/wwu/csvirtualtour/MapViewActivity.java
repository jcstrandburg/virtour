package com.cs.wwu.csvirtualtour;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.*;

public class MapViewActivity extends Activity implements OnContentLoaded {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_map);
		LinearLayout mainView = (LinearLayout) findViewById(R.id.map_layout);
		mainView.setGravity(Gravity.CENTER);
		final int id = Integer.parseInt(getIntent().getExtras().getString("mapId"));
		MapTouchImageView mapView = new MapTouchImageView(this,id);
		mapView.setLayoutParams(MainActivity.SECOND_IMAGE_LAYOUT_PARAMS);
		mapView.setId(999);
		//mapView.setAdjustViewBounds(true);
		mapView.setImageResource(R.drawable.placeholder);
		//mapView.setVisibility(view.);
		mapView.setStops(Globals.getStops());
		mapView.post(new Runnable() {

			@Override
			public void run() {
				MapTouchImageView mapView = (MapTouchImageView) findViewById(999);
				ImageRetrievalTask irt = new ImageRetrievalTask(mapView,MapViewActivity.this);
				irt.execute(Globals.getMaps()[id].getMapUrl());
				
			}
	
		});
		mainView.addView(mapView);
	}

	@Override
	public void onContentLoaded() {
		// TODO Auto-generated method stub
		
	}

}
