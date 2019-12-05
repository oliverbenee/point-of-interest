package com.example.poi_drawer.ui.map;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.poi_drawer.BuildConfig;
import com.example.poi_drawer.MainActivity;
import com.example.poi_drawer.PoInterest;
import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.Welcome.WelcomeFragment;
import com.example.poi_drawer.ui.send.SendFragment;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The MapFragment contains the map to be shown to the user. Through this class, the map is:
 *  - Initialized to be viewed.
 *  - Shows all Points of Interest on the map.
 *
 * @author Oliver Medoc Benée Petersen 201806928
 * @author Android Studio templates - For the implementation of the drawer layout
 * @author Coding Café at https://www.youtube.com/watch?v=4kk-dYWVNsc - For guiding the implementation of location services.
 * @author Simplified Coding at https://www.youtube.com/watch?v=jEmq1B1gveM - For reading from the database to the map.
 * @author kishor at https://www.youtube.com/watch?v=xnqXyorehJI - For guiding the implementation of map data to be extracted to Google Maps.
 * @version 2.0
 * @since 17-11-2019
 */
public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        BottomSheetDialog.BottomSheetListener {

    // Google Map nonsense
    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    // User location
    private LocationRequest locationRequest;
    private Location lastLocation;
    private static final int REQUEST_USER_LOCATION_CODE = 99;

    // Firebase database markers are placed in here.
    private ChildEventListener mChildEventListener;
    private DatabaseReference mPointsOfInterest, yourPointsOfInterest;
    private Marker marker;

    // Point of interest stuff
    private boolean isPoiCreated;
    private double latitude, longitude;

    // List of locations. Used for proximity detection.
    private ArrayList<LatLng> markers = new ArrayList<>();
    private boolean isAllowedToMakeSnackbar = false;

    /**
     * Creates the map and display it immedeately upon opening the fragment.
     *
     * @param inflater Puts the fragment on the activity.
     * @param container The activity to put the container on.
     * @param savedInstanceState The previous location of the map. Used for the fragment.
     * @return View containing the map itself.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inflate MapFragment. Basically the same as setContentView.
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        //Get permission to use the location of the device.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        // Creates a field mMapView, which is the map itself.
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        // GETMAPASYNC A SECOND TIME. This fixes a bug on Galaxy S8, where the application would instantly crash upon loading the map.
        mMapView.getMapAsync(this);
        // Populate the map With Points of Interest.
        mPointsOfInterest = FirebaseDatabase.getInstance().getReference().child("pointsofinterest");
        mPointsOfInterest.push().setValue(marker);

        mMapView.onResume(); // needed to get the map to display immediately
        // Field used to ensure, that no Point of Interest can be created before a marker has been set.
        isPoiCreated = false;
        //Initialize map.
        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Creates map to be viewed. Also adds markers. Also enables Google Maps UI settings.
         */
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            System.out.println("------------------------------");
            System.out.println("Preparing to fetch location...");
            // Request permission to fetch location.
            if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);

                // Sleep for 5 seconds, then find the user's location.
                // After that, move the map camera to the user's location.
                // Move the user to their own location on the map, when the MapFragment is opened.
                final Handler handler = new Handler();
                // Delay is necessary to prevent Null Pointer Exceptions
                handler.postDelayed(() -> {
                    Location location = googleMap.getMyLocation();
                    if (location != null) {
                        LatLng target = new LatLng(location.getLatitude(), location.getLongitude());
                        System.out.println("User was found at: (" + target.latitude + ", " + target.longitude + "). Moving camera.");
                        CameraPosition.Builder builder = new CameraPosition.Builder();
                        builder.zoom(15);
                        builder.target(target);
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                    } else {
                        System.out.println("Location is Null! camera not moved!");
                    }
                }, 5000);
            }
            System.out.println("Done fetching location.");
            System.out.println("------------------------------");

            /*
             * Google Maps UI settings:
             *
             * - Allow zooming through pinching.
             * - Allow zooming through double-taps and two-finger taps
             * - Allow rotating the map through the corresponding gesture.
             */

            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);

            /*
             * Create a new Point of Interest by tapping and holding the map.
             * Sauce: https://stackoverflow.com/questions/17143129/add-marker-on-android-google-map-via-touch-or-tap
             */
            mMap.setOnMapClickListener(point -> {
                latitude = point.latitude;
                longitude = point.longitude;
                mMap.clear();
                onMapReady(googleMap);
                mMap.addMarker(new MarkerOptions().position(point)).setTag("clickedPlace");
                isPoiCreated = true;
                Toast.makeText(getContext(),"latitude: " + latitude + ", longitude: " + longitude, Toast.LENGTH_SHORT).show();
            });
        });

        // Folder for creating a Point of Interest.
        ImageView createFolder = rootView.findViewById(R.id.createfolder);
        /*
         * The user is moved to a new SendFragment, when they click the createFolder.
         * Pass latitude and longitude of the location, they have selected along within the fragment transaction.
         * Otherwise, display a toast to the user.
         * @param view unused parameter, but needed for override of onclick.
         */
        createFolder.setOnClickListener(view -> {
            if(isPoiCreated) {
                /*
                 * Bundle things are based upon:
                 * https://stackoverflow.com/questions/16036572/how-to-pass-values-between-fragments
                 */
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude); // Put anything what you want

                SendFragment sendFragment = new SendFragment();
                //Send marker options too.
                sendFragment.setArguments(bundle);

                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                transaction.replace(R.id.nav_host_fragment, sendFragment);
                transaction.commit();
            } else {
                Toast.makeText(getContext(), "You need to add a marker on the map first!", Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * Use get first time run from:
         * https://stackoverflow.com/questions/4636141/determine-if-android-app-is-the-first-time-used
         *
         * To figure out if TapTargetView should be used.
         * Source for TapTargetView:
         * https://github.com/KeepSafe/TapTargetView
         *
         * @return 0 if this is the first time the application has been started.
         * @return 1 if this is the second (or above) time the application has been started.
         * @return 2 if this is the first time the application has been started after an update.
         *
         * Either: display TapTargetView sequence (isFirstTime is 1)
         * Or: do nothing.
         */
        int isFirstTime = appGetFirstTimeRun();
        System.out.println(" GETFIRSTTIMERUN: " + isFirstTime);
        boolean hasPlayedTutorial = ((MainActivity) Objects.requireNonNull(getActivity())).getHasPlayedTutorial();
        System.out.println(" HASPLAYEDWELCOMETUTORIAL: " + hasPlayedTutorial);
        if(isFirstTime == 0 && !hasPlayedTutorial) {
            new Handler().postDelayed(() -> {
                // The TapTargetView tutorial for the user.
                new TapTargetSequence(getActivity())
                        .targets(
                                TapTarget.forView(rootView.findViewById(R.id.mapView), "Welcome To Point of Interest", "This is your map of all Points of interest. Looks pretty neat, no?").transparentTarget(true).targetRadius(60),
                                TapTarget.forView(rootView.findViewById(R.id.mapView), "Create a new Point of Interest", "To create a new Point of Interest, start by pressing the map.").transparentTarget(true).targetRadius(100),
                                TapTarget.forView(rootView.findViewById(R.id.createfolder), "Then, you can tap the create folder to create your new Point of Interest!").transparentTarget(true).targetRadius(100),
                                TapTarget.forView(rootView.findViewById(R.id.mapView), "You should also try going around and find some new Points of Interest for your own!", "Try and tap some and see what happens!").transparentTarget(true),
                                TapTarget.forView(rootView.findViewById(R.id.mapView), "That covers about everything!", "Now let's get out there and find some awesome places!")
                        ).start();
                ((MainActivity)getActivity()).setHasPlayedTutorial(true);
            }, 8000);
        }

        // If a Point of Interest has just been created, make a dialog to allow users to remove the Point of Interest again by tapping the snackbar.
        Bundle bundleFromSendFragment = this.getArguments();
        if(bundleFromSendFragment != null){
            String id = bundleFromSendFragment.getString("poiId");
            if(id != null){
                Handler handler = new Handler();
                System.out.println(id);
                // remove newly created Point of Interest by tapping the undo button .
                Snackbar undoSnackbar = Snackbar.make(mMapView, "Point of Interest created!", Snackbar.LENGTH_INDEFINITE);
                undoSnackbar.setAction("Undo", view -> {
                    mPointsOfInterest.child(id).removeValue();
                    System.out.println("---------------------------");
                    System.out.println("| REMOVE POI AND SNACKBAR |");
                    System.out.println("---------------------------");
                    undoSnackbar.dismiss();
                    System.out.println(id);
                    Snackbar.make(mMapView, "Removed Point of Interest.", Snackbar.LENGTH_SHORT).show();
                    // Brute force removal of the marker from the map by redrawing map.
                    Handler handler1 = new Handler();
                    handler1.postDelayed(() -> {
                        googleMap.clear();
                        onMapReady(googleMap);
                    }, 500);
                });
                handler.postDelayed(() -> {
                    System.out.println("----------------------");
                    System.out.println("| SHOW UNDO SNACKBAR |");
                    System.out.println("----------------------");
                    undoSnackbar.show();
                }, 1000);
                handler.postDelayed(() -> undoSnackbar.dismiss(), 11000);
            }
        }

        // Allow snackbars to be made 15 seconds after starting the application.
        final Handler handler = new Handler();
        handler.postDelayed(() -> isAllowedToMakeSnackbar = true, 150000);

        return rootView;
    }

    /**
     * Determine if application should use TapTargetView.
     * Source: https://stackoverflow.com/questions/4636141/determine-if-android-app-is-the-first-time-used
     * @return 0 if the application has never started before. 1 if the application has started before. 2 if the application has started previously after an update.
     */
    private int appGetFirstTimeRun() {
        //Check if App Start First Time
        SharedPreferences appPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MyAPP", 0);
        int appCurrentBuildVersion = BuildConfig.VERSION_CODE;
        int appLastBuildVersion = appPreferences.getInt("app_first_time", 0);

        Log.d("appPreferences", "app_first_time = " + appLastBuildVersion);

        if (appLastBuildVersion == appCurrentBuildVersion ) {
            return 1; //ya has iniciado la appp alguna vez

        } else {
            appPreferences.edit().putInt("app_first_time",
                    appCurrentBuildVersion).apply();
            if (appLastBuildVersion == 0) {
                return 0; //es la primera vez
            } else {
                return 2; //es una versión nueva
            }
        }
    }

    //Google Maps and locations

    /**
     * Populate Google Maps Markers.
     * Source: Kishor at https://www.youtube.com/watch?v=xnqXyorehJI
     */
    @Override
    public void onMapReady(GoogleMap googleMap){
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Populate markers on map
        System.out.println("Setting up markers...");
        mPointsOfInterest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("ADDING MARKERS...");
                System.out.println("-----------------");
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    PoInterest user = s.getValue(PoInterest.class);
                    // Add a marker on the map for each location.
                    LatLng location = null;
                    if (user != null) {
                        location = new LatLng(user.latitude, user.longitude);
                    }

                    //There was no pretty way to do this. I spent way too much time figuring this out. Refer to the comments below on how it works.
                    // The extra values are used to make the data readable for when tapping the map.
                    assert user != null;
                    googleMap.addMarker(
                            new MarkerOptions()
                                    .position(location)                                                               // Position: position
                                    .title(user.getTitle())                                                           // Title: title
                                    .snippet(user.getComments())                                                      // Snippet: comments
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                            .setTag(user.getCategory());                                                      // Tag: category
                    markers.add(location);

                    System.out.println(location.toString());
                }
                System.out.println("-----------------");
                System.out.println("Markers added!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });

        /*
         * If the user clicks on a marker, and the marker is at the location where the user is about to add a new marker, remove the marker.
         * Else, show a bottom sheet with information about the discovered Point of Interest.
         *
         * @param markerDiscovered the Point of Interest marker discovered.
         * @return tools boolean attribute. Must be boolean.
         */
        // If a marker is clicked, provide Snackbar, that tells which marker has been clicked. Show discovered bottom sheet.
        googleMap.setOnMarkerClickListener(markerDiscovered -> {
            if(markerDiscovered.getTag() != "clickedPlace") {
                // Points of Interest to be added to the Your Points of Interest list.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String username = (user.getDisplayName()).trim().toLowerCase();
                yourPointsOfInterest = FirebaseDatabase.getInstance().getReference()
                        .child("foundpois")
                        .child(username);
                String id = markerDiscovered.getTitle();
                PoInterest foundpointerest = new PoInterest(
                        markerDiscovered.getPosition().latitude,
                        markerDiscovered.getPosition().longitude,  // Position: position
                        markerDiscovered.getTitle(),               // Title: title
                        markerDiscovered.getSnippet(),             // Snippet: comments
                        Objects.requireNonNull(markerDiscovered.getTag()).toString());     // Tag: category
                // Save new Point of Interest to mDatabase.
                yourPointsOfInterest.child(id).setValue(foundpointerest);

                /*
                 * When a Point of Interest has been discovered, change the color and open bottom sheet.
                 * Bottom Sheet stuff
                 * Source: https://codinginflow.com/tutorials/android/modal-bottom-sheet
                 */

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                // "bottomSheet" is the tag for the bottom sheet.

                bottomSheetDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "bottomSheet");

                // Add text.
                final Handler handler = new Handler();
                // Delay is necessary to prevent Null Pointer Exceptions
                handler.postDelayed(() -> bottomSheetDialog.setTextValues(foundpointerest.getTitle(), foundpointerest.getCategory(), foundpointerest.getComments()), 50);

                // The discovered Point of Interest is marked blue.
                markerDiscovered.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            } else {
                // If the marker, where the user places their Point of Interest is tapped, then remove the marker, and re-add all previous markers.
                googleMap.clear();
                onMapReady(googleMap);
                Snackbar.make(mMapView, "Removed marker for new Point of Interest!", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    /**
     * Ensures, that the map instance is displayed again, when the fragment is resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * Pauses the map instance when no longer viewed.
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * Destroys the map instance when application is closed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * Handles low memory situations for the map view.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Required override method for onMarkerClick. Serves no function.
     *
     * @param marker The marker clicked.
     * @return false. No current event associated with the method.
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * Check if the application has permission to access the user's location. If not, make a request to the permission ACCESS_FINE_LOCATION
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     * @return true if application already has permission. false if the application has to request permission.
     */
    private boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            // Application has no permission. User has explicitly denied permission.
            if(ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION))
            {   // Ask specifically for user permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
            }
            return false;
        // Application already has permission. Proceed as planned.
        } else {
            return true;
        }
    }

    /**
     * If permission is granted to use location, show this on the map.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_USER_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (googleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    googleMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getContext(), "Permission denied for location. Things may not work.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Connect to google location client.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    private synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /**
     * Move camera to user location, when it updates.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Location changed.");
        lastLocation = location;

        /*
         * If the user is near a Point of Interest, show a snackbar.
         * https://stackoverflow.com/questions/32284708/how-to-constantly-detect-nearby-marker-locations-from-current-location-in-google
         */

        // Search through nearby locations.
        // If the user is near a Point of Interest, show a snackbar. Force thread to sleep for 3 minutes before showing again.
        // Else, log, that the user is near a Point of Interest, but show nothing.

        System.out.println("Comparing with other locations.");
        Location target = new Location("target");
        for(LatLng point : markers){
            target.setLatitude(point.latitude);
            target.setLongitude(point.longitude);
            if(location.distanceTo(target) < 10000 && isAllowedToMakeSnackbar) {
                System.out.println("-------------------------------------");
                System.out.println("| Show snackbar for nearby location |");
                System.out.println("-------------------------------------");
                Snackbar nearbysnackbar = Snackbar.make(mMapView, "A Point of Interest is nearby.", Snackbar.LENGTH_LONG);
                nearbysnackbar.setAction("Show me!", view -> googleMap.moveCamera(CameraUpdateFactory.newLatLng(point)));

                isAllowedToMakeSnackbar = false;
                // The next Point of Interest snackbar may be made after 3 minutes.
                final Handler handler = new Handler();
                handler.postDelayed(() -> isAllowedToMakeSnackbar = true, 180000);
            } else {
                Log.d(TAG, "A Point of Interest is nearby, but no snackbar may be made yet.");
            }
        }
    }

    /**
     * Make requests for user locations at an interval of 1100 ms.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        // 10000 and 5000
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    /**
     * Unused override method.
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Unused override method.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Unused override method. Contains a debugging toast.
     */
    @Override
    public void onButtonClicked(String text) {
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Bottom sheet closed!", Toast.LENGTH_SHORT).show();
    }
}