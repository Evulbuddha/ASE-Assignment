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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class CheckInActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.advancedSE.MESSAGE";
	private LocationManager locationManager;
	private String lon;
	private String lat;
	//private TextView gpsText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.checkin);
        setupButtons();
        Intent i = getIntent();
        lon = i.getStringExtra("long");
        lat = i.getStringExtra("lat");
       
    }
    
    private void setupButtons() {

		
		Button addNew = (Button) findViewById(R.id.newLocationButton);
		addNew.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    		TextView name = (TextView) findViewById(R.id.newLocationText);
    	    String textF = name.getText().toString();
    	    
    	    //KnownLocations.newLocation(textF,lon, lat);
    		new LogKnownLocationTask().execute(textF, lon, lat);
    	    
    	    finish();
    		}});

	}
	
	private class LogKnownLocationTask extends AsyncTask< String, Void, Void> {

		protected Void doInBackground(String... info) {

			KnownLocations.newLocation(info[0], info[1], info[2]);
			return null;
		}

		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}
	}
}
