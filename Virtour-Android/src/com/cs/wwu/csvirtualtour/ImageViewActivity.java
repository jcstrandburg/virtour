package com.cs.wwu.csvirtualtour;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ImageViewActivity extends Activity implements OnContentLoaded {
	
	
	int queuedContent = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_map);
		LinearLayout mainView = (LinearLayout) findViewById(R.id.map_layout);
		mainView.setGravity(Gravity.CENTER);
		mainView.setVisibility(View.INVISIBLE);
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
				ImageRetrievalTask irt = new ImageRetrievalTask(mapView, ImageViewActivity.this);
				irt.execute(imageUrl);
				initiateLoadingPopup();
				queuedContent ++;
				
			}
	
		});
		mainView.addView(mapView);
	}
	
	private PopupWindow loadingPopup;
	
	private void initiateLoadingPopup() {
		LayoutInflater inflater = (LayoutInflater) ImageViewActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View LoadingLayout = inflater.inflate(R.layout.pupupwindow_loading, (ViewGroup) findViewById(R.id.loading_layout));
		
		loadingPopup = new PopupWindow(LoadingLayout,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
		
		loadingPopup.showAtLocation(LoadingLayout, Gravity.CENTER, 0, 0);
		
	}

	@Override
	public void onContentLoaded() {
		queuedContent --;
		
		if (queuedContent == 0)
		{
			loadingPopup.dismiss();
			
			LinearLayout mainView = (LinearLayout) findViewById(R.id.map_layout);
			mainView.setVisibility(View.VISIBLE);
		}
		
	}

}
