package com.hth.photopuzzle;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		Typeface comic = Typeface.createFromAsset(getAssets(), "comicbd.ttf");

		TextView title = (TextView) findViewById(R.id.title);
		TextView content = (TextView) findViewById(R.id.content);

		title.setTypeface(comic);
		content.setTypeface(comic);
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
