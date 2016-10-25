package com.hth.datmon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText etUserName;
    EditText etPassword;
    CheckBox cbRememberPasswrord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbRememberPasswrord = (CheckBox) findViewById(R.id.cbRememberPassword);

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                   // MethodsHelper.hideSoftKeyboard(MainActivity.this);
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                 //   MethodsHelper.hideSoftKeyboard(MainActivity.this);
                }
            }
        });
    }

    public void btClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btLogin:
                login();
                break;
        }
    }

    private void login()
    {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }
}
