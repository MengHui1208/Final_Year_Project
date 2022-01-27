package com.example.blooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminDonationEvent extends AppCompatActivity {
    ImageView btnCreate, back;

    AdminData adminData;
    RecyclerView donationEventRecycleView;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    AdminDonationEventAdapter adminDonationEventAdapter;
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_donation_event);

        back = findViewById(R.id.backToMenu);
        btnCreate = findViewById(R.id.btnCreate);

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminCreateDonationEvent.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        firestore = FirebaseFirestore.getInstance();

        donationEventRecycleView = findViewById(R.id.donationEventRecycleView);
        donationEventRecycleView.setHasFixedSize(true);
        donationEventRecycleView.setLayoutManager(new LinearLayoutManager(this));

        Query query = firestore.collection("Hospital Administrator").document(userId).collection("Donation Event")
                .orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<AdminDonationEventList> options =
                new FirestoreRecyclerOptions.Builder<AdminDonationEventList>()
                        .setQuery(query, AdminDonationEventList.class)
                        .build();

        adminDonationEventAdapter = new AdminDonationEventAdapter(options);
        donationEventRecycleView.setAdapter(adminDonationEventAdapter);

        //click to view details
        adminDonationEventAdapter.setOnItemClickListener(new AdminDonationEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                AdminDonationEventList eventData = documentSnapshot.toObject(AdminDonationEventList.class);
                String eventId = documentSnapshot.getId();
                Intent intent = new Intent(AdminDonationEvent.this, AdminDonationEventDetail.class);
                intent.putExtra("eventId",eventId);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adminDonationEventAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adminDonationEventAdapter.stopListening();
    }
}