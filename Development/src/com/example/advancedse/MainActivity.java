package com.example.advancedse;

import java.util.Date;

import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
//import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.advancedSE.MESSAGE";
	LocationManager locationManager;
	TextView gpsText;
	
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
    }

    LocationListener gpsUpdateListener = new LocationListener(){
    	public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
            //updateUILocation(location);
    		gpsText.setText("Latitude:"+location.getLatitude() + "\nLongitude:" + location.getLongitude());
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
    
    public void speakToMe(View view){
    	Date d = new Date();
    	CharSequence s  = DateFormat.format("hh:mm:ss", d.getTime());
    	//TextView tv = (TextView) findViewById(R.id.textView1);
    	//tv.setText(s);
    	
    	/*Intent intent = new Intent(this, DisplayMessageActivity.class);
    	intent.putExtra(EXTRA_MESSAGE, "I am really talking like a real boy.");
    	startActivity(intent);*/
    }
}
