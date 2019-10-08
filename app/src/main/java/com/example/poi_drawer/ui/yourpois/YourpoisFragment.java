package com.example.poi_drawer.ui.yourpois;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.poi_drawer.R;

/**
 * The YourpoisFragment is used to contain the list of Points of Interest.
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
        View root = inflater.inflate(R.layout.fragment_yourpois, container, false);
        return root;
    }
}