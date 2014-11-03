package com.cs.wwu.csvirtualtour;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.*;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class StopActivity extends Activity implements OnClickListener, OnTaskCompleted {

	private static final int MAIN_LAYOUT_ID = 5001;
	
	int stopID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_stop);
		if (getIntent().getExtras() != null){
			stopID = getIntent().getExtras().getInt("StopID");
		}
		else {
			stopID = Integer.parseInt(getIntent().getDataString().replace("CSVTour://", ""));
		}
		//Zero out Title Bar
		setTitle("");
		BuildStop();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stop, menu);
		return true;
	}
	
	private void BuildStop() {
		//Create a ScrollView To Hold Everything
		ScrollView mainView = new ScrollView(this);
		mainView.setLayoutParams(MainActivity.MAIN_LAYOUT_PARAMS);
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setLayoutParams(MainActivity.MAIN_LAYOUT_PARAMS);
		mainLayout.setId(MAIN_LAYOUT_ID);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		//Add The Map
		LinearLayout mapLayout = new LinearLayout(this);
		mapLayout.setLayoutParams(MainActivity.MAIN_LAYOUT_PARAMS);
		mapLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView mapView = new ImageView(this);
		mapView.setLayoutParams(MainActivity.SECOND_IMAGE_LAYOUT_PARAMS);
		mapView.setImageResource(R.drawable.cf420);
		mapView.setBackgroundColor(Color.CYAN);
		mapView.setAdjustViewBounds(true);;
		
//		//Sample preview retrieval
//		FFmpegMediaMetadataRetriever mr = new FFmpegMediaMetadataRetriever();
//		mr.setDataSource("http://strandburg.us/virtour/admin/media/rabbits are stupid.mp4");
//		mapView.setImageBitmap(mr.getFrameAtTime(500));
		//Add Rest of Stop Content
		StopRetrievalTask sr = new StopRetrievalTask(this);
		sr.execute(stopID);
		
		mapLayout.addView(mapView);
		mainLayout.addView(mapLayout);
		//mainLayout.addView(videoPreview);
		mainView.addView(mainLayout);
		
		setContentView(mainView,MainActivity.MAIN_LAYOUT_PARAMS);
		
		
	}
	
	private void BuildStopContent(JSONArray StopContent){
		
		for(int i = 0; i < StopContent.length(); i++)
		{
			try {
				JSONObject widget = StopContent.getJSONObject(i);
				String widgetType = widget.getString("type");
				
				if (widgetType.equals("text")) {
					
					AddTextWidget(widget);
				}
				else if (widgetType.equals("image")){
					AddImageWidget(widget);
				}
				else if (widgetType.equals("video")) {
					
					AddVideoWidget(widget);
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void AddTextWidget(JSONObject Widget) throws JSONException{
		String title, content = "";
		
		title = Widget.getString("title");
		content = Widget.getString("content");
		
		//Make the Title Text Underlined
		SpannableString underlinedTitle = new SpannableString(title);
		underlinedTitle.setSpan(new UnderlineSpan(), 0, title.length(), 0);
		
		//Title Text
		TextView textTitle = new TextView(this);
		textTitle.setLayoutParams(MainActivity.CONTENT_LAYOUT_PARAMS);
		textTitle.setTextSize(20);
		textTitle.setText(underlinedTitle);
		
		//COntent Text
		TextView textContent = new TextView(this);
		textContent.setLayoutParams(MainActivity.CONTENT_LAYOUT_PARAMS);
		textContent.setText(content);
		
		LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		
		MainLayout.addView(textTitle);
		MainLayout.addView(textContent);
		
	}
	
	private void AddImageWidget(JSONObject Widget) throws JSONException{

		String urlString = Widget.getString("url");
			
		ImageView imageContent = new ImageView(this);
		imageContent.setLayoutParams(MainActivity.IMAGE_LAYOUT_PARAMS);
		ImageRetrievalTask irt = new ImageRetrievalTask(imageContent);
		irt.execute(urlString);
		
		LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		MainLayout.addView(imageContent);
		
	}
	
	//TODO: This doesn't work
	private void AddVideoWidget(JSONObject Widget) throws JSONException {
		
		String urlString = Widget.getString("url");
		
		ImageView videoPreview = new ImageView(this);
		videoPreview.setLayoutParams(MainActivity.IMAGE_LAYOUT_PARAMS);
		
		ThumbnailRetrievalTask trt = new ThumbnailRetrievalTask(videoPreview);
		videoPreview.setOnClickListener(this);
		trt.execute(urlString);
		videoPreview.setContentDescription(urlString);
//		FFmpegMediaMetadataRetriever mr = new FFmpegMediaMetadataRetriever();
//		mr.setDataSource(urlString);
//		videoPreview.setImageBitmap(mr.getFrameAtTime(500));
		
//		trt.execute("http://www.ebookfrenzy.com/android_book/movie.mp4");
//		videoPreview.setContentDescription("http://www.ebookfrenzy.com/android_book/movie.mp4");
		
		LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		MainLayout.addView(videoPreview);
	}

	@Override
	public void onTaskCompleted(Stop[] s) {
		// TODO Auto-generated method stub
		Stop thisStop = s[0];
		
		JSONArray stopContent = null;
		try {
			stopContent = new JSONArray(thisStop.getStopContent());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle(thisStop.getStopName());
		BuildStopContent(stopContent);
		
		
		
	}

	@Override
	public void onClick(View v) {
		if (v instanceof ImageView)
		{
			Intent intent = new Intent(this, VideoPlayerActivity.class);
			intent.putExtra("url",v.getContentDescription());
		}
		
	}
	
	

}
