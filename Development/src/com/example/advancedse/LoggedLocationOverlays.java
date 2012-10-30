package com.example.advancedse;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LoggedLocationOverlays extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public LoggedLocationOverlays(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	
	public LoggedLocationOverlays(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}

	public void addOverlay(OverlayItem overlay) {
	    overlays.add(overlay);
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return overlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = overlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}

}
