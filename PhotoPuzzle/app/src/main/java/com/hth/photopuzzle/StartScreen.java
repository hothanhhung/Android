package com.hth.photopuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

//import com.google.analytics.tracking.android.EasyTracker;


public class StartScreen extends Activity implements
		android.view.View.OnClickListener {

	private Button bstart, btheme, babout;
	private Animation left_to_right, right_to_Left, frombottom;
	private Intent i;
	private Typeface comic;
	private SharedPreferences tut;

	@Override
	protected void onCreate(Bundle bund) {
		super.onCreate(bund);
		setContentView(R.layout.startscreen);
		com.hth.photopuzzle.MemoryHelper.freeCacheFolder(this);
		bstart = (Button) findViewById(R.id.bplay);
		btheme = (Button) findViewById(R.id.btheme);
		babout = (Button) findViewById(R.id.babout);

		tut = getSharedPreferences("TUTORIAL", MODE_PRIVATE);

		comic = Typeface.createFromAsset(getAssets(), "comicbd.ttf");

		left_to_right = AnimationUtils.loadAnimation(this,
				R.anim.slide_left_to_right);
		right_to_Left = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_to_left);
		frombottom = AnimationUtils.loadAnimation(this, R.anim.slide_bottom);

		bstart.setAnimation(left_to_right);
		btheme.setAnimation(right_to_Left);
		babout.setAnimation(frombottom);

		bstart.setOnClickListener(this);
		btheme.setOnClickListener(this);
		babout.setOnClickListener(this);

		bstart.setTypeface(comic);
		btheme.setTypeface(comic);
		babout.setTypeface(comic);

		AppRater.app_launched(this);		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bplay:
			/*if (tut.getBoolean("SHOWTUT", true)) {
				i = new Intent(StartScreen.this, Tutorial.class);
				startActivity(i);
			} else {
*/
				i = new Intent(StartScreen.this, GameScreen.class);
				startActivity(i);
		//	}

			break;
		case R.id.babout:
			i = new Intent(StartScreen.this, About.class);
			startActivity(i);
			break;
		case R.id.btheme:
			i = new Intent(StartScreen.this, PhotosScreen.class);
			startActivity(i);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater blow = getMenuInflater();
		blow.inflate(R.menu.startscreen_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_rate_us:
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + getPackageName())));
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ getPackageName())));
			}
			break;
		case R.id.menu_more_games:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse(getResources().getString(
							R.string.developer_group_url))));
			break;
		}
		return false;

	}

	@Override
	public void onBackPressed() {

		finish();
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
	protected void onResume()
	{
		super.onResume();
		System.gc();
	}
}
