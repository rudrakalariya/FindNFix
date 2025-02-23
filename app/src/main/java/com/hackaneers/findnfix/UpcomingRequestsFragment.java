package com.hackaneers.findnfix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hackaneers.findnfix.models.ServiceRequest;

import java.util.ArrayList;
import java.util.List;

public class UpcomingRequestsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ServiceRequestAdapter adapter;
    private List<ServiceRequest> requestList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public UpcomingRequestsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUpcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestList = new ArrayList<>();
        adapter = new ServiceRequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchUpcomingRequests();

        return view;
    }

    private void fetchUpcomingRequests() {
        String providerId = auth.getCurrentUser().getUid(); // Get logged-in provider ID

        db.collection("service_requests")
                .whereEqualTo("providerId", providerId)
                .whereEqualTo("status", "pending") // Fetch only pending/upcoming requests
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    requestList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ServiceRequest request = doc.toObject(ServiceRequest.class);
                        requestList.add(request);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }
}
