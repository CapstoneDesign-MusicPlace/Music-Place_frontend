package com.example.musicplace.youtubeMusicPlayer;

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

import com.example.musicplace.follow.follow;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.main.mainDisplay;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.YoutubeItem;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.VidioImage;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.YoutubeVidioDto;
import com.example.musicplace.playlist.MyPlaylist;
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

        // RetrofitClient에 TokenManager를 전달
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // 리사이클러뷰 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mRecyclerAdapter = new YoutubeRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 음악 검색 버튼
        keywordEditText = findViewById(R.id.Search);
        searchButton = findViewById(R.id.SearchButton);

        searchButton.setOnClickListener(view -> {
            String keywordText = keywordEditText.getText().toString();

            if (api != null) {
                // API 호출
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
                            // 어댑터에 데이터 추가
                            mRecyclerAdapter.setYoutubeItems(youtubeItems);
                        } else {
/*
                            Toast.makeText(SearchMusic.this, "Response Error: " + response.toString(), Toast.LENGTH_SHORT).show();
*/
                            System.out.println(response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<YoutubeVidioDto>> call, Throwable t) {
                        Toast.makeText(SearchMusic.this, "API Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SearchMusic.this, "API 객체가 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // RecyclerView 아이템 클릭 리스너
        mRecyclerAdapter.setOnItemClickListener((position) -> {
            // 클릭된 아이템의 YoutubeItem 데이터 가져오기
            Intent intent = new Intent(getApplicationContext(),musicPlayer.class);
            intent.putExtra("VidioTitle", videoDtoList.get(position).getVidioTitle());
            intent.putExtra("VidioId", videoDtoList.get(position).getVidioId());
            System.out.println(position);
            startActivity(intent);
        });


        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.home) {
                intent = new Intent(SearchMusic.this, mainDisplay.class);
                startActivity(intent);
                return true;
            } /*else if(menuItem.getItemId() == R.id.headset) {
                intent = new Intent(SearchMusic.this, Map.class);
                startActivity(intent);
                return true;
            }*/ else if(menuItem.getItemId() == R.id.person) {
                intent = new Intent(SearchMusic.this, follow.class);
                startActivity(intent);
                return true;
            } else if(menuItem.getItemId() == R.id.add) {
                intent = new Intent(SearchMusic.this, MyPlaylist.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}
