package com.cs.wwu.csvirtualtour;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_video);
		//Get Video File that we are supposed to play
		String url = getIntent().getExtras().getString("url");
		VideoView video = (VideoView)findViewById(R.id.video);
		video.setVideoURI(Uri.parse(url));
		//Play it
		video.start();
	}
	
	@Override
	protected void onStart()
	{
		VideoView video = (VideoView)findViewById(R.id.video);
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(video);
		video.setMediaController(mediaController);
		super.onStart();
	}
	

}
