package com.raisins.sensormonitor.help;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.raisins.sensormonitor.R;
import com.raisins.sensormonitor.main.Client;
import com.raisins.sensormonitor.utils.MessageHandler;

/**
 * 
 * Opens Help page. This is separated by 3 tabs, corresponding to 3 different
 * types of help: Main menu, Monitor and Settings. Includes Options menu with
 * Give feedback, Disclaimer and About on the Action bar. To move between tabs
 * either select the tab or swipe.
 * 
 * @author Nídia Batista
 * @version 1.00, 09 Dec 2014
 *
 */
public class HelpActivity extends FragmentActivity implements TabListener {

	private ActionBar mActionBar;
	private TabsPagerAdapter mAdapter;
	private String[] mTabs; // Tab titles
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Client.setActivity(HelpActivity.this);

		int tabIndex = getIntent().getIntExtra("TAB", 0);

		setContentView(R.layout.activity_help);
		setTitle(R.string.help);
		mTabs = new String[] { getString(R.string.main_menu),
				getString(R.string.monitor), getString(R.string.settings) };

		// Initialisation
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mActionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		mViewPager.setAdapter(mAdapter);
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : mTabs) {
			mActionBar.addTab(mActionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		mActionBar.setSelectedNavigationItem(tabIndex);

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}

					@Override
					public void onPageSelected(int position) {
						// On changing the page
						// make respected tab selected
						mActionBar.setSelectedNavigationItem(position);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.help_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.feedback:
			MessageHandler.showFeedback();
			return true;
		case R.id.disclaimer:
			MessageHandler.showDisclaimer();
			return true;
		case R.id.about:
			MessageHandler.showAbout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// On tab selected, show respective fragment view
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
}
