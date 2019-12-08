package com.example.planningpokeruser.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planningpokeruser.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intentJoin;
    private Button creatSessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentJoin = new Intent(MainActivity.this, JoinActivity.class);

        creatSessionButton = findViewById(R.id.buttonLogin);
        creatSessionButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        if (v == creatSessionButton) {

            startActivity(intentJoin);

        }

    }
}