package com.example.myproject;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

public class MyActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final ListView listView = (ListView) findViewById(R.id.listview);
		final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.test_list_item, new ArrayList<String>());
		listView.setAdapter(listAdapter);

		final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		final LocationListener locationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				listAdapter.add("Location " + location + "\n");
				listView.smoothScrollToPosition(listAdapter.getCount());
			}

			public void onProviderDisabled(String provider) {
				listAdapter.add("Disabled " + provider + "\n");
				listView.smoothScrollToPosition(listAdapter.getCount());
			}

			public void onProviderEnabled(String provider) {
				listAdapter.add("Enabled " + provider + "\n");
				listView.smoothScrollToPosition(listAdapter.getCount());
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				listAdapter.add("Changed " + provider + " " + status + "\n");
				listView.smoothScrollToPosition(listAdapter.getCount());
			}

		};
		
		final Button button_start = (Button) findViewById(R.id.button_start);
		final Button button_stop = (Button) findViewById(R.id.button_stop);
		
		button_stop.setEnabled(false);
		
		button_start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, locationListener);
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
				button_start.setEnabled(false);
				button_stop.setEnabled(true);
			}
		});
		
		button_stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				locationManager.removeUpdates(locationListener);
				button_start.setEnabled(true);
				button_stop.setEnabled(false);
			}
		});
	}
}
