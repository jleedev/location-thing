package com.example.locationthing;

import java.util.ArrayList;

import android.app.ListActivity;
import android.location.Location;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class LocationActivity extends ListActivity implements LocationListener, GpsStatus.Listener {

	ListView listView;
	ArrayAdapter<String> listAdapter;
	LocationManager locationManager;
	ClipboardManager clipboardManager;

	/** Whether the user wants to collect locations. */
	boolean running = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = getListView();

		clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		listAdapter = new ArrayAdapter<String>(this, android.R.layout.test_list_item, new ArrayList<String>());
		listView.setAdapter(listAdapter);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				clipboardManager.setText(item);
				Toast toast = Toast.makeText(LocationActivity.this, R.string.toast_copied, Toast.LENGTH_SHORT);
				toast.show();
			}
		});

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		final Button button_start = (Button) findViewById(R.id.button_start);
		final Button button_stop = (Button) findViewById(R.id.button_stop);

		button_stop.setEnabled(false);

		button_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				running = true;
				button_start.setEnabled(false);
				button_stop.setEnabled(true);
				resumeUpdates();
			}
		});

		button_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				running = false;
				button_start.setEnabled(true);
				button_stop.setEnabled(false);
				pauseUpdates();
			}
		});
	}

	public void pauseUpdates() {
		listAdapter.add("Pausing updates");
		locationManager.removeUpdates(this);
	}

	public void resumeUpdates() {
		if (!running) return;
		listAdapter.add("Resuming updates");
		locationManager.requestLocationUpdates(
			LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(
			LocationManager.NETWORK_PROVIDER, 0, 0, this);
		locationManager.addGpsStatusListener(this);
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
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			listAdapter.add(provider + " is out of service");
			break;
		case LocationProvider.AVAILABLE:
			listAdapter.add(provider + " is available");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			listAdapter.add(provider + " temporarily unavailable");
		}

		for (String key: extras.keySet()) {
			Object obj = extras.get(key);
			listAdapter.add(key + " -> " + obj);
		}
	}

	@Override
	public void onGpsStatusChanged(int event) {
		GpsStatus status = locationManager.getGpsStatus(null);
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			listAdapter.add("GPS: Started");
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			listAdapter.add("GPS: Stopped");
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			listAdapter.add("GPS: First fix in " + status.getTimeToFirstFix());
			break;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			//listAdapter.add("GPS: Satellite Status");
			break;
		}
	}
}
