package com.example.poi_drawer.ui.yourpois;

class ExampleItem {
    private int mImageResource;
    private String mText1;
    private String mText2;

    ExampleItem(int imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    int getImageResource() {
        return mImageResource;
    }

    String getText1() {
        return mText1;
    }

    String getText2() {
        return mText2;
    }
}