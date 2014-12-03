package com.cs.wwu.csvirtualtour;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		
		//Make View FullSreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_video);
		FrameLayout background = (FrameLayout) findViewById(R.id.video_frame);
		background.setBackgroundColor(Color.BLACK);
		
		//Get Video File that we are supposed to play
		String url = getIntent().getExtras().getString("url");
		VideoView video = (VideoView)findViewById(R.id.video);
		video.setVideoURI(Uri.parse(url));
		//Play it
		if (savedInstanceState != null)
		{
			int videoPosition = savedInstanceState.getInt("pos");
			video.seekTo(videoPosition);
		}
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
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		
		VideoView video = (VideoView) findViewById(R.id.video);
		outState.putInt("pos",video.getCurrentPosition());
	}
	

}
