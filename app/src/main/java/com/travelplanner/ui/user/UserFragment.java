package com.travelplanner.ui.user;

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

import com.travelplanner.R;
import com.travelplanner.TravelPlannerApp;
import com.travelplanner.ui.LoginActivity;

public class UserFragment extends Fragment {
    private TextView tvUsername, tvMemberLevel;
    private View btnLogout;
    private View cardFavorites;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tv_username);
        tvMemberLevel = view.findViewById(R.id.tv_member_level);
        btnLogout = view.findViewById(R.id.btn_logout);
        cardFavorites = view.findViewById(R.id.cardFavorites);

        updateUI();

        btnLogout.setOnClickListener(v -> {
            TravelPlannerApp.getInstance().logout();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        cardFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), com.travelplanner.ui.favorite.FavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void updateUI() {
        TravelPlannerApp app = TravelPlannerApp.getInstance();
        tvUsername.setText(app.getUsername());
        Integer level = app.getMemberLevel();
        tvMemberLevel.setText(level != null && level > 0 ? "高级会员" : "普通会员");
    }
}
