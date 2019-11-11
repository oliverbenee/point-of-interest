package com.example.poi_drawer.ui.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.poi_drawer.R;
import com.example.poi_drawer.ui.map.MapFragment;

/**
 * The WelcomeFragment shows the video to the user.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @version 1.0
 * @since 06-10-2019
 */
public class TutorialFragment extends Fragment {

    private Button donebutton, videobutton;
    private VideoView videov;


    /*
     * Create and show the video fragment to the user.
     *
     * @param inflater Puts the fragment on the activity.
     * @param container The activity to put the container on.
     * @param savedInstanceState Unused parameter.
     * @return View containing the map itself.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment.
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        //If user hits the done button, they are moved to the MapFragment.
        donebutton = view.findViewById(R.id.done_button);
        donebutton.setOnClickListener(new View.OnClickListener() {
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

        // TODO: Implement VideoView to fetch and show youtube video instead of redirecting to it.

        videov = (VideoView) view.findViewById(R.id.tutorial_video);
        videobutton = (Button) view.findViewById(R.id.videobutton);

        videobutton.setOnClickListener(new View.OnClickListener() {
            /*
             * Redirect the user to the map, when they move away from the video fragment using the "done" button.
             *
             * @param view the current view presented. Required for method overriding, but not used.
             */
            @Override
            public void onClick(View view) {
                playVideo(view);
            }
        });

        return view;
    }

    public void playVideo(View view){
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
        Log.i("Video", "Video Playing....");

    }
        String videopath = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        Uri uri = Uri.parse(videopath);
        //videov.setVideoURI(uri);

}