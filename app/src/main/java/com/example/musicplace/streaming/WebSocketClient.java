package com.example.musicplace.streaming;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient {
    private WebSocket webSocket;
    private WebSocketCallback callback;
    private final String token; // JWT 토큰 저장

    public interface WebSocketCallback {
        void onOpen();
        void onMessage(String text);
        void onClosing(String reason);
        void onFailure(String error);
    }

    // JWT 토큰을 WebSocketClient 생성자에서 받도록 수정
    public WebSocketClient(WebSocketCallback callback, String token) {
        this.callback = callback;
        this.token = token;
    }

    public void connect(String url) {
        OkHttpClient client = new OkHttpClient();

        // Request에 Authorization 헤더 추가
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token) // 토큰 추가
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                if (callback != null) callback.onOpen();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (callback != null) callback.onMessage(text);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                if (callback != null) callback.onClosing(reason);
                webSocket.close(1000, null);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                if (callback != null) callback.onFailure(t.getMessage());
            }
        });
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "App closed");
            webSocket = null;
        }
    }
}
