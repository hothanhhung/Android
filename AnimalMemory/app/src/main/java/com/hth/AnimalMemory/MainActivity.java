package com.hth.AnimalMemory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hth.utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedValues = new SavedValues(this);
    }

    Dialog progressDialog;
    public void btClick(View view) {
        switch (view.getId()){
            case R.id.btNewGame:
                if(savedValues.getRecordLevel() > 0) {
                    new AlertDialog.Builder(this).setTitle(R.string.warning)
                            .setMessage(R.string.warning_delete_old_game)
                            .setCancelable(false)
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startGame(Data.DIFFICULTY_NEWGAME);
                                }
                            }).show();
                }
                break;
            case R.id.btContinue:
                startGame(Data.DIFFICULTY_CONTINUES);
                break;
            case R.id.btGetMore:
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        UIUtils.showAlertGetMoreAppsServer(MainActivity.this);
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

        Button btContinue = (Button) findViewById(R.id.btContinue);
        if(savedValues.getRecordLevel() > 0)
        {
            btContinue.setVisibility(View.VISIBLE);
        }else{
            btContinue.setVisibility(View.GONE);
        }
    }
}
