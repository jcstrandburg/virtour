package com.cs.wwu.csvirtualtour;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.*;

public class ImageViewActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_map);
		LinearLayout mainView = (LinearLayout) findViewById(R.id.map_layout);
		mainView.setGravity(Gravity.CENTER);
		String[] values = getIntent().getExtras().getString("values").split(",");
		final String imageUrl = values[0];
		
		//if (values.length > 1)
		//{
			final int id = Integer.parseInt(values[1]);
		//}
		//final String imageurl = getIntent().getExtras().getString("imageUrl");
		MapTouchImageView mapView = new MapTouchImageView(this,id);
		mapView.setLayoutParams(MainActivity.SECOND_IMAGE_LAYOUT_PARAMS);
		mapView.setId(999);
		//mapView.setAdjustViewBounds(true);
		mapView.setImageResource(R.drawable.placeholder);
		mapView.setVisibility(View.INVISIBLE);
		mapView.setStops(Globals.getStops());
		mapView.post(new Runnable() {

			@Override
			public void run() {
				MapTouchImageView mapView = (MapTouchImageView) findViewById(999);
				ImageRetrievalTask irt = new ImageRetrievalTask(mapView);
				irt.execute(imageUrl);
				
			}
	
		});
		mainView.addView(mapView);
	}

}
