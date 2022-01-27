package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CalendarView;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PublicCreateRecord extends AppCompatActivity {
    private static final String TAG = "NewDonationRecord";

    ImageView back,imageRecord;
    TextView selectDate;
    DatePickerDialog.OnDateSetListener mOnDateSetListener;
    EditText amount, venue, approvedBy;
    Button btnCreateRecord;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;

//    PublicDonationRecordList recordList;
//    static final int IMAGE_REQUEST = 2;
//    Uri url,imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_create_record);

        back = findViewById(R.id.backToMenu);
        selectDate = findViewById(R.id.txtDate);
        amount = findViewById(R.id.txtAmount);
        venue = findViewById(R.id.txtVenue);
        approvedBy = findViewById(R.id.txtApprovedBy);
        btnCreateRecord = findViewById(R.id.btnAdd);


        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //back to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PublicDonationRecord.class);
                startActivity(intent);
                finish();
            }
        });

        //catch id.../show theme
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PublicCreateRecord.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mOnDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //How to show
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onDateSet: date" + year + "/" + month + "/" + dayOfMonth);
                showDate(year,month+1,dayOfMonth);
            }
        };

        //Open image
//        btnAddImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,IMAGE_REQUEST);
//            }
//        });

        //Add record
        btnCreateRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mDate = selectDate.getText().toString();
                final String mAmount = amount.getText().toString();
                final String mVenue = venue.getText().toString();
                final String mApprove = approvedBy.getText().toString();

                if (mDate.equals("Select a date")) {
                    Toast.makeText(PublicCreateRecord.this, "Please select a date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mAmount)) {
                    amount.setError("Amount of blood donated is required.");
                    amount.requestFocus();
                    return;

                }
                if (TextUtils.isEmpty(mVenue)){
                    venue.setError("Venue is required.");
                    venue.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mApprove)) {
                    approvedBy.setError("Name of the approval is required.");
                    approvedBy.requestFocus();
                    return;
                }
                else {
                    add(mDate,mAmount,mVenue,mApprove);
                }
            }
        });
    }

    private void showDate(int year, int month, int day) {
        selectDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    private void add(String mDate, String mAmount, String mVenue, String mApprove) {
        String userId = mUser.getUid();
        String recordId = firestore.collection("Public").document(userId).collection("Donation Record").document().getId();
        documentReference = firestore.collection("Public").document(userId).collection("Donation Record").document();
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("timestamp", FieldValue.serverTimestamp());
        hashMap.put("recordId",recordId);
        hashMap.put("date",mDate);
        hashMap.put("amount",mAmount);
        hashMap.put("venue",mVenue);
        hashMap.put("approvedBy",mApprove);
        documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PublicCreateRecord.this, "Record has been added successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), PublicDonationRecord.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(PublicCreateRecord.this, "Failed to add record!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
//            imageUri = data.getData();
//            Picasso.get().load(imageUri).into(imageRecord);
//            imageRecord.setVisibility(View.VISIBLE);
//            uploadImage();
//            //https://www.youtube.com/watch?v=Ey2imp7iaP8
//        }
//    }

//    public String getFileExtension(Uri uri) {
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }

//    private void uploadImage() {
//        if (imageUri != null) {
//            final StorageReference imageReference = FirebaseStorage.getInstance().getReference().child("record_images").child(System.currentTimeMillis() + getFileExtension(imageUri));
//            imageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//
//                            url = uri;
//                            mUser = mAuth.getCurrentUser();
//                            String userId = mUser.getUid();
//                            String recordId = getIntent().getStringExtra("recordId");
//                            DocumentReference documentReference = firestore.collection("Public").document(userId).collection("Donation Record").document(recordId);
//                            Map<String, Object> record = new HashMap<>();
//                            record.put("imageUrl", url.toString());
//                            documentReference.update(record).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(PublicCreateRecord.this, "Image uploaded.", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(PublicCreateRecord.this, "Failed.", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    });
//
//                }
//            });
//        }
//    }
}