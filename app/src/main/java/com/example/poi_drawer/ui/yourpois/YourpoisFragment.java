package com.example.poi_drawer.ui.yourpois;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.poi_drawer.MainActivity;
import com.example.poi_drawer.PoInterest;
import com.example.poi_drawer.PoInterestList;
import com.example.poi_drawer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * The YourpoisFragment is used to contain the list of found Points of Interest by the user.
 *
 * @author Oliver Medoc Benée Petersen, 201806928
 * @version 2.0
 * @since 17-11-2019
 */
public class YourpoisFragment extends Fragment {
    private String TAG = "MainActivity";
    private ListView listViewYourpois;
    private DatabaseReference mPointsOfInterest;
    private List<PoInterest> poInterestList;

    /*
     * Creates the Your Points of Interest fragment and show it on the main activity.
     * TODO: NOT FULLY IMPLEMENTED
     * @param inflater Handles showing the fragment.
     * @param container The view to show the fragment at.
     * @param savedInstanceState Parameter not currently used.
     * @return the view to be shown.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO: Ensure fetching from database works as intended.
        View rootView = inflater.inflate(R.layout.fragment_yourpois, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mPointsOfInterest = database.getReference("pointsofinterest");

        //Fetch ListView
        listViewYourpois = rootView.findViewById(R.id.list_view_yourpois);
        poInterestList = new ArrayList<>();

        // Points of Interest to be added to the Your Points of Interest list.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getDisplayName();

        // Show found Points of interest.
        if(username != null) {
            mPointsOfInterest = FirebaseDatabase.getInstance().getReference().child("foundpois").child(username);
            mPointsOfInterest.push().setValue(poInterestList);
        }
        return inflater.inflate(R.layout.fragment_yourpois, container, false);
    }

    /*
     * Add discovered Points of Interest to the List of found points of interest.
     * TODO: FULLY IMPLEMENT THIS FEATURE
     */
    @Override
    public void onStart(){
        super.onStart();
        mPointsOfInterest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                poInterestList.clear();
                for(DataSnapshot poiSnapshot : dataSnapshot.getChildren()){
                    PoInterest poi = poiSnapshot.getValue(PoInterest.class);
                    poInterestList.add(poi);
                }
                PoInterestList adapter = new PoInterestList(getActivity(), poInterestList);
                listViewYourpois.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}