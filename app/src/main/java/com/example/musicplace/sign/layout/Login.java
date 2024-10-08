package com.example.musicplace.sign.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.main.layout.MainDisplay;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.sign.dto.LoginRequestDto;
import com.example.musicplace.sign.dto.LoginResponseDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ImageButton back_login, start;
    private Button forgetId, forgetPw;
    private UserApiInterface api;
    private EditText id, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);


        back_login = (ImageButton) findViewById(R.id.back_login);
        back_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // 초기 화면으로 이동
                finish();
            }
        });


        forgetId = (Button) findViewById(R.id.forgetId);
        forgetId.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {// 아이디 찾기 이동
                Intent intent = new Intent(Login.this, FindId.class);
                startActivity(intent);
            }
        });


        forgetPw = (Button) findViewById(R.id.forgetPw);
        forgetPw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {// 비밀번호 찾기 이동
                Intent intent = new Intent(Login.this, FindPw.class);
                startActivity(intent);
            }
        });


        start = (ImageButton) findViewById(R.id.finish);
        id = findViewById(R.id.id);
        pw = findViewById(R.id.pw);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getId = id.getText().toString();
                String getPw = pw.getText().toString();

                if (getId.isEmpty() || getPw.isEmpty()) {
                    Toast.makeText(Login.this, "ID와 비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginRequestDto loginRequestDto = new LoginRequestDto(getId, getPw);
                Call<LoginResponseDto> call = api.login(loginRequestDto);
                call.enqueue(new Callback<LoginResponseDto>() {
                    @Override
                    public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                        if (response.isSuccessful()) {
                            String token = response.body().getToken();

                            // 토큰 저장
                            TokenManager tokenManager = new TokenManager(Login.this);
                            tokenManager.saveToken(token);

                            // 메인 화면으로 이동
                            Intent intent = new Intent(Login.this, MainDisplay.class);
                            startActivity(intent);
                        } else {
                            System.out.println("로그인 실패: " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                        System.out.println("login error = " + t.getMessage());
                    }
                });
            }
        });


    }


}