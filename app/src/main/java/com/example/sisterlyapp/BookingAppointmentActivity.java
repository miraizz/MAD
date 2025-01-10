package com.example.sisterlyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class BookingAppointmentActivity extends AppCompatActivity {

    private CalendarView appointmentCalendar;
    private RadioGroup timeRadioGroup, mediumRadioGroup;
    private Button nextButton;
    private ImageView backButton;
    private ProgressBar progressBar;

    private String selectedDate = ""; // Holds the selected date
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_appointment);


        // Initialize views
        appointmentCalendar = findViewById(R.id.appointmentCalendar);
        timeRadioGroup = findViewById(R.id.RGTime);
        mediumRadioGroup = findViewById(R.id.mediumRadioGroup);
        nextButton = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.ProgressBar);
        backButton = findViewById(R.id.backButtonBooking);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("AppointmentDetails");

        // Set listener for date selection
        appointmentCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Format selected date as "YYYY-MM-DD"
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        });

        // Set onClickListener for the Next button
        nextButton.setOnClickListener(v -> storeAppointmentData());

        // Back Button Click Listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity
                Intent intent = new Intent(BookingAppointmentActivity.this, InitialAppointmentActivity.class);
                startActivity(intent);
            }
        });
//
//        // Next Button Click Listener
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to the next activity
//                Intent intent = new Intent(BookingAppointmentActivity.this, InfoDetailsActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void storeAppointmentData() {
        // Get selected time
        int selectedTimeId = timeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedTimeButton = findViewById(selectedTimeId);
        String selectedTime = (selectedTimeButton != null) ? selectedTimeButton.getText().toString() : "";

        // Get selected medium
        int selectedMediumId = mediumRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedMediumButton = findViewById(selectedMediumId);
        String selectedMedium = (selectedMediumButton != null) ? selectedMediumButton.getText().toString() : "";

        // Validate inputs
        if (selectedDate.isEmpty() || selectedTime.isEmpty() || selectedMedium.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Create a map to store the appointment details
        Map<String, String> appointmentDetails = new HashMap<>();
        appointmentDetails.put("Date", selectedDate);
        appointmentDetails.put("Time", selectedTime);
        appointmentDetails.put("Medium", selectedMedium);

        // Push data to Firebase
        databaseReference.push().setValue(appointmentDetails)
                .addOnSuccessListener(unused -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Appointment data saved successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to the next activity
                    Intent intent = new Intent(this, InfoDetailsActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}