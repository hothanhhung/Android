package com.hunght.utils;

import android.content.Context;
import android.util.AttributeSet;

public class AspectRatioImageView extends androidx.appcompat.widget.AppCompatImageView {

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
    }
}