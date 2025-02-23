package com.hackaneers.findnfix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hackaneers.findnfix.adapters.ServiceProviderAdapter;
import com.hackaneers.findnfix.models.ServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceProvidersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceProviderAdapter adapter;
    private List<ServiceProvider> providerList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextView noProvidersText;

    private String selectedService;
    private String userCity; // This should come from CustomerDashboard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        noProvidersText = findViewById(R.id.noProvidersText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        providerList = new ArrayList<>();
        adapter = new ServiceProviderAdapter(this, providerList);
        recyclerView.setAdapter(adapter);

        // Get selected service and city from Intent
        selectedService = getIntent().getStringExtra("serviceName");
        userCity = getIntent().getStringExtra("userCity");

        // 🔹 Debug Logs
        if (selectedService == null || selectedService.isEmpty()) {
            Toast.makeText(this, "Error: Missing service!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userCity == null || userCity.isEmpty()) {
            Toast.makeText(this, "Error: Missing location!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch providers from Firestore
        fetchServiceProviders();
    }


    private void fetchServiceProviders() {
        progressBar.setVisibility(View.VISIBLE); // Start loading

        db.collection("users")
                .whereEqualTo("role", "Service Provider")
                .whereEqualTo("profession", selectedService)
                .whereEqualTo("city", userCity)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE); // ✅ Stop loading

                    if (task.isSuccessful()) {
                        List<ServiceProvider> providerList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getString("id");
                            String name = document.getString("name");
                            String city = document.getString("city");
                            String profession = document.getString("profession");
                            double rating = document.contains("rating") ? document.getDouble("rating") : 0.0;

                            providerList.add(new ServiceProvider(id,name, city,profession, rating)); // No need to pass profession
                        }

                        if (providerList.isEmpty()) {
                            noProvidersText.setVisibility(View.VISIBLE);
                        } else {
                            noProvidersText.setVisibility(View.GONE);
                            adapter.updateList(providerList);
                        }
                    } else {
                        Log.e("FirestoreError", "Failed to fetch providers", task.getException());
                        Toast.makeText(this, "Failed to load providers", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
