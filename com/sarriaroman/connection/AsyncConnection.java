package com.sarriaroman.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sarriaroman.example.classes.Commons;
import com.sarriaroman.example.classes.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncConnection  extends AsyncTask<String, Void, String> {
	public static final int ERROR = 500;
	public static final int CACHE = 4401;
	public static final int NOCONNECTION = 4404;
	
	public static final String POST = "post";
	public static final String GET = "GET";
	
	private static final String NO_CONNECTION = "Exception: NO CONNECTION";
	
	private String page;
	private String type;
	private int _ERROR = ERROR;
	private int msg;
	
	private boolean cache = false;
	private Context context;
	
	private ConnectionListener invoker;
	
	private ArrayList<NameValuePair> postValues;
	
	AsyncConnection( String type, String page, ConnectionListener invoker, int msg, ArrayList<NameValuePair> values, int error ) {
		this.type = type;
		this.page = page;
		this.invoker = invoker;
		
		this.msg = msg;
		this._ERROR = error;
		
		this.postValues = values;
	}
	
	/**
	 * Permite crear una conexion del tipo GET
	 * 
	 * Luego de crear la instancia se ejecuta con la funci—n execute();
	 * 
	 * @param page URL a invocar
	 * @param invoker Activity que recibira los datos.
	 * @param msg Bandera que permite identificar la conexi—n.
	 *
	 * @return AsynConnection
	 */
	public static AsyncConnection getInstance( String page, ConnectionListener invoker, int msg ) { 
	    return new AsyncConnection( AsyncConnection.GET, page, invoker, msg, null, ERROR );
	}
	
	/**
	 * Permite crear una conexion del tipo GET que permite especificar
	 * la bandera de error.
	 * 
	 * Luego de crear la instancia se ejecuta con la funci—n execute();
	 * 
	 * @param page URL a invocar
	 * @param invoker Activity que recibira los datos.
	 * @param msg Bandera que permite identificar la conexi—n.
	 * @param error Bandera que permite identificar que conexi—n dio error.
	 *
	 * @return AsynConnection
	 */
	public static AsyncConnection getInstance( String page, ConnectionListener invoker, int msg, int error ) {
	    return new AsyncConnection( AsyncConnection.GET, page, invoker, msg, null, error );
	}
	
	/**
	 * Permite crear una conexion del tipo POST
	 * 
	 * Luego de crear la instancia se ejecuta con la funci—n execute();
	 * 
	 * @param page URL a invocar
	 * @param invoker Activity que recibira los datos.
	 * @param msg Bandera que permite identificar la conexi—n.
	 * @param values Permite pasar los valores a enviar en la cabecera POST
	 *
	 * @return AsynConnection
	 */
	public static AsyncConnection postInstance( String page, ConnectionListener invoker, int msg, ArrayList<NameValuePair> values ) { 
	    return new AsyncConnection( AsyncConnection.POST, page, invoker, msg, values, ERROR );
	}
	
	/**
	 * Permite crear una conexion del tipo POST que permite especificar
	 * la bandera de error.
	 * 
	 * Luego de crear la instancia se ejecuta con la funci—n execute();
	 * 
	 * @param page URL a invocar
	 * @param invoker Activity que recibira los datos.
	 * @param msg Bandera que permite identificar la conexi—n.
	 * @param values Permite pasar los valores a enviar en la cabecera POST
	 * @param error Bandera que permite identificar que conexi—n dio error.
	 *
	 * @return AsynConnection
	 */
	public static AsyncConnection postInstance( String page, ConnectionListener invoker, int msg, ArrayList<NameValuePair> values, int error ) { 
	    return new AsyncConnection( AsyncConnection.POST, page, invoker, msg, values, error );
	}
	
	/**
	 * Permite usar cache en la conxi—n, almacenando el ultimo contenido
	 * descargado mediante la clase.
	 * 
	 * Por default esta desactivada.
	 * 
	 * @param con Activity que ejecuta la acci—n.
	 * @param c True/False => Activa o Desactiva.
	 * 
	 * @return AsyncConnection
	 */
	public AsyncConnection setCache( Context con, boolean c ) {
		context = con;
		cache = c;
		
		if( cache ) executeCache();
		
		return this;
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = 
			new BufferedReader(new InputStreamReader(is), 8192);
		
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	private String connect(String url) throws Exception {

		Log.i(Commons.TAG, "connect -> " + url);

		HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		response = httpclient.execute(httpget);
		// Examine the response status
		//Log.i(CommonFunctions.TAG, response.getStatusLine().toString());

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();
		// If the response does not enclose an entity, there is no need
		// to worry about connection release

		if (entity != null) {

			// A Simple JSON Response Read
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);
			Log.i(Commons.TAG, "connect -> " + result);

			instream.close();

			return result;
		}

		return null;
	}
	
	/**
	 * Conexi—n sincrona solo para retrocompatibilidad.
	 * 
	 * @param url URL a obtener.
	 * 
	 * @return Contenido obtenido
	 * @throws Exception
	 */
	public static String getConnect(String url) throws Exception {

		Log.i(Commons.TAG, "getConnect -> " + url);

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);

		HttpResponse response;
		response = httpclient.execute(httpget);
		//Log.i(CommonFunctions.TAG, response.getStatusLine().toString());

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);
			Log.i(Commons.TAG, "getConnect -> " + result);

			instream.close();
			
			return result;
		}
		return null;
	}
	
	/**
	 * Conexi—n sincrona solo para retrocompatibilidad.
	 * 
	 * Permite saber la respuesta mediante el LOG de android.
	 * 
	 * @param url URL a obtener.
	 * 
	 * @return Contenido obtenido
	 * @throws Exception
	 */
	public static HttpResponse connectWithResponse(String url) throws Exception {

		Log.i(Commons.TAG, "connectWithResponse -> " + url);

		HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		response = httpclient.execute(httpget);
		// Examine the response status
		Log.i(Commons.TAG, "connectWithResponse -> " + response.getStatusLine().toString());

		return response;
	}

	private String postData(
			String url,
			ArrayList<NameValuePair> values
		) throws Exception {
		
		Log.i(Commons.TAG, "postData -> " + url);
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost( url );

		httppost.setEntity(new UrlEncodedFormEntity(values));

		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httppost);

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();
		// If the response does not enclose an entity, there is no need
		// to worry about connection release

		if (entity != null) {

			// A Simple JSON Response Read
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);

			Log.i(Commons.TAG, "postData -> " + result);

			instream.close();
			
			return result;

		}
		return null;
	}

	@Override
	protected String doInBackground(String... param) {
		if( type == AsyncConnection.GET ) {
			try {
				return this.connect( page );
			} catch (Exception e) {
				if( e instanceof HttpHostConnectException ) {
					return NO_CONNECTION;
				} else {
					return null;
				}
			}
		} else {
			try {
				return this.postData( page , postValues);
			} catch (Exception e) {
				if( e instanceof HttpHostConnectException ) {
					return NO_CONNECTION;
				} else {
					return null;
				}
			}
		}
	}
	
	protected void onPostExecute(String obj) {
		if( obj == null ) {
			invoker.ready(_ERROR, null);
		} else if( obj.equals(NO_CONNECTION) ) {
			invoker.ready(NOCONNECTION, null);
		} else {
			if( cache ) writeCache( obj );
			invoker.ready(msg, obj);
		}
	}
	
	private File getCacheDir( ) {
		File cacheDir;
		
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(), Commons.cacheDir);
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
        // ------------------------
        
        return cacheDir;
	}
	
	private void writeCache( String obj ) {
		File cacheDir = getCacheDir();
        
        File cache = new File( cacheDir, String.valueOf( this.page.hashCode() ) );
        
        try {
        	Utils.saveFile(cache,  obj.getBytes() );
        } catch( Exception e ) {
        	Commons.error("Cache error for: " + page + ". Write Error: " + e.toString());
        }
	}
	
	private void executeCache() {
		// Verify the cache directory
		File cacheDir = getCacheDir();
        
        File cache = new File( cacheDir, String.valueOf( this.page.hashCode() ) );
        if( cache.exists() ) {
        	try {
				String content = new String( Utils.readFile( cache ) );
				
				invoker.cacheReady(msg, content);
			} catch (Exception e) {
				Commons.error("Cache error for: " + page + ". Read Error: " + e.toString());
			}
        }
	}
}
