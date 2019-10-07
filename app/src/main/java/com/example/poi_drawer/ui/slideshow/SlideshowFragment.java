package com.example.poi_drawer.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.poi_drawer.R;

public class SlideshowFragment extends Fragment {

    /**
     * The SlideShowFragment is used to contain the list of Points of Interest.
     *
     * @author Oliver Medoc Benée Petersen, 201806928
     * @version 1.0
     * @since 06-10-2019
     */

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yourpois, container, false);
        return root;
    }
}