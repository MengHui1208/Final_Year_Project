package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfile extends AppCompatActivity {
    TextView name,workplace,contact,address,email;
    CircleImageView circleImageView;
    Button btnUpdateProfile,btnResetPwd,btnLogout;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    AdminData adminData;
    String userId;
    static final int IMAGE_REQUEST = 1;
    Uri imageUri,url;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation2);

        name = findViewById(R.id.fullnameText);
        workplace = findViewById(R.id.workplaceText);
        contact = findViewById(R.id.contactText);
        address = findViewById(R.id.addressText);
        email = findViewById(R.id.emailText);
        circleImageView = findViewById(R.id.profileImage);
        btnResetPwd = findViewById(R.id.btnResetPwd);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.profile);
        //bottom bar
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity2.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext()
                                ,AdminNotification.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        //retrieve data from database
        storageReference = FirebaseStorage.getInstance().getReference("admin_profile_images");
        documentReference = firestore.collection("Hospital Administrator").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                adminData = value.toObject(AdminData.class);
                assert adminData != null;
                name.setText(adminData.getName());
                workplace.setText(adminData.getWorkplace());
                contact.setText(adminData.getContact());
                address.setText(adminData.getAddress());
                email.setText(adminData.getEmail());
                if (adminData.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    Glide.with(getApplicationContext()).load(adminData.getImageURL()).into(circleImageView);
                }
            }
        });

        //Reset Password
        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetPassword = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Please enter your new password > 6 characters.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPassword =resetPassword.getText().toString();
                        if (newPassword.length() < 6){
                            Toast.makeText(AdminProfile.this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            mUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminProfile.this, "Passwaord reset successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminProfile.this, "Password reset failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });

        //Update Profile
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminProfileEdit.class);
                startActivity(intent);
                finish();
            }
        });

        //Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminProfile.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminProfile.this, FirstPage.class);
                startActivity(intent);
            }
        });

        //Open image
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(circleImageView);
            uploadImage();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        if (imageUri != null){
            final StorageReference imageReference =storageReference.child(System.currentTimeMillis() + getFileExtension(imageUri));
            imageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            url = uri;
                            FirebaseUser mUser = mAuth.getCurrentUser();
                            String userId = mUser.getUid();
                            DocumentReference documentReference = firestore.collection("Hospital Administrator").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("imageUrl", url.toString());
                            documentReference.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AdminProfile.this, "Image uploaded.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(AdminProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }


}