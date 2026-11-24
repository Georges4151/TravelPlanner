package com.travelplanner.ui.restaurant;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private OnDistrictClickListener districtListener;

    // 存储城市和其区县展开状态
    private List<City> citiesList = new ArrayList<>();
    private List<Boolean> expandedStates = new ArrayList<>();

    public interface OnCityClickListener {
        void onCityClick(City city);
    }

    public interface OnDistrictClickListener {
        void onDistrictClick(City city, String district);
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.listener = listener;
    }

    public void setOnDistrictClickListener(OnDistrictClickListener listener) {
        this.districtListener = listener;
    }

    public void setData(List<City> cities, boolean showProvince) {
        items.clear();
        citiesList.clear();
        expandedStates.clear();

        if (showProvince) {
            String lastProvince = null;
            for (City city : cities) {
                if (!city.getProvince().equals(lastProvince)) {
                    items.add(city.getProvince());
                    lastProvince = city.getProvince();
                }
                items.add(city);
                citiesList.add(city);
                expandedStates.add(false);
            }
        } else {
            items.addAll(cities);
            citiesList.addAll(cities);
            for (int i = 0; i < cities.size(); i++) {
                expandedStates.add(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof String) {
            return 0; // 省份标题
        } else if (item instanceof City) {
            // 检查是否是展开的区县
            City city = (City) item;
            if (city.isExpanded()) {
                return 2; // 展开的区县
            }
            return 1; // 普通城市
        }
        return 0;
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
            // 省份标题
            holder.tvProvince.setVisibility(View.VISIBLE);
            holder.tvProvince.setText((String) item);
            holder.tvCityName.setVisibility(View.GONE);
            holder.tvDistrictHint.setVisibility(View.GONE);
            holder.llDistricts.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else if (item instanceof City) {
            City city = (City) item;
            holder.tvProvince.setVisibility(View.GONE);
            holder.tvCityName.setVisibility(View.VISIBLE);
            holder.tvCityName.setText(city.getName());

            // 显示区县提示
            if (city.getDistricts() != null && !city.getDistricts().isEmpty()) {
                holder.tvDistrictHint.setVisibility(View.VISIBLE);
                holder.tvDistrictHint.setText(city.isExpanded() ? "点击收起" : "点击查看区县");
                holder.llDistricts.setVisibility(city.isExpanded() ? View.VISIBLE : View.GONE);

                // 显示区县列表
                holder.llDistricts.removeAllViews();
                if (city.isExpanded()) {
                    for (String district : city.getDistricts()) {
                        TextView tvDistrict = new TextView(holder.itemView.getContext());
                        tvDistrict.setText("  " + district);
                        tvDistrict.setTextSize(14);
                        tvDistrict.setTextColor(Color.parseColor("#007AFF"));
                        tvDistrict.setPadding(48, 8, 16, 8);
                        tvDistrict.setClickable(true);
                        tvDistrict.setOnClickListener(v -> {
                            if (districtListener != null) {
                                districtListener.onDistrictClick(city, district);
                            }
                        });
                        holder.llDistricts.addView(tvDistrict);
                    }
                }
            } else {
                holder.tvDistrictHint.setVisibility(View.GONE);
                holder.llDistricts.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> {
                // 切换展开状态
                city.setExpanded(!city.isExpanded());
                notifyDataSetChanged();
            });
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (Object item : items) {
            if (item instanceof City) {
                City city = (City) item;
                count++; // 城市本身
                if (city.isExpanded() && city.getDistricts() != null) {
                    count += city.getDistricts().size(); // 展开的区县
                }
            } else {
                count++;
            }
        }
        return count;
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvProvince;
        TextView tvCityName;
        TextView tvDistrictHint;
        LinearLayout llDistricts;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProvince = itemView.findViewById(R.id.tvProvince);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvDistrictHint = itemView.findViewById(R.id.tvDistrictHint);
            llDistricts = itemView.findViewById(R.id.llDistricts);
        }
    }
}
