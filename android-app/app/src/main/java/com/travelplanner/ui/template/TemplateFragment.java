package com.travelplanner.ui.template;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.model.Template;
import com.travelplanner.ui.itinerary.ItineraryDetailActivity;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class TemplateFragment extends Fragment {
    private RecyclerView rvTemplates;
    private SwipeRefreshLayout swipeRefresh;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<Template> templates = new ArrayList<>();
    private TemplateAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTemplates = view.findViewById(R.id.rv_templates);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);

        adapter = new TemplateAdapter(templates);
        rvTemplates.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTemplates.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadTemplates);

        loadTemplates();
    }

    private void loadTemplates() {
        HttpUtil.get("template/hot?limit=10", new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    swipeRefresh.setRefreshing(false);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<List<Template>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Template>>>() {
                        }.getType());

                handler.post(() -> {
                    swipeRefresh.setRefreshing(false);
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        templates.clear();
                        templates.addAll(apiResponse.getData());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void useTemplate(Long templateId, View btnUse) {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            if (getContext() != null) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (btnUse instanceof MaterialButton) {
            ((MaterialButton) btnUse).setEnabled(false);
            ((MaterialButton) btnUse).setText("套用中...");
        }

        HttpUtil.post("itinerary/template/" + templateId + "?userId=" + userId, "", new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    if (btnUse instanceof MaterialButton) {
                        ((MaterialButton) btnUse).setEnabled(true);
                        ((MaterialButton) btnUse).setText("立即套用");
                    }
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<Itinerary> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Itinerary>>() {
                        }.getType());

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    handler.post(() -> {
                        if (btnUse instanceof MaterialButton) {
                            ((MaterialButton) btnUse).setText("✓ 套用成功");
                            ((MaterialButton) btnUse).setBackgroundColor(Color.parseColor("#4CAF50"));
                        }
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "行程创建成功", Toast.LENGTH_SHORT).show();
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(getContext(), ItineraryDetailActivity.class);
                            intent.putExtra("itinerary_id", apiResponse.getData().getId());
                            startActivity(intent);
                            if (btnUse instanceof MaterialButton) {
                                ((MaterialButton) btnUse).setEnabled(true);
                                ((MaterialButton) btnUse).setText("立即套用");
                                ((MaterialButton) btnUse).setBackgroundColor(Color.parseColor("#2196F3"));
                            }
                        }, 800);
                    });
                } else {
                    handler.post(() -> {
                        if (btnUse instanceof MaterialButton) {
                            ((MaterialButton) btnUse).setEnabled(true);
                            ((MaterialButton) btnUse).setText("立即套用");
                        }
                        if (getContext() != null) {
                            String message = apiResponse.getMessage() != null ? apiResponse.getMessage() : "操作失败";
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
        private List<Template> list;

        TemplateAdapter(List<Template> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Template template = list.get(position);
            holder.tvTitle.setText(template.getTitle());
            holder.tvDestination.setText(template.getDestination());
            holder.tvDays.setText(template.getDays() + "天");
            holder.tvHighlights.setText("亮点: " + template.getHighlights());
            holder.tvViews.setText("👁 " + template.getViewCount() + " 浏览");
            holder.tvStyle.setText(template.getStyle() != null ? template.getStyle() : "休闲");
            holder.tvBudget.setText("💰 ¥" + template.getBudget());

            // 加载模板封面图片
            String coverImage = template.getCoverImage();
            if (coverImage != null && !coverImage.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(coverImage)
                        .placeholder(R.drawable.ic_place)
                        .error(R.drawable.ic_place)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(holder.ivTemplateCover);
            } else {
                holder.ivTemplateCover.setImageResource(R.drawable.ic_place);
            }

            holder.btnUse.setOnClickListener(v -> useTemplate(template.getId(), holder.btnUse));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDestination, tvDays, tvHighlights, tvViews, tvStyle, tvBudget;
            ImageView ivTemplateCover;
            View btnUse;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvDestination = itemView.findViewById(R.id.tv_destination);
                tvDays = itemView.findViewById(R.id.tv_days);
                tvHighlights = itemView.findViewById(R.id.tv_highlights);
                tvViews = itemView.findViewById(R.id.tv_views);
                tvStyle = itemView.findViewById(R.id.tv_style);
                tvBudget = itemView.findViewById(R.id.tv_budget);
                ivTemplateCover = itemView.findViewById(R.id.iv_template_cover);
                btnUse = itemView.findViewById(R.id.btn_use);
            }
        }
    }
}
