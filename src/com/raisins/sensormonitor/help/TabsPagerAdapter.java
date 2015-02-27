package com.raisins.sensormonitor.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raisins.sensormonitor.R;

/**
 * 
 * Show different fragments depending on selected tab. Sets layouts of fragments
 * for the 3 different help fragment pages.
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	public static class MonitorFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_monitor,
					container, false);

			return rootView;
		}
	}

	public static class SensorsFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_sensors,
					container, false);

			return rootView;
		}
	}

	public static class SettingsFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);

			return rootView;
		}
	}

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		// Get item count - equal to number of tabs
		return 3;
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Main menu fragment activity
			return new SensorsFragment();
		case 1:
			// Monitor fragment activity
			return new MonitorFragment();
		case 2:
			// Settings fragment activity
			return new SettingsFragment();
		}

		return null;
	}
}
