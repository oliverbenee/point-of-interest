package com.example.poi_drawer.ui.video;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoViewModel extends ViewModel{

private MutableLiveData<String> mText;

public VideoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Video goes here.");
        }

public LiveData<String> getText() {
        return mText;
        }
}