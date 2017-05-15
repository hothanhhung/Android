package com.hunght.numberlink;

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

    public void btOnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btGameLevel:
                Intent intent = new Intent(this, LevelActivity.class);
                startActivity(intent);
                break;
        }
    }
}
