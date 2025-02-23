package com.hackaneers.findnfix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, cityInput, professionInput;
    private RadioGroup roleGroup;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        cityInput = findViewById(R.id.cityInput);
        professionInput = findViewById(R.id.professionInput);
        roleGroup = findViewById(R.id.roleGroup);
        signUpButton = findViewById(R.id.signUpButton);

        // Hide profession input initially
        professionInput.setVisibility(View.GONE);

        // Handle role selection to toggle profession visibility
        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRole = findViewById(checkedId);
            if (selectedRole != null && selectedRole.getText().toString().equalsIgnoreCase("Service Provider")) {
                professionInput.setVisibility(View.VISIBLE);
                cityInput.setVisibility(View.VISIBLE);// Show profession input for providers
            } else {
                professionInput.setVisibility(View.GONE); // Hide profession input for customers
            }
        });

        // Set OnClickListener for Sign Up Button
        signUpButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String city = cityInput.getText().toString().trim();
        String profession = professionInput.getText().toString().trim();

        // Check if a role is selected
        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        if (selectedRoleId == -1) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRole = findViewById(selectedRoleId);
        String role = selectedRole.getText().toString();

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // If user is a provider, city and profession are required
        if (role.equalsIgnoreCase("Provider")) {
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter your city", Toast.LENGTH_SHORT).show();
                return;
            }
            if (profession.isEmpty()) {
                Toast.makeText(this, "Please enter your profession", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Register user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToDatabase(user.getUid(), name, email, role, city, profession);
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String userId, String name, String email, String role, String city, String profession) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id",userId);
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("role", role);

        // Only save city if it's provided
        if (!city.isEmpty()) {
            userMap.put("city", city);
        }

        // Only add profession if the user is a service provider
        if (role.equalsIgnoreCase("Service Provider")) {
            userMap.put("profession", profession);
            userMap.put("rating", 0);
        }

        db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this, "Error saving user data", Toast.LENGTH_SHORT).show();
                });
    }
}
