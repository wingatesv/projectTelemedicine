package com.project.mytelevet.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.mytelevet.R;
import com.project.mytelevet.doctor.viewmodel.ViewVetProfileViewModel;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ViewVetProfileActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    private static Uri imageUri = null;

    EditText tf_firstName, tf_lastName, tf_phoneNumber, tf_avail, tf_description;
    ImageView profileImage;

    ProgressBar progressBar;

    TextView lbl_updateProfilePic;

    Button btn_update;

    ViewVetProfileViewModel viewVetProfileViewModel;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vet_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressBar = findViewById(R.id.VetProfile_progressBar);

        lbl_updateProfilePic = findViewById(R.id.lbl_updateVetProfilePic);
        profileImage = findViewById(R.id.imageView_vetProfilePic);

        StorageReference profilePicRef = storageReference.child("doctors/" + fAuth.getCurrentUser().getUid() + "/profilePic.jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
                progressBar.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tag", e.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        lbl_updateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        viewVetProfileViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewVetProfileViewModel.class);

        viewVetProfileViewModel.getViewVetProfileLiveData(fAuth.getCurrentUser().getUid()).observe(this, Observable -> {});

        viewVetProfileViewModel.getVetProfileItem().observe(this, item -> {

            tf_firstName = findViewById(R.id.tf_ViewVetFirstName);
            tf_lastName = findViewById(R.id.tf_ViewVetLastName);
            tf_phoneNumber = findViewById(R.id.tf_ViewVetPhoneNumber);
            tf_avail = findViewById(R.id.tf_ViewVetAvailability);
            tf_description = findViewById(R.id.tf_ViewVetDescription);

            Map<String, Object> myProfile;

            myProfile = item;

            tf_firstName.setText(myProfile.get("FirstName").toString());
            tf_lastName.setText(myProfile.get("LastName").toString());
            tf_phoneNumber.setText(myProfile.get("PhoneNumber").toString());
            tf_avail.setText(myProfile.get("Availability").toString());
            tf_description.setText(myProfile.get("Description").toString());


        });


        btn_update = findViewById(R.id.btn_vetUpdate);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri != null) {
                    uploadImageToFirebase();
                }

                String update_firstName = tf_firstName.getText().toString();
                String update_lastName = tf_lastName.getText().toString();
                String update_phoneNumber = tf_phoneNumber.getText().toString();
                String update_avail = tf_avail.getText().toString();
                String update_description = tf_description.getText().toString();

                if (update_firstName.isEmpty() || update_lastName.isEmpty() || update_phoneNumber.isEmpty() || update_avail.isEmpty() || update_description.isEmpty())
                {
                    Toast.makeText(ViewVetProfileActivity.this, "Details are incomplete", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (viewVetProfileViewModel.updateData(update_firstName, update_lastName, update_phoneNumber, update_avail, update_description))
                {
                    Toast.makeText(ViewVetProfileActivity.this, "Vet Profile is updated.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewVetProfileActivity.this, VetMainActivity.class));     // goto main activity
                    finish();
                }
                else
                {
                    Log.e("tag", "Unable to update user profile at ViewVetProfileActivity");
                }


            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE)
        {
            if (data != null)
            {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);

            }

        }

    }

    private void uploadImageToFirebase()
    {
        String userID = fAuth.getCurrentUser().getUid();

        StorageReference fileRef = storageReference.child("doctors/" + userID + "/profilePic.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                        Log.i("tag", userID + " profile pic is uploaded into fire cloud.");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("tag", e.getMessage());
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tag", e.getMessage());
            }
        });
    }
}