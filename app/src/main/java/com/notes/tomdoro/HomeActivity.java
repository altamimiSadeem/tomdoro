package com.notes.tomdoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.notes.tomdoro.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {
    ImageView calederIV;
    ImageView noteIV, todoIV, timerIV, aboutIV, logoutIV;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();

        calederIV = findViewById(R.id.imageButton3);
        noteIV = findViewById(R.id.imageButton4);
        todoIV = findViewById(R.id.imageButton5);
        timerIV = findViewById(R.id.tomdoroTimer);
        aboutIV = findViewById(R.id.imageView3);
        logoutIV = findViewById(R.id.imageView18);


        aboutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calenderIntent = new Intent(HomeActivity.this, InfoActivity.class);
                HomeActivity.this.startActivity(calenderIntent);
            }
        });


        timerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calenderIntent = new Intent(HomeActivity.this, TimerActivity.class);
                HomeActivity.this.startActivity(calenderIntent);
            }
        });

        calederIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calenderIntent = new Intent(HomeActivity.this, CalenderActivity.class);
                HomeActivity.this.startActivity(calenderIntent);
            }
        });

        noteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteIntent = new Intent(HomeActivity.this, NoteActivity.class);
                HomeActivity.this.startActivity(noteIntent);
            }
        });

        todoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteIntent = new Intent(HomeActivity.this, TodoListActivity.class);
                HomeActivity.this.startActivity(noteIntent);
            }
        });

        logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteIntent = new Intent(HomeActivity.this, LoginActivity.class);
                HomeActivity.this.startActivity(noteIntent);
                HomeActivity.this.finish();
                editor.clear();
                editor.commit() ;
                editor.apply();
            }
        });
    }
}
