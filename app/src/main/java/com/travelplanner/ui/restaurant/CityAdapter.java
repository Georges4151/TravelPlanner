package com.travelplanner.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.City;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<Object> items = new ArrayList<>();
    private OnCityClickListener listener;

    public interface OnCityClickListener {
        void onCityClick(City city);
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<City> cities, boolean showProvince) {
        items.clear();
        if (showProvince) {
            String lastProvince = null;
            for (City city : cities) {
                if (!city.getProvince().equals(lastProvince)) {
                    items.add(city.getProvince());
                    lastProvince = city.getProvince();
                }
                items.add(city);
            }
        } else {
            items.addAll(cities);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        return item instanceof String ? 0 : 1;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        Object item = items.get(position);
        if (item instanceof String) {
            holder.tvProvince.setVisibility(View.VISIBLE);
            holder.tvProvince.setText((String) item);
            holder.tvCityName.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        } else if (item instanceof City) {
            City city = (City) item;
            holder.tvProvince.setVisibility(View.GONE);
            holder.tvCityName.setVisibility(View.VISIBLE);
            holder.tvCityName.setText(city.getName());
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCityClick(city);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvProvince;
        TextView tvCityName;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProvince = itemView.findViewById(R.id.tvProvince);
            tvCityName = itemView.findViewById(R.id.tvCityName);
        }
    }
}
