package com.travelplanner.ui.favorite;

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

import com.google.gson.Gson;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.data.remote.ApiClient;
import com.travelplanner.data.remote.api.FavoriteApi;
import com.travelplanner.model.ApiResponse;
import com.travelplanner.model.Favorite;
import com.travelplanner.model.amap.AMapPoiSearchResponse;
import com.travelplanner.ui.attraction.AttractionDetailActivity;
import com.travelplanner.ui.hotel.HotelDetailActivity;
import com.travelplanner.ui.restaurant.RestaurantDetailActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoritesListFragment extends Fragment {

    public static final String ARG_TYPE = "type";
    public static final String TYPE_RESTAURANT = "restaurant";
    public static final String TYPE_ATTRACTION = "attraction";
    public static final String TYPE_HOTEL = "hotel";

    private String type;
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private FavoriteAdapter adapter;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();

    public static FavoritesListFragment newInstance(String type) {
        FavoritesListFragment fragment = new FavoritesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        adapter = new FavoriteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnFavoriteClickListener(this::openDetail);

        loadFavorites();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        Long userId = TravelPlannerApp.getInstance().getUserId();
        if (userId == 0) {
            handler.post(() -> {
                adapter.setData(null);
                recyclerView.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("请先登录查看收藏");
            });
            return;
        }

        FavoriteApi api = ApiClient.getFavoriteApi();
        api.getUserFavoritesByType(type, userId).enqueue(new retrofit2.Callback<ApiResponse<List<Favorite>>>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse<List<Favorite>>> call,
                                   retrofit2.Response<ApiResponse<List<Favorite>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Favorite> favorites = response.body().getData();
                    handler.post(() -> {
                        adapter.setData(favorites);
                        if (favorites != null && !favorites.isEmpty()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            tvEmpty.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                            tvEmpty.setText("暂无收藏");
                        }
                    });
                } else {
                    handler.post(() -> {
                        recyclerView.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("加载失败");
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse<List<Favorite>>> call, Throwable t) {
                handler.post(() -> {
                    Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("网络错误");
                });
            }
        });
    }

    private void openDetail(Favorite favorite) {
        if (favorite.getPoiJson() == null) {
            return;
        }

        Intent intent;
        switch (type) {
            case TYPE_RESTAURANT:
                intent = new Intent(getContext(), RestaurantDetailActivity.class);
                intent.putExtra("extra_single_poi_json", favorite.getPoiJson());
                break;
            case TYPE_ATTRACTION:
                intent = new Intent(getContext(), AttractionDetailActivity.class);
                intent.putExtra("extra_single_poi_json", favorite.getPoiJson());
                break;
            case TYPE_HOTEL:
                intent = new Intent(getContext(), HotelDetailActivity.class);
                intent.putExtra("extra_single_poi_json", favorite.getPoiJson());
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
