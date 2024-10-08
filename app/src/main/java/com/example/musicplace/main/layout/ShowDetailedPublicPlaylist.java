package com.example.musicplace.main.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.musicplace.follow.layout.FollowDetailed;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.follow.dto.FollowSaveDto;
import com.example.musicplace.playlist.adapter.CommentRecyclerAdapter;
import com.example.musicplace.playlist.dto.CommentSaveDto;
import com.example.musicplace.playlist.dto.ResponseCommentDto;
import com.example.musicplace.playlist.dto.ResponseMusicDto;
import com.example.musicplace.playlist.layout.playlistInMusicPlayer;
import com.example.musicplace.youtubeMusicPlayer.adapter.YoutubeRecyclerAdapter;
import com.example.musicplace.youtubeMusicPlayer.dto.YoutubeItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailedPublicPlaylist extends AppCompatActivity {
    private Intent intent;
    private UserApiInterface api;
    private RecyclerView musicRecyclerView, commentRecyclerView;
    private YoutubeRecyclerAdapter youtubeRecyclerAdapter;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private List<ResponseMusicDto> musicListDto;
    private List<ResponseCommentDto> commentDtos;
    private TextView textViewTitle, nicknameTextView, commentTextView;
    private EditText commentEditText;
    private Button saveButton, followButton, backButton;
    private ImageView profileImageView;
    private Long playlistId;
    private String playlistTitle, nickname, imageUrl, comment, member_id;

    private LinearLayout profileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_detailed_public_playlist);
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

        // Intent로 전달받은 데이터 수신
        intent = getIntent();
        if (intent != null) {
            playlistId = intent.getLongExtra("playlistId", 1L);
            playlistTitle = intent.getStringExtra("playlistTitle");
            nickname = intent.getStringExtra("nickname");
            imageUrl = intent.getStringExtra("imageUrl");
            comment = intent.getStringExtra("comment");
            member_id = intent.getStringExtra("member_id");
        }

        youtubeRecyclerAdapter.setOnItemClickListener((position) -> {
            // 클릭된 아이템의 YoutubeItem 데이터 가져오기
            Intent intent = new Intent(getApplicationContext(), playlistInMusicPlayer.class);
            intent.putExtra("VidioTitle", musicListDto.get(position).getVidioTitle());
            intent.putExtra("VidioId", musicListDto.get(position).getVidioId());
            intent.putExtra("VidioImage", musicListDto.get(position).getVidioImage());
            startActivity(intent);
        });

        // 댓글 리사이클뷰와 어뎁터 설정
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        commentRecyclerAdapter = new CommentRecyclerAdapter(this, new ArrayList<>());
        commentRecyclerView.setAdapter(commentRecyclerAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadPlaylistMusicData(playlistId);
        loadPlaylistCommentData(playlistId);

        textViewTitle = findViewById(R.id.textViewTitle);
        nicknameTextView = findViewById(R.id.nicknameTextView);
        commentTextView = findViewById(R.id.commentTextView);
        profileImageView = findViewById(R.id.profileImageView);
        commentEditText = findViewById(R.id.commentEditText);

        textViewTitle.setText(playlistTitle);
        nicknameTextView.setText(nickname);
        commentTextView.setText(comment);

        // imageUrl을 이용해 이미지 설정
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                profileImageView.setImageResource(Integer.parseInt(imageUrl));
            } catch (NumberFormatException e) {
                // 기본 이미지를 설정
                profileImageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            // 기본 이미지를 설정
            profileImageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            // 댓글 저장 버튼
            String userComment = String.valueOf(commentEditText.getText());
            if(userComment.isEmpty()){
                Toast.makeText(ShowDetailedPublicPlaylist.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            CommentSaveDto commentSaveDto = new CommentSaveDto(nickname, userComment);
            saveComment(playlistId, commentSaveDto);
            commentEditText.setText("");
        });

        followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(view -> {
            // 구독 버튼
            FollowSave(new FollowSaveDto(member_id, imageUrl, nickname));
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());


        profileLayout = findViewById(R.id.profileLayout);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다른 사용자의 프로필을 볼 수 있는 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), FollowDetailed.class);
                intent.putExtra("memberId", member_id);
                intent.putExtra("image", imageUrl);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
            }
        });
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
                    Toast.makeText(ShowDetailedPublicPlaylist.this, "플레이리스트의 음악 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseMusicDto>> call, Throwable t) {
                Toast.makeText(ShowDetailedPublicPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
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
                        ResponseCommentDto responseCommentDto = new ResponseCommentDto(
                                commentDto.getComment_id(),
                                commentDto.getNickName(),
                                commentDto.getUserComment()
                        );
                        commentItems.add(responseCommentDto);
                    }
                    // 어댑터에 데이터를 설정하고 갱신
                    commentRecyclerAdapter.setCommentItems(commentItems);
                } else {
                    Toast.makeText(ShowDetailedPublicPlaylist.this, "플레이리스트의 음악 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseCommentDto>> call, Throwable t) {
                Toast.makeText(ShowDetailedPublicPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveComment(Long plId, CommentSaveDto commentSaveDto) {
        api.CommentSave(plId, commentSaveDto).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ShowDetailedPublicPlaylist.this, "댓글이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                    // 저장된 댓글을 즉시 리사이클러뷰에 추가
                    ResponseCommentDto newComment = new ResponseCommentDto(
                            response.body(), // 서버로부터 받은 comment_id 사용
                            commentSaveDto.getNickName(),
                            commentSaveDto.getComment()
                    );

                    commentRecyclerAdapter.addComment(newComment);
                    commentEditText.setText(""); // 입력란 초기화
                } else {
                    System.out.println("댓글 저장 실패");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(ShowDetailedPublicPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void FollowSave(FollowSaveDto followSaveDto) {
        api.FollowSave(followSaveDto).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ShowDetailedPublicPlaylist.this, "팔로우 되었습니다..", Toast.LENGTH_SHORT).show();
                    System.out.println("팔로우 성공");
                } else {
                    System.out.println("팔로우 실패");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(ShowDetailedPublicPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
