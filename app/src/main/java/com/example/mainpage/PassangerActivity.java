package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class PassangerActivity extends AppCompatActivity {
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_conductor);
        b1 = findViewById(R.id.locate);
        b1.setOnClickListener(view -> b1.animate().setDuration(500).rotationXBy(360f).start());


    }
}