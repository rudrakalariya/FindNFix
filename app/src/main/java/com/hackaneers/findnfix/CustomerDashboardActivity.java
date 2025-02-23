package com.hackaneers.findnfix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerDashboardActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationAddress;
    private GridView serviceGridView;
    private ServiceAdapter serviceAdapter;
    private List<ServiceItem> serviceList;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        locationAddress = findViewById(R.id.locationAddress);
        serviceGridView = findViewById(R.id.serviceGridView);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Fetch and display the user's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fetchUserLocation();
        }

        // Initialize and populate the services list
        serviceList = new ArrayList<>();
        serviceList.add(new ServiceItem("Salon", R.drawable.b_salon));
        serviceList.add(new ServiceItem("Plumbing", R.drawable.plumber));
        serviceList.add(new ServiceItem("Exterminator", R.drawable.exterminator_3186116));
        serviceList.add(new ServiceItem("Home Care", R.drawable.cleaning_5284707));
        serviceList.add(new ServiceItem("TV Repair", R.drawable.tv_11252680));
        serviceList.add(new ServiceItem("Maintenance", R.drawable.maintenance_10104276));
        serviceList.add(new ServiceItem("Electrician", R.drawable.electrician_3842405));
        serviceList.add(new ServiceItem("Trainer", R.drawable.trainer_3474959));
        serviceList.add(new ServiceItem("Chef", R.drawable.chef));
        serviceList.add(new ServiceItem("Carpenter", R.drawable.carpenter_12308436));
        serviceList.add(new ServiceItem("Car Wash", R.drawable.carwash_5284705));
        serviceList.add(new ServiceItem("Beauty", R.drawable.activity_14402826));

        // Set adapter
        serviceAdapter = new ServiceAdapter(this, serviceList);
        serviceGridView.setAdapter(serviceAdapter);

        serviceGridView.setOnItemClickListener((parent, view, position, id) -> {
            ServiceItem selectedService = serviceList.get(position);

            if (userCity == null || userCity.isEmpty()) {
                Toast.makeText(CustomerDashboardActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CustomerDashboardActivity.this, ServiceProvidersActivity.class);
            intent.putExtra("serviceName", selectedService.getName()); // Pass service name
            intent.putExtra("userCity", userCity); // Pass user city
            startActivity(intent);
        });


        // Bottom Navigation Clicks
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true; // Already on Home
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(CustomerDashboardActivity.this, MyBookingsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(CustomerDashboardActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();
                                getCityName(location.getLatitude(), location.getLongitude());
                            } else {
                                Toast.makeText(CustomerDashboardActivity.this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private String userCity = null; // Global variable

    private void getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                userCity = addresses.get(0).getLocality(); // Store city in global variable
                locationAddress.setText(userCity);
            } else {
                userCity = "Unknown City"; // Default value if city cannot be fetched
                locationAddress.setText(userCity);
            }
        } catch (IOException e) {
            e.printStackTrace();
            userCity = "Error fetching city";
            locationAddress.setText(userCity);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchUserLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
