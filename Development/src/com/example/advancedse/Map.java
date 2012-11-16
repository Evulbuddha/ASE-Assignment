package com.example.advancedse;

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
	private LoggedLocationOverlays itemizedoverlay;
	private MapController mapController;
	//private View zoomControlls;
	
	public Map(MapView mapView, MainActivity activity){
		this.mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
		Drawable drawable = activity.getResources().getDrawable(R.drawable.location_mark);
		itemizedoverlay = new LoggedLocationOverlays(drawable, activity);
	}
	
	public void addOverlay(Location l){
		GeoPoint point = new GeoPoint((int)(l.getLatitude() * 1E6),(int)(l.getLongitude() * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mapController.setCenter(point);
	}
}
