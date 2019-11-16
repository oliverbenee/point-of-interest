package com.example.poi_drawer.ui.Welcome;

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

import com.example.poi_drawer.MainActivity;
import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.map.MapFragment;
import com.example.poi_drawer.ui.send.SendFragment;
import com.example.poi_drawer.ui.video.TutorialFragment;

/**
 * The WelcomeFragment ensures, that the welcome message is displayed to the user.
 *
 * @author Oliver Medoc Benée Petersen, 201806928
 * @version 1.0
 * @since 06 -10-2019
 */
public class WelcomeFragment extends Fragment {

    private com.google.android.gms.common.SignInButton b1;

    /*
     * Create and show the welcome fragment to the user.
     *
     * @param inflater Puts the fragment on the activity.
     * @param container The activity to put the container on.
     * @param savedInstanceState Unused parameter.
     * @return View containing the map itself.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment.
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        b1 = view.findViewById(R.id.sign_in_button);
        b1.setOnClickListener(new View.OnClickListener() {

            /*
             * The user is moved to a new SendFragment, when they click the createButton.
             * @param view unused parameter, but needed for override of onclick.
             */
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).signIn();
                TutorialFragment tutorialFragment = new TutorialFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, tutorialFragment);
                transaction.commit();
            }
        });

        return view;
    }
}