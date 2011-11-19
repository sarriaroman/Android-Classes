package com.sarriaroman.connection;


public interface ConnectionListener {
	public void ready( int msg, String message );
	public void cacheReady( int msg, String message );
}
