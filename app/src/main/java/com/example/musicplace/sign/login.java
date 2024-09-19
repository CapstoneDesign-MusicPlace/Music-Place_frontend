package com.example.musicplace.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.main.mainDisplay;

public class login extends AppCompatActivity {

    private ImageButton back_login, start;
    private Button forgetId, forgetPw;

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

        back_login = (ImageButton) findViewById(R.id.back_login);
        back_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // 초기 화면으로 이동
                Intent intent = new Intent(login.this, start.class);
                startActivity(intent);
            }
        });


        forgetId = (Button) findViewById(R.id.forgetId);
        forgetId.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {// 아이디 찾기 이동
                Intent intent = new Intent(login.this, findId.class);
                startActivity(intent);
            }
        });


        forgetPw = (Button) findViewById(R.id.forgetPw);
        forgetPw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {// 비밀번호 찾기 이동
                Intent intent = new Intent(login.this, findPw.class);
                startActivity(intent);
            }
        });


        start = (ImageButton) findViewById(R.id.finish);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // 로그인 성공시 동작

                // api 연결 필요
                Intent intent = new Intent(login.this, mainDisplay.class);
                startActivity(intent);
            }
        });
    }
}