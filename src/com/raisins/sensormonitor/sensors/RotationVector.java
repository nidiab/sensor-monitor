package com.raisins.sensormonitor.sensors;

import android.hardware.Sensor;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;

/**
 * 
 * Rotation vector class
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class RotationVector implements DeviceSensor {

	public static final int TYPE = Sensor.TYPE_ROTATION_VECTOR;
	public static final String UNIT = "";

	private String mFileLabel;
	private String[] mLabels;
	private int mLayoutDetails;
	private String mName;
	private Sensor mSensor;

	public RotationVector(Sensor sensor) {

		mSensor = sensor;
		mFileLabel = "X*sin(θ/2)" + separator + "Y*sin(θ/2)" + separator
				+ "Z*sin(θ/2)";
		mLabels = new String[] { "X*sin(θ/2)", "Y*sin(θ/2)", "Z*sin(θ/2)" };
		mLayoutDetails = R.layout.rot_info_dialog;
		mName = Client.getString(R.string.rotation_vector);

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
