package com.hth.photopuzzle;

//import com.google.analytics.tracking.android.EasyTracker;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SetTheme extends FragmentActivity implements OnClickListener {

	Button setTheme;
	ViewPager viewPager;
	SharedPreferences theme;
	SharedPreferences.Editor edit;
	Typeface comic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settheme);
		comic = Typeface.createFromAsset(getAssets(), "comicbd.ttf");
		setTheme = (Button) findViewById(R.id.bsettheme);
		setTheme.setOnClickListener(this);
		setTheme.setTypeface(comic);

		theme = getSharedPreferences("THEME", MODE_PRIVATE);
		edit = theme.edit();

		viewPager = (ViewPager) findViewById(R.id.pager);

		/** Getting fragment manager */
		FragmentManager fm = getSupportFragmentManager();

		/** Instantiating FragmentPagerAdapter */
		MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(fm);

		/** Setting the pagerAdapter to the pager object */
		viewPager.setAdapter(pagerAdapter);
		int themeChoosen = theme.getInt("THEMECHOOSEN", 1);
		if (themeChoosen == 1)
			viewPager.setCurrentItem(0);
		if (themeChoosen == 2)
			viewPager.setCurrentItem(1);
		if (themeChoosen == 3)
			viewPager.setCurrentItem(2);
		if (themeChoosen == 4)
			viewPager.setCurrentItem(3);
		if (themeChoosen == 5)
			viewPager.setCurrentItem(4);
		PagerTitleStrip pagerStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);

		for (int i = 0; i < pagerStrip.getChildCount(); ++i) {
			View nextChild = pagerStrip.getChildAt(i);
			if (nextChild instanceof TextView) {
				TextView textViewToConvert = (TextView) nextChild;
				textViewToConvert.setTypeface(comic);
			}
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bsettheme:

			int a = viewPager.getCurrentItem();
			if (a == 1) {
				Toast.makeText(getApplicationContext(),
						"Theme 2 have been applied ", Toast.LENGTH_SHORT)
						.show();
				edit.putInt("THEMECHOOSEN", 2);
				edit.commit();
				finish();
			} else if (a == 2) {
				Toast.makeText(getApplicationContext(),
						"Theme 3 have been applied  ", Toast.LENGTH_SHORT)
						.show();
				edit.putInt("THEMECHOOSEN", 3);
				edit.commit();
				finish();
			} else if (a == 0) {
				Toast.makeText(getApplicationContext(),
						"Theme 1 have been applied  ", Toast.LENGTH_SHORT)
						.show();
				edit.putInt("THEMECHOOSEN", 1);
				edit.commit();
				finish();
			} else if (a == 3) {
				Toast.makeText(getApplicationContext(),
						"Theme 4 have been applied  ", Toast.LENGTH_SHORT)
						.show();
				edit.putInt("THEMECHOOSEN", 4);
				edit.commit();
				finish();
			} else if (a == 4) {
				Toast.makeText(getApplicationContext(),
						"Theme 5 have been applied  ", Toast.LENGTH_SHORT)
						.show();
				edit.putInt("THEMECHOOSEN", 5);
				edit.commit();
				finish();
			}

			break;
		}
	}

	@Override
	protected void onStart() {
		//EasyTracker.getInstance(this).activityStart(this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		//EasyTracker.getInstance(this).activityStop(this);
		super.onStop();
	}
}
