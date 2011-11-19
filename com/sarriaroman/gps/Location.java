package com.sarriaroman.gps;

import com.sarriaroman.example.classes.Commons;

import android.app.Activity;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Location {
	private static Location instance = null;
	
	public double latitude = 0;
	public double longitude = 0;
	
	public Location( Activity a ) {
		/* Use the LocationManager class to obtain GPS locations */
	    LocationManager mlocManager = (LocationManager)a.getSystemService( Activity.LOCATION_SERVICE );

	    LocationListener mlocListener = new MyLocationListener();
	    mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	}
	
	public static Location getInstance( Activity a ) {
		if( instance == null ) {
			instance = new Location( a );
		}
		return instance;
	}
	
	/* Class My Location Listener */
    public class MyLocationListener implements LocationListener {

    	@Override
    	public void onLocationChanged(android.location.Location loc) {
    		latitude = loc.getLatitude();
    		longitude = loc.getLongitude();
    		
    		Commons.info("Lat: " + latitude + " - Long: " + longitude );
    	}

    	@Override
    	public void onStatusChanged(String provider, int status, Bundle extras) {
    		// TODO Auto-generated method stub		
    	}

    	@Override
    	public void onProviderDisabled(String provider) {
    		// TODO Auto-generated method stub		
    	}

    	@Override
    	public void onProviderEnabled(String provider) {
    		// TODO Auto-generated method stub
    	}
    }
}
