package com.example.venuesnearby.ui.databinding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBindingAdapters {

    // Use LinearLayout.VERTICAL or HORIZONTAL integers, or DividerItemDecoration.VERTICAL or HORIZONTAL
    @BindingAdapter("divider")
    public static void addDividerItemDecoration(RecyclerView recyclerView, int orientation) {
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), orientation));
    }
}
