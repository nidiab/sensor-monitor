package com.raisins.sensormonitor.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 
 * Gives static access to current Activity and SharedPreferences. Useful when
 * generating Dialogs outside Activity.
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class Client {

	private static Activity mActivity;
	private static SharedPreferences mSharedPreferences;

	public static Activity getActivity() {
		return mActivity;
	}

	public static SharedPreferences getPreferences() {
		return mSharedPreferences;
	}

	public static String getString(int id) {
		return mActivity.getString(id);
	}

	public static void setActivity(Activity activity) {
		mActivity = activity;
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(activity);
	}

}
