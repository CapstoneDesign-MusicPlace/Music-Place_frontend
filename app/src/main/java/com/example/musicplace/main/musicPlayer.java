package com.example.musicplace.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.musicplace.R;
import com.example.musicplace.dto.youtub.YoutubeVidioDto;
import com.example.musicplace.join;
import com.example.musicplace.start;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class musicPlayer extends AppCompatActivity {

    private ArrayList<YoutubeVidioDto> videoDtoList;
    private int position;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_player);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Intent로부터 데이터 수신
        Intent intent = getIntent();
        if (intent != null) {
            String vidioTitle = intent.getStringExtra("VidioTitle");
            String vidioId = intent.getStringExtra("VidioId");

            // VideoFragment에 데이터 전달
            Bundle bundle = new Bundle();
            bundle.putString("vidioId", vidioId);
            bundle.putString("vidioTitle", vidioTitle);

            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(bundle);

            // FragmentTransaction을 통해 VideoFragment를 추가
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, videoFragment)
                    .commit();
        }


        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start 화면으로 돌아가기
                Intent intent = new Intent(musicPlayer.this, SearchMusic.class);

                startActivity(intent);
            }
        });
    }


}