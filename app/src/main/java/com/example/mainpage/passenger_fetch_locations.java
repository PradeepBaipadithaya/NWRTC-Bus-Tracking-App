package com.example.mainpage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mainpage.databinding.ActivityPassengerFetchLocationsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class passenger_fetch_locations extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityPassengerFetchLocationsBinding binding;
    public DatabaseReference reference;
    public DatabaseReference conductor_phone_num;
    public DatabaseReference conductor_bus_num;
    public LocationManager manager;
    private final int MIN_TIME =1000;//for update
    private final int MIN_DISTANCE =1;//for update
    public double locationLat;
    public double locationLong;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;

    Marker myMarker;
    Marker myMarker_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPassengerFetchLocationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        context = getApplicationContext();
        boolean location_flag=isLocationEnabled(context);

        reference = FirebaseDatabase.getInstance("https://awatar-360605-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Location details").child("Passenger").child("pass_1").child("location");
//        conductor_phone_num = FirebaseDatabase.getInstance("https://awatar-360605-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Conductor").child("conductor_2").child("phone_num");
//        conductor_bus_num = FirebaseDatabase.getInstance("https://awatar-360605-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Conductor").child("conductor_2").child("bus_num");

//        //To get current location
        if(location_flag==true) {
            manager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria cri = new Criteria();
            String provider = manager.getBestProvider(cri, false);

            Location location = manager.getLastKnownLocation(provider);
            if (location != null) {
                locationLat = location.getLatitude();
                locationLong = location.getLongitude();

                getLocationUpdates();
                readChanges();
                markBusLocation();

            } else {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
            }
        }
        else {
            Toast.makeText(context, "Turn ON Location", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
    private void markBusLocation() {
//        if (phone_num.equals("123") && bus_num.equals("123")) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        MyLocation location = snapshot.getValue(MyLocation.class);
                        if (location != null) {
                            myMarker.setPosition(new LatLng(12.7416, 75.260));
                        }
                    } catch (Exception e) {
                        Toast.makeText(passenger_fetch_locations.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    try{
                        MyLocation location = snapshot.getValue(MyLocation.class);
                        if(location!=null){
                            myMarker_user.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
                        }
                    }catch (Exception e){
                         Toast.makeText(passenger_fetch_locations.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocationUpdates() {
        if(manager!=null){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,this);
                }
                else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DISTANCE,this);
                }
                else{
                    Toast.makeText(this, "No provider Enabled", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocationUpdates();
            }
            else{
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                locationLat = location.getLatitude();
//                                locationLong = location.getLongitude();
//                                getLocationUpdates();
//                                readChanges();
//                                markBusLocation();
//                            }
//                        }
//                    });
        // Add a marker in Sydney and move the camera
            LatLng currentLocation = new LatLng(locationLat, locationLong);
            myMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Bus Location"));
            myMarker_user = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//To Zoom upto 10x
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(locationLat, locationLong));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location!=null){
            saveLocation(location);
        }
        else{
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    private void saveLocation(Location location) {
        Toast.makeText(this, ""+location, Toast.LENGTH_SHORT).show();
        reference.setValue(location);
        //reference.setValue(LocationLong);
    }
}