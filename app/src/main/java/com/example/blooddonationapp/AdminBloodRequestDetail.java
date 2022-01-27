package com.example.blooddonationapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminBloodRequestDetail extends AppCompatActivity {
    TextView recipientName,date,bloodType,amount,venue,contact,additionalInfo,status;
    ImageView back;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    BloodRequestList requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_blood_request_approved_detail);

        recipientName = findViewById(R.id.editTextRecipientName);
        date = findViewById(R.id.editTextDate);
        bloodType = findViewById(R.id.editTextBloodType);
        amount = findViewById(R.id.editTextAmount);
        venue = findViewById(R.id.editTextVenue);
        contact = findViewById(R.id.editTextContact);
        additionalInfo = findViewById(R.id.editTextAdditionalInfo);
        status  =findViewById(R.id.status);
        back = findViewById(R.id.backToMenu);
        firestore = FirebaseFirestore.getInstance();

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });

        final String requestId = getIntent().getStringExtra("requestId");
        documentReference = firestore.collection("Blood Request").document(requestId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    requestList = value.toObject(BloodRequestList.class);
                    assert requestList != null;
                    recipientName.setText(requestList.getRecipientName());
                    bloodType.setText(requestList.getBloodType());
                    amount.setText(requestList.getAmount());
                    venue.setText(requestList.getVenue());
                    contact.setText(requestList.getContact());
                    additionalInfo.setText(requestList.getAdditionalInfo());

                    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    //https://stackoverflow.com/questions/59908720/how-to-get-server-timestamp-from-firestore-in-an-android-device
                    Date creationDate = value.getDate("timestamp");
                    String dateString = dateFormat.format(creationDate);
                    date.setText(String.valueOf(dateString));
                }
            }
        });
    }
}