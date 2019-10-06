package com.example.poi_drawer.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * The MapViewModel file is used to create a back-up view, that explains, that the GoogleMap is broken.
 *
 * @author Oliver Medoc Benée Petersen, 201806928
 * @version 1.0
 * @since 06-10-2019
 */

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("If you see this, the MapFragment is broken. Go fix it. ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}