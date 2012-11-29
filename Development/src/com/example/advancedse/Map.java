package com.example.advancedse;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
//import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map {

	private List<Overlay> mapOverlays;
	private LoggedLocationOverlays itemizedoverlay, placesOverlay;
	private MapController mapController;
	//private View zoomControlls;
	
	public Map(MapView mapView, MainActivity activity){
		this.mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
		Drawable userLocIcon = activity.getResources().getDrawable(R.drawable.location_mark);
		Drawable placeIcon = activity.getResources().getDrawable(R.drawable.place_mark);
		itemizedoverlay = new LoggedLocationOverlays(userLocIcon, activity);
		placesOverlay = new LoggedLocationOverlays(placeIcon, activity);
		ArrayList<Place> places = KnownLocations.loadAll();
		for(Place p:places){
			addPlace(p);
		}
	}
	
	public void addOverlay(Location l){
		GeoPoint point = new GeoPoint((int)(l.getLatitude() * 1E6),(int)(l.getLongitude() * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mapController.setCenter(point);
	}
	
	public void addPlace(Place p){
		GeoPoint point = new GeoPoint((int)(p.getLatidude() * 1E6),(int)(p.getLongitude() * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, p.getName(), "I'm in Mexico City!");
		placesOverlay.addOverlay(overlayitem);
		mapOverlays.add(placesOverlay);
		//mapController.setCenter(point);
	}
}
