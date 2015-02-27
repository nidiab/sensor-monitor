package com.raisins.sensormonitor.utils;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;

/**
 * 
 * Manages time when acquiring sensor data
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class Timer {

	private Handler customHandler = new Handler();

	private long startTime = 0L;

	private long timeInMilliseconds = 0L;

	private TextView timerValue;
	private long timeSwapBuff = 0L;
	private long updatedTime = 0L;

	private Runnable updateTimerThread = new Runnable() {

		@Override
		public void run() {

			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			timerValue.setText(String.format("%02d", mins) + ":"
					+ String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds));
			customHandler.postDelayed(this, 0);
		}

	};

	public Timer() {

		timerValue = (TextView) Client.getActivity().findViewById(R.id.time);
		timerValue.setText("00:00:000");

	}

	public long getTimeInMilliseconds() {
		return timeInMilliseconds;
	}

	public void pause() {
		timeSwapBuff += timeInMilliseconds;
		customHandler.removeCallbacks(updateTimerThread);
	}

	public void reset() {
		startTime = 0L;
		timeInMilliseconds = 0L;
		timeSwapBuff = 0L;
		updatedTime = 0L;
	}

	public void start() {
		startTime = SystemClock.uptimeMillis();
		customHandler.postDelayed(updateTimerThread, 0);
	}

}