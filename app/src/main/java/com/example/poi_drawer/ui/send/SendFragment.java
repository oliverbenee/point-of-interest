package com.example.poi_drawer.ui.send;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.poi_drawer.BuildConfig;
import com.example.poi_drawer.MainActivity;
import com.example.poi_drawer.PoInterest;
import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.map.MapFragment;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * The SendFragment contains the form used to create Points of Interest.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @author Simplified Coding at https://www.youtube.com/watch?reload=9&v=EM2x33g4syY - For saving data to the MySQL database.
 * @author Webserveis at https://stackoverflow.com/questions/4636141/determine-if-android-app-is-the-first-time-used - For checking for first time run of application
 * @version 3.0
 * @since 05-12-2019
 */
public class SendFragment extends Fragment {

    private TextView latLngFound;
    private EditText editTextTitle, editTextComments;
    private Button buttonCreate, buttonCancel;
    private Spinner spinner_category;
    private Bundle bundleToMap;

    // Fetch the mDatabase.
    private DatabaseReference mDatabase, yourPointsOfInterest;
    private Bundle bundle;
    private double latitude, longitude;

    /**
     * Create and show the form to the user. Creates the fragment view to be shown.
     *
     * @param inflater Handles showing the fragment.
     * @param container The view to show the fragment at.
     * @param savedInstanceState Parameter not currently used.
     * @return the form view to be shown.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send, container, false);

        /*
         * Sauce: https://www.youtube.com/watch?reload=9&v=EM2x33g4syY
         */
        // Points of Interest to be shown in the map.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pointsofinterest");

        // Fetch buttons, text inputs and spinners.
        latLngFound = root.findViewById(R.id.LatLngFound);
        editTextTitle = root.findViewById(R.id.txttitle);
        editTextComments = root.findViewById(R.id.txt_comments);
        spinner_category = root.findViewById(R.id.spinner_category);

        // Button for Adding a Point of Interest.
        buttonCreate = root.findViewById(R.id.add_poi_button);
        /*
         * Add an entry with the point of interest to the mDatabase.
         * Redirect the user to the map, when they complete the form. Otherwise displays error message.
         *
         * @param view the current view presented. Required for method overriding, but not used.
         */
        buttonCreate.setOnClickListener(view -> {
            boolean poiadded = addPointOfInterest();
            if(poiadded) {
                MapFragment mapFragment = new MapFragment();
                if(bundleToMap != null) {
                    mapFragment.setArguments(bundleToMap);
                }
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, mapFragment);
                transaction.commit();
            } else {
                Toast.makeText(Objects.requireNonNull(getActivity()).getBaseContext(), "Some error occured. Try again. ", Toast.LENGTH_LONG).show();
            }
        });

        // Button for returning to map.
        buttonCancel = root.findViewById(R.id.cancelbutton);
        /*
         * Returns the user to the map.
         */
        buttonCancel.setOnClickListener(view -> {
            MapFragment mapFragment = new MapFragment();
            FragmentTransaction transaction;
            if (getFragmentManager() != null) {
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, mapFragment);
                transaction.commit();
            } else {
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Error moving back to map. Try to close the application", Toast.LENGTH_LONG).show();
            }
        });

        // Fetch latitude and longitude data from MapFragment. If nothing is found force the user back to the map.
        bundle = this.getArguments();
        if(bundle != null){
            // Fetch latitude and longitude from bundle.
            latitude =  bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");
            String latLngText = latitude + ", " + longitude;
            latLngFound.setText(latLngText);
        } else {
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, new MapFragment());
            Toast.makeText(getContext(),"Error: no location found. returning to map.", Toast.LENGTH_LONG).show();
            transaction.commit();
        }

        // TapTargetView tutorial for SendFragment
        int isFirstTime = (appGetFirstTimeRun());
        System.out.println(" GETFIRSTTIMERUN: " + isFirstTime);
        boolean hasPlayedSendTutorial = ((MainActivity) Objects.requireNonNull(getActivity())).getHasPlayedSendTutorial();
        System.out.println(" HASPLAYEDTUTORIAL: " + hasPlayedSendTutorial);
        if(isFirstTime == 1 && !hasPlayedSendTutorial) {
            new Handler().postDelayed(() -> {
                if(getActivity() != null) {
                    // The complex multi-section taptarget.
                    new TapTargetSequence(getActivity())
                            .targets(
                                    TapTarget.forView(root.findViewById(R.id.textView4), "Welcome to the form!", "This is where you tell people about your new awesome Point of Interest!").targetRadius(60),
                                    TapTarget.forView(root.findViewById(R.id.LatLngFound), "This is the location, where you will place your marker!").targetRadius(60).transparentTarget(true),
                                    TapTarget.forView(root.findViewById(R.id.txttitle), "Then, you should type in the title of your new Point of Interest here!").targetRadius(60).transparentTarget(true),
                                    TapTarget.forView(root.findViewById(R.id.spinner_category), "You should also add some comments and a category for the Point of Interst too!").targetRadius(60).transparentTarget(true),
                                    TapTarget.forView(root.findViewById(R.id.add_poi_button), "Finally", "Add your new Point of Interest to the map with this button here!").transparentTarget(true).targetRadius(60).transparentTarget(true),
                                    TapTarget.forView(root.findViewById(R.id.cancelbutton), "Or if you want to place a marker somewhere different, you can cancel the action here.").targetRadius(60).transparentTarget(true)
                            ).start();
                    ((MainActivity) getActivity()).setHasPlayedSendTutorial(true);
                }
            }, 8000);
        }

        return root;
    }

    /**
     * Add a Point of interest to the database.
     * Source:  Simplified coding at https://www.youtube.com/watch?reload=9&v=EM2x33g4syY
     * @return true if the Point of Interest was added, false if the Point of Interest was not added.
     */
    private boolean addPointOfInterest(){
        String title  = editTextTitle.getText().toString().trim();
        String comments = editTextComments.getText().toString().trim();
        String category = spinner_category.getSelectedItem().toString();
        // Fetch location of added Point of Interest marker.
        if(bundle != null){
            // Title specified. store Point of Interest.
            if(!TextUtils.isEmpty(title) || !TextUtils.isEmpty(comments) || spinner_category.getSelectedItem() != null){
                // Generate new ID for the key.
                String id = mDatabase.push().getKey();
                //Create Point of Interest to be saved.
                PoInterest pointerest = new PoInterest(latitude, longitude, title, comments, category);
                // Save the new Point of Interest to the public database.
                assert id != null;
                mDatabase.child(id).setValue(pointerest);

                // Also save the new Point of Interest to the user's own list of Points of Interest.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String username = null;
                if (user != null) {
                    username = (Objects.requireNonNull(user.getDisplayName())).trim().toLowerCase();
                    yourPointsOfInterest = FirebaseDatabase.getInstance().getReference()
                            .child("foundpois")
                            .child(username);
                    // Save new Point of Interest to mDatabase.
                    yourPointsOfInterest.child(title).setValue(pointerest);

                    Toast.makeText(this.getContext(), "Point of Interest added!", Toast.LENGTH_LONG).show();
                    bundleToMap = new Bundle();
                    bundleToMap.putString("poiId", id);
                    return true;
                } else {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Error. Not signed in. Returning to sign in screen.", Toast.LENGTH_LONG).show();
                    return false;
                }
            //Data not specified.
            } else {
                Toast.makeText(this.getContext(), "Please check, that you have added a title and category, then try again.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }

    /**
     * Determine if application should use TapTargetView.
     * Source: Webserveis at https://stackoverflow.com/questions/4636141/determine-if-android-app-is-the-first-time-used
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
}