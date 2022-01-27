package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AdminProfileEdit extends AppCompatActivity {
    EditText editTextFullname,editTextWorkplace,editTextContact,editTextAddress,editTextEmail;
    Button btnUpdate;
    ImageView backToMenu;

    AdminData adminData;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_edit);

        editTextFullname = findViewById(R.id.editTextFullname);
        editTextWorkplace = findViewById(R.id.editTextWorkplace);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextContact = findViewById(R.id.editTextContact);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        backToMenu = findViewById(R.id.backToMenu);


        //back to previous page
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminProfile.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        String userId = mUser.getUid();
        documentReference = firestore.collection("Hospital Administrator").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                adminData = value.toObject(AdminData.class);
                assert adminData != null;
                editTextFullname.setText(adminData.getName());
                editTextWorkplace.setText(adminData.getWorkplace());
                editTextContact.setText(adminData.getContact());
                editTextAddress.setText(adminData.getAddress());
                editTextEmail.setText(adminData.getEmail());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editTextFullname.getText().toString();
                final String workplace = editTextWorkplace.getText().toString();
                final String contact = editTextContact.getText().toString();
                final String address = editTextAddress.getText().toString();
                final String email = editTextEmail.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    editTextFullname.setError("Name is required.");
                    editTextFullname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(workplace)) {
                    editTextWorkplace.setError("Workplace name is required.");
                    editTextWorkplace.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    editTextAddress.setError("Workplace's address is required.");
                    editTextAddress.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(contact)) {
                    editTextContact.setError("Contact number is required.");
                    editTextContact.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email is required.");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    editTextEmail.setError("Invalid Email Address! Eg: example@gmail.com");
                    editTextEmail.requestFocus();
                    return;
                } else{
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    final String userId = mUser.getUid();
                    mUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            documentReference = firestore.collection("Hospital Administrator").document(userId);
                            Map<String,Object> edited = new HashMap<>();
                            edited.put("email",email);
                            documentReference.update(edited);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminProfileEdit.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                    documentReference = firestore.collection("Hospital Administrator").document(userId);
                    Map<String,Object> edited = new HashMap<>();
                    edited.put("name",name);
                    edited.put("workplace",workplace);
                    edited.put("contact",contact);
                    edited.put("address",address);
                    edited.put("email",email);
                    documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminProfileEdit.this,"Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AdminProfile.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}