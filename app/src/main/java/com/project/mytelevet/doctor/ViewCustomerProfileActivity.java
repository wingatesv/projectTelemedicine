package com.project.mytelevet.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.mytelevet.R;
import com.project.mytelevet.common.VideoCallActivity;
import com.project.mytelevet.customer.MainActivity;
import com.project.mytelevet.customer.viewmodel.ViewMyProfileViewModel;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ViewCustomerProfileActivity extends AppCompatActivity {
    String customerID, userID;
    private static final Integer REQUEST_CODE = 1;

    TextView lbl_customerName, lbl_phone, lbl_state;

    ImageView imageView_customerProfilePic;

    ViewMyProfileViewModel viewMyProfileViewModel;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_profile);

        Intent intent = getIntent();
        customerID = intent.getStringExtra("customerID");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        imageView_customerProfilePic = findViewById(R.id.imageView_customerProfilePic);

        StorageReference profilePicRef = storageReference.child("users/" + customerID + "/profilePic.jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView_customerProfilePic);
                //progressBar.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tag", e.getMessage());
                // progressBar.setVisibility(View.INVISIBLE);
            }
        });

        viewMyProfileViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewMyProfileViewModel.class);

        viewMyProfileViewModel.getViewMyProfileLiveData(customerID).observe(this, Observable -> {
        });

        viewMyProfileViewModel.getMyProfileItem().observe(this, item -> {

            lbl_customerName = findViewById(R.id.lbl_customerName);
            lbl_phone = findViewById(R.id.lbl_customerPhone);
            lbl_state = findViewById(R.id.lbl_customerState);

            Map<String, Object> myProfile;

            myProfile = item;

            lbl_customerName.setText(myProfile.get("FullName").toString());
            lbl_phone.setText(myProfile.get("PhoneNumber").toString());
            lbl_state.setText(myProfile.get("State").toString());


        });


    }

    public void call(View view) {
        Intent it = new Intent(Intent.ACTION_DIAL);
        it.setData(Uri.parse("tel:" + lbl_phone.getText().toString()));
        startActivity(it);

    }

    public void videoCall(View view)
    {
        Intent intentView = new Intent(this, VideoCallActivity.class);
        intentView.putExtra("channelName", userID + "_" + customerID);
        startActivity(intentView);
    }




}