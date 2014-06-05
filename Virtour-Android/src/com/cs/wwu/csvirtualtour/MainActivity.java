package com.cs.wwu.csvirtualtour;

import java.net.MalformedURLException;

import android.os.Bundle;
import android.app.Activity;
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
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnTaskCompleted {

	private static final LinearLayout.LayoutParams MAIN_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private static final LinearLayout.LayoutParams CONTENT_LAYOUT_PARAMS = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private static final TableLayout.LayoutParams BUTTON_LAYOUT_PARAMS = new TableLayout.LayoutParams(
			TableLayout.LayoutParams.MATCH_PARENT,
			TableLayout.LayoutParams.WRAP_CONTENT,
			1f);
	private static final TableLayout.LayoutParams IMAGE_LAYOUT_PARAMS = new TableLayout.LayoutParams(
			0,
			TableLayout.LayoutParams.MATCH_PARENT,
			3f);
	
	private static final int FIRST_FLOOR_ID = 9871;
	private static final int FOURTH_FLOOR_ID = 9874;
	
	
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
		scrollLayout.setId(5001);
		
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
		mapView.setImageResource(R.drawable.cf1);
		
		//Buttons for floors (These could maybe be made more dynamic later)
		Button b_floor1 = new Button(this);
		Button b_floor4 = new Button(this);
		
		b_floor1.setLayoutParams(BUTTON_LAYOUT_PARAMS);
		b_floor4.setLayoutParams(BUTTON_LAYOUT_PARAMS);
		
		b_floor1.setText("First \n Floor");
		b_floor4.setText("Fourth \n Floor");
		
		b_floor1.setId(FIRST_FLOOR_ID);
		b_floor4.setId(FOURTH_FLOOR_ID);
		
		b_floor1.setOnClickListener(this);
		b_floor4.setOnClickListener(this);
		//Put it all together
		buttonLayout.addView(b_floor1);
		buttonLayout.addView(b_floor4);
		
		mapLayout.addView(mapView);
		mapLayout.addView(buttonLayout);
		
		scrollLayout.addView(mapLayout);
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
		Toast t = Toast.makeText(this, "Button Clicked " + id , Toast.LENGTH_LONG);
		t.show();
		
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
