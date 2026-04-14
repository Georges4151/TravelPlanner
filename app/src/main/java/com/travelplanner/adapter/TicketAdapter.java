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
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private List<TicketInfo> tickets = new ArrayList<>();
    private OnTicketClickListener listener;

    public interface OnTicketClickListener {
        void onTicketClick(TicketInfo ticket);
    }

    public void setOnTicketClickListener(OnTicketClickListener listener) {
        this.listener = listener;
    }

    public void setTickets(List<TicketInfo> tickets) {
        this.tickets = tickets != null ? tickets : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketInfo ticket = tickets.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTripNumber;
        private TextView tvTransportMode;
        private TextView tvPrice;
        private TextView tvDepartureTime;
        private TextView tvArrivalTime;
        private TextView tvOrigin;
        private TextView tvDestination;
        private TextView tvDuration;
        private TextView tvSeatType;

        ViewHolder(View itemView) {
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

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTicketClick(tickets.get(position));
                }
            });
        }

        void bind(TicketInfo ticket) {
            tvTripNumber.setText(ticket.getTripNumber() != null ? ticket.getTripNumber() : "");
            tvTransportMode.setText(ticket.getTransportMode() != null ? ticket.getTransportMode() : "");
            tvPrice.setText(ticket.getPrice() != null ? "¥" + ticket.getPrice().toString() : "");
            tvDepartureTime.setText(ticket.getDepartureTime() != null ? ticket.getDepartureTime() : "");
            tvArrivalTime.setText(ticket.getArrivalTime() != null ? ticket.getArrivalTime() : "");
            tvOrigin.setText(ticket.getOrigin() != null ? ticket.getOrigin() : "");
            tvDestination.setText(ticket.getDestination() != null ? ticket.getDestination() : "");
            tvDuration.setText(ticket.getDuration() != null ? ticket.getDuration() : "");
            tvSeatType.setText(ticket.getSeatType() != null ? ticket.getSeatType() : "");
        }
    }
}
