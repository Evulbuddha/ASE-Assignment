package com.example.advancedse;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PlaceOverlayItem extends OverlayItem{
	int placeId;
	public PlaceOverlayItem(GeoPoint point, String title, String snippet, int placeID) {
		super(point, title, snippet);
		this.placeId = placeID;
	}
	

}
