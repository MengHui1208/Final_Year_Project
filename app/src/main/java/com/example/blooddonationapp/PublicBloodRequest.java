package com.example.blooddonationapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class PublicBloodRequest extends AppCompatActivity {

    ImageView backToMenu,btnCreate;
    PublicData publicData;
    RecyclerView myRequestRecycleView;

    //firebase
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    //adapter
    BloodRequestList bloodRequestList;
    PublicBloodRequestAdapter publicBloodRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_blood_request);

        btnCreate = findViewById(R.id.btnCreate);
        backToMenu = findViewById(R.id.backToMenu);

        //using firebase's store
        firestore = FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();

        myRequestRecycleView = findViewById(R.id.myRequestRecycleView);
        myRequestRecycleView.setHasFixedSize(true);
        myRequestRecycleView.setLayoutManager(new LinearLayoutManager(this));

        Query query = firestore.collection("Public").document(userId).collection("Blood Request").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BloodRequestList> options =
                new FirestoreRecyclerOptions.Builder<BloodRequestList>()
                        .setQuery(query, BloodRequestList.class)
                        .build();

        publicBloodRequestAdapter = new PublicBloodRequestAdapter(options);
        myRequestRecycleView.setAdapter(publicBloodRequestAdapter);

        publicBloodRequestAdapter.setOnItemClickListener(new PublicBloodRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                BloodRequestList requestList = snapshot.toObject(BloodRequestList.class);
                String requestId = snapshot.getId();
                Intent request_intent = new Intent(getApplicationContext(),PublicBloodRequestDetail.class);
                request_intent.putExtra("requestId",requestId);
                startActivity(request_intent);
                finish();
            }
        });

        //Click the create button to create new request
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicCreateRequest.class);
                startActivity(intent);
                finish();
            }
        });

        //Go back to main page
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        publicBloodRequestAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        publicBloodRequestAdapter.stopListening();
    }

}