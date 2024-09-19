package com.example.musicplace.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;
import com.example.musicplace.follow.follow;
import com.example.musicplace.main.mainDisplay;
import com.example.musicplace.youtubeMusicPlayer.SearchMusic;
import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.retrofit.UserApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyPlaylist extends AppCompatActivity {
    // 사용자의 플레이 리스트 목록을 보여주는 페이지
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private PlaylistRecyclerAdapter playlistRecyclerAdapter;
    private UserApiInterface api;

    private List<ResponsePLDto> videoDtoList;

    private ImageButton addPlaylistBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // RecyclerView와 어댑터 설정
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // 어댑터 초기화 및 설정
        playlistRecyclerAdapter = new PlaylistRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(playlistRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        /*String member_id = null;
        Call<List<ResponsePLDto>> call = api.PLFindAll(member_id);
        call.enqueue(new Callback<List<ResponsePLDto>>() {
            @Override
            public void onResponse(Call<List<ResponsePLDto>> call, Response<List<ResponsePLDto>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ResponsePLDto> playListItems = new ArrayList<>();
                    videoDtoList = response.body();

                    for (ResponsePLDto videoDto : videoDtoList) {

                        playListItems.add(new ResponsePLDto(videoDto.);
                    }
                    // 어댑터에 데이터 추가
                    playlistRecyclerAdapter.setPlaylistItems(playListItems);
                } else {
                    Toast.makeText(MyPlaylist.this, "Response Error: " + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePLDto>> call, Throwable t) {

            }
        })*/


        // 아이템 클릭 이벤트 처리
        playlistRecyclerAdapter.setOnItemClickListener(position -> {

        });


        // 플레이리스트 추가 화면으로 이동
        addPlaylistBtn = (ImageButton) findViewById(R.id.addPlaylistBtn);
        addPlaylistBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                intent = new Intent(MyPlaylist.this, addPlaylist.class);
                startActivity(intent);
            }
        });

        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.add);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.home) {
                intent = new Intent(MyPlaylist.this, mainDisplay.class);
                startActivity(intent);
                return true;
            } else if(menuItem.getItemId() == R.id.search) {
                intent = new Intent(MyPlaylist.this, SearchMusic.class);
                startActivity(intent);
                return true;
            } /*else if(menuItem.getItemId() == R.id.headset) {
                intent = new Intent(MyPlaylist.this, SearchMusic.class);
                startActivity(intent);
                return true;
            }*/ else if(menuItem.getItemId() == R.id.person) {
                intent = new Intent(MyPlaylist.this, follow.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}