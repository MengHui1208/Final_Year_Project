package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity {
    TextView name, workplace, searchDonor, bloodRequest, donationEvent;
    ImageView searchDonorImg, bloodRequestImg,donationEventImg;
    CircleImageView circleImageView;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    AdminData adminData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation2);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //TopLayout of Main Page
        name = findViewById(R.id.name);
        workplace = findViewById(R.id.workplace);
        //Option
        searchDonor = findViewById(R.id.searchDonorText);
        searchDonorImg = findViewById(R.id.searchDonorImage);
        bloodRequest = findViewById(R.id.bloodRequestText);
        bloodRequestImg = findViewById(R.id.bloodRequestImage);
        donationEvent = findViewById(R.id.donationEventText);
        donationEventImg = findViewById(R.id.donationEventImage);
        circleImageView = findViewById(R.id.profileImage);

        //retrieve info
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        String userId = mUser.getUid();
        DocumentReference documentReference = firestore.collection("Hospital Administrator").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                adminData = value.toObject(AdminData.class);
                assert adminData != null;
                name.setText(adminData.getName());
                workplace.setText(adminData.getWorkplace());
                if (adminData.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    Glide.with(getApplicationContext()).load(adminData.getImageURL()).into(circleImageView);
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext()
                                ,AdminNotification.class));
                        overridePendingTransition(0,0);
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

        //Option
        searchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminSearchDonor.class);
                startActivity(intent);
                finish();
            }
        });
        searchDonorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminSearchDonor.class);
                startActivity(intent);
                finish();
            }
        });

        bloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });
        bloodRequestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });

        donationEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminDonationEvent.class);
                startActivity(intent);
                finish();
            }
        });
        donationEventImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminDonationEvent.class);
                startActivity(intent);
                finish();
            }
        });
    }
}