package com.example.sisterlyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InitialAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_initial_appointment);

        // Initialize buttons
        Button viewDetailsButton = findViewById(R.id.viewAppointmentDetailsButton);
        Button addAppointmentButton = findViewById(R.id.addAppointmentButton);

        // Set click listeners
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Appointment Details Activity
                Intent intent = new Intent(InitialAppointmentActivity.this, ViewAppointmentActivity.class);
                startActivity(intent);
            }
        });

        addAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Add Appointment Activity
                Intent intent = new Intent(InitialAppointmentActivity.this, BookingAppointmentActivity.class);
                startActivity(intent);
            }
        });

    }
}