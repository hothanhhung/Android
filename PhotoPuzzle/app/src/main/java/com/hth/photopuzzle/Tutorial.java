package com.hth.photopuzzle;

//import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Tutorial extends Activity {

	TextView tvtutorial;
	Typeface comic;
	private SharedPreferences tut;
	private SharedPreferences.Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);

		tvtutorial = (TextView) findViewById(R.id.tvtutorial);

		tut = getSharedPreferences("TUTORIAL", MODE_PRIVATE);
		edit = tut.edit();

		comic = Typeface.createFromAsset(getAssets(), "comicbd.ttf");
		tvtutorial.setTypeface(comic);

		Button bgame = (Button) findViewById(R.id.bgame);
		bgame.setTypeface(comic);

		bgame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Tutorial.this, GameScreen.class);
				edit.putBoolean("SHOWTUT", false);
				edit.commit();
				startActivity(i);
				finish();

			}
		});

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
