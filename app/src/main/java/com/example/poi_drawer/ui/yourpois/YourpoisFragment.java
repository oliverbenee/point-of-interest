package com.example.poi_drawer.ui.yourpois;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.poi_drawer.R;

public class YourpoisFragment extends Fragment {

    /**
     * The SlideShowFragment is used to contain the list of Points of Interest.
     *
     * @author Oliver Medoc Benée Petersen, 201806928
     * @version 1.0
     * @since 06-10-2019
     */

    private YourpoisViewModel yourpoisViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yourpoisViewModel =
                ViewModelProviders.of(this).get(YourpoisViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yourpois, container, false);
        return root;
    }
}