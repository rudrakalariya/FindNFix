package com.hackaneers.findnfix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackaneers.findnfix.models.ServiceRequest;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<com.hackaneers.findnfix.models.ServiceRequest> requestList;  // List of requests
    private Context context;

    public RequestAdapter(Context context, List<com.hackaneers.findnfix.models.ServiceRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRequest request = requestList.get(position);

        holder.customerName.setText("Customer: " + (request.getCustomerName() != null ? request.getCustomerName() : "Loading..."));
        holder.serviceType.setText(request.getServiceType());
        holder.requestDateTime.setText("Requested on: " + request.getFormattedTimestamp()); // Use formatted timestamp
        holder.requestStatus.setText("Status: " + request.getStatus());

        // Set status color
        if (request.getStatus().equals("Pending")) {
            holder.requestStatus.setTextColor(Color.RED);
        } else if (request.getStatus().equals("Accepted")) {
            holder.requestStatus.setTextColor(Color.GREEN);
        } else if (request.getStatus().equals("Declined")) {
            holder.requestStatus.setTextColor(Color.GRAY);
        }

        // Hide buttons if request is not pending
        if (!request.getStatus().equals("Pending")) {
            holder.acceptRequest.setVisibility(View.GONE);
            holder.declineRequest.setVisibility(View.GONE);
        } else {
            holder.acceptRequest.setVisibility(View.VISIBLE);
            holder.declineRequest.setVisibility(View.VISIBLE);

            holder.acceptRequest.setOnClickListener(v -> {
                holder.acceptRequest.setVisibility(View.GONE);
                holder.declineRequest.setVisibility(View.GONE);
                updateRequestStatus(request, "Accepted", holder);
            });

            holder.declineRequest.setOnClickListener(v -> {
                holder.acceptRequest.setVisibility(View.GONE);
                holder.declineRequest.setVisibility(View.GONE);
                updateRequestStatus(request, "Declined", holder);
            });
        }
    }





    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView requestName, customerName, serviceType, requestDateTime, requestStatus;
        Button acceptRequest, declineRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestName = itemView.findViewById(R.id.requestName);
            customerName = itemView.findViewById(R.id.customerName);
            serviceType = itemView.findViewById(R.id.serviceType);
            requestDateTime = itemView.findViewById(R.id.requestDateTime);
            requestStatus = itemView.findViewById(R.id.requestStatus);
            acceptRequest = itemView.findViewById(R.id.acceptRequest);
            declineRequest = itemView.findViewById(R.id.declineRequest);
        }
    }

    private void updateRequestStatus(ServiceRequest request, String newStatus, ViewHolder holder) {
        if (request.getRequestId() == null || request.getRequestId().isEmpty()) {
            Toast.makeText(context, "Error: Invalid request ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("service_requests")
                .document(request.getRequestId())
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    request.setStatus(newStatus); // Update local object
                    holder.requestStatus.setText("Status: " + newStatus);
                    holder.requestStatus.setTextColor(newStatus.equals("Accepted") ? Color.GREEN : Color.GRAY);

                    // Disable buttons after successful update
                    holder.acceptRequest.setEnabled(false);
                    holder.declineRequest.setEnabled(false);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to update request: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Re-enable buttons if update fails
                    holder.acceptRequest.setEnabled(true);
                    holder.declineRequest.setEnabled(true);
                });
    }

}
