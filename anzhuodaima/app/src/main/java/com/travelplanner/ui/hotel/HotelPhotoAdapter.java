package com.travelplanner.ui.hotel;

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

public class HotelPhotoAdapter extends RecyclerView.Adapter<HotelPhotoAdapter.ViewHolder> {

    private static final String TAG = "HotelPhotoAdapter";
    private List<Photo> photos = new ArrayList<>();

    public void setData(List<Photo> photos) {
        this.photos = photos != null ? photos : new ArrayList<>();
        Log.d(TAG, "设置照片数据，数量: " + this.photos.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Log.d(TAG, "加载照片 " + position + ", 原始URL: " + photo.getUrl());

        if (photo.getUrl() != null && !photo.getUrl().isEmpty()) {
            String cleanUrl = photo.getUrl();
            cleanUrl = cleanUrl.replace("`", "");
            cleanUrl = cleanUrl.trim();
            Log.d(TAG, "清理后URL: " + cleanUrl);

            Glide.with(holder.itemView.getContext())
                    .load(cleanUrl)
                    .placeholder(R.drawable.ic_place)
                    .error(R.drawable.ic_place)
                    .into(holder.ivHotelPhoto);
        } else {
            Log.d(TAG, "URL为空，使用占位图");
            holder.ivHotelPhoto.setImageResource(R.drawable.ic_place);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHotelPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            ivHotelPhoto = itemView.findViewById(R.id.ivHotelPhoto);
        }
    }
}
