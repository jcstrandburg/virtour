package com.cs.wwu.csvirtualtour;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

public class StopActivity extends Activity implements OnClickListener, OnTaskCompleted, OnContentLoaded, OnTouchListener {

	private static final int MAIN_LAYOUT_ID = R.id.layout_stop;
	private static final int MAP_IMAGE_ID = 5006;
	private static final int BTN_NEXT_ID = 4999;
	private static final int BTN_PREVIOUS_ID = 4998;
	
	int stopID;
	int mapId;
	int queuedContent = 0;
	
	private GestureDetector gestureDetector;
	//View.OnTouchListener gestureListener;
	
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
		
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stop, menu);
		return true;
	}
	
	private void BuildStop() {
		//Create a ScrollView To Hold Everything
		ScrollView mainView = (ScrollView) findViewById(R.id.scroll_stop);
		
		LinearLayout mainLayout = (LinearLayout) findViewById(MAIN_LAYOUT_ID);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setVisibility(View.INVISIBLE);
		mainView.setOnTouchListener(this);
		
		//Retrive the desired stop
		StopRetrievalTask sr = new StopRetrievalTask(this);
		sr.execute(stopID);
		
		mainLayout.post(new Runnable() {

			@Override
			public void run() {
				initiateLoadingPopup();
				
			}
		});
		
		
	}
	
	private PopupWindow loadingPopup;
	
	private void initiateLoadingPopup() {
		LayoutInflater inflater = (LayoutInflater) StopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View LoadingLayout = inflater.inflate(R.layout.pupupwindow_loading, (ViewGroup) findViewById(R.id.loading_layout));
		
		loadingPopup = new PopupWindow(LoadingLayout,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
		
		loadingPopup.showAtLocation(LoadingLayout, Gravity.CENTER, 0, 0);
		
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
		addNavigationButtons();
			}
	
	private void addNavigationButtons()
	{
		LinearLayout navigationLayout = new LinearLayout(this);
		navigationLayout.setLayoutParams(MainActivity.CONTENT_LAYOUT_PARAMS);
		navigationLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		Button next = new Button(this);
		android.widget.TableLayout.LayoutParams params = MainActivity.BUTTON_LAYOUT_PARAMS;
		//params.gravity = Gravity.RIGHT;
		next.setLayoutParams(params);
		next.setId(BTN_NEXT_ID);
		next.setText("Next Stop");;
		
		next.setOnClickListener(this);
		
		Button previous = new Button(this);
		//params.gravity = Gravity.LEFT;
		previous.setLayoutParams(params);
		previous.setId(BTN_PREVIOUS_ID);
		previous.setText("Previous Stop");
		
		previous.setOnClickListener(this);
		
		navigationLayout.addView(previous);
		navigationLayout.addView(next);
		
		LinearLayout mainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
		mainLayout.addView(navigationLayout);
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
		
		videoLayout.setOnTouchListener(this);
		
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
		
		Stop[] stops = Globals.getStops();
		
		for (Stop s : stops)
		{
			if (s.getStopMapID() == MapId)
			{
				marks.add(s.getStopPositionX());
				marks.add(s.getStopPositionY());
			}
		}
		
		FrameLayout mapLayout = new FrameLayout(this);
		mapLayout.setLayoutParams(MainActivity.MAIN_LAYOUT_PARAMS);
		//Put map in layout
		MapImageView mapView = new MapImageView(this);
		mapView.setMapMarks(marks);
		mapView.setLayoutParams(MainActivity.SECOND_IMAGE_LAYOUT_PARAMS);
		mapView.setId(MAP_IMAGE_ID);
		mapView.setImageResource(R.drawable.placeholder);
		mapView.setVisibility(View.INVISIBLE);
		mapView.setSelectedMark(markx, marky);
		mapView.setOnClickListener(this);
		
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
		mapView.setContentDescription(thisMap.getMapUrl() + "," + thisMap.getMapId());
		
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
		this.mapId = thisStop.getStopMapID();
		
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
		
		if (v.getId() == MAP_IMAGE_ID)
		{
			ImageView mapView = (ImageView) findViewById(MAP_IMAGE_ID);
	     	Intent intent = new Intent(this, ImageViewActivity.class);
			intent.putExtra("values",mapView.getContentDescription());
			startActivity(intent);
		}
		
		else if (v instanceof ImageView)
		{
			Intent intent = new Intent(this, VideoPlayerActivity.class);
			intent.putExtra("url",v.getContentDescription());
			//Coment
			startActivity(intent);
			
		}
		else if (v.getId() == BTN_NEXT_ID)
		{
			int next = getNextStop();
			
			if (next != -1)
			{
				Intent intent = new Intent(this,StopActivity.class);
				intent.putExtra("StopID", next);
				intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
			}
			else
			{
				Toast t = Toast.makeText(this, "No more Stops", Toast.LENGTH_LONG);
				t.show();
			}
		}
		else if (v.getId() == BTN_PREVIOUS_ID)
		{
			int prev = getPreviousStop();
			
			if (prev != -1)
			{
				Intent intent = new Intent(this,StopActivity.class);
				intent.putExtra("StopID", prev);
				intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
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
	
	private int getPreviousStop() {
		
		Stop[] stops = Globals.getStops();
		
		for (int i = 1; i < stops.length; i++)
		{
			if (stops[i].getStopID() == stopID)
			{
				return stops[i-1].getStopID();
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
			loadingPopup.dismiss();
			LinearLayout MainLayout = (LinearLayout)findViewById(MAIN_LAYOUT_ID);
			MainLayout.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		
		Intent intent = new Intent(this,MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("firstrun", false);
		startActivity(intent);
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
		
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	        try {
	            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                return false;
	            // right to left swipe
	            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                //Toast.makeText(StopActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
	                
	    			int prev = getPreviousStop();
	    			
	    			if (prev != -1)
	    			{
	    				Intent intent = new Intent(StopActivity.this,StopActivity.class);
	    				intent.putExtra("StopID", prev);
	    				//intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
	    				startActivity(intent);
	    			}
	    			else
	    			{
	    				Toast t = Toast.makeText(StopActivity.this, "No more Stops", Toast.LENGTH_LONG);
	    				t.show();
	    			}			
	                
	            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                //Toast.makeText(StopActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
	    			int next = getNextStop();
	    			
	    			if (next != -1)
	    			{
	    				Intent intent = new Intent(StopActivity.this,StopActivity.class);
	    				intent.putExtra("StopID", next);
	    				//intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
	    				startActivity(intent);
	    			}
	    			else
	    			{
	    				Toast t = Toast.makeText(StopActivity.this, "No more Stops", Toast.LENGTH_LONG);
	    				t.show();
	    			}
	            }
	        } catch (Exception e) {
	            // nothing
	        }
	        return true;
	    }
	    
	    @Override
	    public boolean onSingleTapConfirmed(MotionEvent e) {
	    	
	    	Toast.makeText(StopActivity.this, "Single Tap", Toast.LENGTH_LONG).show();
	    	return true;
	    }
	    
	    @Override
	    public boolean onDown(MotionEvent e) {
	    	return true;
	    }
	}

}



