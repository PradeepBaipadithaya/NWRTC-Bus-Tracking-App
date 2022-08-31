package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button loginp, loginc,logina,logint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginp = findViewById(R.id.pass1);
        loginc = findViewById(R.id.pass2);
        logint = findViewById(R.id.pass3);
        logina = findViewById(R.id.conductor_login);

        logina.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        logina.animate().setDuration(500).rotationXBy(360f).start();

    }
});

loginp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        loginp.animate().setDuration(500).rotationXBy(360f).start();
        Intent i =new Intent(MainActivity.this, passanger.class);
        startActivity(i);

    }
});
loginc.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        loginc.animate().setDuration(500).rotationXBy(360f).start();
        Intent i =new Intent(MainActivity.this, conductor_login.class);
        startActivity(i);
    }
});
        logina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginc.animate().setDuration(500).rotationXBy(360f).start();
                Intent i =new Intent(MainActivity.this, Admin_login.class);
                startActivity(i);
            }
        });
        logint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginc.animate().setDuration(500).rotationXBy(360f).start();
                Intent i =new Intent(MainActivity.this, ticket_collector_login.class);
                startActivity(i);
            }
        });
    }

}