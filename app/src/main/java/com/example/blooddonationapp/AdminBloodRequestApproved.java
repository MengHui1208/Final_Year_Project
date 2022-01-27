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

public class AdminBloodRequestApproved extends Fragment {

    RecyclerView approvedRequestRecyclerView;

    FirebaseFirestore firestore;
    AdminBloodRequestAdapter adminBloodRequestAdapter;

    public AdminBloodRequestApproved() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_blood_request_approved, container, false);
        firestore = FirebaseFirestore.getInstance();
        approvedRequestRecyclerView = view.findViewById(R.id.approvedRequestRecyclerView);
        approvedRequestRecyclerView.setHasFixedSize(true);
        approvedRequestRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Query query = firestore.collection("Blood Request").whereEqualTo("status","Approved")/*.orderBy("timestamp", Query.Direction.DESCENDING)*/;
        FirestoreRecyclerOptions<BloodRequestList> options =
                new FirestoreRecyclerOptions.Builder<BloodRequestList>()
                        .setQuery(query, BloodRequestList.class)
                        .build();

        adminBloodRequestAdapter = new AdminBloodRequestAdapter(options);
        approvedRequestRecyclerView.setAdapter(adminBloodRequestAdapter);

        adminBloodRequestAdapter.setOnItemClickListener(new AdminBloodRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                //BloodRequestList requestList = snapshot.toObject(BloodRequestList.class);
                //String userId = requestList.getPublicId();
                String requestId = snapshot.getId();
                Intent request_intent = new Intent(AdminBloodRequestApproved.this.getContext(), AdminBloodRequestDetail.class);
                request_intent.putExtra("requestId",requestId);
                //request_intent.putExtra("publicId",userId);
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