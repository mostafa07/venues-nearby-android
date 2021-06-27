package com.example.venuesnearby.ui.databinding;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class ViewBindingAdapters {

    @BindingAdapter("shown")
    public static void showOrGone(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
