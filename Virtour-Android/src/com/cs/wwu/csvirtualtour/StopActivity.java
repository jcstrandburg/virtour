package com.cs.wwu.csvirtualtour;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

public class StopActivity extends Activity implements OnClickListener, OnTaskCompleted, OnContentLoaded {

	private static final int MAIN_LAYOUT_ID = R.id.layout_stop;
	private static final int MAP_IMAGE_ID = 5006;
	private static final int BTN_NEXT_ID = 4999;
	
	int stopID;
	int queuedContent = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stop);
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
		//ScrollView mainView = (ScrollView) findViewById(R.id.scroll_stop);
		
		LinearLayout mainLayout = (LinearLayout) findViewById(MAIN_LAYOUT_ID);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setVisibility(View.INVISIBLE);
		
		//Retrive the desired stop
		StopRetrievalTask sr = new StopRetrievalTask(this);
		sr.execute(stopID);
		
		
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
					this.queuedContent ++;
				}
				else if (widgetType.equals("video")) {
					
					AddVideoWidget(widget);
					this.queuedContent ++;
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		addNextButton();
			}
	
	private void addNextButton()
	{
		Button next = new Button(this);
		next.setLayoutParams(MainActivity.CONTENT_LAYOUT_PARAMS);
		next.setId(BTN_NEXT_ID);
		next.setText("Next Stop");
		next.setGravity(Gravity.CENTER_HORIZONTAL);
		
		next.setOnClickListener(this);
		
		LinearLayout mainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		mainLayout.addView(next);
	}
	private void AddTextWidget(JSONObject Widget) throws JSONException{
		String titleString, content = "";
		
		titleString = Widget.getString("title");
		content = Widget.getString("content");
		
		
		//Title Text
		TextView textTitle = GenerateTitle(titleString);
		
		//COntent Text
		TextView textContent = new TextView(this);
		textContent.setLayoutParams(MainActivity.CONTENT_LAYOUT_PARAMS);
		textContent.setText("\t" + content);
		textContent.setGravity(Gravity.LEFT);
		
		LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		
		//Add content to screen
		MainLayout.addView(textTitle);
		MainLayout.addView(textContent);
		
	}
	
	private void AddImageWidget(JSONObject Widget) throws JSONException{
		
		//Retrieve values
		String urlString = Widget.getString("url");
		String titleString = Widget.getString("title");
		
		//Generate Content
		TextView textTitle = GenerateTitle(titleString);
		ImageView imageContent = new ImageView(this);
		imageContent.setLayoutParams(MainActivity.SECOND_IMAGE_LAYOUT_PARAMS);
		imageContent.setAdjustViewBounds(true);
		ImageRetrievalTask irt = new ImageRetrievalTask(imageContent,this);
		irt.execute(urlString);
		imageContent.setImageResource(R.drawable.placeholder);
		//Add content to screen
		LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		MainLayout.addView(textTitle);
		MainLayout.addView(imageContent);
		
	}
	
	private void AddVideoWidget(JSONObject Widget) throws JSONException {
		
		//Retrieve Values
		String urlString = Widget.getString("url");
		String titleString = Widget.getString("title");
		
		//Encapsulate image in a Relative Layout, this allows us to superimpose the play button on top
		RelativeLayout videoLayout = new RelativeLayout(this);
		videoLayout.setLayoutParams(MainActivity.MAIN_LAYOUT_PARAMS);
		
		ImageView videoPreview = new ImageView(this);
		videoPreview.setLayoutParams(MainActivity.IMAGE_LAYOUT_PARAMS);
		videoPreview.setImageResource(R.drawable.placeholder);
		videoPreview.setAdjustViewBounds(true);
		
		//Create a title for the view preview
		TextView textTitle = GenerateTitle(titleString);

		//Retrieve a thumbnail and set it to the image preview
		ThumbnailRetrievalTask trt = new ThumbnailRetrievalTask(videoPreview,this);
		videoPreview.setOnClickListener(this);
		trt.execute(urlString);
		videoPreview.setContentDescription(urlString);
		videoLayout.addView(videoPreview);
		
		//Add the play button to the center of the image
		ImageView playView = new ImageView(this);
		playView.setImageResource(R.drawable.play);
		playView.setLayoutParams(MainActivity.CENTERED_IMAGE_LAYOUT_PARAMS);
		RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) playView.getLayoutParams();
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		videoLayout.addView(playView);
		
		//Add content to screen
		LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		MainLayout.addView(textTitle);
		MainLayout.addView(videoLayout);
	}
	
	private TextView GenerateTitle(String titleText) {
		
		//Make the Title Text Underlined
		SpannableString underlinedTitle = new SpannableString(titleText);
		underlinedTitle.setSpan(new UnderlineSpan(), 0, titleText.length(), 0);
		
		//Title Text
		TextView textTitle = new TextView(this);
		LinearLayout.LayoutParams titleparams = MainActivity.CONTENT_LAYOUT_PARAMS;
		titleparams.gravity = Gravity.CENTER;
		textTitle.setLayoutParams(titleparams);
		textTitle.setTextSize(20);
		textTitle.setText(underlinedTitle);
		
		return textTitle;
	}
	
	@SuppressLint("NewApi")
	private FrameLayout GenerateMarkedMap(float markx, float marky, int MapId)
	{
		ArrayList<Float> marks = new ArrayList<Float>();
		marks.add(markx);
		marks.add(marky);
		FrameLayout mapLayout = new FrameLayout(this);
		mapLayout.setLayoutParams(MainActivity.MAIN_LAYOUT_PARAMS);
		//Put map in layout
		MapImageView mapView = new MapImageView(this);
		mapView.setMapMarks(marks);
		mapView.setLayoutParams(MainActivity.SECOND_IMAGE_LAYOUT_PARAMS);
		mapView.setId(MAP_IMAGE_ID);
		mapView.setImageResource(R.drawable.placeholder);
		mapView.setVisibility(View.INVISIBLE);
		
		Map map = null;
		
		for (Map m : Globals.getMaps())
		{
			if (m.getMapId() == MapId)
			{
				map = m;
				break;
			}
		}
		
		final Map thisMap = map;
		
		mapView.post(new Runnable(){

			@Override
			public void run() {
				MapImageView map = (MapImageView) findViewById(MAP_IMAGE_ID);
				ImageRetrievalTask irt = new ImageRetrievalTask(map,StopActivity.this);
				irt.execute(thisMap.getMapUrl());
				queuedContent++;
				
			}
			
		});
		mapView.setAdjustViewBounds(true);
		mapLayout.addView(mapView);
		
		return mapLayout;
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
		
		LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		MainLayout.addView(GenerateMarkedMap(thisStop.getStopPositionX(),thisStop.getStopPositionY(),thisStop.getStopMapID()));
		setTitle(thisStop.getStopName());
		BuildStopContent(stopContent);
		
		
		
	}

	@Override
	public void onClick(View v) {
		if (v instanceof ImageView)
		{
			Intent intent = new Intent(this, VideoPlayerActivity.class);
			intent.putExtra("url",v.getContentDescription());
			//Coment
			startActivity(intent);
			
		}
		if (v.getId() == BTN_NEXT_ID)
		{
			int next = getNextStop();
			
			if (next != -1)
			{
				Intent intent = new Intent(this,StopActivity.class);
				intent.putExtra("StopID", next);
				startActivity(intent);
			}
			else
			{
				Toast t = Toast.makeText(this, "No more Stops", Toast.LENGTH_LONG);
				t.show();
			}
		}
	}
	
	private int getNextStop() {
		
		Stop[] stops = Globals.getStops();
		
		for (int i = 0; i < stops.length - 1; i++)
		{
			if (stops[i].getStopID() == stopID)
			{
				return stops[i+1].getStopID();
			}
		}
		
		return -1;
	}

	@Override
	public void onTaskCompleted(Map[] m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContentLoaded() {
		this.queuedContent --;
		
		if (queuedContent == 0)
		{
			LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
			MainLayout.setVisibility(View.VISIBLE);
		}
		
	}
	
	

}
