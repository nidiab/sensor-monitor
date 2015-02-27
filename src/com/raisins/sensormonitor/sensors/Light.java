package com.raisins.sensormonitor.sensors;

import android.hardware.Sensor;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;

/**
 * 
 * Light sensor class
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class Light implements DeviceSensor {

	public static final int TYPE = Sensor.TYPE_LIGHT;
	public static final String UNIT = "lx";

	private int mDetailsLayout;
	private String mFileLabel;
	private String[] mLabels;
	private String mName;
	private Sensor mSensor;

	public Light(Sensor sensor) {

		mSensor = sensor;
		mFileLabel = Client.getString(R.string.illumination) + "(" + UNIT + ")";
		mLabels = new String[] { Client.getString(R.string.illumination) };
		mDetailsLayout = R.layout.light_info_dialog;
		mName = Client.getString(R.string.light);

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
