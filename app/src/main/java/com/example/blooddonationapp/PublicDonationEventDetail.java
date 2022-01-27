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

public class PublicDonationEventDetail extends AppCompatActivity {
    TextView eventName, startDate, endDate, venue, eventDetails;
    ImageView back;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    PublicDonationEventList eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_donation_event_detail);

        eventName = findViewById(R.id.editTextEventName);
        startDate = findViewById(R.id.editTextStartDate);
        endDate = findViewById(R.id.editTextEndDate);
        venue = findViewById(R.id.editTextVenue);
        eventDetails = findViewById(R.id.editTextEventDetails);
        back = findViewById(R.id.back);
        firestore = FirebaseFirestore.getInstance();

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicDonationEvent.class);
                startActivity(intent);
                finish();
            }
        });

        String eventId = getIntent().getStringExtra("eventId");
        documentReference = firestore.collection("Donation Event").document(eventId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    eventData = value.toObject(PublicDonationEventList.class);
                    assert eventData != null;
                    eventName.setText(eventData.getEventName());
                    startDate.setText(eventData.getStartDate());
                    endDate.setText(eventData.getEndDate());
                    venue.setText(eventData.getVenue());
                    eventDetails.setText(eventData.getEventDetails());
                }
            }
        });
    }
}