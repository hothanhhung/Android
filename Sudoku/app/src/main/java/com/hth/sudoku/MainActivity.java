package com.hth.sudoku;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Dialog difficultyMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        difficultyMenu = createDifficultyMenu();
    }

    public void btClick(View view) {
        switch (view.getId()){
            case R.id.btNewGame:
                openNewGameDialog();
                break;
            case R.id.btContinue:
                startGame(Data.DIFFICULTY_CONTINUES);
                break;
            case R.id.btSettings:
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
        return dialog;
    }
}