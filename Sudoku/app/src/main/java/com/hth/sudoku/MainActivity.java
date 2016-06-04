package com.hth.sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btClick(View view) {
        switch (view.getId()){
            case R.id.btNewGame:
                openNewGameDialog();
                break;
            case R.id.btContinue:
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
        new AlertDialog.Builder(this).setTitle(R.string.new_game_title)
                .setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame(which);
                    }
                }).show();
    }

    private void startGame(int i)
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Data.DIFFICULTY_KEY, i);
        startActivity(intent);
    }
}
