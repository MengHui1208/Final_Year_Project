package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicDonationEvent extends AppCompatActivity {

    RecyclerView publicDERecycleView;
    PublicDonationEventAdapter publicDonationEventAdapter;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_donation_event);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        publicDERecycleView = findViewById(R.id.publicDERecycleView);
        publicDERecycleView.setHasFixedSize(true);
        publicDERecycleView.setLayoutManager(new LinearLayoutManager(this));

        //Set Event Selected
        bottomNavigationView.setSelectedItemId(R.id.event);
        //bottom bar
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.event:
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext()
                                ,PublicNotification.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                ,PublicProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        Query query =firestore.collection("Donation Event")/*.orderBy("timestamp", Query.Direction.DESCENDING)*/;
        FirestoreRecyclerOptions<PublicDonationEventList> options =
                new FirestoreRecyclerOptions.Builder<PublicDonationEventList>()
                        .setQuery(query,PublicDonationEventList.class)
                        .build();

        publicDonationEventAdapter = new PublicDonationEventAdapter(options);
        publicDERecycleView.setAdapter(publicDonationEventAdapter);

        publicDonationEventAdapter.setOnItemClickListener(new PublicDonationEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                PublicDonationEventList eventData = documentSnapshot.toObject(PublicDonationEventList.class);
                String eventId = documentSnapshot.getId();
                Intent intent = new Intent(PublicDonationEvent.this, PublicDonationEventDetail.class);
                intent.putExtra("eventId",eventId);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        publicDonationEventAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        publicDonationEventAdapter.stopListening();
    }
}