package com.raisins.sensormonitor.main;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.help.HelpActivity;
import com.raisins.sensormonitor.settings.SettingsActivity;

/**
 * 
 * This class represents the Main activity. This is what the user views when he
 * opens the app. Shows buttons for all sensors and verifies if they exists on
 * phone. If not disables the buttons. Shows Settings on Action bar and Help on
 * menu bar.
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Client.setActivity(MainActivity.this);

		// List of all current supported sensors by the app
		setupSensorButtons(Sensor.TYPE_ACCELEROMETER,
				Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_LIGHT,
				Sensor.TYPE_GYROSCOPE, Sensor.TYPE_ROTATION_VECTOR,
				Sensor.TYPE_GRAVITY, Sensor.TYPE_LINEAR_ACCELERATION,
				Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_PRESSURE,
				Sensor.TYPE_RELATIVE_HUMIDITY);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings:
			Intent j = new Intent(this, SettingsActivity.class);
			startActivity(j);
			return true;
		case R.id.help:
			Intent k = new Intent(this, HelpActivity.class);
			k.putExtra("TAB", 0); // Open first tab when opening Help in this
									// page
			startActivity(k);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Client.setActivity(MainActivity.this);
	}

	/**
	 * Open MonitorActivity with sensor type as parameter
	 * 
	 * @param view
	 *            Button clicked
	 */
	public void selectSensor(View view) {

		int sensorType = Integer.parseInt((String) view.getTag());

		Intent intent = new Intent(this, MonitorActivity.class);
		intent.putExtra("sensor_type", sensorType);
		startActivity(intent);

	}

	/**
	 * 
	 * Verify if sensor is available on phone, if not disable button.
	 * 
	 * @param sensors
	 *            List of sensors (type)
	 */
	public void setupSensorButtons(int... sensors) {

		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		for (int i = 0; i < sensors.length; i++) {
			// Get the default sensor for the given type. If sensor does not
			// exist in phone returns null.
			final Sensor mSensor = mSensorManager.getDefaultSensor(sensors[i]);

			// Layout containing all buttons
			TableLayout tableLayout = (TableLayout) findViewById(R.id.menu_buttons);

			// Sensors have sensor type value defined in tag property (i.e.
			// Sensor.TYPE_ACCELEROMETER has value 1)
			Button button = (Button) tableLayout.findViewWithTag(String
					.valueOf(sensors[i]));

			// If sensor does not exist in phone, disable button
			if (mSensor == null && button != null) {
				button.setEnabled(false);
			}

		}

	}

}
