package com.raisins.sensormonitor.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.help.HelpActivity;
import com.raisins.sensormonitor.main.Client;
import com.raisins.sensormonitor.utils.MessageHandler;

/**
 * Creates user options for visualisation and data file
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class SettingsActivity extends PreferenceActivity {

	public static final int X_AXIS_RANGE_MIN = 5;
	public static final int X_AXIS_RANGE_MAX = 5000;

	public static final String KEY_PREF_DELAY_TIME = "delayTime";

	public static final String KEY_PREF_X_AXIS_RANGE = "xAxisRange";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Client.setActivity(SettingsActivity.this);

		setTitle(R.string.settings);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.help:
			Intent k = new Intent(this, HelpActivity.class);
			k.putExtra("TAB", 2); // Opens tab 3 when opening Help from Settings
			startActivity(k);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static class SettingsFragment extends PreferenceFragment implements
			OnSharedPreferenceChangeListener {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			Client.getActivity()
					.getWindow()
					.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.settings);

			SharedPreferences sharedPreferences = getPreferenceScreen()
					.getSharedPreferences();
			sharedPreferences.registerOnSharedPreferenceChangeListener(this);

			// Validate entry
			Preference xAxisRangePreference = findPreference(KEY_PREF_X_AXIS_RANGE);
			xAxisRangePreference
					.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
						@Override
						public boolean onPreferenceChange(
								Preference preference, Object newValue) {
							Integer value = Integer.valueOf((String) newValue);

							// Validate X axis range
							boolean valid = value >= X_AXIS_RANGE_MIN
									&& value <= X_AXIS_RANGE_MAX;

							if (!valid) {
								// If range is invalid, warn user
								MessageHandler
										.showMessageWithoutTitle(getString(R.string.range_error));
							}

							return valid;
						}
					});

			setDefault(sharedPreferences);

		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {

			// When preferences changes, refresh summary

			if (key.equals(KEY_PREF_X_AXIS_RANGE)) {

				String summary = sharedPreferences.getString(key, "100") + " "
						+ getString(R.string.events);

				setSummary(key, summary);
			} else if (key.equals(KEY_PREF_DELAY_TIME)) { // Show delay time
															// value in
															// milliseconds
				int value = 0;
				int index = Integer.valueOf(sharedPreferences.getString(key,
						"2"));

				switch (index) {
				case 0:
					value = 0;
					break;
				case 1:
					value = 20;
					break;
				case 2:
					value = 60;
					break;
				case 3:
					value = 200;
					break;
				default:
					break;
				}

				String summary = value + " " + getString(R.string.milliseconds);

				setSummary(key, summary);

			}

		}

		/**
		 * Set summary when opening settings
		 * 
		 * @param sharedPreferences
		 *            List of preferences
		 */
		private void setDefault(SharedPreferences sharedPreferences) {
			setSummary(KEY_PREF_X_AXIS_RANGE,
					sharedPreferences.getString(KEY_PREF_X_AXIS_RANGE, "100")
							+ " " + getString(R.string.events));

			// Show delay time value in milliseconds
			int value = 0;
			int index = Integer.valueOf(sharedPreferences.getString(
					KEY_PREF_DELAY_TIME, "2"));

			switch (index) {
			case 0:
				value = 0;
				break;
			case 1:
				value = 20;
				break;
			case 2:
				value = 60;
				break;
			case 3:
				value = 200;
				break;
			default:
				break;
			}

			// Set summary after selection
			setSummary(KEY_PREF_DELAY_TIME, value + " "
					+ getString(R.string.milliseconds));

		}

		private void setSummary(String key, String summary) {
			Preference connectionPref = findPreference(key);
			connectionPref.setSummary(summary);

		}

	}

}
