package com.example.myproject;

import java.util.ArrayList;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MyActivity extends Activity implements LocationListener {

	ListView listView;
	ArrayAdapter<String> listAdapter;
	LocationManager locationManager;

	/** Whether the user wants to collect locations. */
	boolean running = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(R.id.listview);
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.test_list_item, new ArrayList<String>());
		listView.setAdapter(listAdapter);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		final Button button_start = (Button) findViewById(R.id.button_start);
		final Button button_stop = (Button) findViewById(R.id.button_stop);
		
		button_stop.setEnabled(false);
		
		button_start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				running = true;
				button_start.setEnabled(false);
				button_stop.setEnabled(true);
				resumeUpdates();
			}
		});
		
		button_stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				running = false;
				button_start.setEnabled(true);
				button_stop.setEnabled(false);
				pauseUpdates();
			}
		});
	}

	public void pauseUpdates() {
		locationManager.removeUpdates(this);
	}

	public void resumeUpdates() {
		if (!running) return;
		locationManager.requestLocationUpdates(
			LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(
			LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		pauseUpdates();
	}

	@Override
	public void onResume() {
		super.onResume();
		resumeUpdates();
	}

	@Override
	public void onLocationChanged(Location location) {
		listAdapter.add("Location " + location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		listAdapter.add("Disabled " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		listAdapter.add("Enabled " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status,
			Bundle extras) {
		listAdapter.add("Changed " + provider + " " + status);
	}

}
