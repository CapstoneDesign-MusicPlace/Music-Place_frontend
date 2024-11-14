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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamingCreateRoom extends AppCompatActivity {
    private Intent intent;
    private UserApiInterface api;
    private EditText editTextTitle, editTextText;
    private Button back, add;

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

        // 채팅방 생성 버튼 이벤트
        add.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String comment = editTextText.getText().toString();

            if (title.isEmpty() || comment.isEmpty()) {
                Toast.makeText(this, "제목과 설명을 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                createChatRoom(title, comment);  // 방 생성 메서드 호출
            }
        });

        // 뒤로가기 버튼 이벤트
        back.setOnClickListener(view -> {
            intent = new Intent(StreamingCreateRoom.this, StreamingMain.class);
            startActivity(intent);
            finish();
        });
    }

    private void createChatRoom(String title, String comment) {
        RoomDto roomDto = new RoomDto(null, title, comment, null);
        Call<String> call = api.createChatRoom(roomDto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String roomId = response.body(); // 방 ID를 응답으로 받음
                    System.out.println("Created room ID: " + roomId);

                    // 방 생성이 성공했을 때만 StreamingHostRoom으로 이동
                    intent = new Intent(StreamingCreateRoom.this, StreamingHostRoom.class);
                    intent.putExtra("roomTitle", title);
                    intent.putExtra("roomId", roomId);
                    intent.putExtra("roomComment", comment);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(StreamingCreateRoom.this, "채팅방 생성 실패", Toast.LENGTH_SHORT).show();
                    System.out.println("Response error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(StreamingCreateRoom.this, "네트워크 오류로 채팅방을 생성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                System.out.println("Network error: " + t.getMessage());
            }
        });
    }
}
