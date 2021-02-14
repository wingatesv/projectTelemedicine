package com.project.mytelevet.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.viewmodel.ViewMyPetViewModel;
import com.squareup.picasso.Picasso;
import java.util.Map;
import java.util.Objects;

public class ViewMyPetActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;


    String petName;
    TextView lbl_petName, lbl_breedDOB, lbl_genderConditionSize, lbl_updateImage;


    ImageView petPic;

    ViewMyPetViewModel viewMyPetViewModel;

    ProgressBar progressBar;

    ListView listView;
    String petID = "";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_pet);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();



        Intent intent = getIntent();
        petName = intent.getStringExtra("petName");

        petPic = findViewById(R.id.imageView_petImage);
        progressBar = findViewById(R.id.ViewMyPet_progressBar);




        lbl_updateImage = findViewById(R.id.lbl_UpdatePetPic);
        lbl_updateImage.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setType("image/*");
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent1, "Select Picture"), PICK_IMAGE);
        });



        viewMyPetViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewMyPetViewModel.class);

        viewMyPetViewModel.getViewMyPetLiveData(petName, fAuth.getCurrentUser().getUid()).observe(this, Observable -> {});


        viewMyPetViewModel.getMyPetItem().observe(this, item -> {

            lbl_petName = findViewById(R.id.lbl_ViewPetName);
            lbl_breedDOB = findViewById(R.id.lbl_ViewBreedDOB);
            lbl_genderConditionSize = findViewById(R.id.lbl_ViewGenderConditionSize);

            Map<String, Object> myPet;

            myPet = item;

            lbl_petName.setText(myPet.get("PetName").toString());
            lbl_breedDOB.setText(myPet.get("Breed").toString() + ", " + myPet.get("DOB").toString());
            lbl_genderConditionSize.setText(myPet.get("Gender").toString() + "-" + myPet.get("Condition").toString() + ", " + myPet.get("Size").toString());

            if (myPet.containsKey("PetID") && !myPet.get("PetID").toString().isEmpty()) {
                petID = myPet.get("PetID").toString();
            }
            else
            {
                Log.i("tag", "PetID is empty in item list");
            }

            if (!petID.isEmpty())
            {
                StorageReference petPicRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/pets/" + petID + "Pic.jpg");

                petPicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(petPic);
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.i("tag", "pet id is" + petID);

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("tag", e.getMessage());
                    }
                });

            }

            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i("tag", "Pet ID is missing in Pet Details");
            }



        });


        viewMyPetViewModel.getMyPetList().observe(this, myPetList ->{

            listView = findViewById(R.id.listView_MyPet);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, myPetList);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {

                String component = listView.getItemAtPosition(position).toString();


                if (component.matches("General Information"))
                {

                    Intent intentViewMyPet = new Intent(ViewMyPetActivity.this, ViewMyPetInfoActivity.class);
                    intentViewMyPet.putExtra("petName", petName);
                    startActivity(intentViewMyPet);

                }

                if (component.matches("Remove This Pet"))
                {
                    viewMyPetViewModel.deleteData(petName);
                    Toast.makeText(ViewMyPetActivity.this, "Pet is removed", Toast.LENGTH_SHORT).show();
                    Intent intentMainActivity = new Intent(ViewMyPetActivity.this, MainActivity.class);
                    startActivity(intentMainActivity);
                    finish();
                }




            });

        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE)
        {
            if (data != null) {
                Uri imageUri = data.getData();
                petPic.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }

        }

    }

    private void uploadImageToFirebase(Uri imageUri)
    {

        String userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        if (!petID.isEmpty())
        {

            StorageReference fileRef = storageReference.child("users/" + userID + "/pets/" + petID + "Pic.jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(petPic);
                    Log.i("tag", petID +" Pet Pic is uploaded.");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("tag", e.getMessage());
                }
            })).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("tag", e.getMessage());
                }
            });
        }
        else
        {
            Log.i("tag", "pet ID is empty in upload image to firebase");
        }

    }



}