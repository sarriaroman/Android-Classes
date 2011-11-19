package com.sarriaroman.database;

import android.app.Activity;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

public class KeySaver {
	private static final String KEY = "ExampleKey";
	private static final String PREFIX = "prefix_";
	
	public static String getDeviceID( Activity a ) {
		return Secure.getString( a.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
	}
	
	public static void saveSomething(Activity a, String key ) {
		SharedPreferences settings = a.getSharedPreferences(KEY, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString( PREFIX + "something", key);

	    editor.commit();
	}
	
	public static String getSomething(Activity a) {
		SharedPreferences settings = a.getSharedPreferences(KEY, 0);
		return settings.getString( PREFIX + "something", "");
	}
}
