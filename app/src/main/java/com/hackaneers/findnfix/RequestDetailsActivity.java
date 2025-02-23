package com.hackaneers.findnfix;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackaneers.findnfix.models.ServiceRequest;

public class RequestDetailsActivity extends AppCompatActivity {
    private TextView requestName, customerName, serviceType, requestDateTime, requestStatus;
    private Button acceptRequest, declineRequest;
    private FirebaseFirestore db;
    private String requestId;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        requestName = findViewById(R.id.requestName);
        customerName = findViewById(R.id.customerName);
        serviceType = findViewById(R.id.serviceType);
        requestDateTime = findViewById(R.id.requestDateTime);
        requestStatus = findViewById(R.id.requestStatus);
        acceptRequest = findViewById(R.id.acceptRequest);
        declineRequest = findViewById(R.id.declineRequest);

        db = FirebaseFirestore.getInstance();

        // Get request details from intent
        if (getIntent().hasExtra("request")) {
            ServiceRequest request = (ServiceRequest) getIntent().getSerializableExtra("request");

            requestId = request.getRequestId();
            status = request.getStatus();

            // Display request details
            requestName.setText(request.getServiceType());  // Use service type as the title
            customerName.setText("Customer: " + request.getCustomerName());
            serviceType.setText("Service Type: " + request.getServiceType());
            requestDateTime.setText("Requested on: " + request.getTimestamp());
            requestStatus.setText("Status: " + request.getStatus());

            // Disable buttons if request is already accepted/declined
            if (!status.equals("Pending")) {
                acceptRequest.setEnabled(false);
                declineRequest.setEnabled(false);
            }
        }

        // Handle Accept button
        acceptRequest.setOnClickListener(v -> updateRequestStatus("Accepted"));

        // Handle Decline button
        declineRequest.setOnClickListener(v -> updateRequestStatus("Declined"));
    }

    private void updateRequestStatus(String newStatus) {
        if (requestId == null || requestId.isEmpty()) {
            Toast.makeText(this, "Error: Invalid request ID", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("service_requests")
                .document(requestId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request " + newStatus, Toast.LENGTH_SHORT).show();
                    requestStatus.setText("Status: " + newStatus);
                    acceptRequest.setEnabled(false);
                    declineRequest.setEnabled(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error updating request status", e);
                    Toast.makeText(this, "Failed to update request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
