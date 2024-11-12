package com.example.musicplace.streaming.layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

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
import com.example.musicplace.main.layout.MainDisplay;
import com.example.musicplace.playlist.layout.MyPlaylist;
import com.example.musicplace.profile.layout.Profile;
import com.example.musicplace.streaming.adapter.ChatRoomRecyclerAdapter;
import com.example.musicplace.streaming.dto.RoomDto;
import com.example.musicplace.youtubeMusicPlayer.adapter.YoutubeRecyclerAdapter;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeItem;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeVidioDto;
import com.example.musicplace.youtubeMusicPlayer.layout.MusicPlayer;
import com.example.musicplace.youtubeMusicPlayer.layout.SearchMusic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamingMain extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private UserApiInterface api;
    private ImageButton addRoomBtn;
    private ChatRoomRecyclerAdapter mRecyclerAdapter;
    private List<RoomDto> roomDtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_streaming_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // 리사이클러뷰 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mRecyclerAdapter = new ChatRoomRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadStreamingData();


        mRecyclerAdapter.setOnItemClickListener((position) -> {
            // 클릭된 아이템의 데이터 가져오기
            Intent intent = new Intent(getApplicationContext(), StreamingGuestRoom.class);
            intent.putExtra("chatRoomId", roomDtoList.get(position).getChatRoomId());
            intent.putExtra("roomTitle", roomDtoList.get(position).getRoomTitle());
            intent.putExtra("roomComment", roomDtoList.get(position).getRoomComment());
            intent.putExtra("username", roomDtoList.get(position).getUsername());
            startActivity(intent);
        });


        addRoomBtn = findViewById(R.id.addStreamingRoomBtn);
        addRoomBtn.setOnClickListener(view -> {
            intent = new Intent(StreamingMain.this, StreamingCreateRoom.class);
            startActivity(intent);
            finish();
        });



        // 하단 네비게이션바
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.headset);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.home) {
                intent = new Intent(StreamingMain.this, MainDisplay.class);
                startActivity(intent);
                finish();
                return true;
            } else if(menuItem.getItemId() == R.id.search) {
                intent = new Intent(StreamingMain.this, SearchMusic.class);
                startActivity(intent);
                finish();
                return true;
            } else if(menuItem.getItemId() == R.id.profile) {
                intent = new Intent(StreamingMain.this, Profile.class);
                startActivity(intent);
                finish();
                return true;
            } else if(menuItem.getItemId() == R.id.add) {
                intent = new Intent(StreamingMain.this, MyPlaylist.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }


    private void loadStreamingData() {
        api.getChatRooms().enqueue(new Callback<List<RoomDto>>() {
            @Override
            public void onResponse(Call<List<RoomDto>> call, Response<List<RoomDto>> response) {
                if (response.isSuccessful()) {
                    ArrayList<RoomDto> roomItems = new ArrayList<>();
                    roomDtoList = response.body();

                    for (RoomDto roomDto : roomDtoList) {
                        roomItems.add(new RoomDto(roomDto.getChatRoomId(), roomDto.getRoomTitle(),roomDto.getRoomComment(),roomDto.getUsername()));
                    }
                    // 어댑터에 데이터 추가
                    mRecyclerAdapter.setChatRooms(roomItems);
                } else {
                    System.out.println(response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<RoomDto>> call, Throwable t) {

            }
        });
    }


}