package com.example.advancedse;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
//import android.view.View;

//import com.example.advancedse.MainActivity.LogLocationTask;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map {

	private List<Overlay> mapOverlays;
	private LoggedLocationOverlays placesOverlay;
	private UserLocationOverlay itemizedoverlay;
	private MapController mapController;
	//private View zoomControlls;
	
	private ArrayList<Place> places;
	
	public Map(MapView mapView, MainActivity activity){
		this.mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
		Drawable userLocIcon = activity.getResources().getDrawable(R.drawable.location_mark);
		Drawable placeIcon = activity.getResources().getDrawable(R.drawable.place_mark);
		itemizedoverlay = new UserLocationOverlay(userLocIcon, activity);
		placesOverlay = new LoggedLocationOverlays(placeIcon, activity);
		
		new LoadPlacesTask().execute();
		synchronized(this){
			try {
				this.wait(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Place p:places){
			addPlace(p);
		}
	}
	
	public void addOverlay(Location l){
		itemizedoverlay.clear();
		GeoPoint point = new GeoPoint((int)(l.getLatitude() * 1E6),(int)(l.getLongitude() * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mapController.setCenter(point);
	}
	
	public void addPlace(Place p){
		GeoPoint point = new GeoPoint((int)(p.getLatidude() * 1E6),(int)(p.getLongitude() * 1E6));
		PlaceOverlayItem overlayitem = new PlaceOverlayItem(point, p.getName(), "I'm in Mexico City!", p.getId());
		placesOverlay.addOverlay(overlayitem);
		mapOverlays.add(placesOverlay);
		//mapController.setCenter(point);
	}
	
	private class LoadPlacesTask extends AsyncTask<Void, Void, Void> {

		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}

		@Override
		protected Void doInBackground(Void... params) {
			places = KnownLocations.loadAll();
			synchronized(this){
				this.notifyAll();
			}
			return null;
		}
	}
}
