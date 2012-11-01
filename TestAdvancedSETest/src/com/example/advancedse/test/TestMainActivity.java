package com.example.advancedse.test;

import com.example.advancedse.MainActivity;
import com.google.android.maps.MapActivity;

import android.test.ActivityInstrumentationTestCase2;

public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {

	public TestMainActivity(String name, Class<MainActivity> activityClass) {
		super("com.example.advancedse", MainActivity.class);
	}
	
	protected void setUp() throws Exception {
 		super.setUp();
 	}
	
	public void testisRouteDisplayed(){
		MainActivity main = super.getActivity();
		//main.
		//assert
	}

}
