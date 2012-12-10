package com.example.advancedse;

import java.util.ArrayList;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ScrollView;
import android.widget.TextView;


public class UserLocationOverlay extends ItemizedOverlay<OverlayItem>{
	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Context mContext;
	ArrayList<User> checkIns = new ArrayList<User>();
	
	public UserLocationOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	
	public UserLocationOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}

	public void addOverlay(OverlayItem overlayitem) {
	    overlays.add(overlayitem);
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
		return false;
	}
	
	private class GetCheckinsTask extends AsyncTask<Integer, Void, Void> {

		
		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}

		@Override
		protected Void doInBackground(Integer... arg0) {
			checkIns = CheckInData.load(arg0[0]+"");
			notifyAll();
			return null;
		}
	}

	public void clear() {
		overlays.removeAll(overlays);
	}

}
