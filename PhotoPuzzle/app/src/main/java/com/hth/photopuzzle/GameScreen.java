package com.hth.photopuzzle;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.photopuzzle.R;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class GameScreen extends Activity {

	private static ArrayList orderArray = new ArrayList();
	private static ArrayList randomArray = new ArrayList();
	private static String interstitialId;
	private int cntMove = 0;
	private ImageView currentPhoto;
	private boolean fTimeStart = false;
	private boolean fTimeStop = true;
	private Item item1;
	private Item item10;
	private Item item11;
	private Item item12;
	private Item item13;
	private Item item14;
	private Item item15;
	private Item item16;
	private Item item2;
	private Item item3;
	private Item item4;
	private Item item5;
	private Item item6;
	private Item item7;
	private Item item8;
	private Item item9;
	private long lSeconds = 0;
	private LinearLayout ll1;
	private LinearLayout ll10;
	private LinearLayout ll11;
	private LinearLayout ll12;
	private LinearLayout ll13;
	private LinearLayout ll14;
	private LinearLayout ll15;
	private LinearLayout ll16;
	private LinearLayout ll2;
	private LinearLayout ll3;
	private LinearLayout ll4;
	private LinearLayout ll5;
	private LinearLayout ll6;
	private LinearLayout ll7;
	private LinearLayout ll8;
	private LinearLayout ll9;
	private Context mContext;
	private SavedValues mSet;
	private Position p1;
	private Position p10;
	private Position p11;
	private Position p12;
	private Position p13;
	private Position p14;
	private Position p15;
	private Position p16;
	private Position p2;
	private Position p3;
	private Position p4;
	private Position p5;
	private Position p6;
	private Position p7;
	private Position p8;
	private Position p9;
	Thread timestart;
	private ImageView tv1;
	private ImageView tv10;
	private ImageView tv11;
	private ImageView tv12;
	private ImageView tv13;
	private ImageView tv14;
	private ImageView tv15;
	private ImageView tv16;
	private ImageView tv2;
	private ImageView tv3;
	private ImageView tv4;
	private ImageView tv5;
	private ImageView tv6;
	private ImageView tv7;
	private ImageView tv8;
	private ImageView tv9;
	private TextView tvMoves;
	private TextView tvRecord;
	private TextView tvRecordTime;
	private TextView tvTime;
	private boolean resumeTimer = false;
	private Context context = this;
	private SharedPreferences theme;
	private static int themeChoosen;
	private File picFile;
	private AdView adView;
	private AlertDialog alertGameComplete;
	private InterstitialAd interstitialAd;
	private boolean gameFinished = false;
	private boolean shareScreen = false;

	private void TextViewTimer() {
		tvTime.setText(getString(R.string.time).concat(
				timeFormat(Long.valueOf(lSeconds))));
	}


	private void buildMenuCompeteGame() {
		Object localObject = new ArrayList();
		((ArrayList) localObject).add(new MenuOption("Play Again", null, null));
		((ArrayList) localObject).add(new MenuOption("Play Random", null, null));
		((ArrayList) localObject).add(new MenuOption("Share", null, null));
		((ArrayList) localObject).add(new MenuOption("Cancel", null, null));
		localObject = new MenuOptionAdapter(getApplicationContext(), (ArrayList) localObject);
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle("Game Complete");
		localBuilder.setAdapter((ListAdapter) localObject, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				switch (paramInt) {
					case 0:
						shuffle();
						break;
					case 1:
						initialGame(true);
						break;
					case 2:
						takeScreenshot();
				}
				paramDialogInterface.dismiss();
			}
		});
		localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface) {
			}
		});


		this.alertGameComplete = localBuilder.create();
		this.alertGameComplete.setCanceledOnTouchOutside(false);
/*
		Builder dialog = new Builder(mContext);
		dialog.setTitle(getDialogTitle());
		dialog.setMessage(getString(R.string.dialog_playagain));
		dialog.setPositiveButton(getString(R.string.yes),
				new OnClickListener() {
					public void onClick(DialogInterface var1, int var2) {
						var1.dismiss();
						shuffle();
					}
				});
		dialog.setNegativeButton(getString(R.string.no), new OnClickListener() {
			public void onClick(DialogInterface var1, int var2) {
				var1.dismiss();
				finish();
			}
		});
		dialog.setNeutralButton(getString(R.string.share_screenshot),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						takeScreenshot();

					}
				});
		dialog.setCancelable(false).show();
*/

	}

	private boolean isValid(ArrayList var1) {
		Integer var2 = Integer.valueOf(0);

		for (int var3 = 0; var3 < 15; ++var3) {
			int var4 = ((Item) var1.get(var3)).getNum();
			int var5 = 0;
			for (int var6 = var3 + 1; var6 < 15; ++var6) {
				if (((Item) var1.get(var6)).getNum() < var4) {
					++var5;
				}
			}
			var2 = Integer.valueOf(var5 + var2.intValue());
		}
		if (var2.intValue() % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}

	private String getDialogTitle() {
		return cntMove < 100 ? getString(R.string.dialogtitle4)
				: (cntMove < 150 ? getString(R.string.dialogtitle3)
				: (cntMove < 200 ? this
				.getString(R.string.dialogtitle2) : this
				.getString(R.string.dialogtitle1)));
	}

	private void startAdmobAds() {
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();
		adView = (AdView) findViewById(R.id.adView);
		adView.loadAd(adRequest);

		interstitialAd = new InterstitialAd(this);
		interstitialId = String.valueOf(getResources().getString(
				R.string.interstitial_id));
		interstitialAd.setAdUnitId(interstitialId);
		interstitialAd.loadAd(adRequest);
	}

	private String timeFormat(Long var1) {
		String var2 = String.valueOf((int) (var1.longValue() / 3600L));
		String var3 = String.valueOf((int) (var1.longValue() % 3600L / 60L));
		String var4 = String.valueOf((int) (var1.longValue() % 60L));
		if (var2.length() == 1) {
			var2 = "0".concat(var2);
		}

		if (var3.length() == 1) {
			var3 = "0".concat(var3);
		}

		if (var4.length() == 1) {
			var4 = "0".concat(var4);
		}

		return var2.concat(":").concat(var3).concat(":").concat(var4);
	}

	private void initialGame(boolean randomImage) {
		PhotoManager.initialPhoto(this, randomImage);
		currentPhoto.setImageDrawable(null);
		currentPhoto.setImageBitmap(PhotoManager.getPhotoBitmap());
		shuffle();
	}

	private boolean isArrayFull() {
		boolean var1 = true;
		Iterator var2 = randomArray.iterator();
		while (var2.hasNext()) {
			if ((Item) var2.next() == null) {
				var1 = false;
			}
		}
		return var1;
	}

	private void moveSetText() {
		tvMoves.setText(getString(R.string.moves).concat(
				String.valueOf(cntMove)));
	}

	private void recordSetText() {
		tvRecord.setText(getString(R.string.record).concat(mSet.getRecord()));
	}

	private void recordTimeSetText() {
		tvRecordTime
				.setText(getString(R.string.record).concat(
						timeFormat(Long.valueOf(Long.parseLong(mSet
								.getRecordTime())))));
	}

	private void shuffle() {
		int var1 = 0;
		cntMove = 0;
		lSeconds = 0;
		fTimeStart = false;
		fTimeStop = true;
		timeStop();
		moveSetText();
		TextViewTimer();
		randomArray.clear();
		Random var2 = new Random();
		gameFinished = false;

		for (int var3 = 0; var3 < 16; ++var3) {
			randomArray.add((Object) null);
		}

		randomArray.set(15, item16);

		while (!isArrayFull()) {
			int var6 = var2.nextInt(15);
			if (randomArray.get(var6) == null) {
				randomArray.set(var6, (Item) orderArray.get(var1));
				++var1;
			}
		}

		if (!isValid(randomArray)) {
			shuffle();
			return;
		}

		clearResourceImageView();

		p1 = new Position(ll1, tv1, 1, (Item) randomArray.get(0), this);
		p2 = new Position(ll2, tv2, 2, (Item) randomArray.get(1), this);
		p3 = new Position(ll3, tv3, 3, (Item) randomArray.get(2), this);
		p4 = new Position(ll4, tv4, 4, (Item) randomArray.get(3), this);
		p5 = new Position(ll5, tv5, 5, (Item) randomArray.get(4), this);
		p6 = new Position(ll6, tv6, 6, (Item) randomArray.get(5), this);
		p7 = new Position(ll7, tv7, 7, (Item) randomArray.get(6), this);
		p8 = new Position(ll8, tv8, 8, (Item) randomArray.get(7), this);
		p9 = new Position(ll9, tv9, 9, (Item) randomArray.get(8), this);
		p10 = new Position(ll10, tv10, 10, (Item) randomArray.get(9), this);
		p11 = new Position(ll11, tv11, 11, (Item) randomArray.get(10), this);
		p12 = new Position(ll12, tv12, 12, (Item) randomArray.get(11), this);
		p13 = new Position(ll13, tv13, 13, (Item) randomArray.get(12), this);
		p14 = new Position(ll14, tv14, 14, (Item) randomArray.get(13), this);
		p15 = new Position(ll15, tv15, 15, (Item) randomArray.get(14), this);
		p16 = new Position(ll16, tv16, 16, (Item) randomArray.get(15), this);
		p1.setNeighbours((Position) null, p2, (Position) null, p5);
		p2.setNeighbours(p1, p3, (Position) null, p6);
		p3.setNeighbours(p2, p4, (Position) null, p7);
		p4.setNeighbours(p3, (Position) null, (Position) null, p8);
		p5.setNeighbours((Position) null, p6, p1, p9);
		p6.setNeighbours(p5, p7, p2, p10);
		p7.setNeighbours(p6, p8, p3, p11);
		p8.setNeighbours(p7, (Position) null, p4, p12);
		p9.setNeighbours((Position) null, p10, p5, p13);
		p10.setNeighbours(p9, p11, p6, p14);
		p11.setNeighbours(p10, p12, p7, p15);
		p12.setNeighbours(p11, (Position) null, p8, p16);
		p13.setNeighbours((Position) null, p14, p9, (Position) null);
		p14.setNeighbours(p13, p15, p10, (Position) null);
		p15.setNeighbours(p14, p16, p11, (Position) null);
		p16.setNeighbours(p15, (Position) null, p12, (Position) null);
	}

	public void check() {
		if (p1.getItem().isOnGoalPos() && p2.getItem().isOnGoalPos()
				&& p3.getItem().isOnGoalPos() && p4.getItem().isOnGoalPos()
				&& p5.getItem().isOnGoalPos() && p6.getItem().isOnGoalPos()
				&& p7.getItem().isOnGoalPos() && p8.getItem().isOnGoalPos()
				&& p9.getItem().isOnGoalPos() && p10.getItem().isOnGoalPos()
				&& p11.getItem().isOnGoalPos() && p12.getItem().isOnGoalPos()
				&& p13.getItem().isOnGoalPos() && p14.getItem().isOnGoalPos()
				&& p15.getItem().isOnGoalPos() && p16.getItem().isOnGoalPos()) {

			completeGame();

		}

	}

	private void completeGame()
	{
		if (!gameFinished) {
			gameFinished = true;

			if (interstitialAd.isLoaded())
				interstitialAd.show();

			if (Integer.parseInt(mSet.getRecord()) > cntMove
					|| mSet.getRecord().equals("0")) {
				if (cntMove != 0) {
					mSet.setRecord(String.valueOf(cntMove));
				}

				recordSetText();
			}

			if (Long.parseLong(mSet.getRecordTime()) > lSeconds
					|| mSet.getRecordTime().equals("0")) {
				if (lSeconds != 0) {
					mSet.setRecordTime(String.valueOf(lSeconds));
				}

				recordTimeSetText();
			}

			timeStop();

			this.alertGameComplete.setTitle(getDialogTitle());
			this.alertGameComplete.show();
			/*
			Builder dialog = new Builder(mContext);
			dialog.setTitle(getDialogTitle());
			dialog.setMessage(getString(R.string.dialog_playagain));
			dialog.setPositiveButton(getString(R.string.yes),
					new OnClickListener() {
						public void onClick(DialogInterface var1, int var2) {
							var1.dismiss();
							shuffle();
						}
					});
			dialog.setNegativeButton(getString(R.string.no), new OnClickListener() {
				public void onClick(DialogInterface var1, int var2) {
					var1.dismiss();
					finish();
				}
			});
			dialog.setNeutralButton(getString(R.string.share_screenshot),
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							takeScreenshot();

						}
					});
			dialog.setCancelable(false).show();
			*/
		}
	}

	public void moveIncr() {
		++cntMove;
		moveSetText();
	}

	private void clearResourceImageView(){
		if(tv1 != null) tv1.setImageDrawable(null);
		if(tv2 != null) tv2.setImageDrawable(null);
		if(tv3 != null) tv3.setImageDrawable(null);
		if(tv4 != null) tv4.setImageDrawable(null);
		if(tv5 != null) tv5.setImageDrawable(null);
		if(tv6 != null) tv6.setImageDrawable(null);
		if(tv7 != null) tv7.setImageDrawable(null);
		if(tv8 != null) tv8.setImageDrawable(null);
		if(tv9 != null) tv9.setImageDrawable(null);
		if(tv10 != null) tv10.setImageDrawable(null);
		if(tv11 != null) tv11.setImageDrawable(null);
		if(tv12 != null) tv12.setImageDrawable(null);
		if(tv13 != null) tv13.setImageDrawable(null);
		if(tv14 != null) tv14.setImageDrawable(null);
		if(tv15 != null) tv15.setImageDrawable(null);
		if(tv16 != null) tv16.setImageDrawable(null);
		System.gc();
	}
	private void killGame() {
		clearResourceImageView();
		p1 = null;
		p2 = null;
		p3 = null;
		p4 = null;
		p5 = null;
		p6 = null;
		p7 = null;
		p8 = null;
		p9 = null;
		p10 = null;
		p11 = null;
		p12 = null;
		p13 = null;
		p14 = null;
		p15 = null;
		p16 = null;
		item1 = null;
		item2 = null;
		item3 = null;
		item4 = null;
		item5 = null;
		item6 = null;
		item7 = null;
		item8 = null;
		item9 = null;
		item10 = null;
		item11 = null;
		item12 = null;
		item13 = null;
		item14 = null;
		item15 = null;
		item16 = null;
		ll1 = null;
		ll2 = null;
		ll3 = null;
		ll4 = null;
		ll5 = null;
		ll6 = null;
		ll7 = null;
		ll8 = null;
		ll9 = null;
		ll10 = null;
		ll11 = null;
		ll12 = null;
		ll13 = null;
		ll14 = null;
		ll15 = null;
		ll16 = null;
		tv1 = null;
		tv2 = null;
		tv3 = null;
		tv4 = null;
		tv5 = null;
		tv6 = null;
		tv7 = null;
		tv8 = null;
		tv9 = null;
		tv10 = null;
		tv11 = null;
		tv12 = null;
		tv13 = null;
		tv14 = null;
		tv15 = null;
		tv16 = null;
		if(currentPhoto != null)currentPhoto.setImageDrawable(null);
		currentPhoto = null;
		timeStop();
		lSeconds = 0L;
		timestart = null;
		mSet = null;
		tvMoves = null;
		tvRecord = null;
		tvTime = null;
		tvRecordTime = null;
		randomArray.clear();
		orderArray.clear();
		mContext = null;
		cntMove = 0;
		//finish();
	}

	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		theme = getSharedPreferences("THEME", MODE_PRIVATE);
		themeChoosen = theme.getInt("THEMECHOOSEN", 1);
		setContentView(R.layout.activity_main_1);

		mContext = this;
		mSet = new SavedValues(mContext);

		tvMoves = (TextView) findViewById(R.id.tvMoves);
		tvRecord = (TextView) findViewById(R.id.tvRecord);
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvRecordTime = (TextView) findViewById(R.id.tvRecordTime);
		cntMove = 0;
		lSeconds = 0;
		moveSetText();
		recordSetText();
		TextViewTimer();
		recordTimeSetText();
		fTimeStart = false;
		fTimeStop = true;
		ll1 = (LinearLayout) findViewById(R.id.Cell11);
		ll2 = (LinearLayout) findViewById(R.id.Cell12);
		ll3 = (LinearLayout) findViewById(R.id.Cell13);
		ll4 = (LinearLayout) findViewById(R.id.Cell14);
		ll5 = (LinearLayout) findViewById(R.id.Cell21);
		ll6 = (LinearLayout) findViewById(R.id.Cell22);
		ll7 = (LinearLayout) findViewById(R.id.Cell23);
		ll8 = (LinearLayout) findViewById(R.id.Cell24);
		ll9 = (LinearLayout) findViewById(R.id.Cell31);
		ll10 = (LinearLayout) findViewById(R.id.Cell32);
		ll11 = (LinearLayout) findViewById(R.id.Cell33);
		ll12 = (LinearLayout) findViewById(R.id.Cell34);
		ll13 = (LinearLayout) findViewById(R.id.Cell41);
		ll14 = (LinearLayout) findViewById(R.id.Cell42);
		ll15 = (LinearLayout) findViewById(R.id.Cell43);
		ll16 = (LinearLayout) findViewById(R.id.Cell44);
		tv1 = (ImageView) findViewById(R.id.tv1);
		tv2 = (ImageView) findViewById(R.id.tv2);
		tv3 = (ImageView) findViewById(R.id.tv3);
		tv4 = (ImageView) findViewById(R.id.tv4);
		tv5 = (ImageView) findViewById(R.id.tv5);
		tv6 = (ImageView) findViewById(R.id.tv6);
		tv7 = (ImageView) findViewById(R.id.tv7);
		tv8 = (ImageView) findViewById(R.id.tv8);
		tv9 = (ImageView) findViewById(R.id.tv9);
		tv10 = (ImageView) findViewById(R.id.tv10);
		tv11 = (ImageView) findViewById(R.id.tv11);
		tv12 = (ImageView) findViewById(R.id.tv12);
		tv13 = (ImageView) findViewById(R.id.tv13);
		tv14 = (ImageView) findViewById(R.id.tv14);
		tv15 = (ImageView) findViewById(R.id.tv15);
		tv16 = (ImageView) findViewById(R.id.tv16);
		item1 = new Item(1, false, this);
		item2 = new Item(2, false, this);
		item3 = new Item(3, false, this);
		item4 = new Item(4, false, this);
		item5 = new Item(5, false, this);
		item6 = new Item(6, false, this);
		item7 = new Item(7, false, this);
		item8 = new Item(8, false, this);
		item9 = new Item(9, false, this);
		item10 = new Item(10, false, this);
		item11 = new Item(11, false, this);
		item12 = new Item(12, false, this);
		item13 = new Item(13, false, this);
		item14 = new Item(14, false, this);
		item15 = new Item(15, false, this);
		item16 = new Item(16, true, this);
		orderArray.add(item1);
		orderArray.add(item2);
		orderArray.add(item3);
		orderArray.add(item4);
		orderArray.add(item5);
		orderArray.add(item6);
		orderArray.add(item7);
		orderArray.add(item8);
		orderArray.add(item9);
		orderArray.add(item10);
		orderArray.add(item11);
		orderArray.add(item12);
		orderArray.add(item13);
		orderArray.add(item14);
		orderArray.add(item15);
		orderArray.add(item16);
		buildMenuCompeteGame();
		this.currentPhoto = ((ImageView) findViewById(R.id.currentPhoto));
		initialGame(false);
		startAdmobAds();
	}

	private void takeScreenshot() {
		shareScreen = true;
		View view = findViewById(R.id.RLMain);// your layout id
		view.getRootView();

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File picDir = new File(Environment.getExternalStorageDirectory()
					+ "/" + getResources().getString(R.string.app_name));
			if (!picDir.exists()) {
				picDir.mkdir();
			}
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache(true);
			Bitmap bitmap = view.getDrawingCache();
			String fileName = "fifteen" + ".jpg";
			picFile = new File(picDir + "/" + fileName);
			try {
				picFile.createNewFile();
				FileOutputStream picOut = new FileOutputStream(picFile);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						(bitmap.getHeight()));
				boolean saved = bitmap.compress(CompressFormat.JPEG, 100,
						picOut);
				if (saved) {

				} else {
					Toast.makeText(context, "Error... Please Try Again",
							Toast.LENGTH_SHORT).show();
					// Error
				}
				picOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			view.destroyDrawingCache();
		} else {

			Toast.makeText(context, "Media Not Mounted", Toast.LENGTH_SHORT)
					.show();
		}

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("image/jpeg");
		sharingIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse(picFile.getAbsolutePath()));
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	public int getThemeChoosen() {
		return themeChoosen;
	}

	public void setThemeChoosen(int themeChoosen) {
		this.themeChoosen = themeChoosen;
	}

	public boolean onCreateOptionsMenu(Menu var1) {
		getMenuInflater().inflate(R.menu.activity_main, var1);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_shuffle) {
			timeStop();
			shuffle();
		}

		return true;
	}

	public void timeStart() {
		if (fTimeStart == false) {
			fTimeStart = true;
			fTimeStop = false;
			timestart = new Thread() {
				public void run() {
					try {
						while (!fTimeStop) {
							runOnUiThread(new Runnable() {
								public void run() {
									TextViewTimer();
								}
							});
							sleep(1000L);
							GameScreen game = GameScreen.this;
							game.lSeconds = 1 + game.lSeconds;
						}

					} catch (InterruptedException exception) {
						exception.printStackTrace();
					}
				}
			};
			timestart.start();
		}

	}

	public void timeStop() {
		if (fTimeStart) {
			timestart = null;
			fTimeStop = true;
			fTimeStart = false;
			resumeTimer = true;
		}
	}

	@Override
	protected void onPause() {
		timeStop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (resumeTimer == true && gameFinished == false) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(getResources().getString(R.string.dialogTitle))
					.setPositiveButton(getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
													int arg1) {
									timeStart();
									arg0.dismiss();

								}
							})
					.setNegativeButton(getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
													int arg1) {
									timeStop();
									finish();

								}
							}).setCancelable(false).show();

		}
		if (shareScreen == true) {
			GameScreen.this.finish();
		}
		System.gc();
	}

	@Override
	public void onBackPressed() {
		timeStop();
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getResources().getString(R.string.quitgame))
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								/*Intent i = new Intent(GameScreen.this,
										StartScreen.class);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);*/
								//killGame();
								finish();
							}
						})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								timeStart();
								arg0.dismiss();

							}
						}).setCancelable(false).show();

	}

	@Override
	protected void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		killGame();
		super.onDestroy();
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
