package com.raisins.sensormonitor.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.achartengine.model.XYSeries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.help.HelpActivity;
import com.raisins.sensormonitor.plot.XYPlot;
import com.raisins.sensormonitor.sensors.DeviceSensor;
import com.raisins.sensormonitor.sensors.DeviceSensorFactory;
import com.raisins.sensormonitor.utils.Files;
import com.raisins.sensormonitor.utils.MessageHandler;
import com.raisins.sensormonitor.utils.Timer;

/**
 * 
 * This class represents the monitoring, plot, and all activity related to the
 * sensors. Adds data to plot, registers and unregister sensors and adds data to
 * text file. Uses inner class ASyncTask to generate file and show progress at
 * the same time.
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class MonitorActivity extends Activity implements SensorEventListener {

	public static final String KEY_PREF_SAVE_TIMESTAMP = "saveTimestamp";
	public static final String KEY_PREF_SAVE_INDEX = "saveIndex";

	private DeviceSensor mDeviceSensor;
	private ProgressDialog mProgressDialog;
	private SensorManager mSensorManager;
	private Timer mTimer;

	private XYPlot mXyPlot;

	@Override
	public void onAccuracyChanged(Sensor mSensor, int accuracy) {
		// Nothing done when accuracy changes
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);

		Client.setActivity(MonitorActivity.this);

		int sensorType = (Integer) getIntent().getExtras().get("sensor_type");
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		Sensor sensor = mSensorManager.getDefaultSensor(sensorType);
		mDeviceSensor = DeviceSensorFactory.create(sensor);

		// Set activity title to name of sensor
		setTitle(mDeviceSensor.getName());

		mTimer = new Timer();
		mXyPlot = new XYPlot();

		// Button configuration
		setupButtons();

		// Plot axis configuration
		setupPlot();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sensor_menu, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this); // Make sure sensor is
													// destroyed when leaving
													// this activity
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.more:
			MessageHandler.showSensorDetails(mDeviceSensor);
			return true;
		case R.id.help:
			Intent k = new Intent(this, HelpActivity.class);
			k.putExtra("TAB", 1); // Open seconds tab when entering Help from
									// this page
			startActivity(k);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Client.setActivity(MonitorActivity.this);
	}

	/**
	 * Called every time mSensor values change
	 * 
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 **/
	@Override
	public void onSensorChanged(SensorEvent event) {
		// Proceed only if sensor detected is the selected one
		if (event.sensor.getType() != mDeviceSensor.getType()) {
			return;
		}
		// Add values to plot

		long timeInMillis = (new Date()).getTime()
				+ (event.timestamp - System.nanoTime()) / 1000000L;

		mXyPlot.addValues(timeInMillis, event.values);

	}

	/**
	 * Configures Play/Stop buttons to manage sensor registration. Configures
	 * Save button to save data after acquisition, and Reset button to clear all
	 * data
	 */
	private void setupButtons() {

		final ImageButton play = (ImageButton) findViewById(R.id.play);
		final ImageButton pause = (ImageButton) findViewById(R.id.pause);
		final ImageButton reset = (ImageButton) findViewById(R.id.reset);
		// Don't allow data to be saved into a file at the beginning of session
		final ImageButton save = (ImageButton) findViewById(R.id.save);
		save.setEnabled(false);
		reset.setEnabled(false); // No need to reset anything in the beginning

		// When button PLAY is pressed, start acquisition of accelerometer and
		// show PAUSE button
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAcquisition();
				v.setVisibility(View.INVISIBLE);
				pause.setVisibility(View.VISIBLE);
				save.setEnabled(false);
				reset.setEnabled(false);
			}
		});

		// When button PAUSE is pressed, stop acquisition of accelerometer and
		// show PLAY button
		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopAcquisition();
				v.setVisibility(View.INVISIBLE);
				play.setVisibility(View.VISIBLE);
				// Allow user to either save or clear all data
				save.setEnabled(true);
				reset.setEnabled(true);

			}
		});

		// Clear all data and reset timer. Reset
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTimer = new Timer();
				mXyPlot.clear();
				mXyPlot = new XYPlot();
				setupPlot();
				save.setEnabled(false);
				reset.setEnabled(false);
			}
		});

		// Save data into file
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Setup date to use in filename
				String date = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_",
						Locale.getDefault()).format(new Date());

				// Filename
				String filename = "SM_" + date
						+ mDeviceSensor.getName().replace(" ", "_")
						+ "_Sensor.txt";

				MessageHandler.showFilenameDialog(filename, new SaveFileTask());

			}
		});

	}

	/**
	 * Setup labels in plot
	 */
	public void setupPlot() {
		mXyPlot.createSeries(mDeviceSensor.getLabels());
		mXyPlot.setYAxisLabel(mDeviceSensor.getUnit());
		mXyPlot.setXAxisLabel(getString(R.string.samples));
	}

	/**
	 * Register sensor and start timer. Use delay time in Settings
	 */
	private void startAcquisition() {

		int delayTime = Integer.valueOf(Client.getPreferences().getString(
				"delayTime", "2"));

		mSensorManager.registerListener(this, mDeviceSensor.getSensor(),
				delayTime);
		mTimer.start();

		/*
		 * Sensor delay values
		 * 
		 * SENSOR_DELAY_FASTEST 0ms SENSOR_DELAY_GAME 20ms 50Hz SENSOR_DELAY_UI
		 * 60ms 16,6Hz SENSOR_DELAY_NORMAL 200ms 5Hz
		 */

	}

	/**
	 * Unregister sensor and stop timer
	 */
	private void stopAcquisition() {
		mSensorManager.unregisterListener(this);
		mTimer.pause();
	}

	/**
	 * 
	 * Creates new file and saves sensor data. Shows progress to user in %
	 * 
	 * @author Nídia Batista
	 * @version 1.0 09 Dec 2014
	 *
	 */
	public class SaveFileTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... path) {

			try {
				// Create file
				Files.startFile(path[0], mDeviceSensor.getName(),
						mDeviceSensor.getFileLabel());
			} catch (final IOException e) {

				// If any error, inform user file was not saved. Don't continue.
				// Usually because there is a problem with the filename
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MessageHandler.showError(Client
								.getString(R.string.error_file_saved2));
					}
				});

				e.printStackTrace();
				return null;
			}

			ArrayList<XYSeries> series = mXyPlot.getValues(); // Sensor data
			ArrayList<Long> timestamps = mXyPlot.getTimestamp(); // Timestamp of
																	// sensor
																	// data

			int total = series.get(0).getItemCount(); // Number of rows in file

			boolean saveIndex = Client.getPreferences().getBoolean(
					KEY_PREF_SAVE_INDEX, false);
			boolean saveTimestamp = Client.getPreferences().getBoolean(
					KEY_PREF_SAVE_TIMESTAMP, false);

			for (int i = 0; i < series.get(0).getItemCount(); i++) {
				ArrayList<Number> data = new ArrayList<Number>();

				if (saveIndex) { // Create new column with index
					data.add(i + 1);
				}
				if (saveTimestamp) { // Create new column with timestamp
					data.add(timestamps.get(i));
				}

				for (int j = 0; j < series.size(); j++) { // Add sensor data
					data.add(series.get(j).getY(i));
				}

				try {

					Files.saveColumns(data.toArray()); // Save to file

				} catch (IOException e) {
					// If any error, warn user
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							MessageHandler.showError(Client
									.getString(R.string.error_file_saved));
						}
					});
					e.printStackTrace();
				}

				publishProgress((int) ((i / (float) total) * 100)); // File save
																	// progress

				if (isCancelled()) {
					break; // Stop if user cancels
				}
			}

			return path[0];

		}

		@Override
		protected void onPostExecute(String path) {
			mProgressDialog.dismiss();

			if (path == null) {
				return;
			}

			try {
				Files.endFile(); // Close file
			} catch (IOException e) {
				MessageHandler.showError(getString(R.string.error_file_saved));
				e.printStackTrace();
				return;
			}

			// Check if file exists and give user output path
			File file = new File(path);
			if (file.exists()) {
				// Inform user of file path
				MessageHandler
						.showMessageWithoutTitle(getString(R.string.file_saved)
								+ " " + path);
			} else {
				MessageHandler.showError(getString(R.string.error_file_saved));
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Dialog with progress
			mProgressDialog = new ProgressDialog(Client.getActivity());
			mProgressDialog.setMessage(Client.getString(R.string.saving_file));
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setProgressNumberFormat(null);
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			mProgressDialog.setProgress(progress[0]);
		}
	}

}
