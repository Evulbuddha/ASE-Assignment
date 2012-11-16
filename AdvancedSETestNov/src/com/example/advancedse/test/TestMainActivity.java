package com.example.advancedse.test;

import java.util.Calendar;

import com.example.advancedse.MainActivity;
import com.google.android.maps.MapView;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.format.Time;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DigitalClock;

public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {

	public TestMainActivity(String name) {
		
		super("com.example.advancedse", MainActivity.class);
		setName(name);
	}
	
	MainActivity activity;
	AnalogClock analogClock;
	DigitalClock digitalClock;
	MapView mapView;
	Button gotoMapButton;
	
	protected void setUp() throws Exception {
 		super.setUp();
 		activity = (MainActivity) getActivity();
 		analogClock = (AnalogClock) activity.findViewById(com.example.advancedse.R.id.analogClock1);
 		digitalClock = (DigitalClock) activity.findViewById(com.example.advancedse.R.id.digitalClock1);
 		mapView = (MapView) activity.findViewById(com.example.advancedse.R.id.mapView);
 		gotoMapButton = (Button) activity.findViewById(com.example.advancedse.R.id.goMapButton);
 	}
	
	@SmallTest
	public void testAreViewsCreated(){
		assertNotNull("Activity is null", activity);
		assertNotNull("Analog Clock is null", analogClock);
		assertNotNull("Digital Clock is null", digitalClock);
		assertNotNull("gotoMapButton is null", gotoMapButton);
	}
	
	@SmallTest
	public void testAreViewsOnScreen(){
		ViewAsserts.assertOnScreen(analogClock.getRootView(), analogClock);
		ViewAsserts.assertOnScreen(digitalClock.getRootView(), digitalClock);
		ViewAsserts.assertOnScreen(gotoMapButton.getRootView(), gotoMapButton);
	}
	
	

}
