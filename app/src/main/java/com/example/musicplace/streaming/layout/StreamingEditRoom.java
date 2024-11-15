package com.example.musicplace.streaming.layout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamingEditRoom extends AppCompatActivity {
    private Intent intent;
    private UserApiInterface api;
    private EditText editTextTitle, editTextText;
    private Button back, add;
    private String streamingTitle, streamingId, streamingComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_streaming_edit_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);


        intent = getIntent();
        if (intent != null) {
            streamingId = intent.getStringExtra("roomId");
            streamingTitle = intent.getStringExtra("roomTitle");
            streamingComment = intent.getStringExtra("roomComment");

        }

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextText = findViewById(R.id.editTextText);
        editTextTitle.setText(streamingTitle);
        editTextText.setText(streamingComment);


        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String comment = editTextText.getText().toString();
            RoomDto roomDto = new RoomDto(null, title, comment, null);

            // Chat room을 업데이트하는 API 호출
            patchChatRoom(streamingId, roomDto);

            // 수정된 데이터 Intent에 담기
            Intent resultIntent = new Intent();
            resultIntent.putExtra("roomId", streamingId);
            resultIntent.putExtra("roomTitle", title);
            resultIntent.putExtra("roomComment", comment);

            // 결과 설정 및 Activity 종료
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }


    private void patchChatRoom(String roomId, RoomDto roomDto) {
        api.patchChatRoom(roomId, roomDto).enqueue(new Callback<RoomDto>() {
            @Override
            public void onResponse(Call<RoomDto> call, Response<RoomDto> response) {

            }

            @Override
            public void onFailure(Call<RoomDto> call, Throwable t) {

            }
        });
    }
}