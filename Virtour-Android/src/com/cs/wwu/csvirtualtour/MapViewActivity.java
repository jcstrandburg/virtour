package com.cs.wwu.csvirtualtour;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MapViewActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		LinearLayout mainView = (LinearLayout) findViewById(R.id.map_layout);
		
		MapTouchImageView mapView = new MapTouchImageView(this);
		mapView.setLayoutParams(MainActivity.SECOND_IMAGE_LAYOUT_PARAMS);
		mapView.setId(999);
		mapView.setImageResource(R.drawable.placeholder);
		mapView.setStops(Globals.getStops());
		mapView.post(new Runnable() {

			@Override
			public void run() {
				MapTouchImageView mapView = (MapTouchImageView) findViewById(999);
				mapView.setImageBitmap(ImageProcessor.decodeSampledBitmapFromResource(getResources(),
						R.drawable.cf4_trace, mapView.getWidth(), mapView.getHeight()));
			}
	
		});
		mainView.addView(mapView);
	}

}