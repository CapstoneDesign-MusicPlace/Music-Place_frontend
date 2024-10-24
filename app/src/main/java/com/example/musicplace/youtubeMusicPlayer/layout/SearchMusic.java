package com.example.musicplace.youtubeMusicPlayer.layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;

import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.main.layout.MainDisplay;
import com.example.musicplace.profile.layout.Profile;
import com.example.musicplace.youtubeMusicPlayer.adapter.YoutubeRecyclerAdapter;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeItem;
import com.example.musicplace.youtubeMusicPlayer.dto.VidioImage;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeVidioDto;
import com.example.musicplace.playlist.layout.MyPlaylist;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMusic extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private EditText keywordEditText;
    private Button searchButton;
    private UserApiInterface api;
    private YoutubeRecyclerAdapter mRecyclerAdapter;
    private List<YoutubeVidioDto> videoDtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_music);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // 리사이클러뷰 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mRecyclerAdapter = new YoutubeRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadYoutubeData();



        // 음악 검색 버튼
        keywordEditText = findViewById(R.id.Search);
        searchButton = findViewById(R.id.SearchButton);

        searchButton.setOnClickListener(view -> {
            String keywordText = keywordEditText.getText().toString();
            youtubeSearch(keywordText);
        });

        // RecyclerView 아이템 클릭 리스너
        mRecyclerAdapter.setOnItemClickListener((position) -> {
            // 클릭된 아이템의 YoutubeItem 데이터 가져오기
            Intent intent = new Intent(getApplicationContext(), MusicPlayer.class);
            intent.putExtra("VidioTitle", videoDtoList.get(position).getVidioTitle());
            intent.putExtra("VidioId", videoDtoList.get(position).getVidioId());

            if (videoDtoList.get(position).getParsedVidioImage() != null &&
                    videoDtoList.get(position).getParsedVidioImage().getDefaultQuality() != null) {
                intent.putExtra("VidioImage", videoDtoList.get(position).getParsedVidioImage().getDefaultQuality().getUrl().toString());
            } else {
                intent.putExtra("VidioImage", videoDtoList.get(position).getVidioImage());
            }

            startActivity(intent);
        });



        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.home) {
                intent = new Intent(SearchMusic.this, MainDisplay.class);
                startActivity(intent);
                finish();
                return true;
            } /*else if(menuItem.getItemId() == R.id.headset) {
                intent = new Intent(SearchMusic.this, Map.class);
                startActivity(intent);
                finish();
                return true;
            }*/ else if(menuItem.getItemId() == R.id.profile) {
                intent = new Intent(SearchMusic.this, Profile.class);
                startActivity(intent);
                finish();
                return true;
            } else if(menuItem.getItemId() == R.id.add) {
                intent = new Intent(SearchMusic.this, MyPlaylist.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }




    private void loadYoutubeData() {
        api.youtubeGetPlaylist().enqueue(new Callback<List<YoutubeVidioDto>>() {
            @Override
            public void onResponse(Call<List<YoutubeVidioDto>> call, Response<List<YoutubeVidioDto>> response) {
                if (response.isSuccessful()) {
                    ArrayList<YoutubeItem> youtubeItems = new ArrayList<>();
                    videoDtoList = response.body();

                    for (YoutubeVidioDto videoDto : videoDtoList) {
                       youtubeItems.add(new YoutubeItem(videoDto.getVidioImage(), videoDto.getVidioTitle()));
                    }
                    // 어댑터에 데이터 추가
                    mRecyclerAdapter.setYoutubeItems(youtubeItems);
                } else {
                    System.out.println(response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<YoutubeVidioDto>> call, Throwable t) {

            }
        });
    }

    private void youtubeSearch(String keywordText){
        Call<List<YoutubeVidioDto>> call = api.youtubeSearch(keywordText);
        call.enqueue(new Callback<List<YoutubeVidioDto>>() {
            @Override
            public void onResponse(Call<List<YoutubeVidioDto>> call, Response<List<YoutubeVidioDto>> response) {
                if (response.isSuccessful()) {
                    ArrayList<YoutubeItem> youtubeItems = new ArrayList<>();
                    videoDtoList = response.body();
                    for (YoutubeVidioDto videoDto : videoDtoList) {
                        VidioImage vidioImage = videoDto.getParsedVidioImage();
                        if (vidioImage != null) {
                            youtubeItems.add(new YoutubeItem(vidioImage.getDefaultQuality().getUrl(), videoDto.getVidioTitle()));
                        }
                    }
                    mRecyclerAdapter.setYoutubeItems(youtubeItems);
                } else {
                    System.out.println(response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<YoutubeVidioDto>> call, Throwable t) {
                Toast.makeText(SearchMusic.this, "API Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
