package com.hunght.tinchungkhoan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hunght.utils.SavedValues;

public class SettingsActivity extends AppCompatActivity {
    private static SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        savedValues = new SavedValues(this);
        Switch swInsiteBrowser = findViewById(R.id.swInsiteBrowser);
        Switch swPasswordMucDauTu = findViewById(R.id.swPasswordMucDauTu);
        Switch swPasswordApp = findViewById(R.id.swPasswordApp);

        swInsiteBrowser.setChecked(savedValues.getRecordInappBrowser());
        swInsiteBrowser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordInappBrowser(isChecked);
            }
        });


        swPasswordMucDauTu.setChecked(savedValues.getRecordPasswordMucDauTu());
        swPasswordMucDauTu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordPasswordMucDauTu(isChecked);
            }
        });


        swPasswordApp.setChecked(savedValues.getRecordPasswordInApp());
        swPasswordApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordPasswordInApp(isChecked);
            }
        });
    }


	@Override
    protected  void onResume()
    {
        super.onResume();
      //  checkForShowInterstital();
    }

}
