package com.hackaneers.findnfix;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyBookingsActivity extends AppCompatActivity {
    private RecyclerView bookingsRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private TextView noBookingsText;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        // Initialize UI elements
        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView);
        noBookingsText = findViewById(R.id.noBookingsText);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize booking list and adapter
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(this, bookingList);
        bookingsRecyclerView.setAdapter(bookingAdapter);

        if (currentUser != null) {
            fetchBookings(currentUser.getUid());
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchBookings(String userId) {
        db.collection("service_requests")
                .whereEqualTo("userId", userId)  // Filter bookings by userId
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bookingList.clear();
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d("FirestoreDebug", "No bookings found for user.");
                        noBookingsText.setVisibility(View.VISIBLE);
                        bookingsRecyclerView.setVisibility(View.GONE);
                    } else {
                        noBookingsText.setVisibility(View.GONE);
                        bookingsRecyclerView.setVisibility(View.VISIBLE);

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Booking booking = doc.toObject(Booking.class);
                            if (booking != null) {
                                booking.setBookingId(doc.getId()); // Set Firestore document ID

                                // Check and convert timestamp if available
                                if (doc.contains("timestamp")) {
                                    try {
                                        long timestampLong = doc.getLong("timestamp"); // Get as long
                                        String formattedDate = convertTimestampToDate(timestampLong);
                                        booking.setBookingDate(formattedDate); // Update booking date
                                    } catch (Exception e) {
                                        Log.e("FirestoreError", "Invalid timestamp format", e);
                                    }
                                }
                                bookingList.add(booking);
                            }
                        }
                        bookingAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error fetching bookings: " + e.getMessage(), e);
                    Toast.makeText(MyBookingsActivity.this, "Failed to load bookings", Toast.LENGTH_SHORT).show();
                });
    }

    // Convert timestamp (stored as a number) to readable date format
    private String convertTimestampToDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp)); // Convert to readable date
    }

}
