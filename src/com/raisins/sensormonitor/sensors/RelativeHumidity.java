package com.raisins.sensormonitor.sensors;

import android.hardware.Sensor;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;

/**
 * Relative humidity sensor class
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class RelativeHumidity implements DeviceSensor {

	public static final int TYPE = Sensor.TYPE_RELATIVE_HUMIDITY;
	public static final String UNIT = "%";

	private String mFileLabel;
	private String[] mLabels;
	private int mLayoutDetails;
	private String mName;
	private Sensor mSensor;

	public RelativeHumidity(Sensor sensor) {

		mSensor = sensor;
		mFileLabel = Client.getString(R.string.relative_humidity) + "(" + UNIT
				+ ")";
		mLabels = new String[] { Client.getString(R.string.relative_humidity) };
		mLayoutDetails = R.layout.hum_info_dialog;
		mName = Client.getString(R.string.relative_humidity);

	}

	@Override
	public int getDetailsLayout() {
		return mLayoutDetails;
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
