package com.notes.tomdoro.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.notes.tomdoro.HomeActivity;
import com.notes.tomdoro.R;
import com.notes.tomdoro.signup.SignUpActivity;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signIn, signUp;
    private FirebaseAuth mAuth;
    ProgressBar signInProgress;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView loginWithOutAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        signIn = findViewById(R.id.loginBTN);
        signUp = findViewById(R.id.signUpBTN);
        signUp = findViewById(R.id.signUpBTN);
        loginWithOutAccount = findViewById(R.id.loginWithOutAccount);
        mAuth = FirebaseAuth.getInstance();
        signInProgress = findViewById(R.id.sign_in_loader);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpScreen = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(signUpScreen);
                LoginActivity.this.finish();
            }

        });
        loginWithOutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("userID", getSaltString());
                editor.commit() ;
                editor.apply();
                Intent signUpScreen = new Intent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.startActivity(signUpScreen);

                LoginActivity.this.finish();

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "enter your mail & password", Toast.LENGTH_SHORT).show();
                } else {
                    signInProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                editor.putString("userID", mAuth.getCurrentUser().getUid());
                                                editor.putString("name", dataSnapshot.child("name").getValue().toString());
                                                editor.putString("email", dataSnapshot.child("email").getValue().toString());
                                                editor.commit();
                                                signInProgress.setVisibility(View.GONE);
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                Toast.makeText(LoginActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }

                                        });
                                    } else {
                                        signInProgress.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "wrong data cheek your email & pasSword", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
