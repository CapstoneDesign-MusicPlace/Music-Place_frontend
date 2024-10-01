package com.example.musicplace.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.sign.dto.Gender;
import com.example.musicplace.sign.dto.SignInSaveDto;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class join extends AppCompatActivity {
    // 회원가입을 진행하는 페이지

    private ImageButton back_login, finish;
    private Button idCheck;
    private EditText id, pw, repw, email, nickName, name;
    private boolean checkIdState = false;
    private UserApiInterface api;
    private Gender gender;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);

        // RetrofitClient에 TokenManager를 전달
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        setContentView(R.layout.activity_join);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);
        repw = (EditText) findViewById(R.id.repw);
        email = (EditText) findViewById(R.id.email);
        nickName = (EditText) findViewById(R.id.nickName);
        name = (EditText) findViewById(R.id.vidioTitle);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.femaleRadio) {
                    gender = Gender.female;
                    System.out.println("female");
                } else if (checkedId == R.id.maleRadio) {
                    gender = Gender.male;
                    System.out.println("male");
                }
            }
        };
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);


        back_login = (ImageButton) findViewById(R.id.back_login);
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start 화면으로 돌아가기
                finish();
            }
        });



        finish = (ImageButton) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // id 중복 확인을 하지 않으면 동작x    "/sign-in"  (post)
                String userId = String.valueOf(id.getText());
                String passWord = String.valueOf(pw.getText());
                String rePassWord = String.valueOf(repw.getText());
                String userEmail = String.valueOf(email.getText());
                String userNickName = String.valueOf(nickName.getText());
                String userName = String.valueOf(name.getText());

                if(passWord.equals(rePassWord) && checkIdState) {
                    Call<SignInSaveDto> call = api.saveMember(new SignInSaveDto(userId, passWord, userName, userEmail, userNickName, gender));
                    call.enqueue(new Callback<SignInSaveDto>() {
                        @Override
                        public void onResponse(Call<SignInSaveDto> call, Response<SignInSaveDto> response) {

                            if(response.isSuccessful()) {
                                System.out.println("Successful O : " + response.toString());
                            } else {
                                System.out.println("Successful X : " + response.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<SignInSaveDto> call, Throwable t) {
                            System.out.println("onFailure : " + t.getMessage());
                        }
                    });
                    Intent intent = new Intent(join.this, start.class);
                    startActivity(intent);
                }
                else {
                    if(!passWord.equals(rePassWord)) {
                        Toast.makeText(join.this, "pw와 repw의 비밀번호가 동일하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                    if(!checkIdState) {
                        Toast.makeText(join.this, "아이디 중복을 확인해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        idCheck = (Button) findViewById(R.id.idCheck);
        idCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아이디 중복 체크 동작
                String userId = String.valueOf(id.getText());
                Call<Boolean> call = api.SignInCheckSameId(userId);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()) {
                            checkIdState = response.body();
                            System.out.println("Successful : " + checkIdState);
                            if(!checkIdState) {
                                Toast.makeText(join.this, "동일한 아이디가 존재합니다", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(join.this, "사용 가능한 아이디입니다", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            System.out.println("Successful : " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        System.out.println("onFailure : " + t.getMessage());
                    }
                });
            }
        });
    }
}