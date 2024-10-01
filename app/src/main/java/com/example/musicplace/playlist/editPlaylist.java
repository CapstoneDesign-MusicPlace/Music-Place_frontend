package com.example.musicplace.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.musicplace.R;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.playlist.dto.OnOff;
import com.example.musicplace.youtubeMusicPlayer.YoutubeRecyclerAdapter;

import java.util.ArrayList;

public class editPlaylist extends AppCompatActivity {
    // 플레이리스트를 편집하는 페이지
    private Intent intent;
    private UserApiInterface api;
    private EditText editTextTitle, editTextText;
    private ImageView editImageView;
    private RadioGroup radioGroup;
    private Button saveButton, cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // Intent로 전달받은 데이터 수신
        intent = getIntent();
        String playlistId = intent.getStringExtra("playlistId");
        String playlistTitle = intent.getStringExtra("playlistTitle");
        String nickname = intent.getStringExtra("nickname");
        String imageUrl = intent.getStringExtra("imageUrl");
        String onOff =intent.getStringExtra("onoff");
        String comment = intent.getStringExtra("comment");

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextText = findViewById(R.id.editTextText);
        editTextTitle.setText(playlistTitle);
        editTextText.setText(comment);

        // ImageView 초기화
        editImageView = findViewById(R.id.editImageView);

        // Glide를 사용하여 이미지 로드
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .error(R.drawable.playlistimg)
                    .into(editImageView);
        }

        radioGroup = findViewById(R.id.radioGroup);
        RadioButton publicRadioButton = findViewById(R.id.publicRadioButton);
        RadioButton privateRadioButton = findViewById(R.id.privateRadioButton);

        // onOff 값을 기반으로 RadioButton 선택 설정
        if ("Public".equals(onOff)) {
            publicRadioButton.setChecked(true);  // 공개일 경우
        } else if ("Private".equals(onOff)) {
            privateRadioButton.setChecked(true); // 비공개일 경우
        }

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(editPlaylist.this, DetailedPlaylist.class);
                startActivity(intent);
            }
        });


    }
}