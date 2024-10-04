package com.example.musicplace.playlist.layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.example.musicplace.playlist.dto.OnOff;
import com.example.musicplace.playlist.dto.PLSaveDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlaylist extends AppCompatActivity {

    private EditText editTextTitle, commentEditText;
    private RadioGroup radioGroup;
    private Button saveButton, cancelButton, addImgButton;
    private ImageView imageView;
    private UserApiInterface api;
    private Intent intent;
    private static final int GALLERY_REQUEST_CODE = 1; // 갤러리 요청 코드
    private Uri selectedImageUri; // 선택한 이미지 URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        editTextTitle = findViewById(R.id.editTextTitle);
        commentEditText = findViewById(R.id.commentEditText);
        radioGroup = findViewById(R.id.radioGroup);
        imageView = findViewById(R.id.imageView); // 이미지뷰 초기화

        addImgButton = findViewById(R.id.addImgButton);
        addImgButton.setOnClickListener(view -> openGallery());

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            String title = String.valueOf(editTextTitle.getText());
            String comment = String.valueOf(commentEditText.getText());
            OnOff onOff;
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String image;
            if (selectedImageUri != null) {
                image = selectedImageUri.toString(); // 선택한 이미지 URI
            } else {
                image = "android.resource://com.example.musicplace/drawable/playlistimg"; // 기본 이미지 URI (drawable 리소스 경로)
            }
            if (selectedId != -1) {
                if(selectedId == 1) {
                    onOff = OnOff.Public;
                }
                else {onOff = OnOff.Private;}
            } else {
                Toast.makeText(AddPlaylist.this, "공개/비공개 상태를 선택하세요.", Toast.LENGTH_SHORT).show();
                return; // 선택되지 않은 경우 실행 중단
            }


            PLSaveDto plSaveDto = new PLSaveDto(title, onOff, image, comment);
            addPlaylistData(plSaveDto);

        });

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> finish());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // 모든 이미지 파일을 선택 가능하게 설정
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE); // 갤러리 액티비티 시작
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // 선택한 이미지 URI 가져오기
            imageView.setImageURI(selectedImageUri); // 이미지뷰에 선택한 이미지 설정
        }
    }

    private void addPlaylistData(PLSaveDto plSaveDto) {

        api.PLSave(plSaveDto).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddPlaylist.this, plSaveDto.getTitle()+"가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();  // 액티비티 종료
                } else {
                    Toast.makeText(AddPlaylist.this, plSaveDto.getTitle()+"가 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(AddPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
