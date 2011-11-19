package com.sarriaroman.example.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class Commons {
	public static final String TAG = "ManaTour";
	public static final String cacheDir = "ManaTour";
	
	public static void info( String msg ) {
		Log.i(Commons.TAG, msg);
	}
	
	public static void error( String msg ) {
		Log.e(Commons.TAG, msg);
	}
	
	public static void dialog( Activity context, String msg ) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
