package com.hackaneers.findnfix;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hackaneers.findnfix.models.ServiceRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class ProviderDashboardActivity extends AppCompatActivity {
    private TabLayout requestTabs;
    private RecyclerView requestRecyclerView;
    private RequestAdapter requestAdapter;
    private List<ServiceRequest> requestList;
    private BottomNavigationView bottomNavigation;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_dashboard);

        requestTabs = findViewById(R.id.tabLayout);
        requestRecyclerView = findViewById(R.id.requestRecyclerView);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        providerId = auth.getCurrentUser().getUid(); // Get logged-in provider's ID

        requestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(this, requestList);
        requestRecyclerView.setAdapter(requestAdapter);

        // Load default (Upcoming Requests)
        loadRequests("Pending");

        // Handle Tab Selection
        requestTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadRequests("Pending");
                } else {
                    loadRequests("Completed");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadRequests(String status) {
        if (providerId == null) {
            Log.e("FirestoreDebug", "Provider ID is null! User may not be logged in.");
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("FirestoreDebug", "Loading requests for Provider ID: " + providerId);

        requestList.clear();
        CollectionReference serviceRequestsRef = db.collection("service_requests");

        serviceRequestsRef
                .whereEqualTo("providerId", providerId)
                .whereEqualTo("status", status)
                .orderBy("timestamp", Query.Direction.DESCENDING) // Ensure timestamp exists
                .addSnapshotListener((value, error) -> {


                    if (value == null || value.isEmpty()) {
                        Log.d("FirestoreDebug", "No service requests found for provider.");
//                        Toast.makeText(this, "No requests available.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    requestList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String requestId = doc.getId();
                        String userId = doc.getString("userId"); // Fetch userId from Firestore
                        Log.d("FirestoreDebug", "Found Request ID: " + requestId + ", User ID: " + userId);

                        ServiceRequest request = doc.toObject(ServiceRequest.class);
                        request.setRequestId(requestId);

                        // Fetch customer name using userId
                        fetchCustomerName(userId, request);

                        requestList.add(request);
                    }

                    requestAdapter.notifyDataSetChanged();
                });
    }

    private void fetchCustomerName(String userId, ServiceRequest request) {
        if (userId == null || userId.isEmpty()) {
            Log.e("FirestoreDebug", "User ID is null or empty, cannot fetch customer name.");
            request.setCustomerName("Unknown");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String customerName = documentSnapshot.getString("name");
                        request.setCustomerName(customerName != null ? customerName : "Unknown");

                        // Notify adapter that dataset has changed
                        requestAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Failed to fetch customer name: " + e.getMessage(), e);
                    request.setCustomerName("Unknown");
                });
    }



}
