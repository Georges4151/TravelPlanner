package com.travelplanner.ui.home;

import android.content.Intent;
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
import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Attraction;
import com.travelplanner.model.Itinerary;
import com.travelplanner.model.OffpeakRecommendationResponse;
import com.travelplanner.ui.itinerary.CreateItineraryActivity;
import com.travelplanner.ui.itinerary.ItineraryDetailActivity;
import com.travelplanner.ui.template.TemplateActivity;
import com.travelplanner.ui.transport.TransportDemoActivity;
import com.travelplanner.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvAttractions, rvItineraries;
    private TextView tvOffPeakTip;
    private View btnCreateItinerary, btnTemplate;
    private View btnTransport, btnRestaurant, btnHotel, btnAttraction, btnReminder;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<Attraction> attractions = new ArrayList<>();
    private List<Itinerary> itineraries = new ArrayList<>();
    private AttractionAdapter attractionAdapter;
    private ItineraryAdapter itineraryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        rvAttractions = view.findViewById(R.id.rv_attractions);
        rvItineraries = view.findViewById(R.id.rv_itineraries);
        tvOffPeakTip = view.findViewById(R.id.tv_off_peak_tip);
        btnCreateItinerary = view.findViewById(R.id.btn_create_itinerary);
        btnTemplate = view.findViewById(R.id.btn_template);
        btnTransport = view.findViewById(R.id.btn_transport);
        btnRestaurant = view.findViewById(R.id.btn_restaurant);
        btnHotel = view.findViewById(R.id.btn_hotel);
        btnAttraction = view.findViewById(R.id.btn_attraction);
        btnReminder = view.findViewById(R.id.btn_reminder);

        setupRecyclerViews();
        setupListeners();
        loadData();
    }

    private void setupRecyclerViews() {
        attractionAdapter = new AttractionAdapter(attractions);
        rvAttractions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvAttractions.setAdapter(attractionAdapter);

        itineraryAdapter = new ItineraryAdapter(itineraries);
        rvItineraries.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItineraries.setAdapter(itineraryAdapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(this::loadData);

        btnCreateItinerary.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateItineraryActivity.class));
        });

        btnTemplate.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), TemplateActivity.class));
        });

        btnTransport.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), TransportDemoActivity.class));
        });

        btnRestaurant.setOnClickListener(v -> {
            if (getContext() != null) {
                startActivity(new Intent(getContext(),
                        com.travelplanner.ui.restaurant.RestaurantSearchActivity.class));
            }
        });

        btnHotel.setOnClickListener(v -> {
            if (getContext() != null) {
                startActivity(new Intent(getContext(),
                        com.travelplanner.ui.hotel.HotelSearchActivity.class));
            }
        });

        btnAttraction.setOnClickListener(v -> {
            if (getContext() != null) {
                startActivity(new Intent(getContext(),
                        com.travelplanner.ui.attraction.AttractionSearchActivity.class));
            }
        });

        btnReminder.setOnClickListener(v -> {
            if (getContext() != null) {
                startActivity(new Intent(getContext(),
                        com.travelplanner.ui.reminder.ReminderMainActivity.class));
            }
        });

    }

    private void loadData() {
        loadTopAttractions();
        loadMyItineraries();
        loadOffpeakRecommendation();
    }

    private void loadTopAttractions() {
        HttpUtil.get("attraction/top?limit=5", new okhttp3.Callback() {
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
                ApiResponse<List<Attraction>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Attraction>>>() {
                        }.getType());

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    handler.post(() -> {
                        attractions.clear();
                        attractions.addAll(apiResponse.getData());
                        attractionAdapter.notifyDataSetChanged();
                    });
                }
                handler.post(() -> swipeRefresh.setRefreshing(false));
            }
        });
    }

    private void loadMyItineraries() {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0)
            return;

        HttpUtil.get("itinerary/user/" + userId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String body = response.body().string();
                ApiResponse<List<Itinerary>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Itinerary>>>() {
                        }.getType());

                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    handler.post(() -> {
                        itineraries.clear();
                        itineraries.addAll(apiResponse.getData());
                        itineraryAdapter.notifyDataSetChanged();
                    });
                }
            }
        });
    }

    private void loadOffpeakRecommendation() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        String startDate = sdf.format(new Date());
        String endDate = sdf.format(calendar.getTime());

        HttpUtil.get("itinerary/offpeak/recommend?destination=成都&startDate=" + startDate + "&days=3",
                new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, java.io.IOException e) {
                        handler.post(() -> {
                            tvOffPeakTip.setText("错峰提示：建议工作日出行，景点人流较少，体验更佳！");
                        });
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                        String body = response.body().string();
                        ApiResponse<OffpeakRecommendationResponse> apiResponse = HttpUtil.fromJson(body,
                                new TypeToken<ApiResponse<OffpeakRecommendationResponse>>() {
                                }.getType());

                        handler.post(() -> {
                            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                                OffpeakRecommendationResponse offpeakResponse = apiResponse.getData();
                                if (offpeakResponse.getOffpeakRecommendations() != null
                                        && !offpeakResponse.getOffpeakRecommendations().isEmpty()) {
                                    OffpeakRecommendationResponse.OffpeakRecommendation recommendation = offpeakResponse
                                            .getOffpeakRecommendations().get(0);
                                    String tip = "错峰推荐：" + recommendation.getReason();
                                    tvOffPeakTip.setText(tip);
                                }
                            }
                        });
                    }
                });
    }

    class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {
        private List<Attraction> list;

        AttractionAdapter(List<Attraction> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Attraction attraction = list.get(position);
            holder.tvName.setText(attraction.getName());
            holder.tvCity.setText(attraction.getCity());
            holder.tvRating.setText(String.valueOf(attraction.getRating()));

            // 加载景点图片
            String imageUrl = attraction.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_place)
                        .error(R.drawable.ic_place)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(holder.ivAttractionImage);
            } else {
                holder.ivAttractionImage.setImageResource(R.drawable.ic_place);
            }

            // 设置拥挤度显示
            String crowdLevel = attraction.getCrowdLevel();
            holder.tvCrowd.setText(crowdLevel);

            // 根据拥挤度设置颜色和指示器
            int crowdColor;
            if (crowdLevel != null) {
                if (crowdLevel.contains("低") || crowdLevel.contains("少")) {
                    crowdColor = getResources().getColor(R.color.crowd_low);
                } else if (crowdLevel.contains("中")) {
                    crowdColor = getResources().getColor(R.color.crowd_medium);
                } else {
                    crowdColor = getResources().getColor(R.color.crowd_high);
                }
                holder.tvCrowd.setTextColor(crowdColor);
                holder.viewCrowdIndicator.getBackground().setColorFilter(crowdColor,
                        android.graphics.PorterDuff.Mode.SRC_IN);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(),
                        com.travelplanner.ui.attraction.AttractionDetailActivity.class);
                intent.putExtra("attraction_id", attraction.getId());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvCity, tvRating, tvCrowd;
            ImageView ivAttractionImage;
            View viewCrowdIndicator;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                tvCity = itemView.findViewById(R.id.tv_city);
                tvRating = itemView.findViewById(R.id.tv_rating);
                tvCrowd = itemView.findViewById(R.id.tv_crowd);
                ivAttractionImage = itemView.findViewById(R.id.iv_attraction_image);
                viewCrowdIndicator = itemView.findViewById(R.id.view_crowd_indicator);
            }
        }
    }

    class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {
        private List<Itinerary> list;

        ItineraryAdapter(List<Itinerary> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itinerary, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Itinerary itinerary = list.get(position);
            holder.tvTitle.setText(itinerary.getTitle());
            holder.tvDestination.setText(itinerary.getDestination());
            holder.tvDays.setText(
                    itinerary.getDays() + "天" + (itinerary.getDays() > 1 ? (itinerary.getDays() - 1) + "晚" : ""));

            // 设置日期显示
            if (itinerary.getStartDate() != null) {
                holder.tvDate.setText(itinerary.getStartDate() + " 出发");
            } else {
                holder.tvDate.setText("未设置出发日期");
            }

            View.OnClickListener clickListener = v -> {
                Intent intent = new Intent(getContext(), ItineraryDetailActivity.class);
                intent.putExtra("itinerary_id", itinerary.getId());
                startActivity(intent);
            };

            holder.itemView.setOnClickListener(clickListener);
            holder.btnView.setOnClickListener(clickListener);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDestination, tvDays, tvDate;
            View btnView;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvDestination = itemView.findViewById(R.id.tv_destination);
                tvDays = itemView.findViewById(R.id.tv_days);
                tvDate = itemView.findViewById(R.id.tv_date);
                btnView = itemView.findViewById(R.id.btn_view);
            }
        }
    }
}
