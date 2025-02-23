package com.hackaneers.findnfix;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackaneers.findnfix.models.ServiceRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder> {
    private List<ServiceRequest> requestList;

    public ServiceRequestAdapter(List<ServiceRequest> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRequest request = requestList.get(position);

        // Fetch customer name from Firestore using userId
        fetchCustomerName(request.getCustomerId(), holder.customerName);

        holder.serviceType.setText("Service: " + request.getServiceType());
        holder.status.setText("Status: " + request.getStatus());

        // Format Timestamp

            String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(request.getTimestamp()));
            holder.requestedOn.setText("Requested on: " + formattedDate);


        // Hide buttons after Accept/Reject
        if (!request.getStatus().equals("Pending")) {
            holder.acceptButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);
        } else {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.rejectButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, serviceType, status, requestedOn;
        Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            serviceType = itemView.findViewById(R.id.serviceType);
            status = itemView.findViewById(R.id.status);
            requestedOn = itemView.findViewById(R.id.requestDateTime);
            acceptButton = itemView.findViewById(R.id.acceptRequest);
            rejectButton = itemView.findViewById(R.id.declineRequest);
        }
    }

    private void fetchCustomerName(String serviceRequestId, TextView customerTextView) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("Firestore", "Fetching service request: " + serviceRequestId);

        db.collection("service_requests").document(serviceRequestId)
                .get()
                .addOnSuccessListener(serviceRequestSnapshot -> {
                    if (serviceRequestSnapshot.exists()) {
                        String userId = serviceRequestSnapshot.getString("userId"); // Ensure correct field name

                        if (userId != null && !userId.isEmpty()) {
                            Log.d("Firestore", "User ID found: " + userId);

                            // Fetch customer name from users collection
                            db.collection("users").document(userId)
                                    .get()
                                    .addOnSuccessListener(userSnapshot -> {
                                        if (userSnapshot.exists()) {
                                            String customerName = userSnapshot.getString("name");
                                            Log.d("Firestore", "Fetched Customer Name: " + customerName);
                                            customerTextView.setText("Customer: " + (customerName != null ? customerName : "Unknown"));
                                        } else {
                                            Log.e("Firestore", "User document not found for ID: " + userId);
                                            customerTextView.setText("Customer: Unknown");
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Failed to fetch user data: " + e.getMessage(), e);
                                        customerTextView.setText("Customer: Unknown");
                                    });
                        } else {
                            Log.e("Firestore", "User ID is null or empty in service_requests for ID: " + serviceRequestId);
                            customerTextView.setText("Customer: Unknown");
                        }
                    } else {
                        Log.e("Firestore", "Service request document not found for ID: " + serviceRequestId);
                        customerTextView.setText("Customer: Unknown");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to fetch service request: " + e.getMessage(), e);
                    customerTextView.setText("Customer: Unknown");
                });
    }





}
