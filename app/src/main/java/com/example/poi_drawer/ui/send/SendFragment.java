package com.example.poi_drawer.ui.send;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.poi_drawer.PoInterest;
import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.map.MapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The SendFragment contains the form used to create Points of Interest.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @author Simplified Coding at https://www.youtube.com/watch?reload=9&v=EM2x33g4syY - For saving data to the MySQL database.
 * @version 2.0
 * @since 17-11-2019
 */
public class SendFragment extends Fragment {

    TextView latLngFound;
    EditText editTextTitle, editTextComments;
    Button buttonCreate, buttonCancel;
    Spinner spinner_category;

    // Fetch the mDatabase.
    private DatabaseReference mDatabase;
    Bundle bundle;
    double latitude, longitude;

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
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            /*
             * Add an entry with the point of interest to the mDatabase.
             * Redirect the user to the map, when they complete the form. Otherwise displays error message.
             *
             * @param view the current view presented. Required for method overriding, but not used.
             */
            @Override
            public void onClick(View view) {
                boolean poiadded = addPointOfInterest();
                if(poiadded) {
                    MapFragment mapFragment = new MapFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, mapFragment);
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Some error occured. Try again. ", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Button for returning to map.
        buttonCancel = root.findViewById(R.id.cancelbutton);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            /*
             * Returns the user to the map.
             */
            @Override
            public void onClick(View view) {
                MapFragment mapFragment = new MapFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, mapFragment);
                transaction.commit();
            }
        });

        // Fetch latitude and longitude data from MapFragment. If nothing is found force the user back to the map.
        bundle = this.getArguments();
        if(bundle != null){
            // Fetch latitude and longitude from bundle.
            latitude =  bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");
            Toast.makeText(getContext(),"SendFragment found: latitude: " + latitude + ", longitude: " + longitude, Toast.LENGTH_SHORT).show();
            latLngFound.setText("(" + latitude + ", " + longitude + ")");
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, new MapFragment());
            Toast.makeText(getContext(),"Error: no location found. returning to map.", Toast.LENGTH_LONG).show();
            transaction.commit();
        }
        return root;
    }

    /*
     * Add a Point of interest to the database.
     * Source:  https://www.youtube.com/watch?reload=9&v=EM2x33g4syY
     */
    public boolean addPointOfInterest(){
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
                // Save new Point of Interest to mDatabase.
                mDatabase.child(id).setValue(pointerest);
                Toast.makeText(this.getContext(), "Point of Interest added!", Toast.LENGTH_LONG).show();
                return true;
            //Data not specified.
            } else {
                Toast.makeText(this.getContext(), "Please check, that you have added a title and category, then try again.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }
}