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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class main extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

                switch (menuItem.getItemId()){
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
                }

                return false;
            }
        });
    }
}