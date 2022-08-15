package com.notes.tomdoro.signup;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.notes.tomdoro.HomeActivity;
import com.notes.tomdoro.R;
import com.notes.tomdoro.login.LoginActivity;
import com.notes.tomdoro.model.SignUpData;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    EditText email;
    EditText password;
    EditText userName;
    Button signUp, login;
    ProgressBar signUpLoader;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView loginWithOutAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        userName = findViewById(R.id.userNameET);
        signUp = findViewById(R.id.signUpBTN);
        login = findViewById(R.id.loginBTN);
        loginWithOutAccount = findViewById(R.id.loginWithOutAccount);

        signUpLoader = findViewById(R.id.sign_in_loader);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpScreen = new Intent(SignUpActivity.this, LoginActivity.class);
                SignUpActivity.this.startActivity(signUpScreen);
                SignUpActivity.this.finish();
            }

        });

        loginWithOutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                editor.putString("userID",getSaltString()
                );
                editor.commit() ;
                editor.apply();
                Intent signUpScreen = new Intent(SignUpActivity.this, HomeActivity.class);

                SignUpActivity.this.startActivity(signUpScreen);
                SignUpActivity.this.finish();

            }

        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpLoader.setVisibility(View.VISIBLE);

                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || userName.getText().toString().isEmpty()) {
                    signUpLoader.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Please enter all data", Toast.LENGTH_LONG).show();
                } else if (password.getText().toString().length() < 6) {
                    signUpLoader.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Password must be more than 6 characters", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        myRef.child("Users").child(mAuth.getCurrentUser().getUid())
                                                .setValue(new SignUpData(userName.getText().toString(), email.getText().toString(), mAuth.getCurrentUser().getUid()))
                                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        editor.putString("userID", mAuth.getCurrentUser().getUid());
                                                        editor.putString("name", userName.getText().toString());
                                                        editor.putString("email", email.getText().toString());

                                                        editor.commit();
                                                        signUpLoader.setVisibility(View.GONE);
                                                        Toast.makeText(SignUpActivity.this, "Sign Up successfully.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        SignUpActivity.this.finish();
                                                    }
                                                });

                                    } else {
                                        signUpLoader.setVisibility(View.GONE);
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                            Toast.makeText(getApplicationContext(), "you are already user", Toast.LENGTH_SHORT).show();
                                        else if (task.getException().getMessage().equals("The email address is badly formatted.")) {
                                            Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "some thing wrong please try again", Toast.LENGTH_SHORT).show();
                                        }
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