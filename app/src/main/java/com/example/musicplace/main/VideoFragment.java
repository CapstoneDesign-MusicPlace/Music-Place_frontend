package com.example.musicplace.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.musicplace.R;

import org.jetbrains.annotations.Nullable;

public class VideoFragment extends Fragment {

    private WebView webView;
    private TextView vidioTitleTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        // 뷰 초기화
        webView = view.findViewById(R.id.webView);
        vidioTitleTextView = view.findViewById(R.id.vidioTitle);

        // WebView 설정
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()); // 링크 클릭 시 브라우저로 이동하지 않도록 설정

        // 번들로 전달받은 데이터 처리
        Bundle args = getArguments();
        if (args != null) {
            String vidioId = args.getString("vidioId");
            String vidioTitle = args.getString("vidioTitle");

            // 비디오 제목 설정
            vidioTitleTextView.setText(vidioTitle);

            // YouTube 임베디드 플레이어 URL
            String videoUrl = "https://www.youtube.com/embed/" + vidioId;
            webView.loadUrl(videoUrl);
        }

        return view;
    }
}