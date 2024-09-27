package com.example.musicplace.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.BuildConfig;
import com.example.musicplace.R;
import com.example.musicplace.retrofit.UserApiInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class start extends AppCompatActivity {

    private Button join, google, login;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private UserApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // GoogleSignInOptions 구성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.serverClientId) // 환경 변수로부터 서버 클라이언트 ID 가져오기
                .requestEmail()
                .build();

        // GoogleSignInClient 초기화
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 회원가입 화면 이동
        join = findViewById(R.id.join);
        join.setOnClickListener(view -> {

            Intent intent = new Intent(start.this, join.class);
            startActivity(intent);
        });

        // 구글 로그인 버튼 클릭 시
        google = findViewById(R.id.google);
        google.setOnClickListener(view -> signIn());

        // 로그인 화면 이동
        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(start.this, login.class);
            startActivity(intent);
        });
    }

    // 구글 로그인 요청
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // 로그인 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String idToken = account.getIdToken();
                    sendTokenToServer(idToken);  // 서버로 토큰 전송
                }
            } catch (ApiException e) {
                Log.w("Google Sign In", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 서버로 ID 토큰 전송 (Retrofit 또는 OkHttp를 사용한 구현 필요)
    private void sendTokenToServer(String idToken) {

        Call<Void> call = api.sendIdTokenToServer(idToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(start.this, "토큰 전송 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(start.this, "토큰 전송 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(start.this, "서버 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
