package com.example.advancedse.test;

import com.google.android.maps.MapActivity;

import android.test.ActivityInstrumentationTestCase2;

public class TestMainActivity extends ActivityInstrumentationTestCase2<MapActivity> {

	public TestMainActivity(String name, Class<MapActivity> activityClass) {
		super("com.example.advancedse", MapActivity.class);
	}
	
	protected void setUp() throws Exception {
 		super.setUp();
 	}
	
	public void testisRouteDisplayed(){
		
	}

}
