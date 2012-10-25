package com.example.advancedse.test;

import com.example.advancedse.MainActivity;
import com.example.advancedse.R;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class GPSTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private TextView gpsLocationField;
	
	public GPSTest(String name) {
		super("com.example.advancedse", MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testGPS(){
		MainActivity mainActivity = getActivity();  
	    gpsLocationField = (TextView) mainActivity.findViewById(R.id.gpsLoc);
	    
	}

}
