package com.travelplanner.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travelplanner.R;
import com.travelplanner.model.amap.AMapPoiSearchResponse;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<AMapPoiSearchResponse.Poi> restaurants = new ArrayList<>();
    private OnRestaurantClickListener listener;

    public interface OnRestaurantClickListener {
        void onRestaurantClick(AMapPoiSearchResponse.Poi restaurant, int position);
    }

    public void setOnRestaurantClickListener(OnRestaurantClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<AMapPoiSearchResponse.Poi> restaurants) {
        this.restaurants = restaurants != null ? restaurants : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMapPoiSearchResponse.Poi restaurant = restaurants.get(position);

        holder.tvRestaurantName.setText(restaurant.getName());

        if (restaurant.getPhotos() != null && !restaurant.getPhotos().isEmpty()) {
            String imageUrl = restaurant.getPhotos().get(0).getUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(holder.ivRestaurantImage.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .centerCrop()
                        .into(holder.ivRestaurantImage);
            } else {
                holder.ivRestaurantImage.setImageResource(R.drawable.ic_image_placeholder);
            }
        } else {
            holder.ivRestaurantImage.setImageResource(R.drawable.ic_image_placeholder);
        }

        if (restaurant.getDistance() != null && !restaurant.getDistance().isEmpty()) {
            try {
                int distance = Integer.parseInt(restaurant.getDistance());
                if (distance < 1000) {
                    holder.tvDistance.setText(distance + "米");
                } else {
                    holder.tvDistance.setText(String.format("%.1f公里", distance / 1000.0));
                }
                holder.tvDistance.setVisibility(View.VISIBLE);
            } catch (NumberFormatException e) {
                holder.tvDistance.setText(restaurant.getDistance());
                holder.tvDistance.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }

        String ratingValue = null;
        if (restaurant.getRating() != null && !restaurant.getRating().isEmpty()) {
            ratingValue = restaurant.getRating();
        } else if (restaurant.getBiz_ext() != null && restaurant.getBiz_ext().getRating() != null
                && !restaurant.getBiz_ext().getRating().isEmpty()) {
            ratingValue = restaurant.getBiz_ext().getRating();
        }
        if (ratingValue != null) {
            holder.tvRating.setText(ratingValue + "分");
        } else {
            holder.tvRating.setText("暂无评分");
        }

        String costValue = null;
        if (restaurant.getCost() != null && !restaurant.getCost().isEmpty()) {
            costValue = restaurant.getCost();
        } else if (restaurant.getBiz_ext() != null && restaurant.getBiz_ext().getCost() != null
                && !restaurant.getBiz_ext().getCost().isEmpty()) {
            costValue = restaurant.getBiz_ext().getCost();
        }
        if (costValue != null) {
            try {
                double cost = Double.parseDouble(costValue);
                holder.tvAvgPrice.setText(String.format("人均：¥%.0f", cost));
            } catch (NumberFormatException e) {
                holder.tvAvgPrice.setText("人均：" + costValue);
            }
        } else {
            holder.tvAvgPrice.setText("人均：¥0");
        }

        String businessHoursValue = null;
        if (restaurant.getBiz_ext() != null) {
            if (restaurant.getBiz_ext().getOpentime2() != null && !restaurant.getBiz_ext().getOpentime2().isEmpty()) {
                businessHoursValue = restaurant.getBiz_ext().getOpentime2();
            } else if (restaurant.getBiz_ext().getOpen_time() != null
                    && !restaurant.getBiz_ext().getOpen_time().isEmpty()) {
                businessHoursValue = restaurant.getBiz_ext().getOpen_time();
            }
        }
        if (businessHoursValue != null) {
            String openTime = businessHoursValue;
            if (openTime.contains("全天") || openTime.contains("24小时")) {
                holder.tvQueueStatus.setText("全天营业");
            } else {
                holder.tvQueueStatus.setText("营业时间：" + openTime);
            }
            holder.tvQueueStatus.setTextColor(0xFF4CAF50);
        } else {
            holder.tvQueueStatus.setText("营业时间暂无");
            holder.tvQueueStatus.setTextColor(0xFF999999);
        }

        if (restaurant.getAddress() != null && !restaurant.getAddress().isEmpty()) {
            holder.tvAddress.setText(restaurant.getAddress());
        } else {
            holder.tvAddress.setText("地址信息未知");
        }

        if (restaurant.getFirstTel() != null && !restaurant.getFirstTel().isEmpty()) {
            holder.tvPhone.setText("📞 " + restaurant.getFirstTel());
            holder.tvPhone.setVisibility(View.VISIBLE);
        } else {
            holder.tvPhone.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRestaurantClick(restaurant, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRestaurantImage;
        TextView tvRestaurantName, tvDistance, tvRating, tvAvgPrice, tvAddress, tvQueueStatus, tvPhone;

        ViewHolder(View itemView) {
            super(itemView);
            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvAvgPrice = itemView.findViewById(R.id.tvAvgPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvQueueStatus = itemView.findViewById(R.id.tvQueueStatus);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
