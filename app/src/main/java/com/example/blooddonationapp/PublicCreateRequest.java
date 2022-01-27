package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PublicCreateRequest extends AppCompatActivity {

    ImageView backToMenu;
    EditText recipientName, amount, venue, contact,additionalInfo;
    Button btnAdd;

    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_create_request);

        backToMenu = findViewById(R.id.backToMenu);
        recipientName = findViewById(R.id.txtRecipientName);
        amount = findViewById(R.id.txtAmount);
        contact = findViewById(R.id.txtContact);
        venue = findViewById(R.id.txtVenue);
        additionalInfo = findViewById(R.id.txtAdditionalInfo);
        btnAdd = findViewById(R.id.btnAdd);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        final Spinner mySpinner = (Spinner) findViewById(R.id.bloodTypeSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(PublicCreateRequest.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.blood_type_array));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        //Back to main page
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });

        //Add request
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mName = recipientName.getText().toString();
                final String bloodType = (String) mySpinner.getSelectedItem();
                final String mAmount = amount.getText().toString();
                final String mContact = contact.getText().toString();
                final String mVenue = venue.getText().toString();
                final String mAddInfo = additionalInfo.getText().toString();

                if (TextUtils.isEmpty(mName)) {
                    recipientName.setError("Recipient's name is required.");
                    recipientName.requestFocus();
                    return;
                }
                if (bloodType.trim().equals("--Please select your blood type--")) {
                    Toast.makeText(PublicCreateRequest.this, "Please select your blood type.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mAmount)) {
                    amount.setError("Amount of blood donated is required.");
                    amount.requestFocus();
                    return;

                }
                if (TextUtils.isEmpty(mVenue)) {
                    venue.setError("Venue is required.");
                    venue.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mContact)) {
                    contact.setError("Name of the approval is required.");
                    contact.requestFocus();
                    return;
                } else {
                    add(mName, bloodType, mAmount, mVenue, mContact, mAddInfo);
                }
            }

        });

    }

    private void add(String mName, String bloodType, String mAmount, String mVenue, String mContact, String mAddInfo) {
        String userId = mUser.getUid();
        String requestId = firestore.collection("Public").document(userId).collection("Blood Request").document().getId();
        documentReference = firestore.collection("Public").document(userId).collection("Blood Request").document(requestId);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("publicId",userId);
        hashMap.put("requestId",requestId);
        hashMap.put("recipientName", mName);
        hashMap.put("bloodType", bloodType);
        hashMap.put("amount", mAmount);
        hashMap.put("contact", mContact);
        hashMap.put("venue", mVenue);
        hashMap.put("additionalInfo", mAddInfo);
        hashMap.put("status","Pending");
        hashMap.put("timestamp", FieldValue.serverTimestamp());
        documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
        documentReference = firestore.collection("Blood Request").document(requestId);
        Map<String, Object> request = new HashMap<>();
        String publicId = mUser.getUid();
        request.put("publicId",publicId);
        request.put("requestId",requestId);
        request.put("recipientName", mName);
        request.put("bloodType", bloodType);
        request.put("amount", mAmount);
        request.put("contact", mContact);
        request.put("venue", mVenue);
        request.put("additionalInfo", mAddInfo);
        request.put("status","Pending");
        request.put("timestamp",FieldValue.serverTimestamp());
        documentReference.set(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PublicCreateRequest.this,"The request is added successfully!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),PublicBloodRequest.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PublicCreateRequest.this,"Failed to add new request!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        String notificationId = firestore.collection("Admin Notification").document().getId();
        documentReference = firestore.collection("Admin Notification").document(notificationId);
        Map<String, Object> noti = new HashMap<>();
        noti.put("timestamp",FieldValue.serverTimestamp());
        noti.put("notificationId",notificationId);
        noti.put("content","There is a new blood request has been added. Please review the request on the Blood Request Page.");
        noti.put("name","Recipient Name: "+mName);
        documentReference.set(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //nothing
            }
        });
    }


}