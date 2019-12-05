package com.example.poi_drawer.ui.yourpois;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poi_drawer.R;

import java.util.ArrayList;

/**
 * The ListItem class is a subclass of the RecyclerViewAdapter. It converts the view holder for each item.
 *
 * @author Oliver Medoc Benée Petersen 201806928
 * @author CodingInFlow at https://codinginflow.com/tutorials/android/recyclerview-cardview/part-2-adapter - For guiding the implementation of the class and association with the Recyclerview
 * @version 3.0
 * @since 05-12-2019
 */
public class YourpoisAdapter extends RecyclerView.Adapter<YourpoisAdapter.YourpoisViewHolder> {
    private ArrayList<ListItem> mPointerestList;

    /**
     * A class containing each element to put into the recyclerview.
     * source: CodingInFlow at https://codinginflow.com/tutorials/android/recyclerview-cardview/part-2-adapter
     */
    static class YourpoisViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView1;
        TextView mTextView2;

        YourpoisViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    /**
     * A constructor used for generating the ArrayAdapter.
     * source: CodingInFlow at https://codinginflow.com/tutorials/android/recyclerview-cardview/part-2-adapter
     *
     * @param poiList the list of converted Points of Interest to be displayed in the recyclerview.
     */
    YourpoisAdapter(ArrayList<ListItem> poiList) {
        mPointerestList = poiList;
    }

    /**
     * Inflate the view holder to put elements into.
     * source: CodingInFlow at https://codinginflow.com/tutorials/android/recyclerview-cardview/part-2-adapter
     * @param parent the view to attach the view holder to.
     * @param viewType unused parameter for overriding.
     * @return the generated viewholder.
     */
    @NonNull
    @Override
    public YourpoisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new YourpoisViewHolder(v);
    }

    /**
     * Populate the view holder with content.
     * source: CodingInFlow at https://codinginflow.com/tutorials/android/recyclerview-cardview/part-2-adapter
     * @param holder the view holder generated for the items to be put into.
     * @param position which element in the list to put the items into.
     */
    @Override
    public void onBindViewHolder(@NonNull YourpoisViewHolder holder, int position) {
        ListItem currentItem = mPointerestList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    /**
     * Returns the item count. Used while generating the recyclerview:
     * source: CodingInFlow at https://codinginflow.com/tutorials/android/recyclerview-cardview/part-2-adapter
     * @return the number of items in the list.
     */
    @Override
    public int getItemCount() {
        return mPointerestList.size();
    }
}