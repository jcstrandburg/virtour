package com.cs.wwu.csvirtualtour;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ScrollView;

public class StopActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_stop);
		
		//Create a ScrollView To Hold Everything
		ScrollView mainView = new ScrollView(this);
		//Add The Map
		
		//Add Rest of Stop Content
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stop, menu);
		return true;
	}
	
	

}
