package com.example.musicplace.youtubeMusicPlayer.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.example.musicplace.playlist.dto.MusicSaveDto;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.LoadPlaylistDto;
import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.youtubeMusicPlayer.adapter.SelectPlaylistRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPlaylist extends AppCompatActivity {
    private UserApiInterface api;
    private RecyclerView recyclerView;
    private List<ResponsePLDto> musicListDto;
    private SelectPlaylistRecyclerAdapter selectPlaylistRecyclerAdapter;
    private Button back, add;
    private String vidioTitle, vidioId, vidioImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // RecyclerView와 어댑터 설정
        recyclerView = findViewById(R.id.recyclerView);
        selectPlaylistRecyclerAdapter = new SelectPlaylistRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(selectPlaylistRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // MusicPlayer에서 음악 정보 받아오기
        Intent intent = getIntent();
        if (intent != null) {
            vidioTitle = intent.getStringExtra("VidioTitle");
            vidioId = intent.getStringExtra("VidioId");
            vidioImage = intent.getStringExtra("VidioImage");
        }


        // 데이터를 불러와서 리사이클러뷰에 추가
        loadPlaylistData();

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

        add = findViewById(R.id.add);
        add.setOnClickListener(view -> {
            // 선택된 플레이리스트 ID 가져오기
            Long selectedPlaylistId = selectPlaylistRecyclerAdapter.getSelectedPlaylistId();
            if (selectedPlaylistId != null) {
                MusicSaveDto musicSaveDto = new MusicSaveDto(vidioId, vidioTitle, vidioImage);
                updatePlaylistData(selectedPlaylistId, musicSaveDto);
            } else {
                Toast.makeText(SelectPlaylist.this, "플레이리스트를 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 모든 플레이리스트 데이터를 불러오는 메서드
    private void loadPlaylistData() {
        api.PLFindAll().enqueue(new Callback<List<ResponsePLDto>>() {
            @Override
            public void onResponse(Call<List<ResponsePLDto>> call, Response<List<ResponsePLDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    musicListDto = response.body();

                    ArrayList<LoadPlaylistDto> playlistDtoList = (ArrayList<LoadPlaylistDto>) musicListDto.stream()
                            .map(pl -> new LoadPlaylistDto(pl.getPlaylist_id(), pl.getPLTitle(), pl.getCover_img()))
                            .collect(Collectors.toList());

                    // 어댑터에 데이터를 설정하고 갱신
                    selectPlaylistRecyclerAdapter.setPlaylistItems(playlistDtoList);
                } else {
                    System.out.println("플레이리스트 데이터를 불러오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePLDto>> call, Throwable t) {
                Toast.makeText(SelectPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 선택된 플레이리스트에 음악을 저장하는 메서드
    private void updatePlaylistData(Long playlistId, MusicSaveDto musicSaveDto) {
        api.MusicSave(playlistId, musicSaveDto).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SelectPlaylist.this, "음악이 플레이리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SelectPlaylist.this, "음악 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(SelectPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}