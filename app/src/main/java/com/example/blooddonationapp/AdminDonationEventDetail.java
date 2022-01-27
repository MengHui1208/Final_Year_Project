package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminDonationEventDetail extends AppCompatActivity {

    private static final String TAG = "DonationEventDetail";

    EditText editTextEventName,editTextVenue,editTextEventDetails;
    TextView startDate,endDate;
    ImageView back;
    Button buttonUpdate, buttonDelete;
    DatePickerDialog.OnDateSetListener startOnDateSetListener, endOnDataSetListener;

    FirebaseFirestore firestore;
    DocumentReference documentReference;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    AdminDonationEventList eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_donation_event_detail);

        editTextEventName = findViewById(R.id.editTextEventName);
        editTextVenue = findViewById(R.id.editTextVenue);
        editTextEventDetails = findViewById(R.id.editTextEventDetails);
        startDate = findViewById(R.id.editTextStartDate);
        endDate = findViewById(R.id.editTextEndDate);
        back = findViewById(R.id.back);
        buttonUpdate = findViewById(R.id.btnUpdate);
        buttonDelete = findViewById(R.id.btnDelete);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        String userId = mAuth.getCurrentUser().getUid();
        final String eventId = getIntent().getStringExtra("eventId");

        documentReference = firestore.collection("Hospital Administrator").document(userId).collection("Donation Event").document(eventId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    eventList = value.toObject(AdminDonationEventList.class);
                    assert eventList != null;
                    editTextEventName.setText(eventList.getEventName());
                    editTextVenue.setText(eventList.getVenue());
                    editTextEventDetails.setText(eventList.getEventDetails());
                    startDate.setText(eventList.getStartDate());
                    endDate.setText(eventList.getEndDate());
                }
            }
        });

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminDonationEvent.class);
                startActivity(intent);
                finish();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AdminDonationEventDetail.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        startOnDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        startOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: date" + year + "/" + month + "/" + dayOfMonth);
                showStartDate(year, month + 1, dayOfMonth);
            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal2 = Calendar.getInstance();
                int year = cal2.get(Calendar.YEAR);
                int month = cal2.get(Calendar.MONTH);
                int day = cal2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog2 = new DatePickerDialog(
                        AdminDonationEventDetail.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        endOnDataSetListener, year, month, day);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        endOnDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: date" + year + "/" + month + "/" + dayOfMonth);
                showEndDate(year, month + 1, dayOfMonth);
            }
        };

        //update events
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEventName = editTextEventName.getText().toString();
                final String mStartDate = startDate.getText().toString();
                final String mEndDate = endDate.getText().toString();
                final String mVenue = editTextVenue.getText().toString();
                final String mEventDetails = editTextEventDetails.getText().toString();

                if (TextUtils.isEmpty(mEventName)) {
                    editTextEventName.setError("Event name is required.");
                    editTextEventName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mVenue)) {
                    editTextVenue.setError("Venue is required.");
                    editTextVenue.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mEventDetails)) {
                    editTextEventDetails.setError("Details of the event are required.");
                    editTextEventDetails.requestFocus();
                    return;
                } else {
                    String userId = mUser.getUid();
                    String eventId = getIntent().getStringExtra("eventId");
                    documentReference = firestore.collection("Hospital Administrator").document(userId).collection("Donation Event").document(eventId);
                    Map<String, Object> edited = new HashMap<>();
                    edited.put("eventName", mEventName);
                    edited.put("startDate", mStartDate);
                    edited.put("endDate", mEndDate);
                    edited.put("venue", mVenue);
                    edited.put("eventDetails", mEventDetails);
                    edited.put("timestamp", FieldValue.serverTimestamp());
                    documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //nothing
                        }
                    });
                    documentReference = firestore.collection("Donation Event").document(eventId);
                    Map<String, Object> updated = new HashMap<>();
                    updated.put("eventName", mEventName);
                    updated.put("startDate", mStartDate);
                    updated.put("endDate", mEndDate);
                    updated.put("venue", mVenue);
                    updated.put("eventDetails", mEventDetails);
                    updated.put("timestamp",FieldValue.serverTimestamp());
                    documentReference.update(updated).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminDonationEventDetail.this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AdminDonationEvent.class);
                            startActivity(intent);
                            finish();
                        }
                    });
//                    String notificationId = firestore.collection("Public Notification").document().getId();
//                    documentReference = firestore.collection("Public Notification").document(notificationId);
//                    Map<String, Object> noti = new HashMap<>();
//                    noti.put("notificationId", notificationId);
//                    noti.put("content", "The event of '" + mEventName + "' has been updated. Please go to get the latest information on the Donation Event Page.");
//                    noti.put("name", "Event Name: " + mEventName);
//                    noti.put("date", "Event Date: " + mStartDate + " - " + mEndDate);
//                    noti.put("timestamp", FieldValue.serverTimestamp());
//                    documentReference.set(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            //nothing
//                        }
//                    });
                }
            }
        });

        //delete event
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(v.getContext());
                confirmDeleteDialog.setTitle("Delete Entry");
                confirmDeleteDialog.setMessage("Are you sure to delete?");

                confirmDeleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userId = mAuth.getCurrentUser().getUid();
                        String eventId = getIntent().getStringExtra("eventId");
                        documentReference = firestore.collection("Hospital Administrator").document(userId).collection("Donation Event").document(eventId);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //nothing
                            }
                        });
                        documentReference = firestore.collection("Donation Event").document(eventId);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminDonationEventDetail.this, "Event deleted successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminDonationEventDetail.this, AdminDonationEvent.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AdminDonationEventDetail.this, "Failed to delete.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                confirmDeleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close dialog
                    }
                });
                confirmDeleteDialog.create().show();
            }
        });
    }

    private void showStartDate(int year, int month, int day) {
        startDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    private void showEndDate(int year, int month, int day) {
        endDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }
}