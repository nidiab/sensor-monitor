package com.raisins.sensormonitor.sensors;

import android.hardware.Sensor;

/**
 * 
 * When selecting a sensor, returns a DeviceSensor object corresponding to the
 * sensor type
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class DeviceSensorFactory {

	public static DeviceSensor create(Sensor sensor) {

		int sensorType = sensor.getType();

		switch (sensorType) {
		case Sensor.TYPE_ACCELEROMETER:
			return new Accelerometer(sensor);
		case Sensor.TYPE_MAGNETIC_FIELD:
			return new MagneticField(sensor);
		case Sensor.TYPE_LIGHT:
			return new Light(sensor);
		case Sensor.TYPE_GYROSCOPE:
			return new Gyroscope(sensor);
		case Sensor.TYPE_ROTATION_VECTOR:
			return new RotationVector(sensor);
		case Sensor.TYPE_GRAVITY:
			return new Gravity(sensor);
		case Sensor.TYPE_LINEAR_ACCELERATION:
			return new LinearAcceleration(sensor);
		case Sensor.TYPE_AMBIENT_TEMPERATURE:
			return new AmbientTemperature(sensor);
		case Sensor.TYPE_PRESSURE:
			return new Pressure(sensor);
		case Sensor.TYPE_RELATIVE_HUMIDITY:
			return new RelativeHumidity(sensor);
		default:
			return null;
		}

	}

}
