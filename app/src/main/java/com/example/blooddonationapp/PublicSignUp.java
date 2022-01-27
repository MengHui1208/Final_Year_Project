package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class PublicSignUp extends AppCompatActivity {
    EditText uname,age,contactNo,address,email,password,confirmPassword;
    RadioGroup radioGroupGender;
    Button btnSignup;
    ProgressBar progressBar;
    TextView loginText;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        uname = findViewById(R.id.uname);
        age = findViewById(R.id.age);
        contactNo = findViewById(R.id.contactNo);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        radioGroupGender = findViewById(R.id.radioGroupGender);

        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btnSignup);
        loginText = findViewById(R.id.loginText);


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PublicLogin.class);
                startActivity(intent);
                finish();
            }
        });

        final Spinner mySpinner = (Spinner) findViewById(R.id.bloodTypeSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(PublicSignUp.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.blood_type_array));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uName = uname.getText().toString();
                final String uAge = age.getText().toString();
                final String uContactNo = contactNo.getText().toString();
                final String uAddress = address.getText().toString();
                final String uEmail = email.getText().toString();
                final String uPassword = password.getText().toString();
                final String uConfirmPwd = confirmPassword.getText().toString();

                int checkedId = radioGroupGender.getCheckedRadioButtonId();
                RadioButton selected_gender = radioGroupGender.findViewById(checkedId);
                final String bloodType = (String) mySpinner.getSelectedItem();

                if (selected_gender == null) {
                    Toast.makeText(PublicSignUp.this, "Please select your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    final String uGender = selected_gender.getText().toString();

                    if (TextUtils.isEmpty(uName)) {
                        uname.setError("Please enter your name.");
                        uname.requestFocus();
                        return;
                    }
                    if (bloodType.trim().equals("--Please select your blood type--")) {
                        Toast.makeText(PublicSignUp.this, "Please select your blood type.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(uAge)) {
                        age.setError("Age is required.");
                        age.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(uContactNo)) {
                        contactNo.setError("Contact number is required.");
                        contactNo.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(uAddress)) {
                        address.setError("State and city is required.");
                        address.requestFocus();
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
                        return;
                    } else {
                        register(uName, uGender, uAge, uContactNo, uAddress, uEmail, uPassword, bloodType);
                    }
                }

            }
        });
    }

    private void register(final String uName, final String uGender, final String uAge, final String uContactNo, final String uAddress, final String uEmail, final String uPassword, final String bloodType) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = mAuth.getCurrentUser();
                    String userId = user.getUid();
                    documentReference = firestore.collection("Public").document(userId);
                    Map<String,Object> hashMap = new HashMap<>();
                    hashMap.put("userId",userId);
                    hashMap.put("fullname",uName);
                    hashMap.put("age",uAge);
                    hashMap.put("gender",uGender);
                    hashMap.put("contact",uContactNo);
                    hashMap.put("address",uAddress);
                    hashMap.put("email",uEmail);
                    hashMap.put("bloodType",bloodType);
                    hashMap.put("imageUrl","default");
                    documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                Toast.makeText(PublicSignUp.this, "User has been sign up successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),PublicLogin.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PublicSignUp.this,"Failed to sign up! Please try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
