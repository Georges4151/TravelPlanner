package com.travelplanner.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.data.local.AppDatabase;
import com.travelplanner.data.local.entity.FavoriteEntity;
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
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                List<FavoriteEntity> favorites = db.favoriteDao().getByType(type);
                handler.post(() -> {
                    adapter.setData(favorites);
                    if (favorites != null && !favorites.isEmpty()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void openDetail(FavoriteEntity favorite) {
        if (favorite.getPoiJson() == null) {
            return;
        }

        Gson gson = new Gson();
        AMapPoiSearchResponse.Poi poi = gson.fromJson(favorite.getPoiJson(), AMapPoiSearchResponse.Poi.class);

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
