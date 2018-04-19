package com.hunght.solarlunarcalendar;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hunght.data.DateItemForGridview;

import java.util.Date;

/**
 * Created by Lenovo on 4/18/2018.
 */

public class AboutView extends LinearLayout {
    public AboutView(Context context) {
        super(context);
        initView();
    }

    public AboutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AboutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AboutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        View view = inflate(getContext(), R.layout.about_view, this);
    }
}