package com.notes.tomdoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.notes.tomdoro.login.LoginActivity;

public class splashscreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences("myPref", MODE_PRIVATE);

                Log.e("fdssdf" ,prefs.getString("userID", "null") ) ;
                if (prefs.getString("userID", "null").toString().equals("null")) {
                    Intent mainIntent = new Intent(splashscreen.this, LoginActivity.class);
                    splashscreen.this.startActivity(mainIntent);
                    splashscreen.this.finish();
                } else {
                    Intent mainIntent = new Intent(splashscreen.this, HomeActivity.class);
                    splashscreen.this.startActivity(mainIntent);
                    splashscreen.this.finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }


}
