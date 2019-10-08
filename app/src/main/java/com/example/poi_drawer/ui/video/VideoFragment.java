package com.example.poi_drawer.ui.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.map.MapFragment;
import com.example.poi_drawer.ui.tools.ToolsFragment;
import com.example.poi_drawer.ui.welcome.WelcomeViewModel;

/**
 * The WelcomeFragment shows the video to the user.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @version 1.0
 * @since 06-10-2019
 */
public class VideoFragment extends Fragment {

    private Button b1, b2;

    private VideoViewModel videoViewModel;

    /*
     * Create and show the video fragment to the user.
     *
     * @param inflater Puts the fragment on the activity.
     * @param container The activity to put the container on.
     * @param savedInstanceState Unused parameter.
     * @return View containing the map itself.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        // Inflate the layout for the fragment.
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        //If user hits the done button, they are moved to the MapFragment.
        b1 = view.findViewById(R.id.done_button);
        b1.setOnClickListener(new View.OnClickListener() {

            /*
             * Redirect the user to the map, when they move away from the video fragment using the "done" button.
             *
             * @param view the current view presented. Required for method overriding, but not used.
             */
            @Override
            public void onClick(View view) {
                MapFragment mapFragment = new MapFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, mapFragment);
                transaction.commit();
            }
        });
        return view;
    }
}