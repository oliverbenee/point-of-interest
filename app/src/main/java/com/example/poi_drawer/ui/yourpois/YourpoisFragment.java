package com.example.poi_drawer.ui.yourpois;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poi_drawer.PoInterest;
import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.Welcome.WelcomeFragment;
import com.example.poi_drawer.ui.map.MapFragment;
import com.example.poi_drawer.ui.send.SendFragment;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * The YourpoisFragment is used to contain the list of found Points of Interest by the user.
 *
 * @author Oliver Medoc Benée Petersen, 201806928
 * @version 2.0
 * @since 17-11-2019
 */
public class YourpoisFragment extends Fragment {

    // Firebase variables
    private DatabaseReference mDatabase;
    private DatabaseReference myPointsOfInterest;

    // List that is used to show Points of Interest.
    private ArrayList<ExampleItem> exampleList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

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

        //RecyclerView
        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        mAdapter = new YourpoisAdapter(exampleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Fetch points of interest to be found
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Check, that a user is signed in.
        if(user == null){
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            FragmentTransaction transaction = null;
            if (getFragmentManager() != null) {
                transaction = getFragmentManager().beginTransaction();
            }
            assert transaction != null;
            transaction.replace(R.id.nav_host_fragment, welcomeFragment);
            transaction.commit();
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "You must sign in first!", Toast.LENGTH_LONG).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Guard against Null Pointer Exceptions.
        if(user != null) {
            String username = Objects.requireNonNull(user.getDisplayName()).toLowerCase();

            System.out.println("Fetching at node: '" + username + "'");
            myPointsOfInterest = mDatabase.child("foundpois").child(username);
            System.out.println("----------------------");
            System.out.println("ADDING POIS TO LIST...");
            System.out.println("----------------------");


            myPointsOfInterest.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        PoInterest user = s.getValue(PoInterest.class);
                        assert user != null;
                        Log.d(TAG, "onDataChange: " + user.getTitle());
                        ExampleItem item4 = new ExampleItem(R.drawable.welcome_pointofinteresticon, user.getTitle(), user.getCategory());
                        System.out.println("Found item: " + user);
                        exampleList.add(item4);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            System.out.println("----------------------");
            System.out.println("DONE!");
            System.out.println("----------------------");

            Button refreshButton = rootView.findViewById(R.id.refresh);
            refreshButton.setOnClickListener(view -> myPointsOfInterest.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    exampleList.clear();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        PoInterest user1 = s.getValue(PoInterest.class);
                        assert user1 != null;
                        ExampleItem item = new ExampleItem(R.drawable.welcome_pointofinteresticon, user1.getTitle(), user1.getCategory());
                        exampleList.add(item);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }));
        }
        return rootView;
    }
}