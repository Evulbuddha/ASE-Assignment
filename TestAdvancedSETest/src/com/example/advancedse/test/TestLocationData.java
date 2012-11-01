package com.example.advancedse.test;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import android.location.Location;
import android.location.LocationManager;

import com.example.advancedse.LocationData;

import junit.framework.TestCase;

public class TestLocationData extends TestCase {
	
	LocationData locationData;
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}
	@AfterClass
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testgetDB(){
		Location TEST_LOCATION = new Location(LocationManager.GPS_PROVIDER);
		Date TEST_TIME = new Date();
		String TEST_UUID = "testUUID";
		TEST_LOCATION.setLongitude(-122.084095);
		TEST_LOCATION.setLatitude(37.422006);
		TEST_LOCATION.setTime(TEST_TIME.getTime());
		LocationData.save(TEST_LOCATION, TEST_UUID);
		Location TEST_LOCATION_SAVED = LocationData.load(TEST_UUID, TEST_TIME);
		boolean result = TEST_LOCATION.equals(TEST_LOCATION_SAVED);
		assertTrue(result);
		
	}

}
