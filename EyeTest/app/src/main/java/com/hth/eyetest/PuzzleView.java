package com.hth.eyetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hth.data.Data;

import java.io.InputStream;

/**
 * Created by Lenovo on 6/3/2016.
 */
public class PuzzleView extends View {
    private final GameActivity game;


    public PuzzleView(Context context)
    {
        super(context);
        this.game = (GameActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.game = (GameActivity) context;
        //setFocusable(true);
        //setFocusableInTouchMode(true);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.game = (GameActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private float width;
    private float height;
    private int selX;
    private int selY;
    private final Rect selRect = new Rect();

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        width = w/(1f * Data.COL_NUM);
        height = h/(1f * Data.ROW_NUM);
        getRect(selX, selY, selRect);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void onMeasure(int widthSpec, int heightSpec) {
        try{
            super.onMeasure(widthSpec, heightSpec);
            int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(size, size);
        }catch(Exception ex)
        {
            //ex.printStackTrace();
        }
    }

    private void getRect(int x, int y, Rect rect)
    {
        rect.set((int) (x * width) + 2, (int) (y * height) + 2, (int) ((x + 1) * width) -2, (int) ((y + 1) * height - 2));
    }


    protected void onDraw(Canvas canvas)
    {
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);

        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));
        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));

        for(int i =0; i<Data.COL_NUM; i++)
        {
            canvas.drawLine(i*width, 0, i*width,getHeight(), light);
            canvas.drawLine(i*width + 1,0,i*width + 1, getHeight(), hilite);
        }
        for(int i =0; i<Data.ROW_NUM; i++)
        {
            canvas.drawLine(0, i*height, getWidth(), i*height, light);
            canvas.drawLine(0, i*height + 1, getWidth(), i*height+1, hilite);
        }
/*

        Paint foreground = new Paint();
        foreground.setAntiAlias(true);
        foreground.setColor(Color.RED);
        foreground.setStyle(Paint.Style.FILL_AND_STROKE);
        foreground.setStrokeWidth(4.5f);

        try {
            for (int i = 0; i < Data.COL_NUM; i++) {
                for (int j = 0; j < Data.ROW_NUM; j++) {
                    if(game.getValue(i, j)!=0) {
                        ColorFilter filter = new LightingColorFilter(this.game.getColor(i, j), 1);
                        foreground.setColorFilter(filter);
                        //foreground.setColor(this.game.getColor(i, j));
                        if(game.isChild(i, j)){
                            canvas.drawBitmap(bitmapChild, width * i + width/2 -7, height * j + width/2 -7, foreground);
                        }else{
                            canvas.drawBitmap(bitmap, width * i + 5, height * j + 5, foreground);
                        }
                        //canvas.drawCircle(width * i + width/2, height*j + height/2, 16.0f, foreground);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    Bitmap bitmapBallSprite, bitmapChildBallSprite;
    public void addBallSprite(int x, int y, int color) {

        if (bitmapBallSprite == null || bitmapChildBallSprite == null){
            try {
                InputStream is = game.getAssets().open("ball.png");
                bitmapBallSprite = ImageProcess.decodeBitmapFromStream(is, (int) width - 10, (int) height - 10);
                bitmapChildBallSprite = ImageProcess.decodeBitmapFromStream(is, 14, 14);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        Paint ballForeground = new Paint();
        ballForeground.setAntiAlias(true);
        ballForeground.setColor(Color.RED);
        ballForeground.setStyle(Paint.Style.FILL_AND_STROKE);
        ballForeground.setStrokeWidth(4.5f);
        ColorFilter filter = new LightingColorFilter(color, 1);
        ballForeground.setColorFilter(filter);


    }
    public void select(int x, int y){
        selX = Math.min(Math.max(x, 0), Data.COL_NUM - 1);
        selY = Math.min(Math.max(y, 0), Data.ROW_NUM - 1);
    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() != MotionEvent.ACTION_DOWN){
            return super.onTouchEvent(event);
        }
        Log.d("onTouchEvent", event.getX() + " - " + event.getY());
        select((int) (event.getX() / width), (int) (event.getY() / height));
        //game.showKeypadOrError(selX,selY);
        return true;
    }

    protected  void setSelectedTile(int tile){
        /*if(game.setTileIfValid(selX, selY, tile)){
            invalidate();
        }else{
            startAnimation(AnimationUtils.loadAnimation(game,R.anim.anim));
        }*/
    }

    public int getSelX() {
        return selX;
    }

    public int getSelY() {
        return selY;
    }
}
