package com.hunght.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {
    private static final String TAG = "myhunght";
    private GestureDetector gestureDetector;
    private Context context;

    public OnSwipeTouchListener(Context c) {
        this.context = c;
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends
            GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                long minWidth = Math.round(Resources.getSystem().getDisplayMetrics().widthPixels * 0.5);
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                Log.d(TAG, "minWidth: "+minWidth+" diffX: "+diffX);
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > minWidth && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    super.onFling(e1, e2, velocityX, velocityY);
                    /*if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }*/
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    private void onSwipeUp() {
    }

    private void onSwipeDown() {
    }

    private void onClick() {
    }

    private void onDoubleClick() {
    }

    private void onLongClick() {
    }
}
