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
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
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

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    TextView fullname,email,bloodType,gender,searchDonor, bloodRequest, otherRequester,donationRecord;
    ImageView searchDonorImg, bloodRequestImg, otherRequesterImg, donationRecordImg;
    CircleImageView circleImageView;

    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    PublicData publicData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.ugmail);
        gender = findViewById(R.id.genderSelectedText);
        bloodType = findViewById(R.id.bloodTypeSelectedText);
        //Option
        searchDonor = findViewById(R.id.searchDonorText);
        searchDonorImg = findViewById(R.id.searchDonorImage);
        bloodRequest = findViewById(R.id.bloodRequestText);
        bloodRequestImg = findViewById(R.id.bloodRequestImage);
        otherRequester = findViewById(R.id.otherRequesterText);
        otherRequesterImg = findViewById(R.id.otherRequesterImage);
        donationRecord = findViewById(R.id.donationRecordText);
        donationRecordImg = findViewById(R.id.donationRecordImage);
        circleImageView = findViewById(R.id.profileImage);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        String userId = mUser.getUid();
        DocumentReference documentReference = firestore.collection("Public").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                publicData = value.toObject(PublicData.class);
                assert publicData != null;
                fullname.setText(publicData.getFullname());
                email.setText(publicData.getEmail());
                bloodType.setText(publicData.getBloodType());
                gender.setText(publicData.getGender());
                    if (publicData.getImageURL().equals("default")){
                        circleImageView.setImageResource(R.drawable.ic_launcher_background);
                    }else{
                        Glide.with(getApplicationContext()).load(publicData.getImageURL()).into(circleImageView);
                    }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.event:
                        startActivity(new Intent(getApplicationContext()
                                ,PublicDonationEvent.class));
                        overridePendingTransition(0,0);
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

        //option
        searchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicSearchDonor.class);
                startActivity(intent);
                finish();
            }
        });
        searchDonorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicSearchDonor.class);
                startActivity(intent);
                finish();
            }
        });

        bloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });
        bloodRequestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });
//
//        otherRequester.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PublicDonationEvent.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        otherRequesterImg.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), PublicOtherRequest.class);
               startActivity(intent);
               finish();
           }
        });

        donationRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicDonationRecord.class);
                startActivity(intent);
                finish();
            }
        });
        donationRecordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicDonationRecord.class);
                startActivity(intent);
                finish();
            }
        });
    }

}