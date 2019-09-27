package com.example.pointofinterest;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<LatLng, String> pointsOfInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
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

        // Add a marker at Storcenter nord and move the camera.
        LatLng storcenterNord = new LatLng (56.170799, 10.188614);
        createPointOfInterest((storcenterNord), "Storcenter nord");

        // Add a marker at Chomsky Lab & Fitts Lawn.
        LatLng chomskyOgFitts = new LatLng(56.172822, 10.189847);
        createPointOfInterest((chomskyOgFitts), "Chomsky Lab og Fitts Lawn.");

        // Move the camera to Fitts Lawn.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(storcenterNord));
    }

    public void createPointOfInterest(LatLng location, String name){
        mMap.addMarker(new MarkerOptions().position(location).title(name));
    }
}

