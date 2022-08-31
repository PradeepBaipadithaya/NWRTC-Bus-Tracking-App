package com.example.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class passanger extends AppCompatActivity {
    EditText conductor_phone_num;
    EditText conductor_bus_num;
    Button track;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passanger);
        conductor_phone_num = findViewById(R.id.conductor_mobile_no);
        conductor_bus_num = findViewById(R.id.conductor_bus_no);
        track = findViewById(R.id.conductor_login);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_num = conductor_phone_num.getText().toString();
                String bus_num = conductor_bus_num.getText().toString();
                Intent intent = new Intent(passanger.this, passenger_fetch_locations.class);
                intent.putExtra("conductor_phone_num",phone_num);
                intent.putExtra("conductor_bus_num",bus_num);
                startActivity(intent);
            }
        });

    }
}