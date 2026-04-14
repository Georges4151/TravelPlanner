package com.travelplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.AttractionSearchResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<AttractionSearchResult> results;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AttractionSearchResult result);
    }

    public SearchResultAdapter(List<AttractionSearchResult> results, OnItemClickListener listener) {
        this.results = results;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttractionSearchResult result = results.get(position);
        holder.bind(result, listener);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvType;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvType = itemView.findViewById(R.id.tv_type);
        }

        void bind(AttractionSearchResult result, OnItemClickListener listener) {
            tvName.setText(result.getName());
            tvAddress.setText(result.getAddress() != null && !result.getAddress().isEmpty() 
                ? result.getAddress() : "暂无地址信息");
            tvType.setText(result.getType() != null && !result.getType().isEmpty() 
                ? result.getType() : "景点");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(result);
                }
            });
        }
    }
}
