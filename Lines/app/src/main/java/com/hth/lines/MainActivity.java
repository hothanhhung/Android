package com.hth.lines;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hth.utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    Dialog progressDialog;
    public void btClick(View view) {
        switch (view.getId()){
            case R.id.btNewGame:
                startGame(Data.DIFFICULTY_NEWGAME);
                break;
            case R.id.btContinue:
                startGame(Data.DIFFICULTY_CONTINUES);
                break;
            case R.id.btProfile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.btGetMore:
                //progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait for a moment...", true);;
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait for a moment...", true);
                    }
                });
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        UIUtils.showAlertGetMoreApps(MainActivity.this);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.btAbout:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.btExit:
                finish();
                break;
        }
    }

    private void startGame(int i)
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Data.DIFFICULTY_KEY, i);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();

        SavedValues savedValues = new SavedValues(this);
        Button btContinue = (Button) findViewById(R.id.btContinue);
        if(savedValues.getRecordTime() > 0)
        {
            btContinue.setVisibility(View.VISIBLE);
        }else{
            btContinue.setVisibility(View.GONE);
        }
    }
}
