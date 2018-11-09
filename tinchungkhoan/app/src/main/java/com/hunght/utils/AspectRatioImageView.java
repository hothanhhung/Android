package com.hunght.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

public class AspectRatioImageView extends AppCompatImageView {

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        try {
            super.onMeasure(widthSpec, heightSpec);
            int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(size, size);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

   /* @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	try{
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        setMeasuredDimension(width, height);
    	}catch(Exception ex)
    	{
    		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    		//ex.printStackTrace();
    	}
    }*/
}