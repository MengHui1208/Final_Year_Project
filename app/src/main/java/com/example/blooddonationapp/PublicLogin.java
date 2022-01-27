package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PublicLogin extends AppCompatActivity {
    EditText email,password;
    Button btnLogin;
    TextView txtSignUp;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        txtSignUp = findViewById(R.id.signUpText);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.backToMenu);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirstPage.class);
                startActivity(intent);
                finish();
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PublicSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uEmail = email.getText().toString();
                final String uPassword = password.getText().toString();

                if(TextUtils.isEmpty(uEmail)){
                    email.setError("Please enter your email.");
                    email.requestFocus();
                    return;
                }
                if (!uEmail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                    email.setError("Invalid Email Address! Eg:example@gmail.com");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(uPassword)){
                    password.setError("Please enter your password.");
                    password.requestFocus();
                    return;
                }
                if (uPassword.length() < 6){
                    password.setError("Password must be at least 6 characters!");
                    password.requestFocus();
                    return;
                }
                else{
                    login(uEmail,uPassword);
                }
            }
        });
    }

    private void login(String uEmail, String uPassword) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PublicLogin.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(PublicLogin.this, "Failed to login!Please try again", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}