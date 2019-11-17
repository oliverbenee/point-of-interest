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
 * @version 1.0
 * @since 06-10-2019
 */
public class YourpoisFragment extends Fragment {
    private YourpoisViewModel yourpoisViewModel;

    /*
     * Creates the Your Points of Interest fragment and show it on the main activity.
     * @param inflater Handles showing the fragment.
     * @param container The view to show the fragment at.
     * @param savedInstanceState Parameter not currently used.
     * @return the view to be shown.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yourpoisViewModel =
                ViewModelProviders.of(this).get(YourpoisViewModel.class);
        return inflater.inflate(R.layout.fragment_yourpois, container, false);
    }
}