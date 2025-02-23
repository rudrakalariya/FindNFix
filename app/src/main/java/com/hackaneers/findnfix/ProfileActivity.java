package com.hackaneers.findnfix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView userName, userEmail;
    private ListView profileOptionsList;
    private TextView signOutButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        profileOptionsList = findViewById(R.id.profileOptionsList);
        signOutButton = findViewById(R.id.signOutButton);

        // Set user details (replace with actual user data from Firebase or SharedPreferences)
        userName.setText("John Smith");
        userEmail.setText("test@user.com");

        loadUserProfile();



        signOutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });


        // Profile options
        String[] profileOptions = {
                "My Addresses", "Support", "Privacy Policy",
                "About Us", "FAQs", "Change Language"
        };

        // Set adapter for ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profileOptions);
        profileOptionsList.setAdapter(adapter);

        // Handle item click events
        profileOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = profileOptions[position];
                Toast.makeText(ProfileActivity.this, selectedOption + " clicked", Toast.LENGTH_SHORT).show();
                // Implement navigation to respective pages if needed
            }
        });

        // Handle Sign Out Button Click
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                // Implement sign-out logic here (Firebase sign-out if used)
                finish();
            }
        });
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userName.setText(user.getDisplayName() != null ? user.getDisplayName() : "No Name");
            userEmail.setText(user.getEmail());
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
