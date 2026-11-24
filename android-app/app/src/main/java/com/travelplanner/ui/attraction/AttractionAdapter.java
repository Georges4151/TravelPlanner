package com.travelplanner.ui.attraction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.amap.AMapPoiSearchResponse;

import java.util.ArrayList;
import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private List<AMapPoiSearchResponse.Poi> attractions = new ArrayList<>();
    private OnAttractionClickListener listener;

    public interface OnAttractionClickListener {
        void onAttractionClick(AMapPoiSearchResponse.Poi attraction, int position);
    }

    public void setOnAttractionClickListener(OnAttractionClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<AMapPoiSearchResponse.Poi> attractions) {
        this.attractions = attractions != null ? attractions : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMapPoiSearchResponse.Poi attraction = attractions.get(position);

        holder.tvAttractionName.setText(attraction.getName());

        if (attraction.getDistance() != null && !attraction.getDistance().isEmpty()) {
            try {
                int distance = Integer.parseInt(attraction.getDistance());
                if (distance < 1000) {
                    holder.tvDistance.setText(distance + "米");
                } else {
                    holder.tvDistance.setText(String.format("%.1f公里", distance / 1000.0));
                }
            } catch (NumberFormatException e) {
                holder.tvDistance.setText(attraction.getDistance());
            }
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }

        String ratingText = null;
        String costText = null;

        if (attraction.getRating() != null && !attraction.getRating().isEmpty()) {
            ratingText = attraction.getRating() + "分";
        } else if (attraction.getBiz_ext() != null && attraction.getBiz_ext().getRating() != null
                && !attraction.getBiz_ext().getRating().isEmpty()) {
            ratingText = attraction.getBiz_ext().getRating() + "分";
        }

        if (attraction.getCost() != null && !attraction.getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(attraction.getCost());
                costText = String.format("门票：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "门票：" + attraction.getCost();
            }
        } else if (attraction.getBiz_ext() != null && attraction.getBiz_ext().getCost() != null
                && !attraction.getBiz_ext().getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(attraction.getBiz_ext().getCost());
                costText = String.format("门票：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "门票：" + attraction.getBiz_ext().getCost();
            }
        }

        holder.tvRating.setText(ratingText != null ? ratingText : "暂无评分");
        holder.tvPrice.setText(costText != null ? costText : "门票：免费");

        if (attraction.getAddress() != null && !attraction.getAddress().isEmpty()) {
            holder.tvAddress.setText(attraction.getAddress());
        } else {
            holder.tvAddress.setText("地址信息未知");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAttractionClick(attraction, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttractionName, tvDistance, tvRating, tvPrice, tvAddress;

        ViewHolder(View itemView) {
            super(itemView);
            tvAttractionName = itemView.findViewById(R.id.tvAttractionName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }
}
