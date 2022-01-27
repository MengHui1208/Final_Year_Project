package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminSignUp extends AppCompatActivity {
    EditText uname,workplace,address,contactNo,email,password,confirmPassword;
    TextView loginText;
    Button btnSignup;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        uname = findViewById(R.id.uname);
        workplace = findViewById(R.id.workplace);
        address = findViewById(R.id.address);
        contactNo = findViewById(R.id.contactNo);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btnSignup);
        loginText = findViewById(R.id.loginText);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AdminLogin.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uName = uname.getText().toString();
                final String uWorkplace = workplace.getText().toString();
                final String uAddress = address.getText().toString();
                final String uContact = contactNo.getText().toString();
                final String uEmail = email.getText().toString();
                final String uPassword = password.getText().toString();
                final String uConfirmPwd = confirmPassword.getText().toString();

                if (TextUtils.isEmpty(uName)) {
                    uname.setError("Full name is required.");
                    uname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(uWorkplace)) {
                    workplace.setError("Workplace name is required.");
                    workplace.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(uAddress)) {
                    address.setError("Address is required.");
                    address.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(uContact)) {
                    contactNo.setError("Contact number is required.");
                    contactNo.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(uEmail)) {
                    email.setError("Email is required.");
                    email.requestFocus();
                    return;
                }
                if (!uEmail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    email.setError("Invalid Email Address! Eg: example@gmail.com");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(uPassword)) {
                    password.setError("Password is required.");
                    password.requestFocus();
                    return;
                }
                if (uPassword.length() < 6) {
                    password.setError("Password must be at least 6 characters.");
                    password.requestFocus();
                    return;
                }
                if (!uPassword.equals(uConfirmPwd)) {
                    confirmPassword.setError("Password not matching!");
                    confirmPassword.requestFocus();
                }
                else {
                    register(uName, uWorkplace, uAddress, uContact, uEmail, uPassword);
                }

            }
        });
    }

    private void register(final String uName, final String uWorkplace, final String uAddress, final String uContact, final String uEmail, final String uPassword) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mUser = mAuth.getCurrentUser();
                    String adminId = mUser.getUid();
                    documentReference = firestore.collection("Hospital Administrator").document(adminId);
                    Map<String,Object> hashMap = new HashMap<>();
                    hashMap.put("adminId",adminId);
                    hashMap.put("name",uName);
                    hashMap.put("workplace",uWorkplace);
                    hashMap.put("address",uAddress);
                    hashMap.put("contact",uContact);
                    hashMap.put("email",uEmail);
                    hashMap.put("imageUrl","default");
                    documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                Toast.makeText(AdminSignUp.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),AdminLogin.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(AdminSignUp.this, "Failed to register! Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminSignUp.this, "Failed to register! User has been registered before.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}