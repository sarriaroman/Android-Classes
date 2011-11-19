package com.sarriaroman.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseWrapper {
	public static final String DATABASE_NAME = "something.db";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper _dbHelper;

    public DatabaseWrapper(Context c) {
        _dbHelper = DbHelper.getInstance(c);
    }
    
    public void close() {
    	_dbHelper.close();
    }
    
    public Cursor getAll( ) {
    	SQLiteDatabase db = _dbHelper.getReadableDatabase();
    	
    	return db.query("something", null, null, null, null, null, "name DESC");
    }
    
    public static void deleteAll( Context c ) {
    	DatabaseWrapper afdb = new DatabaseWrapper( c );
    	
    	SQLiteDatabase db = afdb._dbHelper.getWritableDatabase();
    	
    	try {
    		db.delete("something", null, null);
    	} finally {
    		db.close();
    	}
    }
    
    // En update el WHERE es nombre_columna = valor solamente.
	
	public static class DbHelper extends SQLiteOpenHelper {
		static DbHelper instance = null;
		
		private static final String CREATE_DB = "" +
				"create table if not exists something " +
				"(" +
				"id integer primary key unique, " +
				"name varchar(255), " +
				");";
		
		public DbHelper(Context context) {
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public static DbHelper getInstance( Context cnt ) {
			if( instance == null ) {
				instance = new DbHelper( cnt );
			}
			return instance;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		    db.execSQL( CREATE_DB );
		}

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // TODO Auto-generated method stub

	    }

	}
}
