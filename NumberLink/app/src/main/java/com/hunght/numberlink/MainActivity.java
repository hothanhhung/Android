package com.hunght.numberlink;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunght.data.DataProcess;
import com.hunght.data.GameItem;
import com.hunght.data.LevelItem;
import com.hunght.data.SavedValues;
import com.hunght.data.StaticData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "numberlink.inappbilling";

    GridView grvLevelItems;
    SavedValues savedValues;

    GridviewLevelItemAdapter gridviewLevelItemAdapter;

    static final int RC_REQUEST = 10001;
    // The helper object
    static String SKU_GAS = "numberlink.level55.level56";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* In-app purchase */
        // enable debug logging (for a production application, you should set this to false).

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.

        savedValues= new SavedValues(this);
        grvLevelItems = (GridView) findViewById(R.id.grvLevelItems);

        ArrayList<LevelItem> levelItems = DataProcess.getLevelItems(this);

        if(StaticData.isStart()) {
            String currentGameId = savedValues.getCurrentGameId();
            if (currentGameId != "") {
                for (LevelItem levelItem : levelItems) {
                    for (GameItem gameItem : levelItem.getGameItems()) {
                        if (gameItem.getId().equalsIgnoreCase(currentGameId)) {
                            StaticData.setCurrentLevel(levelItem);
                            StaticData.setCurrentGame(gameItem);
                            Intent intent = new Intent(this, GameActivity.class);
                            this.startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
            }
        }
        gridviewLevelItemAdapter = new GridviewLevelItemAdapter(this, levelItems);
        grvLevelItems.setAdapter(gridviewLevelItemAdapter);
    }

    protected void onResume() {
        super.onResume();
        if(gridviewLevelItemAdapter != null) gridviewLevelItemAdapter.notifyDataSetChanged();
        System.gc();
    }

    View vLockLevel = null;
    Dialog dialogUnlockLevel;
    public void showDialog(View view, final LevelItem levelItem){
        dialogUnlockLevel = new Dialog(MainActivity.this);
        vLockLevel = view;
        dialogUnlockLevel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUnlockLevel.setContentView(R.layout.lock_level_dialog);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialogUnlockLevel.getWindow().setBackgroundDrawable(dialogColor);
        dialogUnlockLevel.setCanceledOnTouchOutside(false);
        dialogUnlockLevel.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView text = (TextView) dialogUnlockLevel.findViewById(R.id.tvLockLevelMessage);
        text.setText(levelItem.getLockMessage());

        Button btClose = (Button) dialogUnlockLevel.findViewById(R.id.btClose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUnlockLevel.dismiss();
            }
        });
        dialogUnlockLevel.show();

    }

    public void btPrivacyPolicy(View view){
        try {
            Uri marketUri = Uri.parse("http://hunght.com/htmlpage/privacy.html");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

    }

    private void updateUIandSaveData(){
        if(vLockLevel!=null) {
            LevelItem levelItem = (LevelItem)vLockLevel.getTag();
            if(levelItem!=null)
            {
                StaticData.addUnloclLevel(levelItem.getLevelIdInString());
                savedValues.setUnlockLevels(StaticData.getUnloclLevels());
            }
            vLockLevel.setVisibility(View.GONE);
        }
    }

    private void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
