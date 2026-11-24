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

public class DrivingRouteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SEGMENT = 1;

    private List<Object> items = new ArrayList<>(); // 包含标题和 DrivingRouteSegment
    private OnSegmentClickListener listener;

    public interface OnSegmentClickListener {
        void onSegmentClick(DrivingRouteSegment segment);
    }

    public void setOnSegmentClickListener(OnSegmentClickListener listener) {
        this.listener = listener;
    }

    public void setSegments(List<DrivingRouteSegment> segments) {
        this.items = new ArrayList<>();
        if (segments != null && !segments.isEmpty()) {
            int currentDay = -1;
            for (DrivingRouteSegment segment : segments) {
                int dayNum = segment.getDayNumber();
                if (dayNum != currentDay) {
                    // 添加日期标题
                    String modeName = segment.getModeName() != null ? segment.getModeName() : "驾车";
                    items.add("第" + dayNum + "天 - " + modeName);
                    currentDay = dayNum;
                }
                items.add(segment);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_HEADER : TYPE_SEGMENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_day_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_driving_route, parent, false);
            return new SegmentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((String) items.get(position));
        } else {
            int segmentIndex = 0;
            for (int i = 0; i < position; i++) {
                if (items.get(i) instanceof DrivingRouteSegment) {
                    segmentIndex++;
                }
            }
            ((SegmentViewHolder) holder).bind((DrivingRouteSegment) items.get(position), segmentIndex + 1);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // 日期标题 ViewHolder
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDayHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvDayHeader = itemView.findViewById(R.id.tvDayHeader);
        }

        void bind(String title) {
            tvDayHeader.setText(title);
        }
    }

    // 路线分段 ViewHolder
    class SegmentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSegmentNumber;
        private TextView tvOrigin;
        private TextView tvDestination;
        private TextView tvDistance;
        private TextView tvDuration;
        private TextView tvRoadType;
        private TextView tvInstruction;
        private TextView tvTollFee;

        SegmentViewHolder(View itemView) {
            super(itemView);
            tvSegmentNumber = itemView.findViewById(R.id.tvSegmentNumber);
            tvOrigin = itemView.findViewById(R.id.tvOrigin);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvRoadType = itemView.findViewById(R.id.tvRoadType);
            tvInstruction = itemView.findViewById(R.id.tvInstruction);
            tvTollFee = itemView.findViewById(R.id.tvTollFee);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    Object item = items.get(pos);
                    if (item instanceof DrivingRouteSegment) {
                        listener.onSegmentClick((DrivingRouteSegment) item);
                    }
                }
            });
        }

        void bind(DrivingRouteSegment segment, int segmentNum) {
            tvSegmentNumber.setText("第" + segmentNum + "段");
            tvOrigin.setText(segment.getOrigin() != null ? segment.getOrigin() : "");
            tvDestination.setText(segment.getDestination() != null ? segment.getDestination() : "");
            tvDistance.setText(segment.getDistance() != null ? segment.getDistance() : "");
            tvDuration.setText(segment.getDuration() != null ? segment.getDuration() : "");
            tvRoadType.setText(segment.getRoadType() != null ? segment.getRoadType() : "");
            tvInstruction.setText(segment.getInstruction() != null ? segment.getInstruction() : "");

            // 显示过路费
            if (segment.getTollFee() != null && segment.getTollFee().compareTo(BigDecimal.ZERO) > 0) {
                tvTollFee.setText("过路费: ¥" + segment.getTollFee().toString());
                tvTollFee.setVisibility(View.VISIBLE);
            } else {
                tvTollFee.setVisibility(View.GONE);
            }
        }
    }
}
