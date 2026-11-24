package com.travelplanner.ui.favorite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.travelplanner.R;

public class FavoritesActivity extends AppCompatActivity {

    private static final int TAB_RESTAURANT = 0;
    private static final int TAB_ATTRACTION = 1;
    private static final int TAB_HOTEL = 2;
    private static final int TAB_COUNT = 3;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        setupToolbar();
        setupViews();
        setupViewPager();
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
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    private void setupViewPager() {
        FavoritesPagerAdapter adapter = new FavoritesPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case TAB_RESTAURANT:
                    tab.setText("餐厅");
                    break;
                case TAB_ATTRACTION:
                    tab.setText("景点");
                    break;
                case TAB_HOTEL:
                    tab.setText("酒店");
                    break;
            }
        }).attach();
    }

    private static class FavoritesPagerAdapter extends FragmentStateAdapter {
        public FavoritesPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case TAB_RESTAURANT:
                    return FavoritesListFragment.newInstance(FavoritesListFragment.TYPE_RESTAURANT);
                case TAB_ATTRACTION:
                    return FavoritesListFragment.newInstance(FavoritesListFragment.TYPE_ATTRACTION);
                case TAB_HOTEL:
                    return FavoritesListFragment.newInstance(FavoritesListFragment.TYPE_HOTEL);
                default:
                    return FavoritesListFragment.newInstance(FavoritesListFragment.TYPE_RESTAURANT);
            }
        }

        @Override
        public int getItemCount() {
            return TAB_COUNT;
        }
    }
}
