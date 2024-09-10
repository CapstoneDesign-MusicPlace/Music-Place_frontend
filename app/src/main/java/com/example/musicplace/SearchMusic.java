package com.example.musicplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.dto.youtub.YoutubeItem;
import com.example.musicplace.dto.youtub.VidioImage;
import com.example.musicplace.dto.youtub.YoutubeVidioDto;
import com.example.musicplace.retrofit.RetrofitClient;
import com.example.musicplace.retrofit.UserApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMusic extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
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

        // Retrofit 클라이언트 생성 및 API 인터페이스 연결
        api = RetrofitClient.getRetrofit().create(UserApiInterface.class);

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
                            Toast.makeText(SearchMusic.this, "Response Error: " + response.toString(), Toast.LENGTH_SHORT).show();
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

        /*// 리사이클러뷰 아이템 클릭 이벤트 처리
        mRecyclerAdapter.setOnItemClickListener(position -> {
            // 클릭된 동영상 정보 가져오기
            YoutubeVidioDto selectedVideo = videoDtoList.get(position);

            // 프래그먼트로 데이터 전달
            VideoPlayerFragment fragment = VideoPlayerFragment.newInstance((ArrayList<YoutubeVidioDto>) videoDtoList, position);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            *//*transaction.replace(R.id.fragment_video_player, fragment); // 프래그먼트를 교체할 레이아웃 ID*//*
            transaction.addToBackStack(null);
            transaction.commit();
        });*/



        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            /*switch (menuItem.getItemId()) {
                case R.id.home:
                    intent = new Intent(SearchMusic.this, InputAllergy.class);
                    startActivity(intent);
                    return true;

                case R.id.search:
                    intent = new Intent(SearchMusic.this, AllergyFoodListInfo.class);
                    startActivity(intent);
                    return true;

                case R.id.headset:
                    intent = new Intent(SearchMusic.this, Map.class);
                    startActivity(intent);
                    return true;

                case R.id.person:
                    intent = new Intent(SearchMusic.this, UserInformation.class);
                    startActivity(intent);
                    return true;

                case R.id.add:
                    intent = new Intent(SearchMusic.this, UserInformation.class);
                    startActivity(intent);
                    return true;
            }*/

            return false;
        });
    }
}
