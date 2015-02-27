package com.raisins.sensormonitor.sensors;

import android.hardware.Sensor;

/**
 * 
 * Interface with sensor methods for accessing sensor details
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public interface DeviceSensor {

	String separator = "\t";

	public int getDetailsLayout();

	public String getFileLabel();

	public String[] getLabels();

	public String getName();

	public Sensor getSensor();

	public int getType();

	public String getUnit();

	public void setSensor(Sensor sensor);

}
