package com.hth.lines;

import android.graphics.Canvas;

/*
 * Lớp này là giao diện giao tiếp giữa người sử dụng và Activity
 */
public interface ISurfaceBall {

	public void innittial();

	public void drawBall(Canvas canvas);

	public void runBall();

	public void jumpBall();

	public void hideBall();

	public void showSizeBall();
}
