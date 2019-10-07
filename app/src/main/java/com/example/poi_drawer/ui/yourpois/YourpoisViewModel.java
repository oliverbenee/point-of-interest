package com.example.poi_drawer.ui.yourpois;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class YourpoisViewModel extends ViewModel {

    /**
     * The YourpoisViewModel file is used to show the user a list of discovered Points of Interest.
     *
     * @author Oliver Medoc Benée Petersen, 201806928
     * @version 1.0
     * @since 06-10-2019
     */

    private MutableLiveData<String> mText;

    public YourpoisViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is where the user will see a list of their Points of Interest");
    }

    public LiveData<String> getText() {
        return mText;
    }
}