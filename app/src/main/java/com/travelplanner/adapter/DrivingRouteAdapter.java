package com.travelplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.DrivingRouteSegment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DrivingRouteAdapter extends RecyclerView.Adapter<DrivingRouteAdapter.ViewHolder> {

    private List<DrivingRouteSegment> segments = new ArrayList<>();

    public void setSegments(List<DrivingRouteSegment> segments) {
        this.segments = segments != null ? segments : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driving_route, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrivingRouteSegment segment = segments.get(position);
        holder.bind(segment, position + 1);
    }

    @Override
    public int getItemCount() {
        return segments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSegmentNumber;
        private TextView tvOrigin;
        private TextView tvDestination;
        private TextView tvDistance;
        private TextView tvDuration;
        private TextView tvRoadType;
        private TextView tvInstruction;
        private TextView tvTollFee;

        ViewHolder(View itemView) {
            super(itemView);
            tvSegmentNumber = itemView.findViewById(R.id.tvSegmentNumber);
            tvOrigin = itemView.findViewById(R.id.tvOrigin);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvRoadType = itemView.findViewById(R.id.tvRoadType);
            tvInstruction = itemView.findViewById(R.id.tvInstruction);
            tvTollFee = itemView.findViewById(R.id.tvTollFee);
        }

        void bind(DrivingRouteSegment segment, int segmentNum) {
            tvSegmentNumber.setText("第" + segmentNum + "段");
            tvOrigin.setText(segment.getOrigin() != null ? segment.getOrigin() : "");
            tvDestination.setText(segment.getDestination() != null ? segment.getDestination() : "");
            tvDistance.setText(segment.getDistance() != null ? segment.getDistance() : "");
            tvDuration.setText(segment.getDuration() != null ? segment.getDuration() : "");
            tvRoadType.setText(segment.getRoadType() != null ? segment.getRoadType() : "");
            tvInstruction.setText(segment.getInstruction() != null ? segment.getInstruction() : "");

            if (segment.getTollFee() != null && segment.getTollFee().compareTo(BigDecimal.ZERO) > 0) {
                tvTollFee.setText("过路费: ¥" + segment.getTollFee().toString());
                tvTollFee.setVisibility(View.VISIBLE);
            } else {
                tvTollFee.setVisibility(View.GONE);
            }
        }
    }
}
