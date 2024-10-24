package com.example.musicplace.profile.layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplace.R;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.profile.dto.SignInUpdateDto;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {
    private Intent intent;
    private UserApiInterface api;
    private Button saveButton, cancelButton, changeImagButton;
    private ImageView editImageView;
    private EditText idEditText, pwEditText, emailEditText, nickNameEditText, nameEditText;
    private String name, email, nickname, profile_img_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        nickname = intent.getStringExtra("nickname");
        profile_img_url = intent.getStringExtra("profile_img_url");

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        nickNameEditText = (EditText) findViewById(R.id.nickNameEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        editImageView = (ImageView) findViewById(R.id.editImageView);
        changeImagButton = (Button) findViewById(R.id.changeImagButton);


        emailEditText.setText(email);
        nickNameEditText.setText(nickname);
        nameEditText.setText(name);

        if (profile_img_url != null && !profile_img_url.isEmpty()) {
            editImageView.setImageResource(Integer.parseInt(profile_img_url));
        } else {
            editImageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }


        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            // 수정사항 저장
            @Override
            public void onClick(View view) {
                String getNewEmail = String.valueOf(emailEditText.getText());
                String getNewImage = Arrays.toString(profile_img_url.getBytes());
                String getNewName = String.valueOf(nameEditText.getText());
                String getNewNickName = String.valueOf(nickNameEditText.getText());

                SignInUpdateDto signInUpdateDto = new SignInUpdateDto(getNewEmail, getNewImage, getNewName, getNewNickName);
                SignInUpdate(signInUpdateDto);
            }
        });


        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            // 뒤로가기
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            Uri uri = intent.getData();
                        }
                    }
                }
        );

        changeImagButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {//갤러리 호출
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                activityResultLauncher.launch(intent);
            }

        }) ;





    }

    private void SignInUpdate(SignInUpdateDto signInUpdateDto) {
        api.SignInUpdate(signInUpdateDto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfile.this, "유저 정보를 갱신했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("SignInUpdate 실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProfile.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}