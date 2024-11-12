package com.example.musicplace.streaming.layout;

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
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.youtubeMusicPlayer.adapter.YoutubeRecyclerAdapter;
import com.example.musicplace.youtubeMusicPlayer.dto.VidioImage;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeItem;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeVidioDto;
import com.example.musicplace.youtubeMusicPlayer.layout.MusicPlayer;
import com.example.musicplace.youtubeMusicPlayer.layout.SearchMusic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamingSearchYoutube extends AppCompatActivity {
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
        setContentView(R.layout.activity_streaming_search_youtube);
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


        // 음악 검색 버튼
        keywordEditText = findViewById(R.id.Search);
        searchButton = findViewById(R.id.SearchButton);

        searchButton.setOnClickListener(view -> {
            String keywordText = keywordEditText.getText().toString();
            youtubeSearch(keywordText);
        });

        // StreamingSearchYoutube의 RecyclerView 아이템 클릭 리스너에서 수정
        mRecyclerAdapter.setOnItemClickListener((position) -> {
            // 클릭된 아이템의 YoutubeItem 데이터 가져오기
            Intent intent = new Intent(getApplicationContext(), StreamingMusicPlayer.class);
            intent.putExtra("VidioTitle", videoDtoList.get(position).getVidioTitle());
            intent.putExtra("VidioId", videoDtoList.get(position).getVidioId());

            if (videoDtoList.get(position).getParsedVidioImage() != null &&
                    videoDtoList.get(position).getParsedVidioImage().getDefaultQuality() != null) {
                intent.putExtra("VidioImage", videoDtoList.get(position).getParsedVidioImage().getDefaultQuality().getUrl().toString());
            } else {
                intent.putExtra("VidioImage", videoDtoList.get(position).getVidioImage());
            }

            startActivityForResult(intent, 1);  // StreamingMusicPlayer를 호출
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();  // StreamingSearchYoutube 종료
        }
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
                Toast.makeText(StreamingSearchYoutube.this, "API Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}