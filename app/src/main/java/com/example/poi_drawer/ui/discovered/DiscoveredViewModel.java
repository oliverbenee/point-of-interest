package com.example.poi_drawer.ui.discovered;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The DiscoveredViewModel file is used to the view model for the DiscoveredFragment.
 *
 * @author Oliver Medoc Benée Petersen, 201806928
 * @version 1.0
 * @since 06-10-2019
 */

public class DiscoveredViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    /**
     * Creates the title shown on the DiscoveredFragment.
     */

    public DiscoveredViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Create Point of Interest");
    }

    /**
     * Show the text "Create Point of Interest. "
     * @return a string showing that the user may create a Point of Interest from the button.
     */

    public LiveData<String> getText() {
        return mText;
    }
}