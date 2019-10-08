package com.example.poi_drawer.ui.tools;

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
import com.example.poi_drawer.ui.video.VideoFragment;

public class ToolsFragment extends Fragment {

    private Button signinbutton;
    private ToolsViewModel toolsViewModel;

    /**
     * Shows the tools fragment to the user.
     *
     * @param inflater Handles showing the fragment.
     * @param container The view to show the fragment at.
     * @param savedInstanceState Parameter not currently used.
     * @return the view to be shown.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        signinbutton = root.findViewById(R.id.sign_in_button);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoFragment videoFragment = new VideoFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, videoFragment);
                transaction.commit();
            }
        });
        return root;
    }
}