package com.example.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

public class MyActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final TextView textView = (TextView) findViewById(R.id.textview);
		textView.setText("");
		final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		final LocationListener locationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				textView.append("Location " + location + "\n");
			}

			public void onProviderDisabled(String provider) {
				textView.append("Disabled " + provider + "\n");
			}

			public void onProviderEnabled(String provider) {
				textView.append("Enabled " + provider + "\n");
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				textView.append("Changed " + provider + " " + status + "\n");
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