package com.raisins.sensormonitor.sensors;

import android.hardware.Sensor;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;

/**
 * 
 * Ambient Temperature sensor class
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class AmbientTemperature implements DeviceSensor {

	public static final int TYPE = Sensor.TYPE_AMBIENT_TEMPERATURE;
	public static final String UNIT = "°C";

	private int mDetailsLayout;
	private String mFileLabel;
	private String[] mLabels;
	private String mName;
	private Sensor mSensor;

	public AmbientTemperature(Sensor sensor) {

		mSensor = sensor;
		mFileLabel = Client.getString(R.string.ambient_temperature) + "("
				+ UNIT + ")";
		mLabels = new String[] { Client.getString(R.string.ambient_temperature) };
		mDetailsLayout = R.layout.temp_info_dialog;
		mName = Client.getString(R.string.ambient_temperature);
	}

	@Override
	public int getDetailsLayout() {
		return mDetailsLayout;
	}

	@Override
	public String getFileLabel() {
		return mFileLabel;
	}

	@Override
	public String[] getLabels() {
		return mLabels;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Sensor getSensor() {
		return mSensor;
	}

	@Override
	public int getType() {
		return TYPE;
	}

	@Override
	public String getUnit() {
		return UNIT;
	}

	@Override
	public void setSensor(Sensor sensor) {
		mSensor = sensor;
	}

}
