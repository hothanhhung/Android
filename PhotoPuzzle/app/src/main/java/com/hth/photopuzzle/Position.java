package com.hth.photopuzzle;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Position {

	private LinearLayout linearLayout;
	private GameScreen mainActivity, main1;
	private int num;
	private Position posBottom;
	private Position posLeft;
	private Position posRight;
	private Position posTop;
	private Item item;
	private TextView tv;
	private int themeChoosen;

	public Position(LinearLayout var1, TextView var2, int var3, Item var4,
			GameScreen var5) {
		linearLayout = var1;
		main1 = new GameScreen();
		themeChoosen = main1.getThemeChoosen();
		tv = var2;
		num = var3;
		item = var4;
		item.setPosition(this);
		mainActivity = var5;

		var1.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View var1, MotionEvent var2) {
				if (var2.getAction() == 1) {
					item.moveRight();
					item.moveLeft();
					item.moveBottom();
					item.moveTop();
				}

				mainActivity.check();
				return true;
			}
		});
	}

	public Item getItem() {
		return item;
	}

	public int getNum() {
		return num;
	}

	public Position getPosBottom() {
		return posBottom;
	}

	public Position getPosLeft() {
		return posLeft;
	}

	public Position getPosRight() {
		return posRight;
	}

	public Position getPosTop() {
		return posTop;
	}

	public LinearLayout getll() {
		return linearLayout;
	}

	public TextView gettv() {
		return tv;
	}

	public void setItem(Item var1) {
		item = var1;

		if (item.isEmptyItem()) {
			tv.setBackgroundResource(0);
			linearLayout.setBackgroundResource(0);
			tv.setText("");
		} else if (item.isOnGoalPos()) {
			if (themeChoosen == 1) {
				tv.setBackgroundResource(R.drawable.on_position_theme_1);
				linearLayout
						.setBackgroundResource(R.drawable.theme_1_tile_background);
			} else if (themeChoosen == 2) {
				tv.setBackgroundResource(R.drawable.on_position_theme_2);
				linearLayout
						.setBackgroundResource(R.drawable.theme_2_tile_background);
			} else if (themeChoosen == 3) {
				tv.setBackgroundResource(R.drawable.on_position_theme_3);
				linearLayout
						.setBackgroundResource(R.drawable.theme_3_tile_background);
			} else if (themeChoosen == 4) {
				tv.setBackgroundResource(R.drawable.on_position_theme_4);
				linearLayout
						.setBackgroundResource(R.drawable.theme_4_tile_background);
			} else if (themeChoosen == 5) {
				tv.setBackgroundResource(R.drawable.on_position_theme_5);
				linearLayout
						.setBackgroundResource(R.drawable.theme_5_tile_background);
			}
			tv.setText(String.valueOf(item.getNum()));
		} else {
			if (themeChoosen == 1) {
				tv.setBackgroundResource(R.drawable.off_position_theme_1);
				linearLayout
						.setBackgroundResource(R.drawable.theme_1_tile_background);
			} else if (themeChoosen == 2) {
				tv.setBackgroundResource(R.drawable.off_position_theme_2);
				linearLayout
						.setBackgroundResource(R.drawable.theme_2_tile_background);
			} else if (themeChoosen == 3) {
				tv.setBackgroundResource(R.drawable.off_position_theme_3);
				linearLayout
						.setBackgroundResource(R.drawable.theme_3_tile_background);
			} else if (themeChoosen == 4) {
				tv.setBackgroundResource(R.drawable.off_position_theme_4);
				linearLayout
						.setBackgroundResource(R.drawable.theme_4_tile_background);
			} else if (themeChoosen == 5) {
				tv.setBackgroundResource(R.drawable.off_position_theme_5);
				linearLayout
						.setBackgroundResource(R.drawable.theme_5_tile_background);
			}

			tv.setText(String.valueOf(item.getNum()));
		}
	}

	public void setNeighbours(Position var1, Position var2, Position var3,
			Position var4) {
		posLeft = var1;
		posRight = var2;
		posTop = var3;
		posBottom = var4;
	}
}
