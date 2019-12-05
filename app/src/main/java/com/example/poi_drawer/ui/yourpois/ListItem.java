package com.example.poi_drawer.ui.yourpois;

/**
 * The ListItem class contains the items to be added to the RecyclerView list. This is essentially a "converter" class from PoInterest to a usable item within the RecyclerView
 *
 * @author Oliver Medoc Benée Petersen 201806928
 * @author CodingInFlow at https://codinginflow.com/tutorials/android/recyclerview-cardview/part-1-layouts-model-class - For guiding the implementation of the class and association with the Recyclerview
 * @version 3.0
 * @since 05-12-2019
 */
class ListItem {
    private int mImageResource;
    private String mText1;
    private String mText2;

    /**
     * Constructor of objects of type ListItem
     * @param imageResource The drawable used for the Point of Interest.
     * @param text1 The first line of text within the item view, or the title of the Point of Interest.
     * @param text2 The second line of text within the item view, or the category of the Point of Interest.
     */
    ListItem(int imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    /**
     * Return the image resource to the recyclerview. Used while generating the recyclerview.
     * @return an int with the image resource.
     */
    int getImageResource() {
        return mImageResource;
    }

    /**
     * Return the first line of text to the recyclerview. Used while generating the recyclerview.
     * @return the first line of text to display in the recyclerview.
     */
    String getText1() {
        return mText1;
    }

    /**
     * Return the second line of text to the recyclerview. Used while generating the recyclerview.
     * @return the second line of text to display in the recyclerview.
     */
    String getText2() {
        return mText2;
    }
}