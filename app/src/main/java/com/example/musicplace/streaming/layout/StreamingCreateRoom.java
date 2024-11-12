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

import com.example.musicplace.R;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.streaming.dto.RoomDto;
import com.example.musicplace.youtubeMusicPlayer.dto.VidioImage;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeItem;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeVidioDto;
import com.example.musicplace.youtubeMusicPlayer.layout.SearchMusic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamingCreateRoom extends AppCompatActivity {
    private Intent intent;
    private UserApiInterface api;
    private EditText editTextTitle, editTextText;
    private Button back, add;
    private String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_streaming_create_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextText = findViewById(R.id.editTextText);
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);


        add.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String comment = editTextText.getText().toString();
            createChatRoom(title, comment);
            intent = new Intent(StreamingCreateRoom.this, StreamingHostRoom.class);
            intent.putExtra("roomTitle", title);
            intent.putExtra("roomId", roomId);
            intent.putExtra("roomComment", comment);
            startActivity(intent);
            finish();
        });

        back.setOnClickListener(view -> {
            intent = new Intent(StreamingCreateRoom.this, StreamingMain.class);
            startActivity(intent);
            finish();
        });

    }

    private void createChatRoom(String title, String comment){
        RoomDto roomDto = new RoomDto(null, title, comment, null);
        Call<String> call = api.createChatRoom(roomDto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    roomId = response.body().toString();

                } else {
                    System.out.println(response.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}