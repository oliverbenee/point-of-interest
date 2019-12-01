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
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.poi_drawer.BuildConfig;
import com.example.poi_drawer.MainActivity;
import com.example.poi_drawer.PoInterest;
import com.example.poi_drawer.R;
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
    Marker marker;

    // Point of interest stuff
    private boolean isPoiCreated;
    private double latitude, longitude;

    // Has the tutorial run once?
    private boolean hasPlayedTutorial = false;

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
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Creates map to be viewed. Also adds markers.
         */
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            // Request permission to fetch location.
            if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }

            // Allow zooming
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);

            /*
             * Create a new Point of Interest by tapping and holding the map.
             * Sauce: https://stackoverflow.com/questions/17143129/add-marker-on-android-google-map-via-touch-or-tap
             */
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    //TODO: Tilføj til hashmap og send op til firebase efter?

                    latitude = point.latitude;
                    longitude = point.longitude;
                    mMap.addMarker(new MarkerOptions().position(point));
                    isPoiCreated = true;
                    Toast.makeText(getContext(),"latitude: " + latitude + ", longitude: " + longitude, Toast.LENGTH_SHORT).show();
                }
            });

            /*
             * Creates a SnackBar, which tells the user which Point of Interest has been discovered.
             *
             * TODO: This feature is not implemented fully.
             *
             * @param markerDiscovered the Point of Interest marker discovered.
             * @return tools boolean attribute. Must be boolean.
             */
            // If a marker is clicked, provide Snackbar, that tells which marker has been clicked. Redirect to Discovered fragment.
            mMap.setOnMarkerClickListener(markerDiscovered -> {
                Snackbar.make(mMapView, "Point of Interest " + markerDiscovered.getTitle() + " Discovered!" +
                        "Note, that this feature is not yet fully implemented.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

                // Points of Interest to be added to the Your Points of Interest list.
                // TODO: FULLY IMPLEMENT THE DISCOVERY FEATURE.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String username = user.getDisplayName();
                yourPointsOfInterest = FirebaseDatabase.getInstance().getReference()
                        .child("foundpois")
                        .child(username);
                String id = yourPointsOfInterest.push().getKey();
                PoInterest foundpointerest = new PoInterest(
                        markerDiscovered.getPosition().latitude,
                        markerDiscovered.getPosition().longitude,  // Position: position
                        markerDiscovered.getTitle(),               // Title: title
                        markerDiscovered.getSnippet(),             // Snippet: comments
                        markerDiscovered.getTag().toString());     // Tag: category
                // Save new Point of Interest to mDatabase.
                yourPointsOfInterest.child(id).setValue(foundpointerest);

                /*
                 * When a Point of Interest has been discovered, change the color and open bottom sheet.
                 * Bottom Sheet stuff
                 * Source: https://codinginflow.com/tutorials/android/modal-bottom-sheet
                 */

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                // "bottomSheet" is the tag for the bottom sheet.

                bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "bottomSheet");

                // Add text.
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheetDialog.setTextValues(foundpointerest.getTitle(), foundpointerest.getCategory(), foundpointerest.getComments());
                    }
                // Delay is necessary to prevent Null Pointer Exceptions
                }, 50);

                markerDiscovered.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                return true;
            });
        });

        // Folder for creating a Point of Interest.
        ImageView createFolder = rootView.findViewById(R.id.createfolder);
        createFolder.setOnClickListener(new View.OnClickListener() {

            /*
             * The user is moved to a new SendFragment, when they click the createFolder.
             * Pass latitude and longitude of the location, they have selected along within the fragment transaction.
             * Otherwise, display a toast to the user.
             * @param view unused parameter, but needed for override of onclick.
             */
            @Override
            public void onClick(View view) {
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

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, sendFragment);
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "You need to add a marker on the map first!", Toast.LENGTH_SHORT).show();
                }
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
         * Either: display TapTargetView sequence
         * Or: do nothing.
         */
        int isFirstTime = appGetFirstTimeRun();
        System.out.println(" GETFIRSTTIMERUN: " + isFirstTime);
        if(isFirstTime == 1 && hasPlayedTutorial == false) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Debugging code used for implementing taptargets.
                    /*do something
                    // The basic one-section taptarget.
                    TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                        TapTarget.forView(rootView.findViewById(R.id.createfolder), "This is a target", "We have the best targets, believe me")
                            // All options below are optional
                            .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                            .titleTextSize(20)                  // Specify the size (in sp) of the title text
                            .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                            .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(60)                  // Specify the target radius (in dp)
                    );
                    */
                    // The complex multi-section taptarget.
                    new TapTargetSequence(getActivity())
                        .targets(
                            TapTarget.forView(rootView.findViewById(R.id.mapView), "Welcome To Point of Interest", "This is your map of all Points of interest. Looks pretty neat, no?").transparentTarget(true),
                            // TODO: ADD TAPTARGETS FOR DISCOVERY FEATURE.
                            TapTarget.forView(rootView.findViewById(R.id.mapView), "Create a new Point of Interest", "To create a new Point of Interest, start by pressing the map."),
                            TapTarget.forView(rootView.findViewById(R.id.createfolder), "Then, you can tap the create folder to create your new Point of Interest!").transparentTarget(true),
                            TapTarget.forView(rootView.findViewById(R.id.mapView), "That covers about everything!", "Now let's get out there and find some awesome places!")
                        ).start();
                    hasPlayedTutorial = true;
                }
            }, 2000);
        }

        return rootView;
    }

    /*
     * Determine if application should use TapTargetView.
     * Source: https://stackoverflow.com/questions/4636141/determine-if-android-app-is-the-first-time-used
     * @return 0 if the application has never started before. 1 if the application has started before. 2 if the application has started previously after an update.
     */
    private int appGetFirstTimeRun() {
        //Check if App Start First Time
        SharedPreferences appPreferences = getActivity().getSharedPreferences("MyAPP", 0);
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

    /*
     * Populate Google Maps Markers.
     * Source: Kishor at https://www.youtube.com/watch?v=xnqXyorehJI
     */
    @Override
    public void onMapReady(GoogleMap googleMap){
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Populate markers on map
        mPointsOfInterest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("ADDING MARKERS...");
                System.out.println("-----------------");
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    PoInterest user = s.getValue(PoInterest.class);
                    // Add a marker on the map for each location.
                    LatLng location = new LatLng(user.latitude, user.longitude);

                    //There was no pretty way to do this. I spent way too much time figuring this out. Refer to the comments below on how it works.
                    // The extra values are used to make the data readable for when tapping the map.
                    googleMap.addMarker(
                            new MarkerOptions()
                            .position(location)                                                               // Position: position
                            .title(user.getTitle())                                                           // Title: title
                            .snippet(user.getComments())                                                      // Snippet: comments
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                            .setTag(user.getCategory());                                                      // Tag: category

                    System.out.println(location.toString());
                }
                System.out.println("-----------------");
                System.out.println("Markers added!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    /* Check if the application has permission to access the user's location. If not, make a request to the permission ACCESS_FINE_LOCATION
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     * @return: true if application already has permission. false if the application has to request permission.
     */
    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            // Application has no permission. User has explicitly denied permission.
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
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

    /*
     * If permission is granted to use location, show this on the map.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_USER_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), "Permission denied for location. Things may not work.", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    /*
     * Connect to google location client.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /*
     * Move camera to user location, when it updates.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    /*
     * Make requests for user locations at an interval of 1100 ms.
     * Source: https://www.youtube.com/watch?v=4kk-dYWVNsc
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    /*
     * Unused override method.
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /*
     * Unused override method.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onButtonClicked(String text) {
        Toast.makeText(getActivity().getApplicationContext(), "Bottom sheet closed!", Toast.LENGTH_SHORT).show();
    }
}