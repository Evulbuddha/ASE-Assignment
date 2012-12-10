package com.example.advancedse;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LoggedLocationOverlays extends ItemizedOverlay<PlaceOverlayItem> {

	private ArrayList<PlaceOverlayItem> overlays = new ArrayList<PlaceOverlayItem>();
	private Context mContext;
	ArrayList<User> checkIns = new ArrayList<User>();
	
	public LoggedLocationOverlays(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	
	public LoggedLocationOverlays(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}

	public void addOverlay(PlaceOverlayItem overlay) {
	    overlays.add(overlay);
	    populate();
	}
	
	@Override
	protected PlaceOverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return (PlaceOverlayItem) overlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  PlaceOverlayItem item = (PlaceOverlayItem) overlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  GetCheckinsTask cit = new GetCheckinsTask();
	  cit.execute(item.placeId);
	  synchronized(cit){
		  try {
			cit.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  ScrollView sv = new ScrollView(mContext);
	  for(User u: checkIns){
		  TextView checkIn = new TextView(mContext);
		  checkIn.setText(u.getName());
		  sv.addView(checkIn);
	  }
	  dialog.setView(sv);
	  dialog.setTitle(item.getTitle());
	  //dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	private class GetCheckinsTask extends AsyncTask<Integer, Void, Void> {

		
		protected void onPostExecute(Void result) {

			//MainActivity.this.finish();
		}

		@Override
		protected Void doInBackground(Integer... arg0) {
			checkIns = CheckInData.load(arg0[0]+"");
			synchronized(this){
				notifyAll();
			}
			return null;
		}
	}

}
