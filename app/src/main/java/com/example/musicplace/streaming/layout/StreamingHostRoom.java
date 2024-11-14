package com.example.musicplace.streaming.layout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.example.musicplace.streaming.WebSocketClient;
import com.example.musicplace.streaming.adapter.ChatingAdapter;
import com.example.musicplace.streaming.adapter.ViewType;
import com.example.musicplace.streaming.dto.ReqestChatDto;
import com.example.musicplace.streaming.dto.ResponseChatDto;
import com.example.musicplace.streaming.dto.RoomDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONObject;

public class StreamingHostRoom extends AppCompatActivity {

    private static final String TAG = "StreamingHostRoom";

    private Intent intent;
    private WebView webView;
    private UserApiInterface api;
    private TextView titleTextView;
    private Button changeYoutubeButton, sendButton, endButton, patchButton;
    private String streamingTitle, roomId, roomComment, vidioId;
    private WebSocketClient webSocketClient;
    private ChatingAdapter chatingAdapter;
    private ArrayList<ResponseChatDto> chatList = new ArrayList<>();
    private EditText messageEditText;
    private String wsUrl = "ws://10.0.2.2:8080/chats"; // WebSocket URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_streaming_host_room);
        Log.i(TAG, "Activity created");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Intent에서 데이터 수신
        intent = getIntent();
        if (intent != null) {
            streamingTitle = intent.getStringExtra("roomTitle");
            roomId = intent.getStringExtra("roomId");
            roomComment = intent.getStringExtra("roomComment");
            vidioId = intent.getStringExtra("vidioId"); // 필요 시 vidioId도 수신
            Log.d(TAG, "Intent extras received: roomTitle=" + streamingTitle + ", roomId=" + roomId + ", roomComment=" + roomComment);
        }


        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        String token = tokenManager.getToken();
        Log.d(TAG, "JWT Token retrieved");

        // WebSocketClient에 JWT 토큰 전달하여 생성
        webSocketClient = new WebSocketClient(new WebSocketClient.WebSocketCallback() {
            @Override
            public void onOpen() {
                runOnUiThread(() -> {
                    Log.i(TAG, "WebSocket connected");
                    addSystemMessage("채팅에 연결되었습니다.", ViewType.CENTER_JOIN);
                    sendEnterMessage();
                });
            }

            @Override
            public void onMessage(String text) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Received message: " + text);
                    handleMessage(text);
                });
            }

            @Override
            public void onClosing(String reason) {
                runOnUiThread(() -> {
                    Log.i(TAG, "WebSocket closing: " + reason);
                    addSystemMessage("채팅 연결이 종료되었습니다: " + reason, ViewType.CENTER_JOIN);
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Log.e(TAG, "WebSocket failure: " + error);
                    addSystemMessage("채팅 연결 실패: " + error, ViewType.CENTER_JOIN);
                });
            }
        }, token);  // 생성자에 token 전달
        webSocketClient.connect(wsUrl);

        webView = findViewById(R.id.webView);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(streamingTitle);

        intent = getIntent();
        if (intent != null) {
            streamingTitle = intent.getStringExtra("roomTitle");
            roomId = intent.getStringExtra("roomId");
            roomComment = intent.getStringExtra("roomComment");
            vidioId = intent.getStringExtra("VidioId");
            Log.d(TAG, "Intent extras retrieved: roomTitle=" + streamingTitle + ", roomId=" + roomId);
        }

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatingAdapter = new ChatingAdapter(chatList);
        chatRecyclerView.setAdapter(chatingAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString();
            if (!message.isEmpty()) {
                Log.d(TAG, "Sending message: " + message);
                sendTalkMessage(message);
                addMessage(new ResponseChatDto(message, roomId, vidioId, null, ViewType.RIGHT_CHAT));
                messageEditText.setText("");
            }
        });

        endButton = findViewById(R.id.endButton);
        endButton.setOnClickListener(view -> {
            Log.i(TAG, "End button clicked");
            sendExitMessage();
            deleteChatRoom(roomId);
            intent = new Intent(StreamingHostRoom.this, StreamingMain.class);
            startActivity(intent);
            finish();
        });

        changeYoutubeButton = findViewById(R.id.changeYoutubeButton);
        changeYoutubeButton.setOnClickListener(view -> {
            Log.i(TAG, "Change YouTube button clicked");
            intent = new Intent(StreamingHostRoom.this, StreamingSearchYoutube.class);
            startActivity(intent);
        });

        patchButton = findViewById(R.id.patchButton);
        patchButton.setOnClickListener(view -> {
            Log.i(TAG, "Patch button clicked");
            Intent patchIntent = new Intent(StreamingHostRoom.this, StreamingMain.class);
            patchIntent.putExtra("roomId", roomId);
            patchIntent.putExtra("roomTitle", streamingTitle);
            patchIntent.putExtra("roomComment", roomComment);
            startActivity(patchIntent);
        });
    }

    private void sendEnterMessage() {
        Log.d(TAG, "Sending enter message");
        sendMessage("ENTER", null);
    }

    private void sendTalkMessage(String message) {
        Log.d(TAG, "Sending talk message");
        sendMessage("TALK", message);
    }

    private void sendExitMessage() {
        Log.d(TAG, "Sending exit message");
        sendMessage("EXIT", null);
    }

    private void sendMessage(String type, String message) {
        try {
            ReqestChatDto requestChatDto = new ReqestChatDto(message, roomId, vidioId);
            JSONObject json = new JSONObject();
            json.put("type", type);

            JSONObject payload = new JSONObject();
            payload.put("chatRoomId", requestChatDto.getChatRoomId());
            if (message != null) {
                payload.put("message", requestChatDto.getMessage());
            }
            if (vidioId != null) {
                payload.put("vidioId", requestChatDto.getVidioId());
            }
            json.put("payload", payload);

            Log.d(TAG, "Sending WebSocket message: " + json.toString());
            webSocketClient.sendMessage(json.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error sending message", e);
        }
    }

    private void deleteChatRoom(String roomId) {
        Log.d(TAG, "Deleting chat room: " + roomId);
        api.deleteChatRoom(roomId).enqueue(new Callback<RoomDto>() {
            @Override
            public void onResponse(Call<RoomDto> call, Response<RoomDto> response) {
                Log.i(TAG, "Chat room deleted successfully");
            }

            @Override
            public void onFailure(Call<RoomDto> call, Throwable t) {
                Log.e(TAG, "Failed to delete chat room", t);
            }
        });
    }

    private void addMessage(ResponseChatDto chatDto) {
        chatList.add(chatDto);
        chatingAdapter.notifyItemInserted(chatList.size() - 1);
    }

    private void addSystemMessage(String message, int viewType) {
        addMessage(new ResponseChatDto(message, roomId, null, null, viewType));
    }

    private void handleMessage(String text) {
        try {
            JSONObject json = new JSONObject(text);
            String type = json.getString("type");
            JSONObject payload = json.getJSONObject("payload");
            String message = payload.optString("message");
            String sender = payload.optString("username", null);
            String vidioId = payload.optString("vidioId", null);

            int viewType;
            if ("ENTER".equals(type)) {
                viewType = ViewType.CENTER_JOIN;
                message = sender + "님이 입장하셨습니다.";
            } else if ("EXIT".equals(type)) {
                viewType = ViewType.CENTER_JOIN;
                message = sender + "님이 퇴장하셨습니다.";
            } else {
                viewType = ViewType.LEFT_CHAT;
            }

            Log.d(TAG, "Handling received message: " + message);
            addMessage(new ResponseChatDto(message, roomId, vidioId, sender, viewType));
        } catch (Exception e) {
            Log.e(TAG, "Error handling message", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            Log.i(TAG, "Activity destroyed, closing WebSocket");
            sendExitMessage();
            webSocketClient.close();
        }
    }
}
