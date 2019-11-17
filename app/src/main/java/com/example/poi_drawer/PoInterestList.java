package com.example.poi_drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * An ArrayAdapter used for showing the list containing your points of interest.
 * TODO: Use ArrayAdapter to show found Points of Interest.
 * Source: Simplified Coding at: https://www.youtube.com/watch?reload=9&v=EM2x33g4syY - For guiding the process of implementing the class.
 * @author Oliver Medoc Benée Petersen, 201806928
 */
public class PoInterestList extends ArrayAdapter<PoInterest> {

    private Activity context;
    private List<PoInterest> poInterestList;

    /**
     * Constructor for objects of type PoInterestList
     * @param context the current activity shown.
     * @param poInterestList the list of Points of Interest discovered.
     */
    public PoInterestList(@NonNull Activity context, List<PoInterest> poInterestList) {
        super(context, R.layout.fragment_yourpois, poInterestList);
        this.context = context;
        this.poInterestList = poInterestList;
    }

    /**
     * Converts the textviews into Arrays.
     * @param position unused parameter.
     * @param convertView View to be converted.
     * @param parent The container of the view.
     * @return the new item to be shown in the list.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.txt_title);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.list_category);

        PoInterest poInterest = poInterestList.get(position);
        textViewTitle.setText(poInterest.getTitle());
        textViewCategory.setText(poInterest.getCategory());

        return listViewItem;
    }
}
