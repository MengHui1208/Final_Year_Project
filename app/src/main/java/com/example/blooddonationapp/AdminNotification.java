package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminNotification extends AppCompatActivity {

    RecyclerView notificationRecycleView;
    AdminNotificationAdapter notificationAdapter;

    FirebaseFirestore firestore;

    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation2);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.notification);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity2.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notification:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                ,AdminProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        firestore = FirebaseFirestore.getInstance();

        notificationRecycleView = findViewById(R.id.notificationRecycleView);
        notificationRecycleView.setHasFixedSize(true);
        notificationRecycleView.setLayoutManager(new LinearLayoutManager(this));

        Query query =firestore.collection("Admin Notification").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<AdminNotificationList> options =
                new FirestoreRecyclerOptions.Builder<AdminNotificationList>()
                        .setQuery(query,AdminNotificationList.class)
                        .build();

        notificationAdapter = new AdminNotificationAdapter(options);
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