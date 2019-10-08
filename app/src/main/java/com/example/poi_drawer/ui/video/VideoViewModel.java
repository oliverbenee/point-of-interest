package com.example.poi_drawer.ui.video;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The VideoViewModel file is used for displaying the placeholder for the video, and will handle displaying the video
 * in the video fragment.
 *
 * @author Oliver Medoc Benée Petersen 201806928, Android Studio templates.
 * @version 1.0
 * @since 06-10-2019
 */
public class VideoViewModel extends ViewModel{

    private MutableLiveData<String> mText;

        /**
         * Constructor, that creates the text to tell the user, that this is the video fragment.
         */
        public VideoViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("Video goes here.");
        }

        /**
         * Show the text to tell the user, that the video fragment is shown.
         * @return String text saying "Video goes here."
         */
        public LiveData<String> getText() {
                return mText;
        }
}