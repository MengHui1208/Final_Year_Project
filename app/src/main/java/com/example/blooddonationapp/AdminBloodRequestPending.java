package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminBloodRequestPending extends Fragment {

    RecyclerView pendingRequestRecyclerView;
    FirebaseFirestore firestore;
    AdminBloodRequestAdapter adminBloodRequestAdapter;

    public AdminBloodRequestPending() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_admin_blood_request_pending, container, false);
        firestore = FirebaseFirestore.getInstance();

        pendingRequestRecyclerView = view.findViewById(R.id.pendingRequestRecycleView);
        pendingRequestRecyclerView.setHasFixedSize(true);
        pendingRequestRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Query query = firestore.collection("Blood Request").whereEqualTo("status","Pending").orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<BloodRequestList> options =
                new FirestoreRecyclerOptions.Builder<BloodRequestList>()
                        .setQuery(query, BloodRequestList.class)
                        .build();

        adminBloodRequestAdapter = new AdminBloodRequestAdapter(options);
        pendingRequestRecyclerView.setAdapter(adminBloodRequestAdapter);

        adminBloodRequestAdapter.setOnItemClickListener(new AdminBloodRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                BloodRequestList requestList = snapshot.toObject(BloodRequestList.class);
                String userId = requestList.getPublicId();
                String requestId = snapshot.getId();
                Intent request_intent = new Intent(AdminBloodRequestPending.this.getContext(), AdminBloodRequestPendingDetail.class);
                request_intent.putExtra("requestId",requestId);
                request_intent.putExtra("publicId",userId);
                startActivity(request_intent);
                getActivity().finish();
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adminBloodRequestAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adminBloodRequestAdapter.stopListening();
    }
}