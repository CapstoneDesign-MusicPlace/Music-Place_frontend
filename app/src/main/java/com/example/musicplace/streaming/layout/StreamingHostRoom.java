package com.example.musicplace.streaming.layout;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamingHostRoom extends AppCompatActivity {

    private Intent intent;
    private WebView webView;
    private UserApiInterface api;
    private TextView titleTextView;
    private Button changeYoutubeButton, sendButton, endButton, patchButton;
    private String streamingTitle, roomId, roomComment, vidioId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_streaming_host_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        webView = findViewById(R.id.webView);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(streamingTitle);

        // WebView 설정
        if (vidioId != null) {
            String videoUrl = "https://www.youtube.com/embed/" + vidioId;  // 유튜브 임베드 URL 생성
            webView.getSettings().setJavaScriptEnabled(true);  // 자바스크립트 활성화
            webView.setWebViewClient(new WebViewClient());  // 외부 브라우저로 열리지 않도록 설정
            webView.loadUrl(videoUrl);  // URL 로드
        }

        intent = getIntent();
        if (intent != null) {
            streamingTitle = intent.getStringExtra("roomTitle");
            roomId = intent.getStringExtra("roomId");
            roomComment = intent.getStringExtra("roomComment");
            vidioId = intent.getStringExtra("VidioId");
        }



        endButton = findViewById(R.id.endButton);
        endButton.setOnClickListener(view -> {
            deleteChatRoom(roomId);
            intent = new Intent(StreamingHostRoom.this, StreamingMain.class);
            startActivity(intent);
            finish();
        });


        changeYoutubeButton = findViewById(R.id.changeYoutubeButton);
        changeYoutubeButton.setOnClickListener(view -> {
            intent = new Intent(StreamingHostRoom.this, StreamingSearchYoutube.class);
            startActivity(intent);
        });


        patchButton = findViewById(R.id.patchButton);
        patchButton.setOnClickListener(view -> {
            Intent patchIntent = new Intent(StreamingHostRoom.this, StreamingMain.class);
            patchIntent.putExtra("roomId", roomId);
            patchIntent.putExtra("roomTitle", streamingTitle);
            patchIntent.putExtra("roomComment", roomComment);
            startActivity(patchIntent);
        });

    }

    private void deleteChatRoom(String roomId) {
        api.deleteChatRoom(roomId).enqueue(new Callback<RoomDto>() {
            @Override
            public void onResponse(Call<RoomDto> call, Response<RoomDto> response) {

            }

            @Override
            public void onFailure(Call<RoomDto> call, Throwable t) {

            }
        });
    }


}