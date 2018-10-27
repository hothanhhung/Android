package com.hth.lines;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;

/*
 * Lớp này mô tả đặc tả của 1 quả bóng (Thuộc tính và hành động của nó)
 */
public class Ball {

	final int LARG_RADIUS_CONST = 20, SMALL_RADIUS_CONST = 10;
	int locX, X, Y;
	int locY, MAX, MIN, N = 0;
	int Larg_radius = 20, small_radius = 10;
	int color = 0, sizeRect=53, midRect=26, offsetY = 0;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getLarg_radius() {
		return Larg_radius;
	}

	public void setLarg_radius(int larg_radius) {
		Larg_radius = larg_radius;
	}

	private boolean sizeBall = false, hideBall = false;// true = larg, false =
														// small

	public boolean isHideBall() {
		return hideBall;
	}

	public void setHideBall(boolean hideBall) {
		this.hideBall = hideBall;
	}

	private boolean isRunBall = false, isJumpBall = false, ok = false;
	private ArrayList<Point> arr = null;

	public Ball(int locX, int locY, int color, int sizeRect, int offsetY) {
		X= locX; Y = locY;
		this.offsetY = offsetY;
		this.color = color;
		p.setColor(color);
		p.setAntiAlias(true);
		updateSizeRect(sizeRect);
	}

	private void updateSizeRect(int sizeRect){
        this.sizeRect = sizeRect;
        this.midRect = sizeRect/2;
        this.locX = this.X * sizeRect + midRect;
        this.locY = this.Y * sizeRect + midRect + offsetY;
        MAX = this.locY + 4;
        MIN = this.locY - 10;
    }

	public boolean isRunBall() {
		return isRunBall;
	}

	public void setRunBall(boolean isRunBall, ArrayList<Point> arr) {
		this.arr = arr;
		N = this.arr.size();
		this.isRunBall = isRunBall;
	}

	public boolean isJumpBall() {
		return isJumpBall;
	}

	public void setJumpBall(boolean isJumpBall) {
		this.isJumpBall = isJumpBall;
	}

	public Paint p = new Paint();

	public boolean isSizeBall() {
		return sizeBall;
	}

	public void setSizeBall(boolean sizeBall) {
		if(!this.sizeBall && sizeBall){
			Larg_radius = SMALL_RADIUS_CONST;
		}else if(this.sizeBall && !sizeBall){
			small_radius = LARG_RADIUS_CONST;
		}else if(!this.sizeBall && !sizeBall){
			small_radius = 0;
		}else{
			Larg_radius =LARG_RADIUS_CONST;
		}
		this.sizeBall = sizeBall;
	}

	// Ve bong o toa do locX, locY
	public void drawBall(Canvas canvas, int sizeRect) {
		if(this.sizeRect == 1)
		{
			updateSizeRect(sizeRect);
		}
		if (sizeBall) {// bong lon
			RadialGradient gradient = new RadialGradient(this.locX - 6,
					this.locY - 6, 8, Color.rgb(255, 240, 243), this.color,
					android.graphics.Shader.TileMode.CLAMP);
			p.setDither(true);
			p.setShader(gradient);
			canvas.drawCircle(this.locX, this.locY, Larg_radius, p);
		} else {
			RadialGradient gradient = new RadialGradient(this.locX - 3,
					this.locY - 3, 4, Color.rgb(255, 240, 243), this.color,
					android.graphics.Shader.TileMode.CLAMP);
			p.setDither(true);
			p.setShader(gradient);
			canvas.drawCircle(this.locX, this.locY, small_radius, p);
		}
		//canvas.drawText(""+this.X +"-"+this.Y + "-"+this.isJumpBall, this.locX + 20 , this.locY + 20, p);
	}

	// Bong chay
	public void runBall() {
		if (N > 0 && this.isRunBall) {
			N--;
			this.locX = arr.get(N).x * sizeRect + midRect;
			this.locY = arr.get(N).y * sizeRect + midRect + offsetY;
		} else {
			this.isRunBall = false;
			this.sizeBall = true;
			MAX = this.locY + 5;// set lai MAX cua vi tri moi
			MIN = this.locY - 5;// the same
		}

	}

	public void jumpBall() {
		if (this.isJumpBall) {
			if (!ok) {
				locY+=1;
				if (locY >= MAX)
					ok = true;
			}
			if (ok) {
				locY-=1;
				if (locY <= MIN)
					ok = false;
			}
		}else{
			locY=((locY - offsetY)/sizeRect)*sizeRect+midRect + offsetY;
		}

	}

	public void hideBall() {
		if (this.Larg_radius > 0 && this.hideBall)
			this.Larg_radius -= 2;
	}

	public void showSizeBall() {
		if (!this.hideBall){
			if(this.sizeBall){
				if (this.Larg_radius < LARG_RADIUS_CONST)
					this.Larg_radius += 2;
			}else {
				if (this.small_radius > SMALL_RADIUS_CONST)
				{
					this.small_radius -= 2;
				}
				else if (this.small_radius < SMALL_RADIUS_CONST)
				{
					this.small_radius += 2;
				}
			}
		}
	}

}
