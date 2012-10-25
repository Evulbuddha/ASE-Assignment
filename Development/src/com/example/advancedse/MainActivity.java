package com.example.advancedse;

import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
//import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.advancedSE.MESSAGE";
	private LocationManager locationManager;
	private TextView gpsText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        gpsText = (TextView) findViewById(R.id.gpsLoc);
        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
        	enableLocationSettings();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, gpsUpdateListener);
    
        Thread timer = new Thread() {
            public void run () {
                for (;;) {
                    // do stuff in a separate thread
                	logGPSLocation.sendEmptyMessage(0);
                    try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}    // sleep for 3 seconds
                }
            }
        };
        timer.start();
    }
    
    private LocationData lastKnownLoc;
    private LocationListener gpsUpdateListener = new LocationListener(){
    	public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
    		lastKnownLoc = new LocationData();
    		lastKnownLoc.setLongitude(location.getLongitude());
    		lastKnownLoc.setLatitude(location.getLatitude());
    		lastKnownLoc.setTimestamp(new Date(location.getTime()));
    		gpsText.setText("Latitude:"+ lastKnownLoc.getLatitude() + "\nLongitude:" + lastKnownLoc.getLongitude());
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    
    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private Handler logGPSLocation = new Handler () {
        public void handleMessage (Message msg) {
            if(lastKnownLoc != null){
            	System.out.println("starting save");
            	lastKnownLoc.save();
            	
            }
        }
    };
}
