package com.hth.datmon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hth.data.SavedValues;
import com.hth.data.ServiceProcess;

public class MainActivity extends AppCompatActivity {

    EditText etUserName;
    EditText etPassword;
    TextView tvMessage;
    CheckBox cbRememberPasswrord;
    SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
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

        tvMessage.setText("");
        savedValues = new SavedValues(MainActivity.this);
        etUserName.setText(savedValues.getRecordUserName());
        etPassword.setText(savedValues.getRecordPassword());
        cbRememberPasswrord.setChecked(savedValues.getRecordRemember());
    }

    public void btClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btLogin:
                login();
                MethodsHelper.hideSoftKeyboard(MainActivity.this);
                break;
        }
    }

    private void login()
    {
        if(etUserName.getText() == null || etUserName.getText().length() == 0)
        {
            tvMessage.setText("Tên đăng nhập bắt buộc phải nhập");
        }
        else if(etPassword.getText() == null || etPassword.getText().length() == 0)
        {
            tvMessage.setText("Mật khẩu bắt buộc phải nhập");
        }else {
            String username = etUserName.getText().toString();
            String password = etPassword.getText().toString();
            String message = ServiceProcess.login(username, password);
            if (message == null || message.isEmpty()) {
                if(cbRememberPasswrord.isChecked())
                {
                    savedValues.setRecordUserName(username);
                    savedValues.setRecordPassword(password);
                    savedValues.setRecordRemember(true);
                }else{
                    savedValues.setRecordUserName("");
                    savedValues.setRecordPassword("");
                    savedValues.setRecordRemember(false);
                }
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                tvMessage.setText(message);
            }
        }
    }
}
