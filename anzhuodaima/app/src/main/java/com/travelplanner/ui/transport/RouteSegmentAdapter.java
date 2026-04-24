package com.travelplanner.ui.transport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.RoutePlan;
import com.travelplanner.model.TransportOptions;

import java.util.List;

public class RouteSegmentAdapter extends RecyclerView.Adapter<RouteSegmentAdapter.ViewHolder> {

    private List<TransportOptions> segments;

    public RouteSegmentAdapter() {
        this.segments = new java.util.ArrayList<>();
    }

    public RouteSegmentAdapter(List<TransportOptions> segments) {
        this.segments = segments != null ? segments : new java.util.ArrayList<>();
    }

    public void setData(List<TransportOptions> segments) {
        this.segments = segments != null ? segments : new java.util.ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_segment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransportOptions segment = segments.get(position);

        holder.tvRouteNumber.setText(String.valueOf(position + 1));
        holder.tvFromLocation.setText("从: " + segment.getFromLocation());
        holder.tvToLocation.setText("到: " + segment.getToLocation());

        holder.llTransportModes.removeAllViews();

        addTransportMode(holder, "驾车", segment.getDrivingPlans());
        addTransportMode(holder, "公交", segment.getTransitPlans());
        addTransportMode(holder, "步行", segment.getWalkingPlans());
        addTransportMode(holder, "骑行", segment.getCyclingPlans());
    }

    private void addTransportMode(ViewHolder holder, String modeName, List<RoutePlan> plans) {
        if (plans == null || plans.isEmpty()) {
            return;
        }

        RoutePlan plan = plans.get(0);
        View modeView = LayoutInflater.from(holder.itemView.getContext())
                .inflate(R.layout.item_transport_mode, holder.llTransportModes, false);

        TextView tvModeName = modeView.findViewById(R.id.tvModeName);
        TextView tvModeInfo = modeView.findViewById(R.id.tvModeInfo);
        TextView tvModeCost = modeView.findViewById(R.id.tvModeCost);

        tvModeName.setText(modeName);

        Integer distance = plan.getDistance();
        Integer duration = plan.getDuration();

        double distanceKm = (distance != null ? distance : 0) / 1000.0;
        long durationMinutes = (duration != null ? duration : 0) / 60;
        String info = String.format("距离：%.2f公里  时间：%d分钟", distanceKm, durationMinutes);
        tvModeInfo.setText(info);

        if (plan.getCost() != null) {
            tvModeCost.setText(String.format("¥%.2f", plan.getCost().floatValue()));
        } else {
            if ("步行".equals(modeName)) {
                tvModeCost.setText("免费");
            } else {
                tvModeCost.setText("¥0.00");
            }
        }

        holder.llTransportModes.addView(modeView);
    }

    @Override
    public int getItemCount() {
        return segments != null ? segments.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRouteNumber;
        TextView tvFromLocation;
        TextView tvToLocation;
        LinearLayout llTransportModes;

        ViewHolder(View itemView) {
            super(itemView);
            tvRouteNumber = itemView.findViewById(R.id.tvRouteNumber);
            tvFromLocation = itemView.findViewById(R.id.tvFromLocation);
            tvToLocation = itemView.findViewById(R.id.tvToLocation);
            llTransportModes = itemView.findViewById(R.id.llTransportModes);
        }
    }
}
