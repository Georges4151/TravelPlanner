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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.ui.itinerary.ItineraryDetailActivity;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class TemplateActivity extends AppCompatActivity {
    private RecyclerView rvTemplates;
    private TextView tabAll, tabLeisure, tabFamily, tabOffpeak, tabCulture;
    private Handler handler = new Handler(Looper.getMainLooper());
    private TemplateAdapter adapter;
    private List<Itinerary> templates = new ArrayList<>();
    private String currentCategory = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        initViews();
        loadTemplates();
    }

    private void initViews() {
        rvTemplates = findViewById(R.id.rv_templates);
        tabAll = findViewById(R.id.tab_all);
        tabLeisure = findViewById(R.id.tab_leisure);
        tabFamily = findViewById(R.id.tab_family);
        tabOffpeak = findViewById(R.id.tab_offpeak);
        tabCulture = findViewById(R.id.tab_culture);

        adapter = new TemplateAdapter(templates);
        rvTemplates.setLayoutManager(new LinearLayoutManager(this));
        rvTemplates.setAdapter(adapter);

        tabAll.setOnClickListener(v -> selectCategory("all"));
        tabLeisure.setOnClickListener(v -> selectCategory("休闲"));
        tabFamily.setOnClickListener(v -> selectCategory("亲子"));
        tabOffpeak.setOnClickListener(v -> selectCategory("错峰优先"));
        tabCulture.setOnClickListener(v -> selectCategory("文化"));
    }

    private void selectCategory(String category) {
        currentCategory = category;
        updateTabStyles();
        loadTemplates();
    }

    private void updateTabStyles() {
        resetTabStyle(tabAll);
        resetTabStyle(tabLeisure);
        resetTabStyle(tabFamily);
        resetTabStyle(tabOffpeak);
        resetTabStyle(tabCulture);

        if ("all".equals(currentCategory)) setActiveTabStyle(tabAll);
        else if ("休闲".equals(currentCategory)) setActiveTabStyle(tabLeisure);
        else if ("亲子".equals(currentCategory)) setActiveTabStyle(tabFamily);
        else if ("错峰优先".equals(currentCategory)) setActiveTabStyle(tabOffpeak);
        else if ("文化".equals(currentCategory)) setActiveTabStyle(tabCulture);
    }

    private void resetTabStyle(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.text_primary));
        tab.setBackgroundResource(R.drawable.bg_tag_outline);
    }

    private void setActiveTabStyle(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.white));
        tab.setBackgroundResource(R.drawable.bg_travel_gradient);
    }

    private void loadTemplates() {
        String url = "template";
        if (!"all".equals(currentCategory)) {
            url += "?style=" + currentCategory;
        }

        HttpUtil.get(url, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    Toast.makeText(TemplateActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<List<Itinerary>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Itinerary>>>(){}.getType());

                handler.post(() -> {
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        templates.clear();
                        templates.addAll(apiResponse.getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TemplateActivity.this, "加载失败: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
        private List<Itinerary> list;

        TemplateAdapter(List<Itinerary> list) {
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
            Itinerary template = list.get(position);
            holder.tvTitle.setText(template.getTitle());
            holder.tvDestination.setText("📍 " + template.getDestination());
            holder.tvDays.setText("📅 " + template.getDays() + "天");
            holder.tvBudget.setText("💰 ¥" + template.getBudget());
            holder.tvStyle.setText(template.getTravelStyle() != null ? template.getTravelStyle() : "休闲");
            holder.tvViews.setText("👁 " + (position + 1) + "." + (position + 2) + "k 浏览");

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

            String highlights = getHighlights(template);
            holder.tvHighlights.setText("亮点: " + highlights);

            holder.btnUse.setOnClickListener(v -> useTemplate(template.getId(), holder.btnUse));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDestination, tvDays, tvBudget, tvStyle, tvHighlights, tvViews;
            ImageView ivTemplateCover;
            MaterialButton btnUse;

            ViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tv_title);
                tvDestination = view.findViewById(R.id.tv_destination);
                tvDays = view.findViewById(R.id.tv_days);
                tvBudget = view.findViewById(R.id.tv_budget);
                tvStyle = view.findViewById(R.id.tv_style);
                tvHighlights = view.findViewById(R.id.tv_highlights);
                tvViews = view.findViewById(R.id.tv_views);
                ivTemplateCover = view.findViewById(R.id.iv_template_cover);
                btnUse = view.findViewById(R.id.btn_use);
            }
        }
    }

    private String getHighlights(Itinerary template) {
        if ("成都".equals(template.getDestination())) {
            return "错峰游览熊猫基地、宽窄巷子慢生活体验";
        } else if ("北京".equals(template.getDestination())) {
            return "故宫深度游、长城错峰登顶";
        } else if ("上海".equals(template.getDestination())) {
            return "迪士尼亲子乐园、外滩夜景";
        } else if ("杭州".equals(template.getDestination())) {
            return "西湖晨游、灵隐寺祈福";
        }
        return "精选景点、合理安排行程";
    }

    private void useTemplate(Long templateId, MaterialButton btnUse) {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        btnUse.setEnabled(false);
        btnUse.setText("套用中...");
        
        HttpUtil.post("itinerary/template/" + templateId + "?userId=" + userId, "", new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                handler.post(() -> {
                    btnUse.setEnabled(true);
                    btnUse.setText("立即套用");
                    Toast.makeText(TemplateActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<Itinerary> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<Itinerary>>(){}.getType());

                handler.post(() -> {
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        btnUse.setText("✓ 套用成功");
                        btnUse.setBackgroundColor(Color.parseColor("#4CAF50"));
                        
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(TemplateActivity.this, ItineraryDetailActivity.class);
                            intent.putExtra("itinerary_id", apiResponse.getData().getId());
                            startActivity(intent);
                            
                            btnUse.setEnabled(true);
                            btnUse.setText("立即套用");
                            btnUse.setBackgroundColor(Color.parseColor("#2196F3"));
                        }, 800);
                    } else {
                        btnUse.setEnabled(true);
                        btnUse.setText("立即套用");
                        Toast.makeText(TemplateActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
