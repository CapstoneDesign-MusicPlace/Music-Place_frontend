package com.example.musicplace.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.Nullable;


public class start extends AppCompatActivity {

    private Button join, google, login;
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
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

        // GoogleSignInOptions 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("859929355457-had21bnjr1d1ohs24n1o5jao72o2vjuf.apps.googleusercontent.com") // 클라이언트 ID 사용
                .requestEmail()
                .build();

        // GoogleSignInClient 초기화
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // 회원가입 화면 이동
        join = findViewById(R.id.join);
        join.setOnClickListener(view -> {

            Intent intent = new Intent(start.this, join.class);
            startActivity(intent);
        });


        // 로그인 화면 이동
        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(start.this, login.class);
            startActivity(intent);
        });

        // 로그인 버튼 설정
        Button googleSignInButton = findViewById(R.id.google);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }




    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String idToken = account.getIdToken();
                Log.d("GoogleSignIn", "IdToken: " + idToken);
                // 여기서 idToken을 서버로 전송하거나, 필요한 작업을 수행할 수 있습니다.
                // handleSignInResult(idToken); 호출 없이 처리 가능
            } catch (ApiException e) {
                Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void handleSignInResult(String idToken) {
        // 여기에서 idToken을 서버로 보내서 검증하는 로직을 작성
        // 예시: Retrofit 등을 사용해 서버로 토큰 전송
    }


}
