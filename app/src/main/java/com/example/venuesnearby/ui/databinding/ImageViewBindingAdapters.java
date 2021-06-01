package com.example.venuesnearby.ui.databinding;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.venuesnearby.R;

import java.io.File;

public class ImageViewBindingAdapters {

    @BindingAdapter("imageResource")
    public static void setImage(ImageView imageView, Uri imageUri) {
        Glide.with(imageView.getRootView().getContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.placeholder_large)
                        .error(R.drawable.placeholder_large))
                .load(imageUri)
                .into(imageView);
    }

    @BindingAdapter("imageResource")
    public static void setImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getRootView().getContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.placeholder_large)
                        .error(R.drawable.placeholder_large))
                .load(imageUrl)
                .into(imageView);
    }

    @BindingAdapter("imageResource")
    public static void setImage(ImageView imageView, File imageFile) {
        Glide.with(imageView.getRootView().getContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.placeholder_large)
                        .error(R.drawable.placeholder_large))
                .load(imageFile)
                .into(imageView);
    }

    @BindingAdapter("imageResource")
    public static void setImage(ImageView imageView, int imageResourceId) {
        Glide.with(imageView.getRootView().getContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.placeholder_large)
                        .error(R.drawable.placeholder_large))
                .load(imageResourceId)
                .into(imageView);
    }

    @BindingAdapter("imageResource")
    public static void setImage(ImageView imageView, Drawable imageDrawable) {
        Glide.with(imageView.getRootView().getContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.placeholder_large)
                        .error(R.drawable.placeholder_large))
                .load(imageDrawable)
                .into(imageView);
    }
}