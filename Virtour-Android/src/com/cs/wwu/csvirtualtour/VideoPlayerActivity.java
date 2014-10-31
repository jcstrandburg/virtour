package com.cs.wwu.csvirtualtour;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Get Video File that we are supposed to play
		String url = getIntent().getExtras().getString("url");
		VideoView video = (VideoView)findViewById(R.id.video);
		video.setVideoPath(url);
		//Play it
		video.start();
	}

}
