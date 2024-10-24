package com.example.musicplace.playlist.layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplace.R;
import com.example.musicplace.global.retrofit.RetrofitClient;
import com.example.musicplace.global.retrofit.UserApiInterface;
import com.example.musicplace.global.token.TokenManager;
import com.example.musicplace.playlist.adapter.EditMusicRecyclerAdapter;
import com.example.musicplace.playlist.dto.EditMusicDto;
import com.example.musicplace.playlist.dto.OnOff;
import com.example.musicplace.playlist.dto.PLUpdateDto;
import com.example.musicplace.playlist.dto.ResponseMusicDto;




import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPlaylist extends AppCompatActivity {
    // 플레이리스트를 편집하는 페이지
    private Intent intent;
    private UserApiInterface api;
    private EditText editTextTitle, editTextText;
    private ImageView editImageView;
    private RadioGroup radioGroup;
    private Button saveButton, cancelButton;
    private static final int GALLERY_REQUEST_CODE = 1; // 갤러리 요청 코드
    private Uri selectedImageUri; // 선택한 이미지 URI
    private RecyclerView recyclerView;
    private EditMusicRecyclerAdapter editMusicRecyclerAdapter;

    private List<ResponseMusicDto> musicListDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TokenManager 생성
        TokenManager tokenManager = new TokenManager(this);
        api = RetrofitClient.getRetrofit(tokenManager).create(UserApiInterface.class);

        // 뮤직 리사이클뷰와 어뎁터 설정
        recyclerView = findViewById(R.id.recyclerView);
        editMusicRecyclerAdapter = new EditMusicRecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(editMusicRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Intent로 전달받은 데이터 수신
        intent = getIntent();
        Long playlistId = intent.getLongExtra("playlistId",1L);
        String playlistTitle = intent.getStringExtra("playlistTitle");
        String nickname = intent.getStringExtra("nickname");
        String imageUrl = intent.getStringExtra("imageUrl");
        String onOff =intent.getStringExtra("onoff");
        String comment = intent.getStringExtra("comment");

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextText = findViewById(R.id.editTextText);

        editTextTitle.setText(playlistTitle);
        editTextText.setText(comment);

        loadPlaylistData(playlistId);


        // ImageView 초기화
        editImageView = findViewById(R.id.editImageView);

        // Glide를 사용하여 이미지 로드
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .error(R.drawable.playlistimg)
                    .into(editImageView);
        }

        radioGroup = findViewById(R.id.radioGroup);
        RadioButton publicRadioButton = findViewById(R.id.publicRadioButton);
        RadioButton privateRadioButton = findViewById(R.id.privateRadioButton);

        // onOff 값을 기반으로 RadioButton 선택 설정
        if ("Public".equals(onOff)) {
            publicRadioButton.setChecked(true);  // 공개일 경우
        } else if ("Private".equals(onOff)) {
            privateRadioButton.setChecked(true); // 비공개일 경우
        }

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = String.valueOf(editTextTitle.getText());
                String comment = String.valueOf(editTextText.getText());
                OnOff onOff = null;
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String image;
                if (selectedImageUri != null) {
                    image = selectedImageUri.toString(); // 선택한 이미지 URI
                } else {
                    image = "android.resource://com.example.musicplace/drawable/playlistimg"; // 기본 이미지 URI (drawable 리소스 경로)
                }
                if (selectedId != -1) {
                    if (selectedId == R.id.publicRadioButton) {
                        onOff = OnOff.Public;
                    } else if (selectedId == R.id.privateRadioButton) {
                        onOff = OnOff.Private;
                    }
                } else {
                    Toast.makeText(EditPlaylist.this, "공개/비공개 상태를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return; // 선택되지 않은 경우 실행 중단
                }

                List<Long> selectedMusicIds = editMusicRecyclerAdapter.getSelectedMusicIds();
                deleteSelectedMusic(playlistId, selectedMusicIds);

                PLUpdateDto plUpdateDto = new PLUpdateDto(title, onOff, image, comment);
                updatePlaylistData(playlistId, plUpdateDto);



            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
            editImageView.setImageURI(selectedImageUri); // 이미지뷰에 선택한 이미지 설정
        }
    }

    private void updatePlaylistData(Long pl_id, PLUpdateDto plUpdateDto) {

        api.PLUpdate(pl_id, plUpdateDto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPlaylist.this,plUpdateDto.getTitle() +"가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);

                    finish();  // 액티비티 종료
                } else {
                    Toast.makeText(EditPlaylist.this,plUpdateDto.getTitle() +"가 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlaylistData(Long PLId) {
        api.MusicFindAll(PLId).enqueue(new Callback<List<ResponseMusicDto>>() {
            @Override
            public void onResponse(Call<List<ResponseMusicDto>> call, Response<List<ResponseMusicDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    musicListDto = response.body();

                    // ResponseMusicDto 리스트를 YoutubeItem 리스트로 변환
                    ArrayList<EditMusicDto> musicDtoArrayList = new ArrayList<>();
                    for (ResponseMusicDto musicDto : musicListDto) {
                        // 이미지 URL이 null일 경우 기본 이미지 설정
                        EditMusicDto MusicItem = new EditMusicDto(musicDto.getMusic_id(), musicDto.getVidioTitle(), musicDto.getVidioImage());
                        musicDtoArrayList.add(MusicItem);
                    }

                    // 어댑터에 데이터를 설정하고 갱신
                    editMusicRecyclerAdapter.setEditMusicItems(musicDtoArrayList);
                } else {
                    Toast.makeText(EditPlaylist.this, "플레이리스트의 음악 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseMusicDto>> call, Throwable t) {
                Toast.makeText(EditPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedMusic(Long playlistId, List<Long> selectedMusicIds) {
        api.MusicDelete(playlistId, selectedMusicIds).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    System.out.println("선택한 음악이 삭제되었습니다.");
                    setResult(RESULT_OK);
                    finish(); // 액티비티 종료
                } else {
                    try {
                        if (response.errorBody() != null) {
                            // 에러 메시지를 출력
                            String errorMessage = response.errorBody().string();  // 에러 내용 가져오기
                            System.out.println("===========response.message()==============" + response.message() + "======errorBody======" + errorMessage);
                        } else {
                            System.out.println("===========response.message()==============" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();  // 에러 발생 시 예외 출력
                    }
                    Toast.makeText(EditPlaylist.this, "음악 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(EditPlaylist.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}