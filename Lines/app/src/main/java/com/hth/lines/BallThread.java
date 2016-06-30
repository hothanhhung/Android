package com.hth.lines;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*
 * 
 * Lớp này là trái tim của Line, nó điều phối hoạt động của Ball
 */
public class BallThread extends Thread {

	private SurfaceHolder surfaceHolder;
	private ISurfaceBall surfaceBall;
	private boolean ok = true;

	public BallThread(SurfaceHolder sfHolder, ISurfaceBall sfball) {
		this.surfaceBall = sfball;
		this.surfaceHolder = sfHolder;
	}

	public void run() {
		Canvas canvas = null;
		while (ok) {
			canvas = this.surfaceHolder.lockCanvas();
			if (canvas != null) {
				surfaceBall.jumpBall();
				surfaceBall.runBall();
				surfaceBall.hideBall();
				surfaceBall.showSizeBall();
				surfaceBall.drawBall(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
}
