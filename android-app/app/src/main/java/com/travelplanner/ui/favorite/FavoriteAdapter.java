package com.travelplanner.ui.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travelplanner.R;
import com.travelplanner.model.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Favorite> favorites = new ArrayList<>();
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Favorite favorite);
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Favorite> favorites) {
        this.favorites = favorites != null ? favorites : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);
        holder.tvName.setText(favorite.getName());
        holder.tvAddress.setText(favorite.getAddress());

        if (favorite.getRating() != null && !favorite.getRating().isEmpty()) {
            holder.tvRating.setText(favorite.getRating() + "分");
        } else {
            holder.tvRating.setText("暂无评分");
        }

        if (favorite.getPrice() != null && !favorite.getPrice().isEmpty()) {
            String type = favorite.getType();
            if ("restaurant".equals(type)) {
                holder.tvPrice.setText("人均：¥" + favorite.getPrice());
            } else if ("hotel".equals(type)) {
                holder.tvPrice.setText("¥" + favorite.getPrice() + "/晚");
            } else if ("attraction".equals(type)) {
                holder.tvPrice.setText("门票：¥" + favorite.getPrice());
            } else {
                holder.tvPrice.setText("¥" + favorite.getPrice());
            }
        } else {
            holder.tvPrice.setText("");
        }

        if (favorite.getImageUrl() != null && !favorite.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(favorite.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClick(favorite);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvAddress;
        TextView tvRating;
        TextView tvPrice;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
