package com.example.blooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PublicOtherRequest extends AppCompatActivity {
    ImageView back;
    RecyclerView otherRequestRecycleView;
    FirebaseFirestore firestore;
    PublicBloodRequestAdapter otherRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_other_request);

        back =findViewById(R.id.backToMenu);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();
        otherRequestRecycleView = findViewById(R.id.otherRequestRecycleView);
        otherRequestRecycleView.setHasFixedSize(true);
        otherRequestRecycleView.setLayoutManager(new LinearLayoutManager(this));

        Query query = firestore.collection("Blood Request").whereEqualTo("status","Approved")/*.orderBy("timestamp", Query.Direction.DESCENDING)*/;
        FirestoreRecyclerOptions<BloodRequestList> options =
                new FirestoreRecyclerOptions.Builder<BloodRequestList>()
                        .setQuery(query, BloodRequestList.class)
                        .build();

        otherRequestAdapter = new PublicBloodRequestAdapter(options);
        otherRequestRecycleView.setAdapter(otherRequestAdapter);

        otherRequestAdapter.setOnItemClickListener(new PublicBloodRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                BloodRequestList requestList = snapshot.toObject(BloodRequestList.class);
                String requestId = snapshot.getId();
                Intent request_intent = new Intent(getApplicationContext(),PublicOtherRequestDetail.class);
                request_intent.putExtra("requestId",requestId);
                startActivity(request_intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        otherRequestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        otherRequestAdapter.stopListening();
    }
}