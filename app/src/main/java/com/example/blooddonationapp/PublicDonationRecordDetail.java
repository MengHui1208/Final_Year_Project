package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PublicDonationRecordDetail extends AppCompatActivity {

    TextView date,amount,venue,approvedBy;
    Button btnDelete;
    ImageView backToMenu;

    FirebaseFirestore firestore;
    DocumentReference documentReference;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    PublicDonationRecordList recordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_donation_record_detail);

        date = findViewById(R.id.txtDate);
        amount = findViewById(R.id.txtAmount);
        venue = findViewById(R.id.txtVenue);
        approvedBy = findViewById(R.id.txtApprovedBy);
        btnDelete = findViewById(R.id.btnDelete);
        backToMenu = findViewById(R.id.backToMenu);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        String userId = mAuth.getCurrentUser().getUid();
        final String recordId = getIntent().getStringExtra("recordId");

        documentReference = firestore.collection("Public").document(userId).collection("Donation Record").document(recordId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    recordList = value.toObject(PublicDonationRecordList.class);
                    assert recordList != null;
                    date.setText(recordList.getDate());
                    amount.setText(recordList.getAmount());
                    venue.setText(recordList.getVenue());
                    approvedBy.setText(recordList.getApprovedBy());

                    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    //https://stackoverflow.com/questions/59908720/how-to-get-server-timestamp-from-firestore-in-an-android-device
                    Date creationDate = value.getDate("timestamp");
                    String dateString = dateFormat.format(creationDate);
                    date.setText(String.valueOf(dateString));
                }
            }
        });

        //Back to previous page
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublicDonationRecord.class);
                startActivity(intent);
                finish();
            }
        });

        //delete the request
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(view.getContext());
                confirmDeleteDialog.setTitle("Delete Entry");
                confirmDeleteDialog.setMessage("Are you sure to delete?");

                //Select Yes button
                confirmDeleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userId = mAuth.getCurrentUser().getUid();
                        String recordId = getIntent().getStringExtra("recordId");
                        documentReference = firestore.collection("Public").document(userId).collection("Donation Record").document(recordId);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //nothing
                            }
                        });
                        documentReference = firestore.collection("Donation Record").document(recordId);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PublicDonationRecordDetail.this, "Record deleted successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PublicDonationRecordDetail.this, PublicDonationRecord.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(PublicDonationRecordDetail.this, "Failed to delete.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                //Select No button
                confirmDeleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close dialog
                    }
                });
                confirmDeleteDialog.create().show();
            }
        });


    }
}