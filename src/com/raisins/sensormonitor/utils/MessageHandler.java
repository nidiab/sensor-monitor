package com.raisins.sensormonitor.utils;

import java.io.File;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;
import com.raisins.sensormonitor.main.MonitorActivity.SaveFileTask;
import com.raisins.sensormonitor.sensors.DeviceSensor;

/**
 * 
 * Creates generic messages into dialogs Static access
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class MessageHandler {

	/**
	 * Show "About" dialog
	 */
	public static void showAbout() {

		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					Client.getActivity());
			LayoutInflater inflater = Client.getActivity().getLayoutInflater();

			View view = inflater.inflate(R.layout.about_dialog,
					new LinearLayout(Client.getActivity()), false);
			view.setVerticalScrollBarEnabled(true);
			builder.setView(view);
			builder.setTitle(R.string.about);
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});

			builder.create();
			builder.show();

			PackageInfo pInfo;
			String version = "--";
			try {
				pInfo = Client
						.getActivity()
						.getPackageManager()
						.getPackageInfo(Client.getActivity().getPackageName(),
								0);
				version = pInfo.versionName;
			} catch (NameNotFoundException e) {
				version = "--";
				e.printStackTrace();
			}

			TextView versionView = (TextView) view.findViewById(R.id.version);
			versionView.setText(version);

			TextView yearView = (TextView) view.findViewById(R.id.year);

			Calendar calendar = Calendar.getInstance();
			yearView.setText(String.valueOf(calendar.get(Calendar.YEAR)));
		}

		catch (BadTokenException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show "Disclaimer" dialog
	 */
	public static void showDisclaimer() {

		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					Client.getActivity());
			LayoutInflater inflater = Client.getActivity().getLayoutInflater();

			View view = inflater.inflate(R.layout.disclaimer_dialog,
					new LinearLayout(Client.getActivity()), false);
			view.setVerticalScrollBarEnabled(true);
			builder.setView(view);
			builder.setTitle(R.string.disclaimer);
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});

			builder.create();
			builder.show();

		}

		catch (BadTokenException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show "Give feedback" dialog
	 */
	public static void showFeedback() {

		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					Client.getActivity());
			LayoutInflater inflater = Client.getActivity().getLayoutInflater();

			View view = inflater.inflate(R.layout.feedback_dialog,
					new LinearLayout(Client.getActivity()), false);
			view.setVerticalScrollBarEnabled(true);
			builder.setView(view);
			builder.setTitle(R.string.give_feedback);
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});

			builder.create();
			builder.show();

		}

		catch (BadTokenException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show error message
	 * 
	 * @param message
	 */
	public static void showError(String message) {
		showMessage(Client.getString(R.string.error), message);
	}

	/**
	 * If file exists, warn user
	 * 
	 * @param filename
	 * @param saveFileTask
	 */
	public static void showFileExistsialog(final String filename,
			final SaveFileTask saveFileTask) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				Client.getActivity());

		alert.setTitle(R.string.file_exists);
		alert.setMessage(R.string.replace);

		alert.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {

						File downloadsPath = Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

						String path = downloadsPath.getPath() + File.separator
								+ filename;

						// Remove old file
						File file = new File(path);
						file.delete();

						saveFileTask.execute(path);

					}
				});

		alert.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						showFilenameDialog(filename, saveFileTask);
					}
				});

		alert.show();
	}

	/**
	 * After file is successfully saved, show dialog with location
	 * 
	 * @param filename
	 *            Saved file name
	 * @param saveFileTask
	 *            ASyncTask
	 */
	public static void showFilenameDialog(String filename,
			final SaveFileTask saveFileTask) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				Client.getActivity());

		alert.setTitle(R.string.enter_filename);

		// Set an EditText view to get user input
		final EditText input = new EditText(Client.getActivity());
		alert.setView(input);
		input.setText(filename);

		alert.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {

						String filename = input.getText().toString();
						File downloadsPath = Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

						String path = downloadsPath.getPath() + File.separator
								+ filename;

						// Check if file exists
						File file = new File(path);
						if (file.exists()) {
							showFileExistsialog(filename, saveFileTask);
						} else {
							saveFileTask.execute(path);
						}

					}
				});

		alert.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});

		alert.show();
	}

	/**
	 * Shows a alert dialog with title and message setup by app
	 * 
	 * @param title
	 *            Message title
	 * @param message
	 *            Message
	 */
	public static void showMessage(String title, String message) {

		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Client.getActivity());
			builder.setMessage(message);
			builder.setTitle(title);
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			builder.create();
			builder.show();
		} catch (BadTokenException e) { // Happens when a double click is
										// performed followed by a normal click.
										// Don't show dialog and continue to new
										// activity
			e.printStackTrace();
		}

	}

	/**
	 * Shows an alert dialog without any title
	 * 
	 * @param message
	 *            Message for dialog
	 */
	public static void showMessageWithoutTitle(String message) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Client.getActivity());
			builder.setMessage(message);
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			builder.create();
			builder.show();
		} catch (BadTokenException e) { // Happens when a double click is
										// performed followed by a normal click.
										// Don't show dialog and continue to new
										// activity
			e.printStackTrace();
		}
	}

	/**
	 * Shows a dialog with the selected sensors details like capabilites and a
	 * small description
	 * 
	 * @param deviceSensor
	 *            Selected sensor
	 */
	public static void showSensorDetails(DeviceSensor deviceSensor) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					Client.getActivity());
			LayoutInflater inflater = Client.getActivity().getLayoutInflater();

			View view = inflater.inflate(deviceSensor.getDetailsLayout(), null);
			view.setVerticalScrollBarEnabled(true);

			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(deviceSensor.getSensor().getName());
			TextView vendor = (TextView) view.findViewById(R.id.vendor);
			vendor.setText(deviceSensor.getSensor().getVendor());
			TextView version = (TextView) view.findViewById(R.id.version);
			version.setText("" + deviceSensor.getSensor().getVersion());
			TextView power = (TextView) view.findViewById(R.id.power);
			power.setText("" + deviceSensor.getSensor().getPower());
			TextView resolution = (TextView) view.findViewById(R.id.resolution);
			resolution.setText("" + deviceSensor.getSensor().getResolution());

			builder.setView(view);
			builder.setTitle(Client.getString(R.string.sensor_details));
			builder.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});

			builder.create();
			builder.show();
		}

		catch (BadTokenException e) {
			e.printStackTrace();
		}
	}
}
