package com.raisins.sensormonitor.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;
import com.raisins.sensormonitor.sensors.DeviceSensor;

/**
 * Creates file, saves content and closes file
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class Files {

	private static BufferedWriter out;

	/**
	 * Closes file
	 * 
	 * @throws IOException
	 */
	public static void endFile() throws IOException {
		out.close();
	}

	/**
	 * Saves recorded values of sensor into the previously opened file.
	 * 
	 * @param values
	 *            Sensor values
	 * @throws IOException
	 */
	public static void saveColumns(Object... values) throws IOException {

		String contents = "";

		for (int i = 0; i < values.length; i++) {
			if (i == values.length - 1) {
				contents += values[i] + "\n";
			} else {
				contents += values[i] + DeviceSensor.separator;
			}
		}

		out.write(contents);

	}

	/**
	 * 
	 * Creates file and header containing current date
	 * 
	 * @param path
	 *            Path where the file is being saved
	 * @throws IOException
	 */
	public static void startFile(String path, String sensor, String label)
			throws IOException {

		FileWriter fstream = new FileWriter(path);
		out = new BufferedWriter(fstream);

		PackageInfo pInfo;
		String version = "";
		try {
			pInfo = Client.getActivity().getPackageManager()
					.getPackageInfo(Client.getActivity().getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			version = "";
			e.printStackTrace();
		}

		String header = "#Sensor Monitor " + version;

		header += "#" + Client.getString(R.string.sensor_data) + ": " + sensor
				+ "\n";

		header += "#" + new Date() + "\n\n";

		header += "#";

		boolean saveTimestamp = Client.getPreferences().getBoolean(
				"saveTimestamp", false);
		boolean saveIndex = Client.getPreferences().getBoolean("saveIndex",
				false);

		if (saveIndex) {
			header += "Event" + DeviceSensor.separator;
		}
		if (saveTimestamp) {
			header += "Timestamp(ms)" + DeviceSensor.separator;
		}

		header += label;

		header += "\n";

		out.write(header);
	}

}
