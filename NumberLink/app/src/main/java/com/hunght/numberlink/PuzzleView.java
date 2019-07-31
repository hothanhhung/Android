package com.hunght.numberlink;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.hunght.data.StaticData;

import java.util.ArrayList;

/**
 * Created by Lenovo on 6/3/2016.
 */
public class PuzzleView extends View {
    private final GameActivity game;
    private boolean needDelete = false;
    private boolean isShowLine = true;
    BitmapShader mBitmapShaderBackground;
    BitmapShader mBitmapShaderRock;

    public PuzzleView(Context context)
    {
        super(context);
        this.game = (GameActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        initVars(context);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.game = (GameActivity) context;
        initVars(context);
        //setFocusable(true);
        //setFocusableInTouchMode(true);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.game = (GameActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        initVars(context);
    }

    private float width;
    private float height;
    private int selX;
    private int selY;
    private final Rect selRect = new Rect();

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        width = w/(1.0f * StaticData.getNumberColumns());
        height = h/(1.0f * StaticData.getNumberRows());
        if(width > 75)width = 75;
        if(width > height) width = height;
        else height = width;
        getRect(selX, selY, selRect);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void onMeasure(int widthSpec, int heightSpec) {
        try{
            super.onMeasure(widthSpec, heightSpec);
            if(StaticData.getNumberColumns() > 6) {
                int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
                if(size > StaticData.getNumberColumns() * 75){
                    size = StaticData.getNumberColumns() * 75;
                }
                setMeasuredDimension(size, size);
            }else {
                setMeasuredDimension((int) (StaticData.getNumberColumns() * 75), (int) (StaticData.getNumberRows() * 75));
            }
        }catch(Exception ex)
        {
            //ex.printStackTrace();
        }
    }

    private void initVars(Context context)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rock);
        mBitmapShaderBackground = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.walll);
        mBitmapShaderRock = new BitmapShader(bitmap1, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    private void getRect(int y, int x, Rect rect)
    {
        rect.set((int) (x * width) + 2, (int) (y * height) + 2, (int) ((x + 1) * width)/* -2*/, (int) ((y + 1) * height/* - 2*/));
    }

    protected void onDraw(Canvas canvas)
    {
        Paint background = new Paint();
        //background.setColor(getResources().getColor(R.color.puzzle_background));
        background.setShader(mBitmapShaderBackground);
        Paint rockPaint = new Paint();
        rockPaint.setShader(mBitmapShaderRock);

        canvas.drawRect(0, 0, getWidth(), getHeight(), background);

        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));
        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));


        for(int i =0; i<StaticData.getNumberColumns(); i++)
        {
            canvas.drawLine(i*width, 0, i*width, getHeight(), light);
            canvas.drawLine(i*width + 1,0,i*width + 1, getHeight(), hilite);
        }

        for(int i =0; i<StaticData.getNumberRows(); i++)
        {
            canvas.drawLine(0, i*height, getWidth(), i*height, light);
            canvas.drawLine(0, i*height + 1, getWidth(), i*height+1, hilite);
        }
        canvas.drawLine(0, StaticData.getNumberRows()*height - 1, getWidth(), StaticData.getNumberRows()*height - 1, dark);
        //canvas.drawLine(0, 9*height - 2, getWidth(), 9*height - 2, hilite);
        canvas.drawLine(StaticData.getNumberColumns()*width - 1, 0, StaticData.getNumberColumns()*width - 1,getHeight(), dark);
        //canvas.drawLine(9*width - 2,0,9*width - 2, getHeight(), hilite);

        Paint canNotChangeValue = new Paint();
        canNotChangeValue.setColor(getResources().getColor(R.color.puzzle_can_not_change_value));

        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextSize(height * 0.7f);
        foreground.setTextScaleX(width / height);
        foreground.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = foreground.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;

        Rect canNotChange = new Rect();
        for(int i = 0; i < StaticData.getNumberRows(); i++)
        {
            for(int j = 0; j < StaticData.getNumberColumns(); j++){
                if(!this.game.canChangeValue(i,j)){
                    getRect(i,j, canNotChange);
                    if(this.game.canSeeValue(i,j)) {
                        canvas.drawRect(canNotChange, canNotChangeValue);
                    }else{
                        canvas.drawRect(canNotChange, rockPaint);
                    }
                }
                canvas.drawText(this.game.getGameItemString(i, j), j * width + x,
                        i*height+y, foreground);
            }
        }
        ArrayList<ArrayList<Integer>> lines = StaticData.getLines();
        if(isShowLine || StaticData.getCurrentGame().isWin()) {
            Paint paintLine = new Paint();
            if(StaticData.getCurrentGame().isWin()) {
                paintLine.setColor(Color.rgb(51, 160, 75));
            }else{
                paintLine.setColor(Color.rgb(40, 165, 135));
            }
            paintLine.setStrokeWidth(5);
            Paint paintLineStartEnd = new Paint();
            if(StaticData.getCurrentGame().isWin()) {
                paintLineStartEnd.setColor(Color.argb(200, 51, 160, 75));
            }else{
                paintLineStartEnd.setColor(Color.argb(200, 255, 153, 51));
            }

            paintLineStartEnd.setStrokeWidth(5);

            float cycleSize = height / 8;
            if(cycleSize > 15) cycleSize = 15;

            for (ArrayList<Integer> line : lines) {
                for (int i = 1; i < line.size(); i++) {
                    int point1 = line.get(i - 1) % 100, point2 = line.get(i) % 100;
                    float startX, startY, stopX, stopY;
                    startY = point1 / StaticData.getNumberColumns() * width + width / 2;
                    startX = point1 % StaticData.getNumberColumns() * height + height / 2;
                    stopY = point2 / StaticData.getNumberColumns() * width + width / 2;
                    stopX = point2 % StaticData.getNumberColumns() * height + height / 2;

                /*if(startX>stopX)
                {
                    startX = startX - width/2;
                    stopX = stopX + width/2;
                }else{
                    startX = startX + width/2;
                    stopX = stopX - width/2;
                }

                if(startY>stopY)
                {
                    startY = startY - height/2;
                    stopY = stopY + height/2;
                }else{
                    startY = startY + height/2;
                    stopY = stopY - height/2;
                }*/

                    canvas.drawLine(startX, startY, stopX, stopY, paintLine);

                    canvas.drawCircle(startX, startY, cycleSize, paintLine);
                    canvas.drawCircle(stopX, stopY, cycleSize, paintLine);
                    if (StaticData.getCurrentGame().isWin()) {
                        if (i == 1) {
                            canvas.drawCircle(startX, startY, 25, paintLineStartEnd);
                        } else if (i == line.size() - 1) {
                            canvas.drawCircle(stopX, stopY, 25, paintLineStartEnd);
                        }
                    }
                }
            }
        }
        if(!StaticData.getCurrentGame().isWin()) {
            Paint selected = new Paint();
            selected.setColor(getResources().getColor(R.color.puzzle_selected));
            canvas.drawRect(selRect, selected);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY-1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY+1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX-1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX+1, selY);
                break;
            default:
                return  super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void select(int y, int x){
        if(game.canChangeValue(x, y)) {
            needDelete = true;
            invalidate(selRect);
            selX = Math.min(Math.max(x, 0), StaticData.getNumberRows() - 1);
            selY = Math.min(Math.max(y, 0), StaticData.getNumberColumns() - 1);
            getRect(selX, selY, selRect);
            invalidate(selRect);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        if(StaticData.getCurrentGame().isWin()) return true;
        if(event.getAction() != MotionEvent.ACTION_DOWN){
            return super.onTouchEvent(event);
        }
        select((int) (event.getX() / width), (int) (event.getY() / height));
        //game.showKeypadOrError(selX,selY);
        game.resetNumberButton();
        return true;
    }

    public void changeIsShowLineValue(boolean isShowLines)
    {
        this.isShowLine = isShowLines;
        invalidate();
    }

    protected  void setSelectedTile(int tile){

        int value = tile;
        if(!needDelete){
            value = game.getGameItem(selX, selY) * 10 + tile;
            if(value > StaticData.getMaxValue())
            {
                value = tile;
            }
        }
        needDelete = false;
        if(tile == -1){
            value = 0;
        }else if(tile == -2)
        {
            value = StaticData.execHint(selX, selY);
        }
        if(game.setTileIfValid(selX, selY, value)){
            invalidate();
        }else{
            startAnimation(AnimationUtils.loadAnimation(game,R.anim.anim));
        }
    }

    public int getSelX() {
        return selX;
    }

    public int getSelY() {
        return selY;
    }
}
