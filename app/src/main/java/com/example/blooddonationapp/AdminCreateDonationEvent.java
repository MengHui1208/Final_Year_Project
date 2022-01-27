package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminCreateDonationEvent extends AppCompatActivity {

    private static final String TAG = "NewDonationEvent";

    ImageView back;
    TextView startDate, endDate;
    DatePickerDialog.OnDateSetListener startOnDateSetListener, endOnDataSetListener;
    EditText eventName, venue, eventDetails;
    Button btnAdd;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_donation_event);

        back = findViewById(R.id.backToMenu);
        eventName = findViewById(R.id.editTextEventName);
        startDate = findViewById(R.id.editTextStartDate);
        endDate = findViewById(R.id.editTextEndDate);
        venue = findViewById(R.id.editTextVenue);
        eventDetails = findViewById(R.id.editTextEventDetails);
        btnAdd = findViewById(R.id.btnAdd);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminDonationEvent.class);
                startActivity(intent);
                finish();
            }
        });

        //catch id.../show theme
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AdminCreateDonationEvent.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        startOnDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //How to show
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
                        AdminCreateDonationEvent.this,
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

        //Add event
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEventName = eventName.getText().toString();
                final String mStartDate = startDate.getText().toString();
                final String mEndDate = endDate.getText().toString();
                final String mVenue = venue.getText().toString();
                final String mEventDetails = eventDetails.getText().toString();

                if (TextUtils.isEmpty(mEventName)) {
                    eventName.setError("Event name is required.");
                    eventName.requestFocus();
                    return;
                }
                if (mStartDate.equals("Select a date")) {
                    Toast.makeText(AdminCreateDonationEvent.this, "Please select a started date of the event.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEndDate.equals("Select a date")) {
                    Toast.makeText(AdminCreateDonationEvent.this, "Please select a ended date of event.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mVenue)) {
                    venue.setError("Venue is required.");
                    venue.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mEventDetails)) {
                    eventDetails.setError("Details of the event are required.");
                    eventDetails.requestFocus();
                    return;
                }
                else {
                    add(mEventName, mStartDate, mEndDate, mVenue, mEventDetails);
                }
            }

        });
    }

    private void showStartDate(int year, int month, int day) {
        startDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }
    private void showEndDate(int year, int month, int day) {
        endDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    private void add(String mEventName, String mStartDate, String mEndDate, String mVenue, String mEventDetails) {
        FirebaseUser mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        String eventId = firestore.collection("Hospital Administrator").document(userId).collection("Donation Event").document().getId();
        documentReference = firestore.collection("Hospital Administrator").document(userId).collection("Donation Event").document(eventId);
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("eventId",eventId);
        hashMap.put("startDate",mStartDate);
        hashMap.put("endDate",mEndDate);
        hashMap.put("eventName",mEventName);
        hashMap.put("venue",mVenue);
        hashMap.put("eventDetails",mEventDetails);
        hashMap.put("timestamp", FieldValue.serverTimestamp());
        documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //nothing
            }
        });
        documentReference = firestore.collection("Donation Event").document(eventId);
        Map<String,Object> event = new HashMap<>();
        hashMap.put("eventId",eventId);
        event.put("startDate",mStartDate);
        event.put("endDate",mEndDate);
        event.put("eventName",mEventName);
        event.put("venue",mVenue);
        event.put("eventDetails",mEventDetails);
        event.put("timestamp", FieldValue.serverTimestamp());
        documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminCreateDonationEvent.this, "Event has been added successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AdminDonationEvent.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(AdminCreateDonationEvent.this, "Failed to add event!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        String notificationId = firestore.collection("Public Notification").document().getId();
//        documentReference = firestore.collection("Public Notification").document(notificationId);
//        Map<String, Object> noti = new HashMap<>();
//        noti.put("notificationId",notificationId);
//        noti.put("content","There is a new blood donation event has been added. Please go to get more details of the event on the Donation Event Page.");
//        noti.put("name","Event Name: "+mEventName);
//        noti.put("date","Event Date: "+mStartDate + " - " +mEndDate);
//        noti.put("timestamp", FieldValue.serverTimestamp());
//        documentReference.set(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                //nothing
//            }
//        });
    }
}