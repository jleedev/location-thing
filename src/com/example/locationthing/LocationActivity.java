package com.example.locationthing;

import java.util.ArrayList;

import android.app.ListActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class LocationActivity extends ListActivity implements LocationListener {

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
		listView = getListView();

		View toolbar = View.inflate(this, R.layout.main_toolbar, null);
		listView.addHeaderView(toolbar);

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