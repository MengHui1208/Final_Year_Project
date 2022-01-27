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

public class PublicOtherRequestDetail extends AppCompatActivity {

    TextView recipientName,date,bloodType,amount,venue,contact,additionalInfo;
    ImageView back;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    BloodRequestList requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_other_request_detail);

        recipientName = findViewById(R.id.txtRecipientName);
        date = findViewById(R.id.txtDate);
        bloodType = findViewById(R.id.txtBloodType);
        amount = findViewById(R.id.txtAmount);
        venue = findViewById(R.id.txtVenue);
        contact = findViewById(R.id.txtContact);
        additionalInfo = findViewById(R.id.txtAdditionalInfo);
        back = findViewById(R.id.backToMenu);
        firestore = FirebaseFirestore.getInstance();

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
                    Date creationDate = value.getDate("timestamp");
                    String dateString = dateFormat.format(creationDate);
                    date.setText(String.valueOf(dateString));
                }
            }
        });

        //Back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicOtherRequest.class);
                startActivity(intent);
                finish();
            }
        });

    }
}