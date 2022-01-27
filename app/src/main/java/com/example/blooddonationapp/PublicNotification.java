package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PublicNotification extends AppCompatActivity {

    RecyclerView notificationRecycleView;
    PublicNotificationAdapter notificationAdapter;

    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_notification);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.notification);

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
                        startActivity(new Intent(getApplicationContext()
                                ,PublicDonationEvent.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notification:
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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        firestore = FirebaseFirestore.getInstance();

        notificationRecycleView = findViewById(R.id.notificationRecycleView);
        notificationRecycleView.setHasFixedSize(true);
        notificationRecycleView.setLayoutManager(new LinearLayoutManager(this));

        Query query =firestore.collection("Public Notification").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PublicNotificationList> options =
                new FirestoreRecyclerOptions.Builder<PublicNotificationList>()
                        .setQuery(query,PublicNotificationList.class)
                        .build();

        notificationAdapter = new PublicNotificationAdapter(options);
        notificationRecycleView.setAdapter(notificationAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notificationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }
}