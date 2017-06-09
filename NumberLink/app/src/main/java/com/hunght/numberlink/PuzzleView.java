package com.hunght.numberlink;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

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
        width = w/9f;
        height = h/9f;
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

        for(int i =0; i<9; i++)
        {
            canvas.drawLine(0, i*height, getWidth(), i*height, light);
            canvas.drawLine(0, i*height + 1, getWidth(), i*height+1, hilite);
            canvas.drawLine(i*width, 0, i*width,getHeight(), light);
            canvas.drawLine(i*width + 1,0,i*width + 1, getHeight(), hilite);
        }
        for(int i=0; i<9;i++)
        {
            if(i%3 == 0){
                canvas.drawLine(0, i*height, getWidth(), i*height, dark);
               // canvas.drawLine(0, i*height - 1, getWidth(), i*height-1, hilite);
                canvas.drawLine(i*width, 0, i*width,getHeight(), dark);
               // canvas.drawLine(i*width - 1,0,i*width - 1, getHeight(), hilite);

            }
        }
        canvas.drawLine(0, 9*height - 1, getWidth(), 9*height - 1, dark);
        //canvas.drawLine(0, 9*height - 2, getWidth(), 9*height - 2, hilite);
        canvas.drawLine(9*width - 1, 0, 9*width - 1,getHeight(), dark);
        //canvas.drawLine(9*width - 2,0,9*width - 2, getHeight(), hilite);

        Paint canNotChangeValue = new Paint();
        canNotChangeValue.setColor(getResources().getColor(R.color.puzzle_can_not_change_value));

        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextSize(height * 0.75f);
        foreground.setTextScaleX(width / height);
        foreground.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = foreground.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;

        Rect canNotChange = new Rect();
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++){
                if(!this.game.canChangeValue(i,j)){
                    getRect(i,j, canNotChange);
                    canvas.drawRect(canNotChange, canNotChangeValue);
                }
                canvas.drawText(this.game.getGameItemString(i, j), i * width + x,
                        j*height+y, foreground);
            }
        }

        Paint selected = new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));
        canvas.drawRect(selRect, selected);
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

    public void select(int x, int y){
        if(game.canChangeValue(x, y)) {
            invalidate(selRect);
            selX = Math.min(Math.max(x, 0), 8);
            selY = Math.min(Math.max(y, 0), 8);
            getRect(selX, selY, selRect);
            invalidate(selRect);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() != MotionEvent.ACTION_DOWN){
            return super.onTouchEvent(event);
        }
        select((int) (event.getX() / width), (int) (event.getY() / height));
        //game.showKeypadOrError(selX,selY);
        game.resetNumberButton();
        return true;
    }

    protected  void setSelectedTile(int tile){
        if(game.setTileIfValid(selX, selY, tile)){
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
