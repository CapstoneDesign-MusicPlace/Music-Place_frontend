package com.example.musicplace.follow;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.main.mainDisplay;
import com.example.musicplace.playlist.layout.MyPlaylist;
import com.example.musicplace.youtubeMusicPlayer.layout.SearchMusic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class follow extends AppCompatActivity {
    //팔로우 대상자를 보여주는 페이지
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_follow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.person);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.home) {
                intent = new Intent(follow.this, mainDisplay.class);
                startActivity(intent);
                return true;
            } else if(menuItem.getItemId() == R.id.search) {
                intent = new Intent(follow.this, SearchMusic.class);
                startActivity(intent);
                return true;
            } /*else if(menuItem.getItemId() == R.id.headset) {
                intent = new Intent(follow.this, SearchMusic.class);
                startActivity(intent);
                return true;
            }*/
            else if(menuItem.getItemId() == R.id.add) {
                intent = new Intent(follow.this, MyPlaylist.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}