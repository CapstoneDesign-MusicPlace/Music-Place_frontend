package com.example.musicplace.follow.layout;

import android.content.Intent;
import android.media.Image;
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
import com.example.musicplace.follow.dto.FollowSaveDto;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.main.layout.MainDisplay;
import com.example.musicplace.main.layout.ShowDetailedPublicPlaylist;
import com.example.musicplace.playlist.adapter.PlaylistRecyclerAdapter;
import com.example.musicplace.playlist.dto.PlaylistDto;
import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.profile.layout.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowDetailed extends AppCompatActivity {
    private Intent intent;
    private PlaylistRecyclerAdapter playlistRecyclerAdapter;
    private UserApiInterface api;
    private List<ResponsePLDto> responsePLDtos;
    private String otherMemberId, otherMemberImage, otherNickName;

    private ImageButton backImageButton;
    private Button followButton;
    private TextView playlistCountTextView, followCountTextView, userNicknameTextView;
    private Image ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_follow_detailed);

        // Window insets 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // Intent에서 값 가져오기
        Intent intent = getIntent();
        if (intent != null) {
            otherMemberId = intent.getStringExtra("memberId");
            otherMemberImage = intent.getStringExtra("image");
            otherNickName = intent.getStringExtra("nickname");
        }

        // 뷰 초기화
        userNicknameTextView = findViewById(R.id.userNicknameTextView);
        playlistCountTextView = findViewById(R.id.playlistCountTextView);
        followCountTextView = findViewById(R.id.followCountTextView);

        // 가져온 값 적용
        userNicknameTextView.setText(otherNickName);

        // RecyclerView 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        playlistRecyclerAdapter = new PlaylistRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(playlistRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // 아이템 클릭 이벤트 처리
        playlistRecyclerAdapter.setOnItemClickListener(position -> {
            ResponsePLDto selectedItem = responsePLDtos.get(position);
            // 새로운 intent 변수를 선언하여 값을 설정
            Intent newIntent = new Intent(FollowDetailed.this, ShowDetailedPublicPlaylist.class);
            // 선택된 아이템의 정보를 Intent로 전달
            newIntent.putExtra("playlistId", selectedItem.getPlaylist_id());
            newIntent.putExtra("playlistTitle", selectedItem.getPLTitle());
            newIntent.putExtra("nickname", selectedItem.getNickname());
            newIntent.putExtra("imageUrl", selectedItem.getCover_img());
            newIntent.putExtra("comment", selectedItem.getComment());
            newIntent.putExtra("member_id", selectedItem.getMember_id());
            startActivityForResult(newIntent, 1001);
        });


        // API 호출
        getUserPublicPlaylist(otherMemberId);
        loadOtherPLCount(otherMemberId);
        loadOtherFollowCount(otherMemberId);

        // 뒤로가기 버튼 초기화 및 리스너 설정
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(view -> finish());

        // 팔로우 버튼 초기화 및 리스너 설정
        followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(view -> FollowSave(new FollowSaveDto(otherMemberId, otherMemberImage, otherNickName)));
    }


    private void getUserPublicPlaylist(String otherMemberId) {
        api.getOtherUserPL(otherMemberId).enqueue(new Callback<List<ResponsePLDto>>() {
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
                    System.out.println("다른 사용자의 플레이리스트 데이터를 불러오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<List<ResponsePLDto>> call, Throwable t) {
                Toast.makeText(FollowDetailed.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void loadOtherPLCount(String otherMemberId) {
        api.otherPLCount(otherMemberId).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    playlistCountTextView.setText(response.body().toString());
                } else {
                    System.out.println("load PL other Count 실패");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(FollowDetailed.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOtherFollowCount(String otherMemberId) {
        api.otherFollowCount(otherMemberId).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    followCountTextView.setText(response.body().toString());
                } else {
                    System.out.println("load Follow other Count 실패");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(FollowDetailed.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void FollowSave(FollowSaveDto followSaveDto) {
        api.FollowSave(followSaveDto).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FollowDetailed.this, "팔로우 되었습니다..", Toast.LENGTH_SHORT).show();
                    System.out.println("팔로우 성공");
                } else {
                    System.out.println("팔로우 실패");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(FollowDetailed.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}