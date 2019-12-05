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

public class YourpoisAdapter extends RecyclerView.Adapter<YourpoisAdapter.YourpoisViewHolder> {
    private ArrayList<ExampleItem> mPointerestList;

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

    YourpoisAdapter(ArrayList<ExampleItem> poiList) {
        mPointerestList = poiList;
    }

    @NonNull
    @Override
    public YourpoisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new YourpoisViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YourpoisViewHolder holder, int position) {
        ExampleItem currentItem = mPointerestList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mPointerestList.size();
    }
}