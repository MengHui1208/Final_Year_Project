package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

public class PublicProfileEdit extends AppCompatActivity {
    EditText txtEditName,txtEditAge,txtEditContact,txtAddress,txtEditEmail;
    TextView txtGenderSelected,txtBloodTypeSelected;
    RadioGroup radioGroupGender;
    ImageView backToMenu;

    Button btnUpdate;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    PublicData publicData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile_edit);

        txtEditName = findViewById(R.id.txtEditName);
        txtEditAge = findViewById(R.id.txtEditAge);
        txtEditContact = findViewById(R.id.txtEditContact);
        txtAddress = findViewById(R.id.editTextAddress);
        txtEditEmail = findViewById(R.id.txtEditEmail);
        txtGenderSelected = findViewById(R.id.txtGenderSelected);
        txtBloodTypeSelected = findViewById(R.id.txtBloodTypeSelected);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        btnUpdate = findViewById(R.id.btnUpdate);
        backToMenu = findViewById(R.id.backToMenu);

        final Spinner mySpinner = (Spinner) findViewById(R.id.bloodTypeSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(PublicProfileEdit.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.blood_type_array));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        //retrieve data
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        String userId = mUser.getUid();
        documentReference = firestore.collection("Public").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                publicData = value.toObject(PublicData.class);
                assert publicData != null;
                txtEditName.setText(publicData.getFullname());
                txtGenderSelected.setText(publicData.getGender());
                txtEditAge.setText(publicData.getAge());
                txtBloodTypeSelected.setText(publicData.getBloodType());
                txtEditContact.setText(publicData.getContact());
                txtAddress.setText(publicData.getAddress());
                txtEditEmail.setText(publicData.getEmail());
            }
        });

        //back to previous page
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PublicProfile.class);
                startActivity(intent);
                finish();
            }
        });

        //update data
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname = txtEditName.getText().toString();
                final String age = txtEditAge.getText().toString();
                final String contact = txtEditContact.getText().toString();
                final String address = txtAddress.getText().toString();
                final String email = txtEditEmail.getText().toString();
                //radio
                int checkedId = radioGroupGender.getCheckedRadioButtonId();
                RadioButton selected_gender = radioGroupGender.findViewById(checkedId);
                //selection
                final String bloodType = (String) mySpinner.getSelectedItem();

                if (selected_gender == null){
                    Toast.makeText(PublicProfileEdit.this, "lease select your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    final String gender = selected_gender.getText().toString();
                    if (TextUtils.isEmpty(fullname)) {
                        txtEditName.setError("Full name is required.");
                        txtEditName.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(age)) {
                        txtEditAge.setError("Age is required.");
                        txtEditAge.requestFocus();
                        return;
                    }
                    if (bloodType.trim().equals("--Please select your blood type--")) {
                        Toast.makeText(PublicProfileEdit.this, "Please select your blood type.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(contact)) {
                        txtEditContact.setError("Contact no. is required.");
                        txtEditContact.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        txtAddress.setError("City and state is required.");
                        txtAddress.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        txtEditEmail.setError("Email is required.");
                        txtEditEmail.requestFocus();
                        return;
                    }
                    if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        txtEditEmail.setError("Invalid Email Address! Eg: example@gmail.com");
                        txtEditEmail.requestFocus();
                        return;
                    }
                    else {
                        updateProfile(fullname, gender, age, contact, address, email,bloodType);
                    }
                }
            }
        });
    }

    private void updateProfile(final String fullname, final String gender, final String age, final String contact, final String address, final String email, final String bloodType) {
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String userId = mUser.getUid();
        mUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                documentReference = firestore.collection("Public").document(userId);
                Map<String,Object> edited = new HashMap<>();
                edited.put("email",email);
                documentReference.update(edited);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PublicProfileEdit.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        documentReference = firestore.collection("Public").document(userId);
        Map<String,Object> edited = new HashMap<>();
        edited.put("fullname",fullname);
        edited.put("gender",gender);
        edited.put("age",age);
        edited.put("contact",contact);
        edited.put("address",address);
        edited.put("email",email);
        edited.put("bloodType",bloodType);
        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PublicProfileEdit.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PublicProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }
}