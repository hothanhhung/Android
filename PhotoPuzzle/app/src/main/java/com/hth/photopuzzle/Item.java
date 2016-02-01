package com.hth.photopuzzle;

import com.hth.photopuzzle.GameScreen;
import com.hth.photopuzzle.Position;

public class Item {

	private boolean bEmpty;
	private GameScreen gameScreen;
	private int num;
	private Position position;

	public Item(int var1, boolean var2, GameScreen var3) {
		num = var1;
		bEmpty = var2;
		gameScreen = var3;
	}

	private boolean shiftBottom(Position var1) {
		if (var1.getPosBottom() != null) {
			if (!var1.getPosBottom().getItem().isEmptyItem()) {
				shiftBottom(var1.getPosBottom());
			}

			if (var1.getPosBottom().getItem().isEmptyItem()) {
				Item var2 = new Item(var1.getPosBottom().getItem().getNum(),
						var1.getPosBottom().getItem().isEmptyItem(), gameScreen);
				Item var3 = new Item(var1.getItem().getNum(), var1.getItem()
						.isEmptyItem(), gameScreen);
				var2.setPosition(var1);
				var3.setPosition(var1.getPosBottom());
				return true;
			}
		}

		return false;
	}

	private boolean shiftLeft(Position var1) {
		if (var1.getPosLeft() != null) {
			if (!var1.getPosLeft().getItem().isEmptyItem()) {
				shiftLeft(var1.getPosLeft());
			}

			if (var1.getPosLeft().getItem().isEmptyItem()) {
				Item var2 = new Item(var1.getPosLeft().getItem().getNum(), var1
						.getPosLeft().getItem().isEmptyItem(), gameScreen);
				Item var3 = new Item(var1.getItem().getNum(), var1.getItem()
						.isEmptyItem(), gameScreen);
				var2.setPosition(var1);
				var3.setPosition(var1.getPosLeft());
				return true;
			}
		}

		return false;
	}

	private boolean shiftRight(Position var1) {
		if (var1.getPosRight() != null) {
			if (!var1.getPosRight().getItem().isEmptyItem()) {
				shiftRight(var1.getPosRight());
			}

			if (var1.getPosRight().getItem().isEmptyItem()) {
				Item var2 = new Item(var1.getPosRight().getItem().getNum(),
						var1.getPosRight().getItem().isEmptyItem(), gameScreen);
				Item var3 = new Item(var1.getItem().getNum(), var1.getItem()
						.isEmptyItem(), gameScreen);
				var2.setPosition(var1);
				var3.setPosition(var1.getPosRight());
				return true;
			}
		}

		return false;
	}

	private boolean shiftTop(Position var1) {
		if (var1.getPosTop() != null) {
			if (!var1.getPosTop().getItem().isEmptyItem()) {
				shiftTop(var1.getPosTop());
			}

			if (var1.getPosTop().getItem().isEmptyItem()) {
				Item var2 = new Item(var1.getPosTop().getItem().getNum(), var1
						.getPosTop().getItem().isEmptyItem(), gameScreen);
				Item var3 = new Item(var1.getItem().getNum(), var1.getItem()
						.isEmptyItem(), gameScreen);
				var2.setPosition(var1);
				var3.setPosition(var1.getPosTop());
				return true;
			}
		}

		return false;
	}

	public int getNum() {
		return num;
	}

	public Position getPosition() {
		return position;
	}

	public boolean isEmptyItem() {
		return bEmpty;
	}

	public boolean isOnGoalPos() {
		return num == getPosition().getNum();
	}

	public void moveBottom() {
		if (!isEmptyItem() && shiftBottom(position)) {
			gameScreen.moveIncr();
			gameScreen.timeStart();
		}

	}

	public void moveLeft() {
		if (!isEmptyItem() && shiftLeft(position)) {
			gameScreen.moveIncr();
			gameScreen.timeStart();
		}

	}

	public void moveRight() {
		if (!isEmptyItem() && shiftRight(position)) {
			gameScreen.moveIncr();
			gameScreen.timeStart();
		}

	}

	public void moveTop() {
		if (!isEmptyItem() && shiftTop(position)) {
			gameScreen.moveIncr();
			gameScreen.timeStart();
		}

	}

	public void setPosition(Position var1) {
		position = var1;
		position.setItem(this);
	}

}
