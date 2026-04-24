package com.travelplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.ui.home.HomeFragment;
import com.travelplanner.ui.itinerary.ItineraryFragment;
import com.travelplanner.ui.template.TemplateFragment;
import com.travelplanner.ui.user.UserFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 检查登录状态，未登录则跳转到登录页
        if (!TravelPlannerApp.getInstance().isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        try {
            setContentView(R.layout.activity_main);

            bottomNav = findViewById(R.id.bottom_nav);
            bottomNav.setOnItemSelectedListener(item -> {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.nav_itinerary) {
                    fragment = new ItineraryFragment();
                } else if (itemId == R.id.nav_template) {
                    fragment = new TemplateFragment();
                } else if (itemId == R.id.nav_user) {
                    fragment = new UserFragment();
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
                return true;
            });

            bottomNav.setSelectedItemId(R.id.nav_home);
        } catch (Exception e) {
            e.printStackTrace();
            // 出错时返回登录页
            TravelPlannerApp.getInstance().logout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
