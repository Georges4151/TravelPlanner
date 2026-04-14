package com.travelplanner.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.travelplanner.R;
import com.travelplanner.model.City;
import com.travelplanner.util.CityDataUtil;

import java.util.List;

public class CitySelectActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_CITY = "selected_city";
    public static final String EXTRA_SELECTED_CITY_NAME = "selected_city_name";

    private Toolbar toolbar;
    private EditText etSearchCity;
    private RecyclerView recyclerViewCities;
    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);

        setupToolbar();
        setupViews();
        setupRecyclerView();
        loadAllCities();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViews() {
        etSearchCity = findViewById(R.id.etSearchCity);
        recyclerViewCities = findViewById(R.id.recyclerViewCities);

        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCities(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new CityAdapter();
        recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCities.setAdapter(adapter);

        adapter.setOnCityClickListener(city -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SELECTED_CITY, city);
            resultIntent.putExtra(EXTRA_SELECTED_CITY_NAME, city.getName());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void loadAllCities() {
        List<City> allCities = CityDataUtil.getAllCities();
        adapter.setData(allCities, true);
    }

    private void searchCities(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            loadAllCities();
        } else {
            List<City> searchResults = CityDataUtil.searchCities(keyword);
            adapter.setData(searchResults, false);
        }
    }
}
