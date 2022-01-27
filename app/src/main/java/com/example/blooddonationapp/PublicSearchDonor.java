package com.example.blooddonationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicSearchDonor extends AppCompatActivity {
    ImageView backToMenu;
    EditText searchBox;
    RecyclerView donorRecyclerView;

    FirebaseFirestore firestore;
    SearchDonorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_search_donor);

        searchBox = findViewById(R.id.searchBox);
        backToMenu = findViewById(R.id.backToMenu);

        //back to previous page
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Spinner mySpinner = (Spinner) findViewById(R.id.bloodTypeSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(PublicSearchDonor.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.blood_type_array));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        firestore = FirebaseFirestore.getInstance();
        donorRecyclerView = findViewById(R.id.donorRecycleView);
        donorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donorRecyclerView.setHasFixedSize(true);

        //Query
        Query query =firestore.collection("Public").orderBy("fullname", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<DonorList> options =
                new FirestoreRecyclerOptions.Builder<DonorList>()
                        .setQuery(query,DonorList.class)
                        .build();

        adapter = new SearchDonorAdapter(options);
        donorRecyclerView.setAdapter(adapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Query query;
                if (s.toString().isEmpty()) {
                    query = firestore.collection("Public").orderBy("fullname", Query.Direction.ASCENDING);
                } else {
                    final String bloodType = (String) mySpinner.getSelectedItem();
                    query = firestore.collection("Public")
                            .whereEqualTo("bloodType",bloodType)
                            .orderBy("address").startAt(s.toString()).endAt(s.toString()+ "\uf8ff");

                }
                FirestoreRecyclerOptions<DonorList> options =
                        new FirestoreRecyclerOptions.Builder<DonorList>()
                                .setQuery(query,DonorList.class)
                                .build();
                adapter.updateOptions(options);
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