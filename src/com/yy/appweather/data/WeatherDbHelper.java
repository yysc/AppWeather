package com.yy.appweather.data;

import com.yy.appweather.data.WeatherContract.LocationEntry;
import com.yy.appweather.data.WeatherContract.WeatherEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WeatherDbHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "weather.db";
	
	public WeatherDbHelper(Context context) {
		// TODO Auto-generated constructor stub
	
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		// TODO Auto-generated method stub
		final String SQL_CREATE_LOCATION_TABLE="CREATE TABLE "+LocationEntry.TABLE_NAME+" ("+
				LocationEntry._ID+" INTEGER PRIMARY KEY,"+
				LocationEntry.COLUMN_LOCATION_SETTING+" TEXT UNIQUE NOT NULL, "+
				LocationEntry.COLUMN_CITY_NAME+" TEXT NOT NULL,"+
				LocationEntry.COLUMN_COORD_LAT+" REAL NOT NULL,"+
				LocationEntry.COLUMN_COORD_LONG+" REAL NOT NULL,"+
				" UNIQUE ("+LocationEntry.COLUMN_LOCATION_SETTING+") ON CONFLICT IGNORE"+
				" );";
				
		final String SQL_CREATE_WEATHER_TABLE=
				"CREATE TABLE "+WeatherEntry.TABLE_NAME+"("+
						WeatherEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
						WeatherEntry.COLUMN_LOC_KEY+" INTEGER NOT NULL,"+
						WeatherEntry.COLUMN_DATETEXT+" TEXT NOT NULL,"+
						WeatherEntry.COLUMN_SHORT_DESC+" TEXT NOT NULL,"+
						WeatherEntry.COLUMN_WEATHER_ID+" INTEGER NOT NULL,"+
						WeatherEntry.COLUMN_MIN_TEMP+" REAL NOT NULL,"+
						WeatherEntry.COLUMN_MAX_TEMP+" REAL NOT NULL,"+
						
						WeatherEntry.COLUMN_HUMIDITY+" REAL NOT NULL,"+
						WeatherEntry.COLUMN_PRESSURE+" REAL NOT NULL,"+
						WeatherEntry.COLUMN_WIND_SPEED+" REAL NOT NULL,"+
						WeatherEntry.COLUMN_DEGREES+" REAL NOT NULL,"+
		
						" FOREIGN KEY ("+WeatherEntry.COLUMN_LOC_KEY+" ) REFERENCES "+
						LocationEntry.TABLE_NAME+" ( "+LocationEntry._ID+" ),"+
						
						" UNIQUE ("+WeatherEntry.COLUMN_DATETEXT+" ,"+
						WeatherEntry.COLUMN_LOC_KEY+" ) ON CONFLICT REPLACE);";		
		
		sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);	
		
		Log.d("yytest", "WeatherDb CreateDb Done");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		sqLiteDatabase.execSQL("DROP TABLE IF EXITS "+ LocationEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXITS "+ WeatherEntry.TABLE_NAME);
		onCreate(sqLiteDatabase);
	}
	

}
