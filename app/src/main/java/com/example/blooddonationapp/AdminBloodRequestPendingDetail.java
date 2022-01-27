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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminBloodRequestPendingDetail extends AppCompatActivity {
    TextView recipientName,date,bloodType,amount,venue,contact,additionalInfo,status;
    Button buttonApproved,buttonRejected;
    ImageView back;

    FirebaseFirestore firestore;
    DocumentReference documentReference;
    BloodRequestList requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_blood_request_pending_detail);

        recipientName = findViewById(R.id.editTextRecipientName);
        date = findViewById(R.id.editTextDate);
        bloodType = findViewById(R.id.editTextBloodType);
        amount = findViewById(R.id.editTextAmount);
        venue = findViewById(R.id.editTextVenue);
        contact = findViewById(R.id.editTextContact);
        additionalInfo = findViewById(R.id.editTextAdditionalInfo);
        status  =findViewById(R.id.status);
        buttonRejected = findViewById(R.id.buttonRejected);
        buttonApproved = findViewById(R.id.buttonApproved);
        back = findViewById(R.id.backToMenu);
        firestore = FirebaseFirestore.getInstance();

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });

        final String userId = getIntent().getStringExtra("publicId");
        final String requestId = getIntent().getStringExtra("requestId");
        documentReference = firestore.collection("Blood Request").document(requestId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    requestList = value.toObject(BloodRequestList.class);
                    assert requestList != null;
                    recipientName.setText(requestList.getRecipientName());
                    bloodType.setText(requestList.getBloodType());
                    amount.setText(requestList.getAmount());
                    venue.setText(requestList.getVenue());
                    contact.setText(requestList.getContact());
                    additionalInfo.setText(requestList.getAdditionalInfo());

                    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    //https://stackoverflow.com/questions/59908720/how-to-get-server-timestamp-from-firestore-in-an-android-device
                    Date creationDate = value.getDate("timestamp");
                    String dateString = dateFormat.format(creationDate);
                    date.setText(String.valueOf(dateString));
                }
            }
        });

        //approved request
        buttonApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mName = recipientName.getText().toString();
                final String mDate = date.getText().toString();
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(v.getContext());
                confirmDialog.setTitle("Request Confirmation");
                confirmDialog.setMessage("Are you sure to approve this request?");
                confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //final String mStatus = status.getText().toString();
                        documentReference = firestore.collection("Blood Request").document(requestId);
                        Map<String, Object> approved = new HashMap<>();
                        approved.put("status", "Approved");
//                        approved.put("timestamp", FieldValue.serverTimestamp());
                        documentReference.update(approved).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        documentReference = firestore.collection("Public").document(userId).collection("Blood Request").document(requestId);
                        Map<String, Object> approved2 = new HashMap<>();
                        approved2.put("status", "Approved");
                        documentReference.update(approved).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminBloodRequestPendingDetail.this, "Request approved.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), AdminBloodRequest.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        String notificationId = firestore.collection("Public Notification").document().getId();
                        documentReference = firestore.collection("Public Notification").document(notificationId);
                        Map<String, Object> noti = new HashMap<>();
                        noti.put("notificationId",notificationId);
                        noti.put("content","There is a new approved blood request has been added. Please go to get more details of the request on the Blood Request Page.");
                        noti.put("name",mName);
                        noti.put("date",mDate);
                        noti.put("timestamp",FieldValue.serverTimestamp());
                        documentReference.set(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //nothing
                            }
                        });
                    }
                });
                confirmDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                confirmDialog.create().show();
            }
        });

        //reject request
        buttonRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(v.getContext());
                confirmDialog.setTitle("Request Confirmation");
                confirmDialog.setMessage("Are you sure to reject this request?");
                confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //final String mStatus = status.getText().toString();
                        documentReference = firestore.collection("Blood Request").document(requestId);
                        Map<String, Object> rejected = new HashMap<>();
                        rejected.put("status", "Rejected");
//                        rejected.put("timestamp",FieldValue.serverTimestamp());
                        documentReference.update(rejected).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        documentReference = firestore.collection("Public").document(userId).collection("Blood Request").document(requestId);
                        Map<String, Object> rejected2 = new HashMap<>();
                        rejected2.put("status", "Rejected");
                        documentReference.update(rejected2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminBloodRequestPendingDetail.this, "Request rejected.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), AdminBloodRequest.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
                confirmDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                confirmDialog.create().show();
            }
        });


    }
}