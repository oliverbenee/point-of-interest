package com.example.poi_drawer.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.poi_drawer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * The bottom sheet dialog interface handles the bottom sheet for discovering Points of Interest.
 * @author Oliver Medoc Benée Petersen, 201806928
 * @author CodingInFlow at: https://codinginflow.com/tutorials/android/modal-bottom-sheet
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private TextView discovered_title, discovered_category, discovered_comments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        Button button1 = v.findViewById(R.id.close_dialog_button);

        discovered_title = v.findViewById(R.id.discovered_title);
        discovered_category = v.findViewById(R.id.discovered_category);
        discovered_comments = v.findViewById(R.id.discovered_comments);

        button1.setOnClickListener(view -> {
            mListener.onButtonClicked("Button 1 clicked.");
            // Dismiss dialog.
            dismiss();
        });
        return v;
    }

    /**
     * The interface BottomSheetListener is used for handling events in other activities.
     */
    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    /**
     * Handle creating the bottom sheet.
     *
     * @param context the activity, that opens the dialog
     * @throws ClassCastException if the class does not implement the BottomSheetDialog interface.
     */
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            // If the activity fails to implement the BottomSheetDialog, throw an exception.
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

    /**
     * Sets text values for the discovered bottom sheet.
     *
     * @param title The title of the found Point of Interest
     * @param category The category of the found Point of Interest
     * @param comments The comments of the found Point of Interest
     */
    void setTextValues(String title, String category, String comments){
        discovered_title.setText(title);
        discovered_category.setText(category);
        discovered_comments.setText(comments);
    }
}
