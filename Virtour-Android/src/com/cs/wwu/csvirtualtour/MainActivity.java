package com.cs.wwu.csvirtualtour;

import java.util.ArrayList;
import java.util.Arrays;

import com.cs.wwu.csvirtualtour.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.*;

public class MainActivity extends Activity implements OnClickListener, OnTaskCompleted, OnContentLoaded {

	public static final LinearLayout.LayoutParams MAIN_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT);
	public static final LinearLayout.LayoutParams CONTENT_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT);
	public static final TableLayout.LayoutParams BUTTON_LAYOUT_PARAMS = new TableLayout.LayoutParams(
			TableLayout.LayoutParams.MATCH_PARENT,
			TableLayout.LayoutParams.WRAP_CONTENT,
			1f);
	public static final TableLayout.LayoutParams IMAGE_LAYOUT_PARAMS = new TableLayout.LayoutParams(
			TableLayout.LayoutParams.WRAP_CONTENT,
			TableLayout.LayoutParams.MATCH_PARENT,
			3f);
	public static final TableLayout.LayoutParams SECOND_IMAGE_LAYOUT_PARAMS = new TableLayout.LayoutParams(
			TableLayout.LayoutParams.MATCH_PARENT,
			TableLayout.LayoutParams.WRAP_CONTENT);
	public static final FrameLayout.LayoutParams VIDEO_LAYOUT_PARAMS = new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.WRAP_CONTENT,
			FrameLayout.LayoutParams.WRAP_CONTENT);
	public static RelativeLayout.LayoutParams CENTERED_IMAGE_LAYOUT_PARAMS = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,
			RelativeLayout.LayoutParams.WRAP_CONTENT
			);
	
	//QR Code Button ID
	private static final int QR_READER_ID = 9890;
	
	//Layout IDs
	private static final int SCROLLING_LAYOUT_ID = 5001;
	private static final int MAP_IMAGE_ID = 5002;
	private static final int MAP_LAYOUT_ID = 5003;
	private static final int MAP_BUTTON_LAYOUT_ID = 5004;
	private static final int STOP_LIST_ID =5005;
	
	//Stop IDs
	private static final int MAIN_SCREEN_STOP_ID = -1;
	
	//Current Map ID
	int currentMapId = -1;
	
	int queuedContent = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		generateHome();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void generateHome(){

		//Create a Scrolling Layout
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setLayoutParams(MAIN_LAYOUT_PARAMS);
		mainLayout.setGravity(Gravity.CENTER);
		
		ScrollView mainView = new ScrollView(this);
		mainView.setLayoutParams(MAIN_LAYOUT_PARAMS);
		//mainView.setId(5001);
		
		LinearLayout scrollLayout = new LinearLayout(this);
		scrollLayout.setLayoutParams(CONTENT_LAYOUT_PARAMS);
		scrollLayout.setOrientation(LinearLayout.VERTICAL);
		scrollLayout.setId(SCROLLING_LAYOUT_ID);
		
		//Add map with floor buttons
		LinearLayout mapLayout = new LinearLayout(this);
		mapLayout.setLayoutParams(CONTENT_LAYOUT_PARAMS);
		mapLayout.setOrientation(LinearLayout.VERTICAL);
		mapLayout.setId(MAP_LAYOUT_ID);
		
		//Button Layout
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setLayoutParams(CONTENT_LAYOUT_PARAMS);
		buttonLayout.setOrientation(LinearLayout.VERTICAL);
		buttonLayout.setId(MAP_BUTTON_LAYOUT_ID);
		
		
//		ListView stopList = new ListView(this);
//		stopList.setLayoutParams(BUTTON_LAYOUT_PARAMS);
//		stopList.setId(STOP_LIST_ID);
		
		//Map 
		MapImageView mapView = new MapImageView(this);
		mapView.setLayoutParams(IMAGE_LAYOUT_PARAMS);
		mapView.setAdjustViewBounds(true);
		mapView.setId(MAP_IMAGE_ID);
		mapView.setOnClickListener(this);
		
//		//Welcome Message Static for now, but could be made to be pulled from web
//		TextView welcomeTitle = new TextView(this);
//		welcomeTitle.setLayoutParams(CONTENT_LAYOUT_PARAMS);
//		welcomeTitle.setText(R.string.welcome);
//		welcomeTitle.setTextSize(20);
		


				//Temporary QR scanning button
		Button b_scanner = new Button(this);
		b_scanner.setLayoutParams(BUTTON_LAYOUT_PARAMS);
		b_scanner.setText("QR Code");
		b_scanner.setOnClickListener(this);
		b_scanner.setId(QR_READER_ID);
		
		mapLayout.addView(mapView);
		//mapLayout.addView(buttonLayout);
		
		scrollLayout.addView(mapLayout);
		scrollLayout.addView(b_scanner);
		//scrollLayout.addView(welcomeTitle);
	//crollLayout.addView(welcomeMessage);
		scrollLayout.addView(buttonLayout);
		
		
		//Add Buttons to View 
		addStops();
		retrieveMaps();
		mainView.addView(scrollLayout);
		mainLayout.addView(mainView);
//		mainLayout.addView(stopList);
		setContentView(mainLayout,MAIN_LAYOUT_PARAMS);
		
		
//		mapView.post(new Runnable () {
//			@Override
//			public void run() {
//				ImageView mapView = (ImageView) findViewById(MAP_IMAGE_ID);
//				mapView.setImageBitmap(ImageProcessor.decodeSampledBitmapFromResource(getResources(), 
//						R.drawable.cf1_trace, mapView.getMeasuredWidth(), mapView.getMeasuredHeight()));
//			}
//		});
		mainLayout.post(new Runnable() {

			@Override
			public void run() {
				inititateIntroPopup();
				
			}
			
		});
		
	}
	
	private PopupWindow pw;
	
	private void inititateIntroPopup() {
		LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View layout = inflater.inflate(R.layout.popupwindow_stop_preview, (ViewGroup) findViewById(R.id.popup_layout));
		pw = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
		
		Button guide = (Button) layout.findViewById(R.id.btn_tour);
		
		guide.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),StopActivity.class);
				intent.putExtra("StopID",Globals.getStops()[0].getStopID());
				pw.dismiss();
				startActivity(intent);
				
			}
			
		});
		
		Button close = (Button) layout.findViewById(R.id.btn_close);
		
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();	
			}
			
		});
		
		pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}
	
	private void addStops(){
	
		StopRetrievalTask sr = new StopRetrievalTask(this);
		sr.execute(MAIN_SCREEN_STOP_ID);

	}
	
	private void retrieveMaps() {
		MapRetrievalTask mr = new MapRetrievalTask(this);
		mr.execute();
		
	}
	
//	@Override
//	protected void onStart()
//	{
//		LinearLayout mapLayout = (LinearLayout)findViewById(MAP_LAYOUT_ID);
//		ImageView mapView = new ImageView(this);
//		mapView.setLayoutParams(IMAGE_LAYOUT_PARAMS);
//		mapView.setImageBitmap(ImageProcessor.decodeSampledBitmapFromResource(getResources(), 
//				R.drawable.cf1_trace, mapLayout.getWidth(), mapLayout.getHeight()));
//		mapView.setAdjustViewBounds(true);
//		mapView.setId(MAP_IMAGE_ID);
//		mapLayout.addView(mapView);
//		super.onStart();
//	}
//	
	public void onClick(View v){
		
		int id = v.getId();
		//Toast t = Toast.makeText(this, "Button Clicked " + id , Toast.LENGTH_LONG);
		//t.show();
		
		//if QR Reader was clicked, open a new QR Scanner
		if (id == QR_READER_ID){
			Intent intent = new Intent(this,QRReaderActivitiy.class);
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		else if (id == MAP_IMAGE_ID) {
			ImageView map = (ImageView) findViewById(MAP_IMAGE_ID);
			Intent intent = new Intent(this,ImageViewActivity.class);
			intent.putExtra("values",map.getContentDescription());
			//intent.putExtra("imageUrl", value)
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(this,StopActivity.class);
			intent.putExtra("StopID", id);
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		
	}
	
	
	public void populateStops(int mapId){
		
		Stop[] stops = Globals.getStops();
		
		LinearLayout scrollLayout = (LinearLayout)findViewById(5001);
		LinearLayout buttonLayout = (LinearLayout) findViewById(MAP_BUTTON_LAYOUT_ID);
//		Log.d("Main","attempting to add stops");
		//build a button for each stop (set id so we can determine which stop?)
		
		//ListView stopList = (ListView) findViewById(STOP_LIST_ID);
		if (stops != null)
		{
			buttonLayout.removeAllViews();
			//ArrayList<String> sArrayList = new ArrayList<String>();
			Arrays.sort(stops);
			Globals.setStops(stops);
			for (Stop s : stops){
				if (s.getStopMapID() == mapId)
				{
					//sArrayList.add(s.getStopName());
					Button Temp = new Button(this);
					Temp.setLayoutParams(BUTTON_LAYOUT_PARAMS);
					Log.d("Main",s.getStopName());
					Temp.setText(s.getStopName());
					Temp.setId(s.getStopID());
					Temp.setOnClickListener(this);
					buttonLayout.addView(Temp);
				}
			}
			//ArrayAdapter<String> stopAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,sArrayList);
			//stopList.setAdapter(stopAdapter);
		}
		//scrollLayout.addView(buttonLayout);
	}
	
	public void onTaskCompleted(Stop[] stops)
	{
		if (stops != null)
		{
			Arrays.sort(stops);
			Globals.setStops(stops);
		}
	}
	
	public void onTaskCompleted(Map[] maps)
	{
		//Retrieve Views
		LinearLayout MapLayout = (LinearLayout) findViewById(MAP_LAYOUT_ID);
		//LinearLayout ButtonLayout = (LinearLayout) findViewById(MAP_BUTTON_LAYOUT_ID);
		//ImageView mapView = (ImageView) findViewById(MAP_IMAGE_ID);
		
		
		//Fill out Spinner
		Spinner mapSpinner = new Spinner(this);
		
		final MapSpinnerAdapter msa = new MapSpinnerAdapter(this, android.R.layout.simple_spinner_item, maps);
		
		mapSpinner.setAdapter(msa);
		
		mapSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override 
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				
				Map m = msa.getItem(position);
				MapImageView map = (MapImageView)findViewById(MAP_IMAGE_ID);
				map.setAlpha(.3f);
				ArrayList<Float> marks = new ArrayList<Float>();
				for (Stop s : Globals.getStops())
				{
					if (s.getStopMapID() == m.getMapId())
					{
						marks.add(s.getStopPositionX());
						marks.add(s.getStopPositionY());
					}
				}
				map.setMapMarks(marks);
				map.setContentDescription(m.getMapUrl() + "," + m.getMapId());
				ImageRetrievalTask irt = new ImageRetrievalTask(map,MainActivity.this);
				irt.execute(m.getMapUrl());
				populateStops(m.getMapId());
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});	
		MapLayout.addView(mapSpinner);
	}


	@Override
	public void onContentLoaded() {
		// TODO Auto-generated method stub
		
	}

}
