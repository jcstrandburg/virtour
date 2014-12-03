package com.cs.wwu.csvirtualtour;

import java.util.ArrayList;
import java.util.Arrays;

import com.cs.wwu.csvirtualtour.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MainActivity extends Activity implements OnClickListener, OnTaskCompleted {

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
	
	//Floor Button IDs
	private static final int FIRST_FLOOR_ID = 9871;
	private static final int FOURTH_FLOOR_ID = 9874;
	
	//QR Code Button ID
	private static final int QR_READER_ID = 9890;
	
	//Layout IDs
	private static final int SCROLLING_LAYOUT_ID = 5001;
	private static final int MAP_IMAGE_ID = 5002;
	private static final int MAP_LAYOUT_ID = 5003;
	private static final int MAP_BUTTON_LAYOUT_ID = 5004;
	
	//Stop IDs
	private static final int MAIN_SCREEN_STOP_ID = -1;
	
	//Current Map ID
	int currentMapId = -1;
	
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
		
		//Map (Maybe pull image from web later)
		MapImageView mapView = new MapImageView(this);
		mapView.setLayoutParams(IMAGE_LAYOUT_PARAMS);
		mapView.setAdjustViewBounds(true);
		mapView.setId(MAP_IMAGE_ID);
		mapView.setOnClickListener(this);
		
//		//Buttons for floors (These could maybe be made more dynamic later)
//		Button b_floor1 = new Button(this);
//		Button b_floor4 = new Button(this);
//		
//		b_floor1.setLayoutParams(BUTTON_LAYOUT_PARAMS);
//		b_floor4.setLayoutParams(BUTTON_LAYOUT_PARAMS);
//		
//		b_floor1.setText(R.string.floor1);
//		b_floor4.setText(R.string.floor4);
//		
//		b_floor1.setId(FIRST_FLOOR_ID);
//		b_floor4.setId(FOURTH_FLOOR_ID);
//		
//		b_floor1.setOnClickListener(this);
//		b_floor4.setOnClickListener(this);
		
		//Welcome Message Static for now, but could be made to be pulled from web
		TextView welcomeTitle = new TextView(this);
		welcomeTitle.setLayoutParams(CONTENT_LAYOUT_PARAMS);
		welcomeTitle.setText(R.string.welcome);
		welcomeTitle.setTextSize(20);
		
		TextView welcomeMessage = new TextView(this);
		welcomeMessage.setLayoutParams(CONTENT_LAYOUT_PARAMS);
		welcomeMessage.setText(R.string.welcome_para);
		
		//Temporary QR scanning button
		Button b_scanner = new Button(this);
		b_scanner.setLayoutParams(BUTTON_LAYOUT_PARAMS);
		b_scanner.setText("QR Code");
		b_scanner.setOnClickListener(this);
		b_scanner.setId(QR_READER_ID);
		
		
//		//Put it all together
//		buttonLayout.addView(b_floor1);
//		buttonLayout.addView(b_floor4);

		mapLayout.addView(mapView);
		mapLayout.addView(buttonLayout);
		
		scrollLayout.addView(mapLayout);
		scrollLayout.addView(b_scanner);
		scrollLayout.addView(welcomeTitle);
		scrollLayout.addView(welcomeMessage);
		//scrollLayout.addView(mapLayout);
		//Add Buttons to View 
		addStops();
		retrieveMaps();
		mainView.addView(scrollLayout);
		mainLayout.addView(mainView);
		setContentView(mainLayout,MAIN_LAYOUT_PARAMS);
		
//		mapView.post(new Runnable () {
//			@Override
//			public void run() {
//				ImageView mapView = (ImageView) findViewById(MAP_IMAGE_ID);
//				mapView.setImageBitmap(ImageProcessor.decodeSampledBitmapFromResource(getResources(), 
//						R.drawable.cf1_trace, mapView.getMeasuredWidth(), mapView.getMeasuredHeight()));
//			}
//		});
		
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
		if (id == MAP_IMAGE_ID) {
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
			startActivity(intent);
		}
		
	}
	
	
	public void onTaskCompleted(Stop[] stops){
		
		LinearLayout scrollLayout = (LinearLayout)findViewById(5001);
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setOrientation(LinearLayout.VERTICAL);
		buttonLayout.setLayoutParams(CONTENT_LAYOUT_PARAMS);
		Log.d("Main","attempting to add stops");
		//build a button for each stop (set id so we can determine which stop?)
		if (stops != null)
		{
			Arrays.sort(stops);
			Globals.setStops(stops);
			//mapCoordinates = new float[stops.length * 2];
			int i = 0;
			for (Stop s : stops){
				Button Temp = new Button(this);
				Temp.setLayoutParams(BUTTON_LAYOUT_PARAMS);
				Log.d("Main",s.getStopName());
				Temp.setText(s.getStopName());
				Temp.setId(s.getStopID());
				Temp.setOnClickListener(this);
				buttonLayout.addView(Temp);
				//mapCoordinates[i] = s.getStopPositionX();
				//mapCoordinates[i+1] = s.getStopPositionY();
				i = i + 2;
			}
		}
		scrollLayout.addView(buttonLayout);
	}
	
	public void onTaskCompleted(Map[] maps)
	{
		//Retrieve Views
		LinearLayout MapLayout = (LinearLayout) findViewById(MAP_LAYOUT_ID);
		LinearLayout ButtonLayout = (LinearLayout) findViewById(MAP_BUTTON_LAYOUT_ID);
		ImageView mapView = (ImageView) findViewById(MAP_IMAGE_ID);
		
		
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
				ImageRetrievalTask irt = new ImageRetrievalTask(map);
				irt.execute(m.getMapUrl());
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});	
		ButtonLayout.addView(mapSpinner);
	}

}
