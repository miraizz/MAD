package com.example.sisterlyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewAppointmentActivity extends AppCompatActivity {

    private TextView tvDate, tvTime, tvMedium, tvCounsellingType, tvAnonymity;
    private ImageView backButtonView;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_appointment);

        // Initialize UI elements
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvMedium = findViewById(R.id.tvMedium);
        tvCounsellingType = findViewById(R.id.tvCounsellingType);
        tvAnonymity = findViewById(R.id.tvAnonymity);
        backButtonView = findViewById(R.id.backButtonView);
        progressBar = findViewById(R.id.progressBar);

        // Show progress bar while loading
        progressBar.setVisibility(View.VISIBLE);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("AppointmentDetails");

        // Fetch the latest appointment details
        fetchAppointmentDetails();

        // Back button click listener
        backButtonView.setOnClickListener(v -> finish());

    }

    private void fetchAppointmentDetails() {
        databaseReference.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);  // Hide progress bar
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, String> appointmentDetails = (HashMap<String, String>) snapshot.getValue();

                        if (appointmentDetails != null) {
                            tvDate.setText("Date: " + (appointmentDetails.get("Date") != null ? appointmentDetails.get("Date") : "N/A"));
                            tvTime.setText("Time: " + (appointmentDetails.get("Time") != null ? appointmentDetails.get("Time") : "N/A"));
                            tvMedium.setText("Medium: " + (appointmentDetails.get("Medium") != null ? appointmentDetails.get("Medium") : "N/A"));
                            tvCounsellingType.setText("Counselling Type: " + (appointmentDetails.get("CounsellingType") != null ? appointmentDetails.get("CounsellingType") : "N/A"));
                            tvAnonymity.setText("Anonymity: " + (appointmentDetails.get("Anonymity") != null ? appointmentDetails.get("Anonymity") : "N/A"));

                        }
                    }
                } else {
                    Toast.makeText(ViewAppointmentActivity.this, "No appointment details found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);  // Hide progress bar
                Toast.makeText(ViewAppointmentActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}