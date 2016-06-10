package com.hth.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hth.sudoku.Data;
import com.hth.sudoku.GameActivity;
import com.hth.sudoku.PuzzleReview;
import com.hth.sudoku.R;
import com.hth.sudoku.SudokuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Lenovo on 6/9/2016.
 */
public class MethodsHelper {

    static public String getCurrentDateToOrder(){
        return getCurrentDate("yyyyMMddHHmmss");
    }

    static public String getCurrentDate(){
        return getCurrentDate("HH:mm:ss - MMM dd, yyyy");
    }

    static public String getCurrentDate(String format){
        Date now = new Date();
        String formattedDate = new SimpleDateFormat(format).format(now);
        return formattedDate;
    }

    static public Dialog createProfileDetailDialog(final SudokuItem sudokuItem, final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profile);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ;

        ((TextView) dialog.findViewById(R.id.tvLevel)).setText(sudokuItem.getName());
        ((TextView) dialog.findViewById(R.id.tvTime)).setText(sudokuItem.getTotalTime());
        ((TextView) dialog.findViewById(R.id.tvMoves)).setText(""+sudokuItem.getChanges());
        ((TextView) dialog.findViewById(R.id.tvStartOn)).setText(sudokuItem.getStartAt());
        ((TextView) dialog.findViewById(R.id.tvEndOn)).setText(sudokuItem.getEndAt());
        ((EditText) dialog.findViewById(R.id.etComment)).setText(sudokuItem.getComment());

        PuzzleReview puzzleViewProfile = (PuzzleReview) dialog.findViewById(R.id.puzzleViewProfile);
        puzzleViewProfile.setPuzzle(sudokuItem.getResolvedItem());

        Button btWingameSaveComment = (Button) dialog.findViewById(R.id.btWingameSaveComment);
        Button btWingamePlay = (Button) dialog.findViewById(R.id.btWingamePlay);
        Button btWingameProfile = (Button) dialog.findViewById(R.id.btWingameProfile);
        Button btWingameDimiss = (Button) dialog.findViewById(R.id.btWingameDimiss);
        btWingamePlay.setText("PLAY AGAIN");
        // if button is clicked, close the custom dialog
        btWingameSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) (context.getSystemService(context.INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                String comment = ((EditText) dialog.findViewById(R.id.etComment)).getText().toString();
                sudokuItem.setComment(comment);
                (new DataBaseHelper(context)).saveCommentWin(sudokuItem.getOriginalMap(), sudokuItem.getComment());
                Toast.makeText(dialog.getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        btWingamePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.ReplayLevel = sudokuItem.getDifficulty();
                Data.ReplayOriginalMap = sudokuItem.getOriginalMap();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra(Data.DIFFICULTY_KEY, Data.DIFFICULTY_PLAY_AGAIN);
                context.startActivity(intent);
                context.finish();
            }
        });

        btWingameDimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }
}
