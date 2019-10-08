package com.example.poi_drawer.ui.tools;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The ToolsViewModel file is used as a back-up view for the ToolsFragment.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @version 1.0
 * @since 06-10-2019
 */
public class ToolsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     * Constructor, that creates the title shown on the ToolsFragment.
     */
    public ToolsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("A view for changing account goes here. ");
    }

    /**
     * Show the text to tell the user, that the tools fragment is shown.
     * @return String text saying "A view for changing account goes here."
     */
    public LiveData<String> getText() {
        return mText;
    }
}