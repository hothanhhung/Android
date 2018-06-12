package com.hth.eyetest;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Lenovo on 12/26/2016.
 */

public class DialogIngameMenu extends DialogFragment {

    GameActivity gameActivity;
    boolean isPlaying;
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.fragment_ingame_menu_dialog, container, false);
        if(isPlaying){
            Button btResume = (Button)view.findViewById(R.id.btResume);
            Button btPlay = (Button)view.findViewById(R.id.btPlay);
            btResume.setVisibility(View.VISIBLE);
            btPlay.setText("Replay");
        }else{
            Button btResume = (Button)view.findViewById(R.id.btResume);
            Button btPlay = (Button)view.findViewById(R.id.btPlay);
            btResume.setVisibility(View.GONE);
            btPlay.setText("Play");
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Activity activity) {
        gameActivity = (GameActivity) activity;
        super.onAttach(activity);
    }

    public void show(FragmentManager manager, String tag, boolean isPlaying)
    {
        this.isPlaying = isPlaying;
        super.show(manager, tag);
    }

    public void onIngameMenuClick(View view)
    {
       // gameActivity.menuIngameClick(view.getId());
    }
}
