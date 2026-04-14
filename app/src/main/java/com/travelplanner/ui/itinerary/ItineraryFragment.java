package com.travelplanner.ui.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.reflect.TypeToken;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Itinerary;
import com.travelplanner.ui.itinerary.ItineraryDetailActivity;
import com.travelplanner.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class ItineraryFragment extends Fragment {
    private RecyclerView rvItineraries;
    private SwipeRefreshLayout swipeRefresh;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<Itinerary> itineraries = new ArrayList<>();
    private ItineraryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itinerary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvItineraries = view.findViewById(R.id.rv_itineraries);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);

        adapter = new ItineraryAdapter(itineraries);
        rvItineraries.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItineraries.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadItineraries);

        loadItineraries();
    }

    private void loadItineraries() {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            swipeRefresh.setRefreshing(false);
            return;
        }

        HttpUtil.get("itinerary/user/" + userId, new okhttp3.Callback() {
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
                ApiResponse<List<Itinerary>> apiResponse = HttpUtil.fromJson(body,
                        new TypeToken<ApiResponse<List<Itinerary>>>() {
                        }.getType());

                handler.post(() -> {
                    swipeRefresh.setRefreshing(false);
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        itineraries.clear();
                        itineraries.addAll(apiResponse.getData());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
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
