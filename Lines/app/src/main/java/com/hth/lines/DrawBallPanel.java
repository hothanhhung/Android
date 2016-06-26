package com.hth.lines;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hth.utils.MethodsHelper;

/*
 * Lớp này là giao diện hoạt động của Line
 */

public class DrawBallPanel extends SurfaceView implements ISurfaceBall,
		SurfaceHolder.Callback {

	private final int YOFFSET_BOARD = 159;
	private final int YOFFSET_INFO = YOFFSET_BOARD - 30;
	private final int XNUM = 9, YNUM = 9;
	private final int [] MENU_BUTTON = new int[] {10, 10, 70, 70};
	private final int [] SPEAKER_BUTTON = new int[] {80, 10, 140, 70};
	private final int [] UNDO_BUTTON = new int[] {-70, 10, -10, 70};
	private int dxFirst = -1, dyFirst = -1, dau_x = -1, dau_y = -1;
	private BallThread thread = null;
	private Paint p = new Paint();
	private int[][] matrix = new int[XNUM][YNUM];
	private Ball[][] ball = new Ball[XNUM][YNUM];
	private ArrayList<Point> pointXY = new ArrayList<Point>();
	public boolean runsuccess = false, dachay = false;
	private int sizeRectangle = 53;

	private MediaPlayer backgroundMusic = null;
	private boolean isPlayMusic = true;
	SavedValues savedValues;

	public DrawBallPanel(Context context) {

		super(context);
		this.innittial();
		thread = new BallThread(this.getHolder(), this);
		// TODO Auto-generated constructor stub

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (!thread.isAlive()) {
			Log.d("VAO DAY", "START THREAD");
			thread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	public void innittial() {
		// TODO Auto-generated method stub
		for (int i = 0; i < XNUM; i++) {
			for (int j = 0; j < YNUM; j++) {
				matrix[i][j] = 0;
				ball[i][j] = null;
			}
		}
		p.setStyle(Paint.Style.FILL);
		p.setStrokeJoin(Paint.Join.ROUND);
		p.setStrokeCap(Paint.Cap.ROUND);
		getHolder().addCallback(this);
		this.generateSmallBall();
		this.ShowLargeBall();
		this.generateSmallBall();
		this.timeStart();
		savedValues = new SavedValues(getContext());
		isPlayMusic = savedValues.getRecordPlaybackground();
		backgroundMusic = MediaPlayer.create(getContext(), R.raw.backgroundmusic);
		backgroundMusic.setLooping(true);
	}

	public void drawBall(Canvas canvas) {
		// TODO Auto-generated method stub

		sizeRectangle = getWidth() / XNUM;
		canvas.drawColor(Color.WHITE);
		drawBackground(canvas);

		synchronized (ball) {
			for (int i = 0; i < XNUM; i++) {
				for (int j = 0; j < YNUM; j++) {
					if (ball[i][j] != null) {
						ball[i][j].drawBall(canvas);
					}
				}
			}
		}
		nextBall(canvas);
		createButton(canvas);
	}

	private void createButton(Canvas canvas) {

		//Log.d("createButton", getWidth() +" x " +getHeight());
		//Log.d("createButton", getContext().getWidth() +" x " +getHeight());
		Drawable d = getResources().getDrawable(R.drawable.pause);
		d.setBounds(getPosLeftOfButton(MENU_BUTTON), getPosTopOfButton(MENU_BUTTON), getPosRightOfButton(MENU_BUTTON), getPosBottomOfButton(MENU_BUTTON));
		d.draw(canvas);
		if(isPlayMusic)
		{
			d = getResources().getDrawable(R.drawable.speaker);
		}else{
			d = getResources().getDrawable(R.drawable.speaker_mute);
		}
		d.setBounds(getPosLeftOfButton(SPEAKER_BUTTON), getPosTopOfButton(SPEAKER_BUTTON), getPosRightOfButton(SPEAKER_BUTTON), getPosBottomOfButton(SPEAKER_BUTTON));
		d.draw(canvas);

		d = getResources().getDrawable(R.drawable.undo);
		d.setBounds(getPosLeftOfButton(UNDO_BUTTON), getPosTopOfButton(UNDO_BUTTON), getPosRightOfButton(UNDO_BUTTON), getPosBottomOfButton(UNDO_BUTTON));
		d.draw(canvas);
	}

	private int getPosLeftOfButton(int [] arrayPosButton)
	{
		if(arrayPosButton.length > 0)
		{
			return (getWidth() + arrayPosButton[0]) % getWidth();
		}
		return 0;
	}

	private int getPosRightOfButton(int [] arrayPosButton)
	{
		if(arrayPosButton.length > 2)
		{
			return (getWidth() + arrayPosButton[2]) % getWidth();
		}
		return 0;
	}

	private int getPosTopOfButton(int [] arrayPosButton)
	{
		if(arrayPosButton.length > 1)
		{
			return (getHeight() + arrayPosButton[1]) % getHeight();
		}
		return 0;
	}

	private int getPosBottomOfButton(int [] arrayPosButton)
	{
		if(arrayPosButton.length > 3)
		{
			return (getHeight() + arrayPosButton[3]) % getHeight();
		}
		return 0;
	}

	private boolean touchInButton(int [] arrayPosButton, int x, int y)
	{
		int l = getPosLeftOfButton(arrayPosButton), r = getPosRightOfButton(arrayPosButton);
		int t = getPosTopOfButton(arrayPosButton), b = getPosBottomOfButton(arrayPosButton);

		if(x>l && x <r && y>t &&y<b) return true;

		return false;
	}

	private void nextBall(Canvas canvas){
		int a=202;
		Paint p=new Paint();
		
		for(int i=0;i<XNUM;i++){
			for(int j=0;j<YNUM;j++){
				if(matrix[i][j]==2&&ball[i][j]!=null){
					RadialGradient gradient = new RadialGradient(a-4,
							YOFFSET_INFO - 19, 8, Color.rgb(255, 240, 243), ball[i][j].p.getColor(),
							android.graphics.Shader.TileMode.CLAMP);
					p.setDither(true);
					p.setShader(gradient);
					p.setColor(ball[i][j].p.getColor());
					canvas.drawCircle( a, YOFFSET_INFO - 15, 15, p);
					a+=32;
				}
			}
		}
	}
	public boolean onTouchEvent(MotionEvent event) {
		if(touchInButton(MENU_BUTTON, (int)event.getX(), (int)event.getY())){

		}else if(touchInButton(SPEAKER_BUTTON, (int)event.getX(), (int)event.getY())){
			isPlayMusic = !isPlayMusic;
			savedValues.setRecordPlaybackground(isPlayMusic);
			if(isPlayMusic)
			{
				backgroundMusic.start();
			}else{
				backgroundMusic.pause();
			}

		}else if(touchInButton(MENU_BUTTON, (int)event.getX(), (int)event.getY())){

		}else {
			synchronized (matrix) {
				int i = (int) (event.getX() / 53);
				int j = (int) ((event.getY() - YOFFSET_BOARD) / 53);
				if (i < XNUM && j < YNUM && i >= 0 && j >= 0) {
					switch (matrix[i][j]) {
						case 1:
							dau_x = i;
							dau_y = j;
							// Thuc hien dung qua bong jumper ball[dxFirst][dyFirst]
							// va cho nhay qua bong hien tai
							if (dxFirst != -1 && dyFirst != -1) {
								ball[dxFirst][dyFirst].setJumpBall(false);
								// System.out.println("Vi tri bong:" + dxFirst + " , "
								// + dyFirst + " = false");
								dxFirst = i;
								dyFirst = j;
								ball[dau_x][dau_y].setJumpBall(true);
								// System.out.println("BONG NHAY LAN 2");
							} else {
								ball[dau_x][dau_y].setJumpBall(true);
								dxFirst = i;
								dyFirst = j;
								// System.out.println("BONG NHAY LAN DAU");
							}
							break;
						case 0:
							if (dau_x != -1 && dau_y != -1
									&& (dau_x != i || dau_y != j)) {
								Algorithm findWay = new Algorithm(matrix, dau_x, dau_y,
										i, j);
								if (findWay.isGo()) {
									matrix[dau_x][dau_y] = 0;
									matrix[i][j] = 1;
									ball[dau_x][dau_y].setJumpBall(false);
									pointXY = findWay.getWay();

									ball[i][j] = ball[dau_x][dau_y];
									ball[i][j].setRunBall(true, pointXY);
									ball[dau_x][dau_y] = null;
									dau_x = -1;
									dau_y = -1;
									dxFirst = -1;
									dyFirst = -1;
									// System.out
									// .println("OK TIM DUOC DUONG DI VA SINH BONG CON");
								} else
									System.out.println("KHONG TIM DUOC DUONG  ");
							}
							break;
						case 2:
							if (dau_x != -1 && dau_y != -1
									&& (dau_x != i || dau_y != j)) {
								Algorithm findWay = new Algorithm(matrix, dau_x, dau_y,
										i, j);
								if (findWay.isGo()) {
									ball[dau_x][dau_y].setJumpBall(false);
									pointXY = findWay.getWay();
									matrix[dau_x][dau_y] = 0;
									matrix[i][j] = 1;
									int color = ball[i][j].getColor();
									ball[i][j] = ball[dau_x][dau_y];
									ball[i][j].setRunBall(true, pointXY);
									ball[dau_x][dau_y] = null;
									this.generateSmallBall(color);// chua xu ly sinh
									// dung
									// mau bong
									// this.generateSmallBall(3);
									dau_x = -1;
									dau_y = -1;
									dxFirst = -1;
									dyFirst = -1;
								} else
									System.out.println("KHONG TIM DUOC DUONG  TH MT=2");
							}
							break;

					}

				}
			}
		}
		// this.generateSmallBall();
		return false;
	}

	private void drawBackground(Canvas canvas) {
		int maxHeight = YOFFSET_BOARD + YNUM * sizeRectangle;
		Paint background = new Paint();
	//	canvas.clipRect(0, YOFFSET_BOARD, getWidth(), YOFFSET_BOARD + XNUM * sizeRectangle);
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, YOFFSET_BOARD, getWidth(), maxHeight, background);

		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));

		for(int i =0; i<=this.XNUM; i++)
		{
			canvas.drawLine(i*sizeRectangle, YOFFSET_BOARD, i*sizeRectangle, maxHeight, dark);
			canvas.drawLine(i*sizeRectangle + 1, YOFFSET_BOARD, i*sizeRectangle + 1, maxHeight, hilite);
		}
		for(int i =0; i<=this.YNUM; i++)
		{
			canvas.drawLine(0, i*sizeRectangle + YOFFSET_BOARD, getWidth(), i*sizeRectangle + YOFFSET_BOARD, dark);
			canvas.drawLine(0, i*sizeRectangle + 1 + YOFFSET_BOARD, getWidth(), i*sizeRectangle+1 + YOFFSET_BOARD, hilite);
		}

		/*for (int i = 0; i < this.XNUM; i++) {
			for (int j = 0; j < this.YNUM; j++) {
				p.setColor(Color.GRAY);
				canvas.drawLines(new float[] { j * sizeRectangle,
						i * sizeRectangle + sizeRectangle - 1,
						j * sizeRectangle, i * sizeRectangle,
						j * sizeRectangle + sizeRectangle - 1,
						i * sizeRectangle }, p);
				p.setColor(Color.BLACK);
				canvas.drawLines(new float[] { j * sizeRectangle,
						i * sizeRectangle + sizeRectangle - 2,
						j * sizeRectangle + sizeRectangle - 2,
						i * sizeRectangle + sizeRectangle - 2,
						j * sizeRectangle + sizeRectangle - 2,
						i * sizeRectangle }, p);
			}
		}
		p.setColor(Color.BLACK);
		canvas.drawLine(this.getWidth() - 3, 0, this.getWidth() - 3,
				this.getHeight(), p);*/
		//-----------------------------------------------------draw text

		//canvas.clipRect(0, 0, getWidth(), getHeight());
		//p.setColor(0xffe6f0ff);
		p.setColor(Color.LTGRAY);
		canvas.drawRect(new Rect(0, YOFFSET_INFO - 45, this.getWidth(), YOFFSET_BOARD - 10), p);
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "digital-7.ttf");
		//Typeface tf = Typeface.create("Times New Roman",Typeface.BOLD);
		p.setColor(Color.WHITE);p.setTextSize(35);p.setTypeface(tf);
		//canvas.drawText(" Score           Next Balls           Time", 0, 30, p);
		p.setColor(Color.BLUE);
		canvas.drawText(MethodsHelper.getScoreInFormat(MARK), 10, YOFFSET_INFO, p);
		canvas.drawText(MethodsHelper.getTimeFormat(TIME), getWidth() - 130, YOFFSET_INFO, p);
	}

	private int MARK=0;
	private long TIME=0;
	int color1 = 0;
	int A = 0;
	int B = 0;

	public void runBall() {
		// TODO Auto-generated method stub

		synchronized (ball) {

			for (int i = 0; i < XNUM; i++) {
				for (int j = 0; j < YNUM; j++) {

					if (ball[i][j] != null && ball[i][j].isRunBall()) {
						color1 = ball[i][j].getColor();
						ball[i][j].runBall();
						A = i;
						B = j;
						this.runsuccess = true;
					}
				}
			}
			if (runsuccess) {
				runsuccess = false;
				dachay = true;
			} else if (dachay) {
				// Kiem tra vi tri bong moi.
				// Neu OK thi cho Hide ball <Bang cach dat thuoc tinh an bong
				// cho lop Ball>
				// Danh sach bong duoc lay tu toa do bong can an

				// System.out.println("PX="+pX1+" Color="+ color1);
				Scoring score1 = new Scoring(getAllColor());
				if (score1.TinhDiem(A, B, color1)) {
					MARK+=score1.KetQua();System.out.println("XU LY TRONG RUN THREAD");
					for (Point p : score1.arrayList) {
						ball[p.x][p.y].setHideBall(true);
					}
				} else{
					generateSmallBall();A=0;B=0;}
				dachay = false;
			}
		}
	}

	public int[][] getAllColor() {
		int[][] a = new int[XNUM][YNUM];
		for (int i = 0; i < XNUM; i++) {
			for (int j = 0; j < YNUM; j++) {
				if (ball[i][j] != null&&matrix[i][j]==1)
					a[i][j] = ball[i][j].getColor();
				else
					a[i][j] = 0;
			}
		}
		return a;
	}

	public void jumpBall() {
		// TODO Auto-generated method stub
		synchronized (ball) {
			for (int i = 0; i < XNUM; i++) {
				for (int j = 0; j < YNUM; j++) {
					if (ball[i][j] != null) {
						ball[i][j].jumpBall();
					}
				}
			}
		}
	}

	public void generateSmallBall(int color) {
		int index = 0;
		while (index < 1) {
			int row = new Random().nextInt(9);
			int column = new Random().nextInt(9);
			if (this.matrix[row][column] == 0) {
				ball[row][column] = new Ball(row, column, color, sizeRectangle, YOFFSET_BOARD);
				this.matrix[row][column] = 2;
				index++;
			}

		}
	}

	public void generateSmallBall() {
		// Called generateLargeBall from small ball
		synchronized (ball) {
			int index = 0;
			int[] color = new int[] { Color.BLACK, Color.RED,
					Color.rgb(255, 165, 0), Color.CYAN, Color.BLUE,
					Color.GREEN, Color.rgb(255, 0, 255) };
			ShowLargeBall();
			while (index < 3) {
				int row = new Random().nextInt(9);
				int column = new Random().nextInt(9);
				if (this.matrix[row][column] == 0) {
					int i = new Random().nextInt(7);
					ball[row][column] = new Ball(row, column, color[i], sizeRectangle, YOFFSET_BOARD);
					this.matrix[row][column] = 2;
					index++;
				}

			}
		}
		for (int i = 0; i < XNUM; i++) {
			for (int j = 0; j < YNUM; j++) {
				System.out.print(matrix[j][i] + "  ");
			}
			System.out.println();
		}
		for (int i = 0; i < XNUM; i++) {
			for (int j = 0; j < YNUM; j++) {
				if (ball[j][i] != null && ball[j][i].isJumpBall())
					System.out.print(" X ");
				else
					System.out.print(" 0 ");
			}
			System.out.println();
		}

	}

	private void ShowLargeBall() {
		synchronized (ball) {
			for (int i = 0; i < XNUM; i++) {
				for (int j = 0; j < YNUM; j++) {
					if (matrix[i][j] == 2) {
						matrix[i][j] = 1;
						ball[i][j].setSizeBall(true);
						Scoring score1 = new Scoring(getAllColor());
						if (score1.TinhDiem(i, j, ball[i][j].getColor())) {
							MARK+=score1.KetQua();
							System.out.println("XU LY TRONG LARGBALL");
							for (Point p : score1.arrayList) {
								ball[p.x][p.y].setHideBall(true);
							}
						}
					}
				}
			}
		}
	}

	public void hideBall() {
		// TODO Auto-generated method stub
		synchronized (ball) {

			for (int i = 0; i < XNUM; i++) {
				for (int j = 0; j < YNUM; j++) {
					if (ball[i][j] != null && ball[i][j].getLarg_radius() > 0
							&& ball[i][j].isHideBall()) {
						ball[i][j].hideBall();
					}
					if (ball[i][j] != null && ball[i][j].isHideBall()
							&& ball[i][j].getLarg_radius() <= 0) {
						ball[i][j] = null;
						matrix[i][j] = 0;
						System.out.println("DA NULL BONG= " + i + " , " + j);
					}

				}
			}
		}
		/*
		 * if (arr != null) for (Point p : arr) { if
		 * (ball[p.x][p.y].getLarg_radius() > 0 && ball[p.x][p.y] != null) {
		 * ball[p.x][p.y].setHideBall(true); matrix[p.x][p.y] = 0;
		 * ball[p.x][p.y].hideBall(); } else {
		 * ball[p.x][p.y].setHideBall(false); if (p.equals(arr.get(arr.size() -
		 * 1))) { arr = null; break; } }
		 * 
		 * }
		 */

	}

	Thread timestart;

	private boolean fTimeStart = false;
	private boolean fTimeStop = true;
	private void timeStart() {
		if (fTimeStart == false) {
			fTimeStart = true;
			fTimeStop = false;
			timestart = new Thread() {
				public void run() {
					try {
						while (!fTimeStop) {
							sleep(1000L);
							TIME = 1 + TIME;
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
		}
	}


	protected void onPause() {
		timeStop();
		if (backgroundMusic.isPlaying()) {
			backgroundMusic.pause();
		}
	}

	protected void onDestroy() {
		backgroundMusic.release();
		backgroundMusic = null;
	}

	public void onResume()
	{
		if (isPlayMusic) {
			backgroundMusic.start();
		}
		timeStart();
	}
}
