package com.example.musicplace.youtubeMusicPlayer.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.musicplace.R;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeVidioDto;

import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

    private ArrayList<YoutubeVidioDto> videoDtoList;
    private int position;
    private Button back, add;

    private String vidioTitle, vidioId, vidioImage;

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
            vidioTitle = intent.getStringExtra("VidioTitle");
            vidioId = intent.getStringExtra("VidioId");
            vidioImage = intent.getStringExtra("VidioImage");

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
                // 이전 화면으로 이동
                finish();
            }
        });


        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 플레이 리스트 목록으로 이동
                Intent intent = new Intent(MusicPlayer.this, SelectPlaylist.class);
                intent.putExtra("VidioTitle", vidioTitle);
                intent.putExtra("VidioId", vidioId);
                intent.putExtra("VidioImage",vidioImage);
                startActivity(intent);

            }
        });
    }


}