package com.hth.photopuzzle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 5;

	/** Constructor of the class */
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {

		MyFragment myFragment = new MyFragment();
		Bundle data = new Bundle();
		data.putInt("current_page", arg0 + 1);
		myFragment.setArguments(data);
		return myFragment;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String st = null;
		switch (position) {
		case 0:
			st = "Theme-1";
			break;
		case 1:
			st = "Theme-2";
			break;
		case 2:
			st = "Theme-3";
			break;
		case 3:
			st = "Theme-4";
			break;
		case 4:
			st = "Theme-5";
			break;
		}
		return st;
	}

}
