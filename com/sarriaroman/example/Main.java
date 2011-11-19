package com.sarriaroman.example;

import com.sarriaroman.R;
import com.sarriaroman.connection.AsyncConnection;
import com.sarriaroman.connection.ConnectionListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity implements ConnectionListener {
	private static final int GOOGLE = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*
         * Conexion asincrona comun.
         * 
         * Al terminar de obtener el contenido lo envia con la
         * bandera definida para identificar al listener ready.
         */
        AsyncConnection.getInstance("http://google.com", this, GOOGLE).execute();
        
        /*
         * Conexion asincrona con cache de conexiones anteriores.
         * 
         * Si hay cache para el sitio la devuelve al iniciar la conexion.
         * Cuando termina de obtener el nuevo sitio lo envia mediante el listener
         * ready. Si hay cache lo envia con la misma bandera de identificaci—n
         * a cacheReady.
         */
        AsyncConnection.getInstance("http://google.com", this, GOOGLE).setCache(this, true).execute();
    }

	@Override
	public void ready(int msg, String message) {
		switch( msg ) {
		case GOOGLE:
			Log.i("Connection class", message);
			break;
		}
	}

	@Override
	public void cacheReady(int msg, String message) {
		switch( msg ) {
		case GOOGLE:
			Log.i("Connection class", "Cache: " + message);
			break;
		}
	}
}