package com.example.advancedse;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class CheckInActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.advancedSE.MESSAGE";
	private String lon;
	private String lat;
	private String email;
	//private TextView gpsText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.checkin);
        setupButtons();
        Intent i = getIntent();
        lon = i.getStringExtra("long");
        lat = i.getStringExtra("lat");
        email = i.getStringExtra("email");
        Location currentLocation = new Location(LocationManager.GPS_PROVIDER);
        currentLocation.setLatitude(Double.parseDouble(lat));
        currentLocation.setLongitude(Double.parseDouble(lon));
        loadClosestLocations(currentLocation);
        
       
    }
    
    private void loadClosestLocations(Location location) {
		TableLayout sv = (TableLayout) findViewById(R.id.closeLocationsLayout);
		for(Place p:KnownLocations.loadAll()){
			Location placeLoc = new Location(LocationManager.GPS_PROVIDER);
			placeLoc.setLatitude(p.getLatidude());
			placeLoc.setLongitude(p.getLongitude());
			final int id = p.getId();
			if(placeLoc.distanceTo(location) < 200){
				Button loc = new Button(this.getBaseContext());
				loc.setOnClickListener(new View.OnClickListener() {
		    		public void onClick(View view) {
		    			//login to location

		        		new LoginToKnownLocationTask().execute(id+"");
		    			finish();
		    		}});
				loc.setText(p.getName());
				sv.addView(loc);
			}
		}
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
			double lon = Double.parseDouble(info[1]);
			double lat = Double.parseDouble(info[2]);
			int id = KnownLocations.newLocation(info[0], lon, lat);
			CheckInData.save(id+"", Util.encodeEmail(email));
			return null;
		}

		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}
	}
	
	private class LoginToKnownLocationTask extends AsyncTask< String, Void, Void> {

		protected Void doInBackground(String... info) {
			String id = info[0];
			CheckInData.save(id+"", Util.encodeEmail(email));
			return null;
		}

		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}
	}
}
