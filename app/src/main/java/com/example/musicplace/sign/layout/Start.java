package com.example.musicplace.sign.layout;

import android.content.Intent;
import android.os.Bundle;



import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.global.retrofit.UserApiInterface;


public class Start extends AppCompatActivity {

    private Button join, login;

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


        // 회원가입 화면 이동
        join = findViewById(R.id.join);
        join.setOnClickListener(view -> {
            Intent intent = new Intent(Start.this, Join.class);
            startActivity(intent);

        });


        // 로그인 화면 이동
        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(Start.this, Login.class);
            startActivity(intent);
        });


    }


}
