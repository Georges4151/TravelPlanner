package com.travelplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.Checklist;

import java.util.ArrayList;
import java.util.List;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {
    private List<Checklist> items = new ArrayList<>();
    private OnItemCheckListener checkListener;
    private OnItemDeleteListener deleteListener;

    public void setItems(List<Checklist> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Checklist> getItems() {
        return items;
    }

    public void setOnItemCheckListener(OnItemCheckListener listener) {
        this.checkListener = listener;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Checklist item = items.get(position);
        holder.tvItemName.setText(item.getItemName());
        holder.tvCategory.setText(item.getCategory());
        boolean isChecked = item.getIsChecked() != null && item.getIsChecked();
        holder.cbItem.setChecked(isChecked);
        updateCheckedTag(holder, isChecked);

        holder.cbItem.setOnCheckedChangeListener((buttonView, isChecked2) -> {
            if (checkListener != null) {
                checkListener.onItemCheck(item.getId(), isChecked2);
            }
            updateCheckedTag(holder, isChecked2);
        });

        holder.ivDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onItemDelete(item.getId());
            }
        });
    }

    private void updateCheckedTag(ViewHolder holder, boolean checked) {
        holder.tvCheckedTag.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbItem;
        TextView tvItemName;
        TextView tvCategory;
        TextView tvCheckedTag;
        ImageView ivDelete;

        ViewHolder(View itemView) {
            super(itemView);
            cbItem = itemView.findViewById(R.id.cbItem);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvCheckedTag = itemView.findViewById(R.id.tvCheckedTag);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    public interface OnItemCheckListener {
        void onItemCheck(Long id, boolean checked);
    }

    public interface OnItemDeleteListener {
        void onItemDelete(Long id);
    }
}
