package com.travelplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.TicketInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_DAY_HEADER = 0;    // 天数标题
    private static final int TYPE_MODE_HEADER = 1;   // 交通方式标题
    private static final int TYPE_TICKET = 2;        // 班次卡片

    private List<Object> items = new ArrayList<>(); // 包含 DayHeader / ModeHeader / TicketInfo
    private OnTicketClickListener listener;

    // 天数标题数据类
    public static class DayHeader {
        public int dayNumber;
        public DayHeader(int dayNumber) { this.dayNumber = dayNumber; }
    }

    // 交通方式标题数据类
    public static class ModeHeader {
        public String mode;        // 原始mode代码: train/flight/transit等
        public String modeName;    // 显示名: 火车票/飞机票等
        public String modeIcon;    // emoji图标
        public int count;          // 该方式下的班次数

        public ModeHeader(String mode, String modeName, String modeIcon, int count) {
            this.mode = mode;
            this.modeName = modeName;
            this.modeIcon = modeIcon;
            this.count = count;
        }
    }

    public interface OnTicketClickListener {
        void onTicketClick(TicketInfo ticket);
    }

    public void setOnTicketClickListener(OnTicketClickListener listener) {
        this.listener = listener;
    }

    /**
     * 【改造】按 天数→交通方式 二级分组设置数据
     */
    public void setTickets(List<TicketInfo> tickets) {
        this.items = new ArrayList<>();
        if (tickets == null || tickets.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        // 第一步：按天数分组
        LinkedHashMap<Integer, List<TicketInfo>> dayMap = new LinkedHashMap<>();
        for (TicketInfo ticket : tickets) {
            int dayNum = ticket.getDayNumber();
            if (!dayMap.containsKey(dayNum)) {
                dayMap.put(dayNum, new ArrayList<>());
            }
            dayMap.get(dayNum).add(ticket);
        }

        // 第二步：对每一天，按交通方式再分组
        for (Map.Entry<Integer, List<TicketInfo>> dayEntry : dayMap.entrySet()) {
            int dayNum = dayEntry.getKey();
            List<TicketInfo> dayTickets = dayEntry.getValue();

            // 添加天数标题
            items.add(new DayHeader(dayNum));

            // 按交通方式分组
            LinkedHashMap<String, List<TicketInfo>> modeMap = new LinkedHashMap<>();
            for (TicketInfo ticket : dayTickets) {
                String modeKey = getModeKey(ticket);
                if (!modeMap.containsKey(modeKey)) {
                    modeMap.put(modeKey, new ArrayList<>());
                }
                modeMap.get(modeKey).add(ticket);
            }

            // 添加每个交通方式的标题和班次
            for (Map.Entry<String, List<TicketInfo>> modeEntry : modeMap.entrySet()) {
                String modeKey = modeEntry.getKey();
                List<TicketInfo> modeTickets = modeEntry.getValue();

                String[] modeInfo = getModeDisplayInfo(modeKey);
                items.add(new ModeHeader(modeKey, modeInfo[0], modeInfo[1], modeTickets.size()));

                for (TicketInfo ticket : modeTickets) {
                    items.add(ticket);
                }
            }
        }

        notifyDataSetChanged();
    }

    /**
     * 获取票的分组key
     */
    private String getModeKey(TicketInfo ticket) {
        String mode = ticket.getMode();
        if (mode == null || mode.isEmpty()) {
            // 通过transportMode推断
            String tm = ticket.getTransportMode();
            if (tm != null) {
                if (tm.contains("飞机") || tm.contains("航班") || tm.contains("flight")) return "flight";
                if (tm.contains("高铁") || tm.contains("动车") || tm.contains("highspeed")) return "highspeed";
                if (tm.contains("火车") || tm.contains("列车")) return "train";
                if (tm.contains("公交") || tm.contains("地铁")) return "transit";
                if (tm.contains("大巴") || tm.contains("汽车")) return "bus";
            }
            return "other";
        }
        return mode;
    }

    /**
     * 获取交通方式的显示名和图标
     * @return [显示名, emoji图标]
     */
    private String[] getModeDisplayInfo(String modeKey) {
        switch (modeKey) {
            case "train":
                return new String[]{"火车票", "🚆"};
            case "highspeed":
                return new String[]{"高铁/动车票", "🚄"};
            case "flight":
                return new String[]{"飞机票", "✈️"};
            case "transit":
                return new String[]{"公交/地铁票", "🚇"};
            case "bus":
                return new String[]{"大巴票", "🚌"};
            case "taxi":
                return new String[]{"打车", "🚕"};
            case "walk":
                return new String[]{"步行", "🚶"};
            case "ride":
                return new String[]{"骑行", "🚲"};
            case "drive":
                return new String[]{"自驾", "🚗"};
            default:
                return new String[]{"其他票务", "🎫"};
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof DayHeader) return TYPE_DAY_HEADER;
        if (item instanceof ModeHeader) return TYPE_MODE_HEADER;
        return TYPE_TICKET;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_DAY_HEADER:
                View dayView = inflater.inflate(R.layout.item_day_header, parent, false);
                return new DayHeaderViewHolder(dayView);
            case TYPE_MODE_HEADER:
                View modeView = inflater.inflate(R.layout.item_mode_header, parent, false);
                return new ModeHeaderViewHolder(modeView);
            default:
                View ticketView = inflater.inflate(R.layout.item_ticket, parent, false);
                return new TicketViewHolder(ticketView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof DayHeaderViewHolder) {
            ((DayHeaderViewHolder) holder).bind((DayHeader) item);
        } else if (holder instanceof ModeHeaderViewHolder) {
            ((ModeHeaderViewHolder) holder).bind((ModeHeader) item);
        } else {
            ((TicketViewHolder) holder).bind((TicketInfo) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ============ 天数标题 ViewHolder ============
    class DayHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDayHeader;

        DayHeaderViewHolder(View itemView) {
            super(itemView);
            tvDayHeader = itemView.findViewById(R.id.tvDayHeader);
        }

        void bind(DayHeader header) {
            tvDayHeader.setText("第" + header.dayNumber + "天");
        }
    }

    // ============ 交通方式标题 ViewHolder ============
    class ModeHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvModeIcon;
        private TextView tvModeName;
        private TextView tvModeCount;

        ModeHeaderViewHolder(View itemView) {
            super(itemView);
            tvModeIcon = itemView.findViewById(R.id.tvModeIcon);
            tvModeName = itemView.findViewById(R.id.tvModeName);
            tvModeCount = itemView.findViewById(R.id.tvModeCount);
        }

        void bind(ModeHeader header) {
            tvModeIcon.setText(header.modeIcon);
            tvModeName.setText(header.modeName);
            tvModeCount.setText(header.count + "个班次");
        }
    }

    // ============ 班次卡片 ViewHolder ============
    class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTripNumber;
        private TextView tvTransportMode;
        private TextView tvPrice;
        private TextView tvDepartureTime;
        private TextView tvArrivalTime;
        private TextView tvOrigin;
        private TextView tvDestination;
        private TextView tvDuration;
        private TextView tvSeatType;
        private View cardContainer;

        TicketViewHolder(View itemView) {
            super(itemView);
            tvTripNumber = itemView.findViewById(R.id.tvTripNumber);
            tvTransportMode = itemView.findViewById(R.id.tvTransportMode);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);
            tvOrigin = itemView.findViewById(R.id.tvOrigin);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvSeatType = itemView.findViewById(R.id.tvSeatType);
            cardContainer = itemView.findViewById(R.id.cardContainer);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    Object item = items.get(pos);
                    if (item instanceof TicketInfo) {
                        listener.onTicketClick((TicketInfo) item);
                    }
                }
            });
        }

        void bind(TicketInfo ticket) {
            String mode = ticket.getMode();

            // 步行/骑行特殊显示
            if ("walk".equals(mode) || "ride".equals(mode)) {
                tvTripNumber.setText(ticket.getDistance() != null ? ticket.getDistance() : "");

                String transportName = "walk".equals(mode) ? "步行" : "骑行";
                tvTransportMode.setText(transportName);

                if (ticket.getDuration() != null && !ticket.getDuration().isEmpty()) {
                    tvDuration.setText(ticket.getDuration());
                } else {
                    tvDuration.setText("");
                }

                tvOrigin.setText(ticket.getOrigin() != null ? ticket.getOrigin() : "");
                tvDestination.setText(ticket.getDestination() != null ? ticket.getDestination() : "");

                tvPrice.setText("");
                tvDepartureTime.setText("");
                tvArrivalTime.setText("");

                StringBuilder specialInfo = new StringBuilder();
                if ("walk".equals(mode)) {
                    if (ticket.getCaloriesBurned() != null) {
                        specialInfo.append("🔥 ").append(ticket.getCaloriesBurned());
                    }
                } else {
                    if (ticket.getBikeStations() != null) {
                        specialInfo.append("🚲 ").append(ticket.getBikeStations());
                    }
                }
                if (ticket.getInstruction() != null && !ticket.getInstruction().isEmpty()) {
                    if (specialInfo.length() > 0) specialInfo.append("\n");
                    specialInfo.append(ticket.getInstruction());
                }
                if (ticket.getLandmarks() != null && !ticket.getLandmarks().isEmpty()) {
                    if (specialInfo.length() > 0) specialInfo.append("\n");
                    specialInfo.append("📍 ").append(ticket.getLandmarks());
                }
                if (ticket.getSafetyTip() != null && !ticket.getSafetyTip().isEmpty()) {
                    if (specialInfo.length() > 0) specialInfo.append("\n");
                    specialInfo.append("⚠️ ").append(ticket.getSafetyTip());
                }
                if (ticket.getHealthTip() != null && !ticket.getHealthTip().isEmpty()) {
                    if (specialInfo.length() > 0) specialInfo.append("\n");
                    specialInfo.append("💪 ").append(ticket.getHealthTip());
                }

                if (specialInfo.length() > 0) {
                    tvSeatType.setVisibility(View.VISIBLE);
                    tvSeatType.setText(specialInfo.toString());
                } else {
                    tvSeatType.setVisibility(View.GONE);
                }

            } else if ("taxi".equals(mode)) {
                tvTripNumber.setText(ticket.getSeatType() != null ? ticket.getSeatType() : "打车");
                tvTransportMode.setText("🚕 打车");

                if (ticket.getPrice() != null) {
                    tvPrice.setText("¥" + ticket.getPrice().toString());
                } else {
                    tvPrice.setText("");
                }

                tvDuration.setText(ticket.getDuration() != null ? ticket.getDuration() : "");
                tvDepartureTime.setText("");
                tvArrivalTime.setText("");

                StringBuilder locationInfo = new StringBuilder();
                if (ticket.getOrigin() != null && !ticket.getOrigin().isEmpty()) {
                    locationInfo.append("📍 上车: ").append(ticket.getOrigin());
                }
                if (ticket.getDestination() != null && !ticket.getDestination().isEmpty()) {
                    if (locationInfo.length() > 0) locationInfo.append("\n");
                    locationInfo.append("🎯 下车: ").append(ticket.getDestination());
                }

                if (ticket.getDistance() != null && !ticket.getDistance().isEmpty()) {
                    if (locationInfo.length() > 0) locationInfo.append("\n");
                    locationInfo.append("📏 距离: ").append(ticket.getDistance());
                }
                if (ticket.getHealthTip() != null && !ticket.getHealthTip().isEmpty()) {
                    if (locationInfo.length() > 0) locationInfo.append("\n");
                    locationInfo.append("🚦 ").append(ticket.getHealthTip());
                }
                if (ticket.getSafetyTip() != null && !ticket.getSafetyTip().isEmpty()) {
                    if (locationInfo.length() > 0) locationInfo.append("\n");
                    locationInfo.append("💡 ").append(ticket.getSafetyTip());
                }

                if (locationInfo.length() > 0) {
                    tvSeatType.setVisibility(View.VISIBLE);
                    tvSeatType.setText(locationInfo.toString());
                } else {
                    tvSeatType.setVisibility(View.GONE);
                }

            } else {
                // 普通公共交通模式
                tvTripNumber.setText(ticket.getTripNumber() != null ? ticket.getTripNumber() : "");

                String modeDisplay = ticket.getTransportMode() != null ? ticket.getTransportMode() : "";
                if (ticket.getSeatType() != null && !ticket.getSeatType().isEmpty()) {
                    modeDisplay = ticket.getSeatType();
                }
                tvTransportMode.setText(modeDisplay);

                if (ticket.getPrice() != null) {
                    tvPrice.setText("¥" + ticket.getPrice().toString());
                } else {
                    tvPrice.setText("");
                }

                tvDepartureTime.setText(ticket.getDepartureTime() != null ? ticket.getDepartureTime() : "");
                tvArrivalTime.setText(ticket.getArrivalTime() != null ? ticket.getArrivalTime() : "");
                tvOrigin.setText(ticket.getOrigin() != null ? ticket.getOrigin() : "");
                tvDestination.setText(ticket.getDestination() != null ? ticket.getDestination() : "");
                tvDuration.setText(ticket.getDuration() != null ? ticket.getDuration() : "");

                if (ticket.getSeatType() != null && !ticket.getSeatType().isEmpty()) {
                    tvSeatType.setVisibility(View.VISIBLE);
                    tvSeatType.setText(ticket.getSeatType());
                } else if (ticket.getInstruction() != null && !ticket.getInstruction().isEmpty()) {
                    tvSeatType.setVisibility(View.VISIBLE);
                    tvSeatType.setText(ticket.getInstruction());
                } else {
                    tvSeatType.setVisibility(View.GONE);
                }
            }
        }
    }
}
