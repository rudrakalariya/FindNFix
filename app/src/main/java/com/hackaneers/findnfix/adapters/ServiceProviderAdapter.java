package com.hackaneers.findnfix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackaneers.findnfix.R;
import com.hackaneers.findnfix.models.ServiceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderAdapter.ViewHolder> {
    private Context context;
    private static List<ServiceProvider> providerList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public ServiceProviderAdapter(Context context, List<ServiceProvider> providerList) {
        this.context = context;
        this.providerList = providerList;
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void updateList(List<ServiceProvider> newList) {
        providerList.clear();
        providerList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_provider, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceProvider provider = providerList.get(position);
        holder.nameTextView.setText(provider.getName());
        holder.cityTextView.setText(provider.getCity());
        holder.ratingTextView.setText("Rating: " + provider.getRating());
        holder.bookNowButton.setOnClickListener(v -> bookService(provider));
    }

    private void bookService(ServiceProvider provider) {
        String userId = auth.getCurrentUser().getUid(); // Get the logged-in user ID
        if (userId == null) {
            Toast.makeText(context, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a booking request
        Map<String, Object> booking = new HashMap<>();
        booking.put("userId", userId);
        booking.put("providerId", provider.getId());  // ID of the service provider
        booking.put("providerName", provider.getName());
        booking.put("service", provider.getProfession());
        booking.put("city", provider.getCity());
        booking.put("status", "Pending"); // Initially Pending
        booking.put("timestamp", System.currentTimeMillis());

        // Store in Firestore under 'service_request' collection
        db.collection("service_requests")
                .add(booking)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(context, "Booking Successful!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Booking Failed!", Toast.LENGTH_SHORT).show()
                );
    }




    @Override
    public int getItemCount() {
        return providerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, cityTextView,ratingTextView;
        Button bookNowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.providerName);
            cityTextView = itemView.findViewById(R.id.providerCity);
            ratingTextView = itemView.findViewById(R.id.providerRating);
            bookNowButton = itemView.findViewById(R.id.bookNowButton);

        }

    }
}
