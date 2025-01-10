package com.example.sisterlyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class InfoDetailsActivity extends AppCompatActivity {

    private EditText nameInput, ageInput, contactInput, nricInput, counselingTypeInput;
    private RadioGroup anonymityRadioGroup;
    private DatabaseReference databaseReference;
    private Button submitButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_details);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        contactInput = findViewById(R.id.contactInput);
        nricInput = findViewById(R.id.nricInput);
        counselingTypeInput = findViewById(R.id.counselingTypeInput);
        anonymityRadioGroup = findViewById(R.id.anonymityRadioGroup);
        backButton = findViewById(R.id.backButtonInfoDetails);
        submitButton = findViewById(R.id.submitButton);

        // Initialize Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");

        // Submit data to Firebase
        submitButton.setOnClickListener(v -> submitDataToFirebase());

        // Back Button Click Listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity
                Intent intent = new Intent(InfoDetailsActivity.this, BookingAppointmentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitDataToFirebase() {
        String name = nameInput.getText().toString().trim();
        String age = ageInput.getText().toString().trim();
        String contact = contactInput.getText().toString().trim();
        String nric = nricInput.getText().toString().trim();
        String counselingType = counselingTypeInput.getText().toString().trim();

        int selectedAnonymityId = anonymityRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedAnonymity = findViewById(selectedAnonymityId);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(contact) ||
                TextUtils.isEmpty(nric) || TextUtils.isEmpty(counselingType) || selectedAnonymity == null) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String anonymity = selectedAnonymity.getText().toString();

        // Create a map for the data
        Map<String, String> appointmentData = new HashMap<>();
        appointmentData.put("Name", name);
        appointmentData.put("Age", age);
        appointmentData.put("Contact", contact);
        appointmentData.put("NRIC", nric);
        appointmentData.put("CounselingType", counselingType);
        appointmentData.put("Anonymity", anonymity);

        // Store the data in Firebase
        databaseReference.push().setValue(appointmentData)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Data submitted successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}