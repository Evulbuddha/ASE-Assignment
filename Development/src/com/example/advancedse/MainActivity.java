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
//import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends MapActivity {

	public final static String EXTRA_MESSAGE = "com.example.advancedSE.MESSAGE";
	private LocationManager locationManager;
	//private TextView gpsText;
	//private DigitalClock digitalClock;
	//private AnalogClock analogClock;
	private ViewFlipper vf;
	private MapView mapV;
	private Map map;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);
        vf = (ViewFlipper) findViewById(R.id.flipper);
    
        
        setupLocation();
        
        setupButtons();
        mapV = (MapView) findViewById(R.id.mapView);

        map = new Map(mapV, this);
		mapV.setBuiltInZoomControls(true);
		mapV.setClickable(true);
       
    }
    
    private void setupButtons() {
		mapV = (MapView) findViewById(R.id.mapView);
		mapV.setBuiltInZoomControls(true);
		
		Button backButton = (Button) findViewById(R.id.backButton);
//		backButton.setOnClickListener(new View.OnClickListener() {
//    		public void onClick(View view) {
//    		//vf.setInAnimation(inFromRightAnimation());
//    		//vf.setOutAnimation(outToLeftAnimation());
//    		vf.showPrevious();
//    		}});

	}

	private void setupLocation() {
    	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //gpsText = (TextView) findViewById(R.id.gpsLoc);
        //if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
        	//enableLocationSettings();
        //}
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, gpsUpdateListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, gpsUpdateListener);
        
        Thread timer = new Thread() {
            public void run () {
                for (;;) {
                    // do stuff in a separate thread
                	logGPSLocation.sendEmptyMessage(0);
                    try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}    // sleep for 3 seconds
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
            	///LocationData.save(lastKnownLoc, "uuid");
            	new LogLocationTask().execute(lastKnownLoc);

            	
            }
        }
    };

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class LogLocationTask extends AsyncTask<Location, Void, Void> {

		protected Void doInBackground(Location... locations) {

			LocationData.save(locations[0], "uuid");

			return null;
		}

		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}
	}
}
