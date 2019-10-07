package com.example.poi_drawer.ui.discovered;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.map.MapFragment;

/**
 * The DiscoveredFragment contains a view of a discovered Point of Interest.
 *
 * @author Oliver Medoc Benée Petersen, 201806928
 * @version 1.0
 * @since 07-10-2019
 */

public class DiscoveredFragment extends Fragment {

    private DiscoveredViewModel discoveredViewModel;

    /**
     * Show the form to the user.
     *
     * @param inflater Handles showing the fragment.
     * @param container The view to show the fragment at.
     * @param savedInstanceState Parameter not currently used.
     * @return the form view to be shown.
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoveredViewModel =
                ViewModelProviders.of(this).get(DiscoveredViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discovered, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        //discoveredViewModel.getText().observe(this, new Observer<String>() {

            /**
             * Changes the String value to be displayed on the send fragment. Serves as a function for debugging.
             *
             * @param s the string showing the error.
             */

        //    @Override
        //    public void onChanged(@Nullable String s) {
        //        textView.setText(s);
        //    }
        //});

        /**
         * Button for Adding a Point of Interest.
         */
        Button createButton = root.findViewById(R.id.returntomapbutton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment mapFragment = new MapFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, mapFragment);
                transaction.commit();
            }
        });
        return root;
    }
}