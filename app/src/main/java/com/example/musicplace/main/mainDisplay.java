package com.example.musicplace.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.follow.follow;
import com.example.musicplace.playlist.layout.MyPlaylist;
import com.example.musicplace.youtubeMusicPlayer.layout.SearchMusic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mainDisplay extends AppCompatActivity {
    // 팔로워들의 플리 목록을 보여주는 페이지
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.search) {
                intent = new Intent(mainDisplay.this, SearchMusic.class);
                startActivity(intent);
                return true;
            } /*else if(menuItem.getItemId() == R.id.headset) {
                intent = new Intent(mainDisplay.this, Map.class);
                startActivity(intent);
                return true;
            } */else if(menuItem.getItemId() == R.id.person) {
                intent = new Intent(mainDisplay.this, follow.class);
                startActivity(intent);
                return true;
            }
            else if(menuItem.getItemId() == R.id.add) {
                intent = new Intent(mainDisplay.this, MyPlaylist.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}