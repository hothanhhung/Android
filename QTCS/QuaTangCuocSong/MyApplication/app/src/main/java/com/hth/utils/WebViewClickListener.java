package com.hth.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class WebViewClickListener implements View.OnTouchListener {
    private int position;
    private ViewGroup vg;
    private View wv;
    private boolean isListview;

    public WebViewClickListener(View wv, ViewGroup vg, int position, boolean isListview) {
        this.vg = vg;
        this.position = position;
        this.wv = wv;
        this.isListview = isListview;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
                return true;
            case MotionEvent.ACTION_UP:
                sendClick();
                return true;
        }

        return false;
    }

    public void sendClick() {
    	try{
	    	if(isListview){
		        ListView lv = (ListView) vg;
		        lv.performItemClick(wv, position, 0);
	    	}else{
	            GridView lv = (GridView) vg;
	            lv.performItemClick(wv, position, 0);
	    	}
    	}catch(Exception ex)
    	{
    		
    	}
    }
}

