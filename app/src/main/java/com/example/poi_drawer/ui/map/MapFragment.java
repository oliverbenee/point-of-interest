package com.example.poi_drawer.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.discovered.DiscoveredFragment;
import com.example.poi_drawer.ui.send.SendFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.*;

/**
 * The MapFragment contains the map to be shown to the user. Through this class, the map is:
 *  - Initialized to be viewed.
 *  - Shows all Points of Interest on the map.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @version 1.0
 * @since 07-10-2019
 */
public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private MapView mMapView;
    private GoogleMap googleMap;

    /*
     * Creates the map and display it immedeately upon opening the fragment.
     *
     * @param inflater Puts the fragment on the activity.
     * @param container The activity to put the container on.
     * @param savedInstanceState The previous location of the map. Used for the fragment.
     * @return View containing the map itself.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Ask for permission to use location.
        googleMap.setMyLocationEnabled(true);

        //Inflate MapFragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Creates a field mMapView, which is the map itself.
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Creates map to be viewed. Also adds a Marker in Sydney.
         * TODO: Create markers from list.
         */
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            // For showing a move to my location button
            googleMap.setMyLocationEnabled(true);

            ArrayList markers = new ArrayList<>();
            // Creates 3 new markers on the map.
            // Create Sydney.
            LatLng sydney = new LatLng(-34, 151);
            Marker sydneymarker = googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Sydney")
                .snippet("Click to view more."));
            markers.add(sydneymarker);

            // Create Lyngbygaard Golf.
            LatLng Lyggolf = new LatLng(56.170128, 10.030579);
            Marker lyggolfmarker = googleMap.addMarker(new MarkerOptions()
                .position(Lyggolf)
                .title("Lyngbygaard Golf")
                .snippet("Click to view more"));
            markers.add(lyggolfmarker);

            // Create Storcenter Nord.
            LatLng Storcenternord = new LatLng(56.170721, 10.188607);
            Marker storcenternordmarker = googleMap.addMarker(new MarkerOptions()
                .position(Storcenternord)
                .title("Storcenter Nord")
                .snippet("Click to view more"));
            markers.add(storcenternordmarker);

            // Allow zooming
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);

            /*
             * Creates a SnackBar, which tells the user which Point of Interest has been discovered.
             *
             * @param marker the Point of Interest marker discovered.
             * @return tools boolean attribute. Must be boolean.
             */
            // If a marker is clicked, provide Snackbar, that tells which marker has been clicked. Redirect to Discovered fragment.
            mMap.setOnMarkerClickListener(markerDiscovered -> {
                Snackbar.make(mMapView, "Point of Interest " + markerDiscovered.getTitle() + " Discovered!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

                 //Create a fragmentTransaction to send the user to the DiscoveredFragment.
                 DiscoveredFragment discoveredFragment = new DiscoveredFragment();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();
                 transaction.replace(R.id.nav_host_fragment, discoveredFragment);
                 transaction.commit();
                return true;
            });
        });

        Button createButton = rootView.findViewById(R.id.createpoi);
        createButton.setOnClickListener(new View.OnClickListener() {

            /*
             * The user is moved to a new SendFragment, when they click the createButton.
             * @param view unused parameter, but needed for override of onclick.
             */
            @Override
            public void onClick(View view) {
                SendFragment sendFragment = new SendFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, sendFragment);
                transaction.commit();
            }
        });

        return rootView;
    }

    /*
     * Ensures, that the map instance is displayed again, when the fragment is resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /*
     * Pauses the map instance when no longer viewed.
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /*
     * Destroys the map instance when application is closed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /*
     * Handles low memory situations for the map view.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /*
     * Required override method for onMarkerClick. Serves no function.
     *
     * @param marker The marker clicked.
     * @return false. No current event associated with the method.
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}