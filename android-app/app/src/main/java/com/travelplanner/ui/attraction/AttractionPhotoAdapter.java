package com.travelplanner.ui.attraction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelplanner.R;

import java.util.ArrayList;
import java.util.List;

public class AttractionPhotoAdapter extends RecyclerView.Adapter<AttractionPhotoAdapter.ViewHolder> {

    private static final String TAG = "AttractionPhotoAdapter";
    private List<String> imageUrls = new ArrayList<>();

    public void setData(List<String> urls) {
        this.imageUrls = urls != null ? urls : new ArrayList<>();
        Log.d(TAG, "设置图片数据，数量: " + this.imageUrls.size());
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
        String url = imageUrls.get(position);
        Log.d(TAG, "加载图片 " + position + ", URL: " + url);

        if (url != null && !url.isEmpty()) {
            String cleanUrl = url.replace("`", "").trim();
            Glide.with(holder.itemView.getContext())
                    .load(cleanUrl)
                    .placeholder(R.drawable.ic_place)
                    .error(R.drawable.ic_place)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.ivPhoto);
        } else {
            holder.ivPhoto.setImageResource(R.drawable.ic_place);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivHotelPhoto);
        }
    }
}
