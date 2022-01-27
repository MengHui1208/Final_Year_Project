package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicDonationRecord extends AppCompatActivity {
    ImageView btnCreate,back;
    PublicData publicData;
    RecyclerView drecordRecyclerView;
    DocumentReference documentReference;
    DonationRecordAdapter adapter;

    FirebaseFirestore firestore;
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_donation_record);

        btnCreate = findViewById(R.id.btnCreate);
        back = findViewById(R.id.backToMenu);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        firestore = FirebaseFirestore.getInstance();

        drecordRecyclerView = findViewById(R.id.drecordRecycleView);
        drecordRecyclerView.setHasFixedSize(true);
        drecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query =firestore.collection("Public").document(userId).collection("Donation Record").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PublicDonationRecordList> options =
                new FirestoreRecyclerOptions.Builder<PublicDonationRecordList>()
                        .setQuery(query,PublicDonationRecordList.class)
                        .build();

        adapter = new DonationRecordAdapter(options);
        drecordRecyclerView.setAdapter(adapter);


        //Create record
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PublicCreateRecord.class);
                startActivity(intent);
                finish();
            }
        });

        //back to mainPage
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        adapter.setOnItemClickListener(new DonationRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                PublicDonationRecordList recordList = snapshot.toObject(PublicDonationRecordList.class);
                String recordId = snapshot.getId();
                Intent intent = new Intent(getApplicationContext(),PublicDonationRecordDetail.class);
                intent.putExtra("recordId",recordId);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}