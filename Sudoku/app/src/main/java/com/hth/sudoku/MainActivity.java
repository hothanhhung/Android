package com.hth.sudoku;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.hth.utils.DataBaseHelper;
import com.hth.utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    Dialog difficultyMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.createDataBase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        difficultyMenu = createDifficultyMenu();
    }

    Dialog progressDialog;
    public void btClick(View view) {
        switch (view.getId()){
            case R.id.btNewGame:
                openNewGameDialog();
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

    private void openNewGameDialog(){
        SavedValues savedValues = new SavedValues(this);
        if(savedValues.getRecordTime() > 0)
        {
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
                            showDialogDifficulty();
                        }
                    }).show();
        }else{
            showDialogDifficulty();
        }
    }

    private void showDialogDifficulty()
    {
        if(difficultyMenu == null){
            difficultyMenu = createDifficultyMenu();
        }
        difficultyMenu.show();
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

    private Dialog createDifficultyMenu() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_difficulty);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ;

        Button btEasy = (Button) dialog.findViewById(R.id.btEasy);
        Button btMedium = (Button) dialog.findViewById(R.id.btMedium);
        Button btHard = (Button) dialog.findViewById(R.id.btHard);
        Button btExpert = (Button) dialog.findViewById(R.id.btExpert);
        Button btCreate = (Button) dialog.findViewById(R.id.btCreate);
        Button btSpecial = (Button) dialog.findViewById(R.id.btSpecial);

        btEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGame(Data.DIFFICULTY_EASY);
            }
        });
        btMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGame(Data.DIFFICULTY_MEDIUM);
            }
        });
        btHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGame(Data.DIFFICULTY_HARD);
            }
        });
        btExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGame(Data.DIFFICULTY_EXPERT);
            }
        });
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGame(Data.DIFFICULTY_CREATE);
            }
        });
        btSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startGame(Data.DIFFICULTY_SPECIAL);
            }
        });
        return dialog;
    }
}
