package com.example.advancedse;

import java.text.SimpleDateFormat;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.Dialog;
//import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends MapActivity {

	public final static String EXTRA_MESSAGE = "com.example.advancedSE.MESSAGE";
	private LocationManager locationManager;
	private MapView mapV;
	private Map map;
	
	private String email;
	
	//toast handlers can be called from within other threads
	Handler toastHandler = new Handler();
	Runnable loggedLocationToastRunnable = new Runnable() {public void run() {Toast.makeText(MainActivity.this, "Logged location to cloud", Toast.LENGTH_SHORT).show();}};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.map_screen);
    
        //get email address user logged in with
        Intent i = getIntent();
        email = i.getStringExtra("email");
        
        Toast.makeText(getApplicationContext(),
                "Logged in as " + email, Toast.LENGTH_LONG).show();
        
        setupLocation();
        
        setupButtons();
        mapV = (MapView) findViewById(R.id.mapView);
        map = new Map(mapV, this);
        KnownLocations.setMap(map);
		mapV.setBuiltInZoomControls(true);
		mapV.setClickable(true);
    }
    
    private void setupButtons() {
		mapV = (MapView) findViewById(R.id.mapView);
		mapV.setBuiltInZoomControls(true);
		
		Button backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    			if(lastKnownLoc != null){//check we actually have a location
		    		Intent i = new Intent(getApplicationContext(), CheckInActivity.class);
		    		
		    		i.putExtra("long", lastKnownLoc.getLongitude() + "");
		    		i.putExtra("lat", lastKnownLoc.getLatitude() + "");
		    		i.putExtra("email", email);
		    		startActivity(i);
    			}
    		}});

	}

	private void setupLocation() {
    	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //gpsText = (TextView) findViewById(R.id.gpsLoc);
        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
        	//Dialog dialog = new Dialog(this.getBaseContext());
        	//dialog.setContentView(R.layout.enable_gps);
        	//dialog.show();
        	//enableLocationSettings();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, gpsUpdateListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, gpsUpdateListener);
        
        Thread timer = new Thread() {
            public void run () {
                for (;;) {
                    // do stuff in a separate thread
                	logGPSLocation.sendEmptyMessage(0);
                    try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}    // sleep for 30 seconds
                }
            }
        };
        timer.start();
		
	}

	private Location lastKnownLoc;
    private LocationListener gpsUpdateListener = new LocationListener(){
    	public void onLocationChanged(Location location) {
            // A new location update is received.
    		if(lastKnownLoc == null){//if there is no prev location, use this
    			lastKnownLoc = location;
    		}else{
    			if(lastKnownLoc.hasAccuracy() && location.hasAccuracy()){//if both the current location and the new one have an accuracy
    				if(lastKnownLoc.getAccuracy() <= location.getAccuracy()){//use the one that is more accurate
        				lastKnownLoc = location;
        			}
    			}
    		}
    		map.addOverlay(location);
    		//gpsText.setText("Latitude:"+ lastKnownLoc.getLatitude() + "\nLongitude:" + lastKnownLoc.getLongitude());
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
            	new LogLocationTask().execute(lastKnownLoc);
            }
        }
    };
	
	private class LogLocationTask extends AsyncTask<Location, Void, Void> {

		protected Void doInBackground(Location... locations) {
			LocationData.save(locations[0], email);
			toastHandler.post(loggedLocationToastRunnable);
			return null;
		}

		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
