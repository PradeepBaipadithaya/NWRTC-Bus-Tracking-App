package com.example.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class conductor_login extends AppCompatActivity {
    Button conductor_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_login);
        conductor_login = findViewById(R.id.conductor_login);
        conductor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(conductor_login.this, conductor_locate.class);
                startActivity(intent);
            }
        });
    }
}