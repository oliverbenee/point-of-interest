package com.example.poi_drawer.ui.discovered;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The DiscoveredViewModel file is used as a back-up view for the DisoveredFragment.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @version 1.0
 * @since 06-10-2019
 */
public class DiscoveredViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    /*
     * Constructor, that creates the title shown on the DiscoveredFragment.
     */
    public DiscoveredViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Create Point of Interest");
    }

    /*
     * Show the text "Create Point of Interest. "
     * @return a string showing that the user may create a Point of Interest from the button.
     */
    public LiveData<String> getText() {
        return mText;
    }
}