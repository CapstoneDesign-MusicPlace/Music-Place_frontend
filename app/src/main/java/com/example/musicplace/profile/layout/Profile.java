package com.example.musicplace.profile.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.musicplace.main.layout.MainDisplay;
import com.example.musicplace.playlist.layout.MyPlaylist;
import com.example.musicplace.profile.dto.SignInGetUserDataDto;
import com.example.musicplace.sign.layout.Start;
import com.example.musicplace.youtubeMusicPlayer.layout.SearchMusic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    // 사용자의 프로필 조작 가능
    private Intent intent;
    private UserApiInterface api;
    private TextView userNicknameTextView;
    private TextView playlistCountTextView;
    private TextView followCountTextView;
    private LinearLayout playlistLinearLayout, followLinearLayout;
    private Button profileButton, secessionButton, logOutButton;
    private BottomNavigationView bottomNavigationView;
    private String name, email, nickname, profile_img_url;
    private ImageView editImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        playlistCountTextView = findViewById(R.id.playlistCountTextView);
        followCountTextView = findViewById(R.id.followCountTextView);

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        loadProfileData();
        loadPLCount();
        loadFollowCount();

        editImageView = (ImageView) findViewById(R.id.editImageView);
        userNicknameTextView = (TextView) findViewById(R.id.userNicknameTextView);
        userNicknameTextView.setText(nickname);

        editImageView = findViewById(R.id.editImageView);
        if (profile_img_url != null && !profile_img_url.isEmpty()) {
            editImageView.setImageResource(Integer.parseInt(profile_img_url));
        } else {
            editImageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }


        followLinearLayout = findViewById(R.id.followLinearLayout);
        followLinearLayout.setOnClickListener(new View.OnClickListener() {
            // 팔로워 상세 리스트 이동
            @Override
            public void onClick(View view) {

            }
        });

        profileButton = (Button) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            // 프로필 수정 페이지 이동
            @Override
            public void onClick(View view) {
                intent = new Intent(Profile.this, EditProfile.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("nickname", nickname);
                intent.putExtra("profile_img_url",profile_img_url);
                startActivity(intent);
            }
        });

        secessionButton = (Button) findViewById(R.id.secessionButton);
        secessionButton.setOnClickListener(new View.OnClickListener() {
            // 탈퇴 경고창 이동
            @Override
            public void onClick(View view) {
                SignInDelete();
            }
        });


        logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            // 로그아웃
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {

            if(menuItem.getItemId() == R.id.search) {
                intent = new Intent(Profile.this, SearchMusic.class);
                startActivity(intent);
                finish();
                return true;
            } /*else if(menuItem.getItemId() == R.id.headset) {
                intent = new Intent(mainDisplay.this, Map.class);
                startActivity(intent);
                finish();
                return true;
            } */else if(menuItem.getItemId() == R.id.home) {
                intent = new Intent(Profile.this, MainDisplay.class);
                startActivity(intent);
                finish();
                return true;
            }
            else if(menuItem.getItemId() == R.id.add) {
                intent = new Intent(Profile.this, MyPlaylist.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });

    }

    private void loadProfileData() {
        api.SignInGetUserData().enqueue(new Callback<SignInGetUserDataDto>() {
            @Override
            public void onResponse(Call<SignInGetUserDataDto> call, Response<SignInGetUserDataDto> response) {
                if (response.isSuccessful()) {
                    name = response.body().getName();
                    nickname = response.body().getNickname();
                    email = response.body().getEmail();
                    profile_img_url = response.body().getProfile_img_url();
                    userNicknameTextView.setText(nickname);
                } else {
                    try {
                        System.out.println("load Profile Data 실패: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignInGetUserDataDto> call, Throwable t) {
                Toast.makeText(Profile.this, "네트워크 오류가 발생했습니다: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPLCount() {
        api.PLCount().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    playlistCountTextView.setText(response.body().toString());
                } else {
                    System.out.println("load PL Count 실패");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(Profile.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFollowCount() {
        api.FollowCount().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    followCountTextView.setText(response.body().toString());
                } else {
                    System.out.println("load Follow Count 실패");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(Profile.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void logOut() {
        api.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 로그아웃 성공 시 초기 화면으로 이동
                    Toast.makeText(Profile.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Profile.this, Start.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    System.out.println("로그아웃 실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Profile.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SignInDelete() {
        api.SignInDelete().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 로그아웃 성공 시 초기 화면으로 이동
                    Toast.makeText(Profile.this, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Profile.this, Start.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    System.out.println("탈퇴 실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Profile.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}