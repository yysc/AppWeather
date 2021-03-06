package com.yy.appweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class HolderFragment extends Fragment {

	private ArrayAdapter<String> mForecastAdapter = null;
	public HolderFragment() {
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.forecastfragment,menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id=item.getItemId();
		if(id==R.id.action_refresh){
			updateWeather();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootview = inflater.inflate(R.layout.fragment_main, container,false);
//		
//		String[] forecastArray={
//				"Today-Sunny-88/63",
//				"tomorrow-Sunny-88/63",
//				"Fri-wind-88/63",
//				"Tues-rainy-88/63",
//				"Sun-Sunny-88/63",
//				"Sat-Sunny-88/63",
//				"Mon-rainy-88/63"
//		};
//		
//		List<String> weekForecast=new ArrayList<String>(
//				Arrays.asList(forecastArray));
		
		mForecastAdapter = 
				new ArrayAdapter<String>(getActivity(),
						R.layout.list_item_forecast,
						R.id.list_item_forecast_textview,
						new ArrayList<String>());
		ListView listview = (ListView) rootview.findViewById(R.id.listview_forecast);
		listview.setAdapter(mForecastAdapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String forecast = mForecastAdapter.getItem(position);
				Intent intent = new Intent(getActivity(),DetailActivity.class)
							.putExtra(Intent.EXTRA_TEXT, forecast);
				startActivity(intent);
				//Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
			}
		});
		
		return rootview;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		updateWeather();
	}
	private void updateWeather() {
		// TODO Auto-generated method stub
		FetchWeatherTask weatherTask=new FetchWeatherTask();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
		weatherTask.execute(location);	
	}
	public class FetchWeatherTask extends AsyncTask<String, String,String[]>{
	
		private final String LOG_TAG=FetchWeatherTask.class.getSimpleName();

		private String getReadableDateString(long time) {
			// TODO Auto-generated method stub
			Date date=new Date(time*1000);
			SimpleDateFormat format=new SimpleDateFormat("E,MMM d");
			return format.format(date).toString();
		}
	
		public String formatHighLows(double high, double low) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPrefs =
					PreferenceManager.getDefaultSharedPreferences(getActivity());
			String unitType = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_metric));
			
			if(unitType.equals(getString(R.string.pref_units_imperial))){
				high = (high*1.8) +32;
				low = (low*1.8) +32;
			}else if(unitType.equals(getString(R.string.pref_units_metric))){
				Log.d(LOG_TAG, "Unit type not found :"+ unitType);
			}
			long roundedHigh=Math.round(high);
			long roundedLow=Math.round(low);
			
			return roundedHigh+"/"+roundedLow;
		}
		
		protected String[] doInBackground(String...urlpara) {
			// TODO Auto-generated method stub
				if(urlpara.length==0){
					return null;
				}
				HttpURLConnection urlConnection = null;
				BufferedReader reader = null;
				 
				// Will contain the raw JSON response as a string.
				String forecastJsonStr = null;
				
				String format = "json";
				String units = "metric";
				int numDays=7;
				
				try {
				    // Construct the URL for the OpenWeatherMap query
				    // Possible parameters are avaiable at OWM's forecast API page, at
				    // http://openweathermap.org/API#forecast
					final String FORECAST_BASE_URL = 
							"http://api.openweathermap.org/data/2.5/forecast/daily?";
					final String QUERY_PARAM = "q";
					final String FORMAT_PARAM = "mode";
					final String UNITS_PARAM = "units";
					final String DAYS_PARAM = "cnt";
					
				    Uri buildUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
				    		.appendQueryParameter(QUERY_PARAM, urlpara[0])
				    		.appendQueryParameter(FORMAT_PARAM, format)
				    		.appendQueryParameter(UNITS_PARAM, units)
				    		.appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
				    		.build();
				 
				    // Create the request to OpenWeatherMap, and open the connection
				    
				    URL url = new URL(buildUri.toString());
				    Log.v(LOG_TAG, "Build URI"+buildUri.toString());
				    
				    urlConnection = (HttpURLConnection) url.openConnection();
				    urlConnection.setRequestMethod("GET");
				    urlConnection.connect();
				 
				    // Read the input stream into a String
				    InputStream inputStream = urlConnection.getInputStream();
				    StringBuffer buffer = new StringBuffer();
				    if (inputStream == null) {
				        // Nothing to do.
				    	return null;
				    }
	
				    reader = new BufferedReader(new InputStreamReader(inputStream));
				 
				    String line;
				    while ((line = reader.readLine()) != null) {
				        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
				        // But it does make debugging a *lot* easier if you print out the completed
				        // buffer for debugging.
				        buffer.append(line + "\n");
				    }
				 
				    if (buffer.length() == 0) {
				        // Stream was empty.  No point in parsing.
				        forecastJsonStr = null;
				    }
				    forecastJsonStr = buffer.toString();
				    Log.v(LOG_TAG,"forecast Json String"+forecastJsonStr);
				} catch (IOException e) {
				    Log.e("PlaceholderFragment", "Error ", e);
				    // If the code didn't successfully get the weather data, there's no point in attemping
				    // to parse it.
				    forecastJsonStr = null;
				} finally{
				    if (urlConnection != null) {
				        urlConnection.disconnect();
				    }
				    if (reader != null) {
				        try {
				            reader.close();
				        } catch (final IOException e) {
				            Log.e("PlaceholderFragment", "Error closing stream", e);
				        }
				    }
				}
				try{
					return getWeatherDataFromJson(forecastJsonStr, numDays);
				}catch(JSONException e){
					Log.e("getWeatherDataFromJson", "Error GetData", e);
					e.printStackTrace();
				}				
				return null;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			if(result!=null){
				mForecastAdapter.clear();
				for(String dayForecastStr:result){
					mForecastAdapter.add(dayForecastStr);
				}
			}
		}
		
		private String[] getWeatherDataFromJson(String forecastJsonStr,int numDays)
		throws JSONException{
			final String OWM_LIST="list";
			final String OWM_WEATHER="weather";
			final String OWM_TEMPRATURE="temp";
			final String OWM_MAX="max";
			final String OWM_MIN="min";
			final String OWM_DATETIME="dt";
			final String OWM_DESCRIPTION="main";
				
			JSONObject forecastJson=new JSONObject(forecastJsonStr);
			JSONArray weatherArray=forecastJson.getJSONArray(OWM_LIST);
			String[] resultStrs=new String[numDays];
			
			for(int i=0;i<weatherArray.length();i++)
			{
				String day;
				String description;
				String highAndLow;
				JSONObject dayForecast=weatherArray.getJSONObject(i);
				
				long dateTime=dayForecast.getLong(OWM_DATETIME);
				day=getReadableDateString(dateTime);
				
				JSONObject weatherObject=dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
				description=weatherObject.getString(OWM_DESCRIPTION);
				
				JSONObject temperatureObject=dayForecast.getJSONObject(OWM_TEMPRATURE);
				double high=temperatureObject.getDouble(OWM_MAX);
				double low=temperatureObject.getDouble(OWM_MIN);
				
				highAndLow=formatHighLows(high,low);
				resultStrs[i]=day+"-"+description+"-"+highAndLow;				
			}
			return resultStrs;
		}		
	}
}
