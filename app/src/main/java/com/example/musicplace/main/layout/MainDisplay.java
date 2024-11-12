package com.example.musicplace.main.layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.playlist.adapter.PlaylistRecyclerAdapter;
import com.example.musicplace.playlist.dto.PlaylistDto;
import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.playlist.layout.MyPlaylist;
import com.example.musicplace.profile.layout.Profile;
import com.example.musicplace.streaming.layout.StreamingMain;
import com.example.musicplace.youtubeMusicPlayer.layout.SearchMusic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDisplay extends AppCompatActivity {
    // 팔로워들의 플리 목록을 보여주는 페이지
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private PlaylistRecyclerAdapter playlistRecyclerAdapter;
    private UserApiInterface api;
    private List<ResponsePLDto> responsePLDtos;


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


        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        playlistRecyclerAdapter = new PlaylistRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(playlistRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadPublicPlaylistData();

        // 아이템 클릭 이벤트 처리
        playlistRecyclerAdapter.setOnItemClickListener(position -> {
            ResponsePLDto selectedItem = responsePLDtos.get(position);
            Intent intent = new Intent(MainDisplay.this, ShowDetailedPublicPlaylist.class);
            // 선택된 아이템의 정보를 Intent로 전달
            intent.putExtra("playlistId", selectedItem.getPlaylist_id());
            intent.putExtra("playlistTitle", selectedItem.getPLTitle());
            intent.putExtra("nickname", selectedItem.getNickname());
            intent.putExtra("imageUrl", selectedItem.getCover_img());
            intent.putExtra("comment",selectedItem.getComment());
            intent.putExtra("member_id",selectedItem.getMember_id());
            startActivityForResult(intent, 1001);
        });



        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.search) {
                intent = new Intent(MainDisplay.this, SearchMusic.class);
                startActivity(intent);
                finish();
                return true;
            } else if(menuItem.getItemId() == R.id.headset) {
                intent = new Intent(MainDisplay.this, StreamingMain.class);
                startActivity(intent);
                finish();
                return true;
            } else if(menuItem.getItemId() == R.id.profile) {
                intent = new Intent(MainDisplay.this, Profile.class);
                startActivity(intent);
                finish();
                return true;
            }
            else if(menuItem.getItemId() == R.id.add) {
                intent = new Intent(MainDisplay.this, MyPlaylist.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }


    private void loadPublicPlaylistData() {
        api.PLFindPublic().enqueue(new Callback<List<ResponsePLDto>>() {
            @Override
            public void onResponse(Call<List<ResponsePLDto>> call, Response<List<ResponsePLDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responsePLDtos = response.body();

                    ArrayList<PlaylistDto> playlistDtoList = (ArrayList<PlaylistDto>) responsePLDtos.stream()
                            .map(pl -> new PlaylistDto(pl.getPLTitle(), pl.getNickname(), pl.getCover_img()))
                            .collect(Collectors.toList());

                    // 어댑터에 데이터를 설정하고 갱신
                    playlistRecyclerAdapter.setPlaylistItems(playlistDtoList);
                } else {
                    System.out.println("플레이리스트 데이터를 불러오는데 실패했습니다.");
                }
            }
            @Override
            public void onFailure(Call<List<ResponsePLDto>> call, Throwable t) {
                Toast.makeText(MainDisplay.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}