package com.example.advancedse.test;



import java.util.Date;

import com.example.advancedse.LocationData;
import com.example.advancedse.MainActivity;
import com.google.android.maps.MapView;

import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DigitalClock;

public class TestLocationData extends ActivityInstrumentationTestCase2<MainActivity> {

	public TestLocationData(String name) {
		
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
	public void testLocationSave(){
		Location location = new Location(LocationManager.GPS_PROVIDER);
		location.setLatitude(100.000000);
		location.setLongitude(200.000000);
		Date currentTime = new Date();
		LocationData.save(location, "testDevice", currentTime);
		Location loadedLocation = LocationData.load("testDevice", currentTime);
		assertEquals("Longitudes are not equal", location.getLongitude(), loadedLocation.getLongitude());
		assertEquals("Latitudes are not equal", location.getLatitude(), loadedLocation.getLatitude());
	}
	
	
	
	

}
