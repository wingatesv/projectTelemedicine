package com.project.mytelevet.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.viewmodel.ViewMyPetViewModel;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ViewCustomerPetProfileActivity extends AppCompatActivity {
    String customerID, petName, petID;

    TextView lbl_petName, lbl_info1, lbl_info2;


    ImageView imageView_petProfilePic;

    ViewMyPetViewModel viewMyPetViewModel;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_pet_profile);

        Intent intent = getIntent();
        customerID = intent.getStringExtra("customerID");
        petName = intent.getStringExtra("petName");

        imageView_petProfilePic = findViewById(R.id.imageView_customerPetProfileImage);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        viewMyPetViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewMyPetViewModel.class);

        viewMyPetViewModel.getViewMyPetLiveData(petName, customerID).observe(this, Observable -> {});


        viewMyPetViewModel.getMyPetItem().observe(this, item -> {

        lbl_petName = findViewById(R.id.lbl_customerPetName);
        lbl_info1 = findViewById(R.id.lbl_customerPetInfo1);
        lbl_info2 = findViewById(R.id.lbl_customerPetInfo2);

            Map<String, Object> myPet;

            myPet = item;

            lbl_petName.setText(myPet.get("PetName").toString());
            lbl_info1.setText(myPet.get("Breed").toString() + ", " + myPet.get("DOB").toString());
            lbl_info2.setText(myPet.get("Gender").toString() + "-" + myPet.get("Condition").toString() + ", " + myPet.get("Size").toString());

            if (myPet.containsKey("PetID") && !myPet.get("PetID").toString().isEmpty()) {
                petID = myPet.get("PetID").toString();
            }
            else
            {
                Log.i("tag", "PetID is empty in item list");
            }

            if (!petID.isEmpty())
            {
                StorageReference petPicRef = storageReference.child("users/" + customerID + "/pets/" + petID + "Pic.jpg");

                petPicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(imageView_petProfilePic);
                   // progressBar.setVisibility(View.INVISIBLE);
                    Log.i("tag", "pet id is" + petID);

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // progressBar.setVisibility(View.INVISIBLE);
                        Log.e("tag", e.getMessage());
                    }
                });

            }

            else
            {
               // progressBar.setVisibility(View.INVISIBLE);
                Log.i("tag", "Pet ID is missing in Pet Details");
            }



        });











    }
}