package com.yy.appweather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HolderFragment extends Fragment {


	public HolderFragment() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootview = inflater.inflate(R.layout.fragment_main, container,false);
		
		String[] forecastArray={
				"Today-Sunny-88/63",
				"tomorrow-Sunny-88/63",
				"Fri-wind-88/63",
				"Tues-rainy-88/63",
				"Sun-Sunny-88/63",
				"Sat-Sunny-88/63",
				"Mon-rainy-88/63"
		};
		
		List<String> weekForecast=new ArrayList<String>(
				Arrays.asList(forecastArray));
		
		ArrayAdapter<String> mForecastAdapter = 
				new ArrayAdapter<String>(getActivity(),
						R.layout.list_item_forecast,
						R.id.list_item_forecast_textview,
						weekForecast);
		ListView listview = (ListView) rootview.findViewById(R.id.listview_forecast);
		listview.setAdapter(mForecastAdapter);
		
		
		return rootview;
	}
}
