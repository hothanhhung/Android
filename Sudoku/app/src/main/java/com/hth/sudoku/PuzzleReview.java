package com.hth.sudoku;

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
public class PuzzleReview extends View {

    Item [] puzzle;
    public PuzzleReview(Context context)
    {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public PuzzleReview(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setFocusable(true);
        //setFocusableInTouchMode(true);
    }

    public PuzzleReview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private float width;
    private float height;

    public Item[] getPuzzle() {
        if(puzzle == null || puzzle.length != 81)
        {
            puzzle = new Item[81];
            for(int i =0; i<81; i++){
                puzzle[i] = new Item(0);
            }
        }
        return puzzle;
    }

    public void setPuzzle(Item[] puzzle) {
        this.puzzle = puzzle;
        this.invalidate();
    }

    public Item getPuzzle(int x, int y)
    {
        int index = y*9 +x;
        if(index < 81) return getPuzzle()[index];
        return new Item(0);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        width = w/9f;
        height = h/9f;
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
                if(!getPuzzle(i,j).canChange()){
                    getRect(i,j, canNotChange);
                    canvas.drawRect(canNotChange, canNotChangeValue);
                }
                canvas.drawText(this.getPuzzle(i,j).getValueInString(), i * width + x,
                        j*height+y, foreground);
            }
        }
    }

}
