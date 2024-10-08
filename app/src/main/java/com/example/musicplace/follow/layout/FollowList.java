package com.example.musicplace.follow.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;
import com.example.musicplace.follow.adapter.FollowRecyclerAdapter;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.follow.dto.FollowResponseDto;
import com.example.musicplace.main.layout.ShowDetailedPublicPlaylist;
import com.example.musicplace.playlist.adapter.PlaylistRecyclerAdapter;
import com.example.musicplace.playlist.dto.ResponseMusicDto;
import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.playlist.layout.DetailedPlaylist;
import com.example.musicplace.playlist.layout.MyPlaylist;
import com.example.musicplace.profile.dto.SignInUpdateDto;
import com.example.musicplace.youtubeMusicPlayer.layout.MusicPlayer;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.YoutubeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowList extends AppCompatActivity {
    private UserApiInterface api;
    private Intent intent;

    private FollowRecyclerAdapter followRecyclerAdapter;

    private List<FollowResponseDto> followResponseDto;
    private TextView userNicknameTextView;
    private String getNickname;
    private ImageButton backImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_follow_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        userNicknameTextView = findViewById(R.id.userNicknameTextView);

        intent = getIntent();
        if (intent != null) {
            getNickname = intent.getStringExtra("nickname");
        }
        userNicknameTextView.setText(getNickname);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        followRecyclerAdapter = new FollowRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(followRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFollowData();

        followRecyclerAdapter.setOnItemClickListener((position) -> {
            // 클릭된 아이템의 YoutubeItem 데이터 가져오기
            intent = new Intent(getApplicationContext(), FollowDetailed.class);
            intent.putExtra("memberId", followResponseDto.get(position).getTarget_id());
            intent.putExtra("image", followResponseDto.get(position).getProfile_img_url());
            intent.putExtra("nickname", followResponseDto.get(position).getNickname());
            startActivity(intent);
        });

        backImageButton = (ImageButton) findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            // 수정사항 저장
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    private void loadFollowData() {
        api.FollowFindAll().enqueue(new Callback<List<FollowResponseDto>>() {
            @Override
            public void onResponse(Call<List<FollowResponseDto>> call, Response<List<FollowResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    followResponseDto = response.body();
                    // 어댑터에 데이터를 설정하고 갱신
                    followRecyclerAdapter.setFollowItems((ArrayList<FollowResponseDto>) followResponseDto);
                } else {
                    Toast.makeText(FollowList.this, "플레이리스트의 음악 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FollowResponseDto>> call, Throwable t) {

            }
        });
    }


}