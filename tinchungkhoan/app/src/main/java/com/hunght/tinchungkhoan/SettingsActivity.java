package com.hunght.tinchungkhoan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.hunght.utils.SavedValues;

public class SettingsActivity extends AppCompatActivity {
    private static SavedValues savedValues;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        savedValues = new SavedValues(this);
        Switch swInsiteBrowser = findViewById(R.id.swInsiteBrowser);
        Switch swPasswordMucDauTu = findViewById(R.id.swPasswordMucDauTu);
        Switch swPasswordApp = findViewById(R.id.swPasswordApp);
        Switch swShowHeader = findViewById(R.id.swShowHeader);
        Switch swHideChungKhoanDauTu = findViewById(R.id.swHideChungKhoanDauTu);
        Switch swHideChiMucDauTu = findViewById(R.id.swHideChiMucDauTu);

        swHideChungKhoanDauTu.setChecked(savedValues.getRecordHideChungKhoanDauTu());
        swHideChungKhoanDauTu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordHideChungKhoanDauTu(isChecked);
            }
        });

        swHideChiMucDauTu.setChecked(savedValues.getRecordHideChiMucDauTu());
        swHideChiMucDauTu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordHideChiMucDauTu(isChecked);
            }
        });

        etPassword = findViewById(R.id.etPassword);

        etPassword.setText(savedValues.getRecordPassword());
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

        swShowHeader.setChecked(savedValues.getRecordShowHeader());
        swShowHeader.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savedValues.setRecordShowHeader(isChecked);
            }
        });

        ((Button) findViewById(R.id.btDisplayPassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (!isChecked) {
                    // show password
                  //  etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //} else {
                    // hide password
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
              //  }
            }
        });

        ((Button) findViewById(R.id.btSavePassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedValues.setRecordPassword(etPassword.getText().toString());
            }
        });
    }

    public void onClickBack(View v) {
        finish();
    }

	@Override
    protected  void onResume()
    {
        super.onResume();
      //  checkForShowInterstital();
    }

}
