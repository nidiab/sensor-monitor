package com.raisins.sensormonitor.plot;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.RelativeLayout;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;

/**
 * Uses achartengine to plot sensor events into graph
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class XYPlot {

	public static final String KEY_PREF_SAVE_TIMESTAMP = "saveTimestamp";
	public static final String KEY_PREF_X_AXIS_RANGE = "xAxisRange";

	private int eventIndex = -1; // Index for X axis
	private GraphicalView mChartView;

	private XYMultipleSeriesDataset mDataset;
	private XYMultipleSeriesRenderer mRenderer;
	private boolean saveTimestamp;

	private ArrayList<XYSeries> series;
	private ArrayList<Long> timeStamp;

	private int xAxisRange;

	public XYPlot() {

		saveTimestamp = Client.getPreferences().getBoolean(
				KEY_PREF_SAVE_TIMESTAMP, false);
		xAxisRange = Integer.valueOf(Client.getPreferences().getString(
				KEY_PREF_X_AXIS_RANGE, "100"));

	}

	/**
	 * Add values to plot Set X axis range to preferences value
	 * 
	 */
	public void addValues(long timestamp, float... data) {
		eventIndex++;

		if (saveTimestamp) {
			timeStamp.add(timestamp);
		}

		for (int i = 0; i < series.size(); i++) {
			series.get(i).add(eventIndex, data[i]);
		}

		// If current samples exceeds maximum number of allows samples, increase
		// minimum value for X axis
		if (eventIndex > xAxisRange) {
			mRenderer.setXAxisMin(eventIndex - xAxisRange);
			mRenderer.setXAxisMax(eventIndex);
		}

		// Refresh plot
		mChartView.invalidate();
	}

	public void clear() {
		mDataset.clear();
		mChartView.repaint();
		mChartView.invalidate();

	}

	public void createSeries(String... labels) {

		series = new ArrayList<XYSeries>();
		timeStamp = new ArrayList<Long>();

		for (int i = 0; i < labels.length; i++) {
			series.add(new XYSeries(labels[i]));
		}

		setup(); // Plot setup

		Activity mActivity = Client.getActivity();

		RelativeLayout layout = (RelativeLayout) mActivity
				.findViewById(R.id.chart);
		mChartView = ChartFactory.getLineChartView(mActivity, mDataset,
				mRenderer);
		layout.addView(mChartView); // Set chart view into existing layout

	}

	public ArrayList<Long> getTimestamp() {
		return timeStamp;
	}

	public ArrayList<XYSeries> getValues() {
		return series;
	}

	public String getYAxisLabel() {
		return mRenderer.getYTitle();
	}

	/**
	 * Defines colors of lines, axis labels and series
	 */
	public void setup() {

		int[] colors = { Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED };

		mDataset = new XYMultipleSeriesDataset();

		for (int i = 0; i < series.size(); i++) {
			mDataset.addSeries(series.get(i));
		}

		mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setLabelsTextSize(18);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		mRenderer.setYLabelsPadding(7);
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setLegendTextSize(16);

		int[] margins = mRenderer.getMargins();
		margins[0] += 5;
		margins[1] += 5;
		margins[2] += 15;
		margins[3] += 5;
		mRenderer.setMargins(margins);
		mRenderer.setFitLegend(true);
		mRenderer.setZoomEnabled(false, false);
		mRenderer.setPanEnabled(false, false);

		mRenderer.setXAxisMax(xAxisRange);

		for (int j = 0; j < series.size(); j++) {
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			renderer.setColor(colors[j]);
			renderer.setLineWidth(3);
			mRenderer.addSeriesRenderer(renderer);
		}
	}

	public void setXAxisLabel(String xLabel) {
		mRenderer.setXTitle(xLabel);
	}

	public void setYAxisLabel(String yLabel) {
		mRenderer.setYTitle(yLabel);
	}

}