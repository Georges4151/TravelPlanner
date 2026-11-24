package com.travelplanner.ui.restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travelplanner.R;
import com.travelplanner.model.amap.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoPagerAdapter extends RecyclerView.Adapter<PhotoPagerAdapter.ViewHolder> {

    private static final String TAG = "PhotoPagerAdapter";
    private Context context;
    private List<Photo> photos = new ArrayList<>();

    public PhotoPagerAdapter(Context context) {
        this.context = context;
    }

    public void setPhotos(List<Photo> photos) {
        Log.d(TAG, "setPhotos called, size: " + (photos != null ? photos.size() : 0));
        this.photos = photos != null ? photos : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Log.d(TAG, "onBindViewHolder position: " + position + ", url: " + photo.getUrl());
        if (photo.getUrl() != null && !photo.getUrl().isEmpty()) {
            Log.d(TAG, "Loading image with Glide: " + photo.getUrl());
            Glide.with(context)
                    .load(photo.getUrl())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .centerCrop()
                    .into(holder.ivPhoto);
        } else {
            Log.d(TAG, "No URL, using placeholder");
            holder.ivPhoto.setImageResource(R.drawable.ic_image_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }
    }
}
