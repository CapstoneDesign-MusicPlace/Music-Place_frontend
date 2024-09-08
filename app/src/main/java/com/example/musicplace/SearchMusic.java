package com.example.musicplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.dto.FriendItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Map;

public class SearchMusic extends AppCompatActivity {
    // 로그인 이후 가장 처음으로 나오는 페이지(실질적으로 main 페이지가 여기이다)
    private BottomNavigationView bottomNavigationView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_music);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /* initiate adapter */
        MyRecyclerAdapter mRecyclerAdapter = new MyRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        /* adapt data */
        ArrayList<FriendItem> mfriendItems = new ArrayList<>();
        for(int i=1;i<=10;i++){
            if(i%2==0)
                mfriendItems.add(new FriendItem(R.drawable.menu_person,i+"번째 사람",i+"번째 상태메시지"));
            else
                mfriendItems.add(new FriendItem(R.drawable.menu_person,i+"번째 사람",i+"번째 상태메시지"));

        }
        mRecyclerAdapter.setFriendList(mfriendItems);




        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

                /*switch (menuItem.getItemId()){
                    case R.id.home:
                        intent = new Intent(main.this, InputAllergy.class);
                        startActivity(intent);
                        return true;

                    case R.id.search:
                        intent = new Intent(main.this, AllergyFoodListInfo.class);
                        startActivity(intent);
                        return true;

                    case R.id.headset:
                        intent = new Intent(main.this, Map.class);
                        startActivity(intent);
                        return true;

                    case R.id.person:
                        intent = new Intent(main.this, UserInformation.class);
                        startActivity(intent);
                        return true;

                    case R.id.add:
                        intent = new Intent(main.this, UserInformation.class);
                        startActivity(intent);
                        return true;
                }*/

                return false;
            }
        });


    }



}