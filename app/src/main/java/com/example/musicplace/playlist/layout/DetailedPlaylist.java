package com.example.musicplace.playlist.layout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.musicplace.playlist.adapter.CommentRecyclerAdapter;
import com.example.musicplace.playlist.dto.ResponseCommentDto;
import com.example.musicplace.playlist.dto.ResponseMusicDto;
import com.example.musicplace.youtubeMusicPlayer.adapter.YoutubeRecyclerAdapter;
import com.example.musicplace.youtubeMusicPlayer.layout.MusicPlayer;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.YoutubeItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedPlaylist extends AppCompatActivity {
    // MyPlaylist에서 플레이리스트 항목을 선택하면 넘어가는 상세 페이지
    private Intent intent;
    private Button editButton, deleteButton, backButton;
    private TextView textViewTitle, publicAndPrivate, commentTextView;
    private UserApiInterface api;
    private RecyclerView musicRecyclerView, commentRecyclerView;
    private YoutubeRecyclerAdapter youtubeRecyclerAdapter;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private List<ResponseMusicDto> musicListDto;
    private List<ResponseCommentDto> commentDtos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // 뮤직 리사이클뷰와 어뎁터 설정
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        youtubeRecyclerAdapter = new YoutubeRecyclerAdapter(this, new ArrayList<>());
        musicRecyclerView.setAdapter(youtubeRecyclerAdapter);
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        youtubeRecyclerAdapter.setOnItemClickListener((position) -> {
            // 클릭된 아이템의 YoutubeItem 데이터 가져오기
            Intent intent = new Intent(getApplicationContext(), playlistInMusicPlayer.class);
            intent.putExtra("VidioTitle", musicListDto.get(position).getVidioTitle());
            intent.putExtra("VidioId", musicListDto.get(position).getVidioId());
            intent.putExtra("VidioImage",musicListDto.get(position).getVidioImage());
            startActivity(intent);
        });

        // 댓글 리사이클뷰와 어뎁터 설정
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        commentRecyclerAdapter = new CommentRecyclerAdapter(this, new ArrayList<>());
        commentRecyclerView.setAdapter(commentRecyclerAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Intent로 전달받은 데이터 수신
        intent = getIntent();
        Long playlistId = intent.getLongExtra("playlistId",1L);
        String playlistTitle = intent.getStringExtra("playlistTitle");
        String nickname = intent.getStringExtra("nickname");
        String imageUrl = intent.getStringExtra("imageUrl");
        String onOff =intent.getStringExtra("onoff");
        String comment = intent.getStringExtra("comment");

        loadPlaylistMusicData(playlistId);
        loadPlaylistCommentData(playlistId);

        textViewTitle = findViewById(R.id.textViewTitle);
        publicAndPrivate = findViewById(R.id.publicAndPrivate);
        commentTextView = findViewById(R.id.commentTextView);

        textViewTitle.setText(playlistTitle);
        publicAndPrivate.setText(onOff);
        commentTextView.setText(comment);


        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });



        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                intent = new Intent(DetailedPlaylist.this, EditPlaylist.class);
                intent.putExtra("playlistId", playlistId);
                intent.putExtra("playlistTitle", playlistTitle);
                intent.putExtra("nickname;", nickname);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("onoff", onOff);
                intent.putExtra("comment",comment);
                startActivityForResult(intent, 1002);
            }
        });





        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.PLDelete(playlistId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 삭제 성공 시 처리
                            Toast.makeText(DetailedPlaylist.this,  playlistTitle +"이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();  // 액티비티 종료
                        } else {
                            // 실패 시 처리
                            Toast.makeText(DetailedPlaylist.this, playlistTitle +"이 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // 요청 실패 시 처리
                        Toast.makeText(DetailedPlaylist.this, "서버 요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", t.getMessage());
                    }
                });
            }
        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);  // MyPlaylist에 결과를 전달
            finish();  // DetailedPlaylist 종료
        }
    }


    private void loadPlaylistMusicData(Long PLId) {
        api.MusicFindAll(PLId).enqueue(new Callback<List<ResponseMusicDto>>() {
            @Override
            public void onResponse(Call<List<ResponseMusicDto>> call, Response<List<ResponseMusicDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    musicListDto = response.body();

                    // ResponseMusicDto 리스트를 YoutubeItem 리스트로 변환
                    ArrayList<YoutubeItem> youtubeItems = new ArrayList<>();
                    for (ResponseMusicDto musicDto : musicListDto) {
                        YoutubeItem youtubeItem = new YoutubeItem(musicDto.getVidioImage(), musicDto.getVidioTitle());
                        youtubeItems.add(youtubeItem);
                    }

                    // 어댑터에 데이터를 설정하고 갱신
                    youtubeRecyclerAdapter.setYoutubeItems(youtubeItems);
                } else {
                    Toast.makeText(DetailedPlaylist.this, "플레이리스트의 음악 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseMusicDto>> call, Throwable t) {
                Toast.makeText(DetailedPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlaylistCommentData(Long PLId) {
        api.CommentFindAll(PLId).enqueue(new Callback<List<ResponseCommentDto>>() {
            @Override
            public void onResponse(Call<List<ResponseCommentDto>> call, Response<List<ResponseCommentDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentDtos = response.body();


                    ArrayList<ResponseCommentDto> commentItems = new ArrayList<>();
                    for (ResponseCommentDto commentDto : commentDtos) {
                        ResponseCommentDto responseCommentDto = new ResponseCommentDto(commentDto.getComment_id(),commentDto.getNickName(), commentDto.getUserComment());
                        System.out.println(commentDto.getComment_id()+commentDto.getNickName()+ commentDto.getUserComment());
                        commentItems.add(responseCommentDto);
                    }


                    // 어댑터에 데이터를 설정하고 갱신
                    commentRecyclerAdapter.setCommentItems(commentItems);
                } else {
                    Toast.makeText(DetailedPlaylist.this, "플레이리스트의 음악 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseCommentDto>> call, Throwable t) {
                Toast.makeText(DetailedPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}

