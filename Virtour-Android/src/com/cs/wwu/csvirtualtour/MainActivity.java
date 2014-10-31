package com.cs.wwu.csvirtualtour;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class MainActivity extends Activity implements OnClickListener, OnTaskCompleted {

	public static final LinearLayout.LayoutParams MAIN_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	public static final LinearLayout.LayoutParams CONTENT_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
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
			TableLayout.LayoutParams.MATCH_PARENT);
	
	//Floor Button IDs
	private static final int FIRST_FLOOR_ID = 9871;
	private static final int FOURTH_FLOOR_ID = 9874;
	
	//QR Code Button ID
	private static final int QR_READER_ID = 9890;
	
	//Layout IDs
	private static final int SCROLLING_LAYOUT_ID = 5001;
	private static final int MAP_IMAGE_ID = 5002;
	
	ImageProcessor ip;
	
	
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
		mapLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		//Button Layout
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setLayoutParams(CONTENT_LAYOUT_PARAMS);
		buttonLayout.setOrientation(LinearLayout.VERTICAL);
		
		//Map (Maybe pull image from web later)
		ImageView mapView = new ImageView(this);
		mapView.setLayoutParams(IMAGE_LAYOUT_PARAMS);
		mapView.setImageResource(R.drawable.cf1_trace);
		mapView.setAdjustViewBounds(true);
		mapView.setId(MAP_IMAGE_ID);
		
		//Buttons for floors (These could maybe be made more dynamic later)
		Button b_floor1 = new Button(this);
		Button b_floor4 = new Button(this);
		
		b_floor1.setLayoutParams(BUTTON_LAYOUT_PARAMS);
		b_floor4.setLayoutParams(BUTTON_LAYOUT_PARAMS);
		
		b_floor1.setText(R.string.floor1);
		b_floor4.setText(R.string.floor4);
		
		b_floor1.setId(FIRST_FLOOR_ID);
		b_floor4.setId(FOURTH_FLOOR_ID);
		
		b_floor1.setOnClickListener(this);
		b_floor4.setOnClickListener(this);
		
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
		
		
		//Put it all together
		buttonLayout.addView(b_floor1);
		buttonLayout.addView(b_floor4);
		
		mapLayout.addView(mapView);
		mapLayout.addView(buttonLayout);
		
		scrollLayout.addView(mapLayout);
		scrollLayout.addView(welcomeTitle);
		scrollLayout.addView(welcomeMessage);
		scrollLayout.addView(b_scanner);
		//scrollLayout.addView(mapLayout);
		//Add Buttons to View 
		addStops();
		mainView.addView(scrollLayout);
		mainLayout.addView(mainView);
		setContentView(mainLayout,MAIN_LAYOUT_PARAMS);
		
	}
	
	private void addStops(){
		
		//Get stop 0
		//try {
			StopRetrievalTask sr = new StopRetrievalTask(this);
			sr.execute(0);
			
			//build a button for each stop (set id so we can determine which stop?)
			//for (Stop s : stops){
				//Button Temp = new Button(this);
				//Temp.setLayoutParams(BUTTON_LAYOUT_PARAMS);
				//Temp.setText(s.getStopName());
				//Temp.setId(s.getStopID());
				//v.addView(Temp);
				
			//}

	}
	
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
		else {
			//If a map button was clicked, change the map, 
			//otherwise open a stop activity with the specified stop
			if (id == FIRST_FLOOR_ID){
				ImageView map = (ImageView)findViewById(MAP_IMAGE_ID);
				//((BitmapDrawable)map.getDrawable()).getBitmap().recycle();
				map.setImageBitmap(ImageProcessor.decodeSampledBitmapFromResource(getResources(), 
						R.drawable.cf1_trace, map.getWidth(), map.getHeight()));
			}
			else if (id == FOURTH_FLOOR_ID){
				ImageView map = (ImageView)findViewById(MAP_IMAGE_ID);
				((BitmapDrawable)map.getDrawable()).getBitmap().recycle();
				map.setImageBitmap(ImageProcessor.decodeSampledBitmapFromResource(getResources(), 
						R.drawable.cf4_trace, map.getWidth(), map.getHeight()));
				
			}
			else{
				Intent intent = new Intent(this,StopActivity.class);
				intent.putExtra("StopID", id);
				startActivity(intent);
			}
		}
		
	}
	
	
	public void onTaskCompleted(Stop[] stops){
		
		LinearLayout scrollLayout = (LinearLayout)findViewById(5001);
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setOrientation(LinearLayout.VERTICAL);
		buttonLayout.setLayoutParams(CONTENT_LAYOUT_PARAMS);;
		Log.d("Main","attempting to add stops");
		//build a button for each stop (set id so we can determine which stop?)
		for (Stop s : stops){
			Button Temp = new Button(this);
			Temp.setLayoutParams(BUTTON_LAYOUT_PARAMS);
			Log.d("Main",s.getStopName());
			Temp.setText(s.getStopName());
			Temp.setId(s.getStopID());
			Temp.setOnClickListener(this);
			buttonLayout.addView(Temp);
		}
		scrollLayout.addView(buttonLayout);
	}
	
	public void openStop(int id){
		
	}

}
