package com.example.poi_drawer.ui.send;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    /**
     * Creates the title shown on the SendFragment.
     */

    public SendViewModel() {
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