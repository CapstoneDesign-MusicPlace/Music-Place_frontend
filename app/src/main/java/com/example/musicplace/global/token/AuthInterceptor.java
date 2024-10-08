package com.example.musicplace.global.token;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // 특정 경로에는 토큰을 추가하지 않음
        if (originalRequest.url().encodedPath().contains("/sign_in/save") ||
                originalRequest.url().encodedPath().contains("/auth/login") ||
                originalRequest.url().encodedPath().contains("/sign_in/{member_id}/sameid")) {
            return chain.proceed(originalRequest);
        }

        // 토큰을 가져와서 헤더에 추가
        String token = tokenManager.getToken();
        if (token != null) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)  // 토큰을 Bearer 형식으로 추가
                    .build();
            Response response = chain.proceed(newRequest);

            // 401 Unauthorized 응답이 왔을 때 토큰 삭제
            if (response.code() == 401) {
                tokenManager.deleteToken();
                // 로그아웃 처리 - 필요 시 로그아웃 UI로 전환할 수 있음
                // 예: 로그아웃 화면으로 이동하거나, 사용자에게 알림을 띄움
            }

            return response;
        }

        return chain.proceed(originalRequest);
    }
}
