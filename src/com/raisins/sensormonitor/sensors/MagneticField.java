package com.raisins.sensormonitor.sensors;

import android.hardware.Sensor;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;

/**
 * 
 * Magnetic field sensor class
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class MagneticField implements DeviceSensor {

	public static final int TYPE = Sensor.TYPE_MAGNETIC_FIELD;
	public static final String UNIT = "μT";

	private int mDetailsLayout;
	private String mFileLabel;
	private String[] mLabels;
	private String mName;
	private Sensor mSensor;

	public MagneticField(Sensor sensor) {

		mSensor = sensor;
		mFileLabel = "X(" + UNIT + ")" + separator + "Y(" + UNIT + ")"
				+ separator + "Z(" + UNIT + ")";
		mLabels = new String[] { "X", "Y", "Z" };
		mDetailsLayout = R.layout.mag_info_dialog;
		mName = Client.getString(R.string.magnetic_field);

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
