package com.hunght.utils;

import com.hunght.tinchungkhoan.MainActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebview extends WebView{

	Context context;
	private boolean isScroll = false;
	private boolean isScrollUp = false;
	
	public MyWebview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public MyWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
		this.context = context;
    }

    public MyWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		this.context = context;
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(isScroll && event.getAction() == MotionEvent.ACTION_UP)
		{
			((MainActivity)context).updateWhenScrollChange(isScrollUp);
			isScroll = false;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onScrollChanged (int l, int t, int oldl, int oldt)
	{
		isScroll = true;
		isScrollUp = (oldt > t);
	}
}
