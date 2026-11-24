package com.travelplanner.ui.hotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.amap.AMapPoiSearchResponse;

import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private List<AMapPoiSearchResponse.Poi> hotels = new ArrayList<>();
    private OnHotelClickListener listener;

    public interface OnHotelClickListener {
        void onHotelClick(AMapPoiSearchResponse.Poi hotel, int position);

        void onBookClick(AMapPoiSearchResponse.Poi hotel, int position);
    }

    public void setOnHotelClickListener(OnHotelClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<AMapPoiSearchResponse.Poi> hotels) {
        this.hotels = hotels != null ? hotels : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMapPoiSearchResponse.Poi hotel = hotels.get(position);

        holder.tvHotelName.setText(hotel.getName());

        if (hotel.getDistance() != null && !hotel.getDistance().isEmpty()) {
            try {
                int distance = Integer.parseInt(hotel.getDistance());
                if (distance < 1000) {
                    holder.tvDistance.setText(distance + "米");
                } else {
                    holder.tvDistance.setText(String.format("%.1f公里", distance / 1000.0));
                }
            } catch (NumberFormatException e) {
                holder.tvDistance.setText(hotel.getDistance());
            }
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }

        String ratingText = null;
        String costText = null;

        if (hotel.getRating() != null && !hotel.getRating().isEmpty()) {
            ratingText = hotel.getRating() + "分";
        } else if (hotel.getBiz_ext() != null && hotel.getBiz_ext().getRating() != null
                && !hotel.getBiz_ext().getRating().isEmpty()) {
            ratingText = hotel.getBiz_ext().getRating() + "分";
        }

        if (hotel.getCost() != null && !hotel.getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(hotel.getCost());
                costText = String.format("人均：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "人均：" + hotel.getCost();
            }
        } else if (hotel.getBiz_ext() != null && hotel.getBiz_ext().getCost() != null
                && !hotel.getBiz_ext().getCost().isEmpty()) {
            try {
                double cost = Double.parseDouble(hotel.getBiz_ext().getCost());
                costText = String.format("人均：¥%.0f", cost);
            } catch (NumberFormatException e) {
                costText = "人均：" + hotel.getBiz_ext().getCost();
            }
        }

        holder.tvRating.setText(ratingText != null ? ratingText : "暂无评分");
        if (costText != null) {
            holder.tvPrice.setText(costText);
            holder.llPriceRow.setVisibility(View.VISIBLE);
        } else {
            holder.llPriceRow.setVisibility(View.GONE);
        }

        if (hotel.getAddress() != null && !hotel.getAddress().isEmpty()) {
            holder.tvAddress.setText(hotel.getAddress());
        } else {
            holder.tvAddress.setText("地址信息未知");
        }

        String type = hotel.getType();
        if (type != null && !type.isEmpty()) {
            String[] types = type.split(";");
            if (types.length > 0) {
                holder.tvRoomType.setText("类型：" + types[types.length - 1]);
            } else {
                holder.tvRoomType.setText("类型：酒店");
            }
        } else {
            holder.tvRoomType.setText("类型：酒店");
        }

        if (hotel.getFirstTel() != null && !hotel.getFirstTel().isEmpty()) {
            holder.tvPhone.setText("📞 " + hotel.getFirstTel());
            holder.tvPhone.setVisibility(View.VISIBLE);
        } else {
            holder.tvPhone.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHotelClick(hotel, position);
            }
        });

        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(hotel, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHotelName, tvDistance, tvRating, tvPrice, tvAddress, tvRoomType, tvPhone;
        View llPriceRow;
        Button btnBook;

        ViewHolder(View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            llPriceRow = itemView.findViewById(R.id.ll_price_row);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}
