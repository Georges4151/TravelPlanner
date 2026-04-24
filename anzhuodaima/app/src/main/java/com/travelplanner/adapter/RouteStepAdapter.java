package com.travelplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.RouteStep;

import java.util.ArrayList;
import java.util.List;

public class RouteStepAdapter extends RecyclerView.Adapter<RouteStepAdapter.ViewHolder> {
    private List<RouteStep> steps = new ArrayList<>();

    public void setSteps(List<RouteStep> steps) {
        this.steps = steps != null ? steps : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteStep step = steps.get(position);

        if (step.getInstruction() != null && !step.getInstruction().isEmpty()) {
            holder.tvInstruction.setText(step.getInstruction());
        } else {
            holder.tvInstruction.setText("继续前行");
        }

        if (step.getRoad() != null && !step.getRoad().isEmpty()) {
            holder.tvRoad.setText(step.getRoad());
            holder.tvRoad.setVisibility(View.VISIBLE);
        } else {
            holder.tvRoad.setVisibility(View.GONE);
        }

        if (step.getDistance() != null && step.getDistance() > 0) {
            holder.tvDistance.setText(formatDistance(step.getDistance()));
            holder.tvDistance.setVisibility(View.VISIBLE);
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    private String formatDistance(int meters) {
        if (meters < 1000) {
            return meters + "米";
        } else {
            return String.format("%.1f公里", meters / 1000.0);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInstruction;
        TextView tvRoad;
        TextView tvDistance;

        ViewHolder(View itemView) {
            super(itemView);
            tvInstruction = itemView.findViewById(R.id.tvInstruction);
            tvRoad = itemView.findViewById(R.id.tvRoad);
            tvDistance = itemView.findViewById(R.id.tvDistance);
        }
    }
}
