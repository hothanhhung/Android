package com.hth.lines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.hth.utils.MethodsHelper;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private DrawBallPanel drawBallPanel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawBallPanel = new DrawBallPanel(this);
        setContentView(drawBallPanel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(drawBallPanel!=null){
            drawBallPanel.onPause();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(drawBallPanel!=null){
            drawBallPanel.onResume();
        }

    }

    @Override
    protected void onDestroy() {
        if(drawBallPanel!=null){
            drawBallPanel.onDestroy();
        }
        super.onDestroy();
    }
}
