package com.example.poi_drawer.ui.Welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The WelcomeViewModel contains the  contains the welcome menu. The class itself serves to create and show the view to the user.
 *
 * @author Oliver Medoc Benée Petersen, 201806928
 * @version 1.0
 * @since 06-10-2019
 */
public class WelcomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /*
     * Constructor, that creates the title shown on the Welcome screen, saying "Welcome to Point of Interest!"
     * Sets value mText to "Welcome to Point of Interest!", the string displayed as welcome message.
     */
    public WelcomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to Point of Interest!");
    }

    /*
     * Show the text "Welcome to Point of Interest!" on the fragment.
     * @return String text saying "Welcome to Point of Interest!"
     */
    public LiveData<String> getText() {
        return mText;
    }
}