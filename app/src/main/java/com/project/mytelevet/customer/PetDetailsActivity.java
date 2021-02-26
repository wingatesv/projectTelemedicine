package com.project.mytelevet.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.project.mytelevet.R;

import java.util.HashMap;
import java.util.Map;

public class PetDetailsActivity extends AppCompatActivity {

    EditText tf_dob, tf_breed;

    RadioGroup rg_gender, rg_size, rg_condition;

    Button btn_save;

    String gender = "";
    String size = "";
    String condition = "";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        Intent intent = getIntent();
        String pet_name = intent.getStringExtra("PetName");
        String pet_type = intent.getStringExtra("PetType");
        ((TextView)findViewById(R.id.lbl_petName)).setText(pet_name);


        tf_dob = findViewById(R.id.tf_dob);
        tf_breed = findViewById(R.id.tf_breed);
        rg_gender = findViewById(R.id.rb_gender);
        rg_size = findViewById(R.id.rb_size);
        rg_condition = findViewById(R.id.rb_neutered);
        btn_save = findViewById(R.id.btn_save);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();




        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dob = tf_dob.getText().toString().trim();
                String breed = tf_breed.getText().toString().trim();

                if (TextUtils.isEmpty(dob))   // true if email is not written
                {
                    tf_dob.setError("Email is required!");
                    return;
                }

                if (TextUtils.isEmpty(breed))   // true if email is not written
                {
                    tf_breed.setError("Email is required!");
                    return;
                }

                if (gender.isEmpty())
                {
                    Toast.makeText(PetDetailsActivity.this, "Gender is not selected", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (size.isEmpty())
                {
                    Toast.makeText(PetDetailsActivity.this, "Size is not selected", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (condition.isEmpty())
                {
                    Toast.makeText(PetDetailsActivity.this, "Condition is not selected", Toast.LENGTH_SHORT).show();
                    return;

                }

                DocumentReference documentReference = fStore.collection("users").document(userID).collection("pets").document();



                Map<String, Object> pet = new HashMap<>();

                pet.put("PetName", pet_name);
                pet.put("PetType", pet_type);
                pet.put("DOB", dob);
                pet.put("Gender", gender);
                pet.put("Breed", breed);
                pet.put("Size", size);
                pet.put("Condition", condition);
                pet.put("PetID", documentReference.getId());



                documentReference.set(pet).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PetDetailsActivity.this, "Pet Profile is saved.", Toast.LENGTH_SHORT).show();
                        Log.i("tag", "Pet Profile is saved");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("tag", "Pet Profile is not updated." + e.getMessage());
                    }
                });

                DocumentReference documentReference1 = fStore.collection("users").document(userID).collection("pets").document("petList_" + userID);

                Map<String, Object> petList = new HashMap<>();

                petList.put(documentReference.getId(), pet_name);




                documentReference1.set(petList, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("tag", "Pet List is updated");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));     // goto main activity
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("tag", "Pet List is not updated." + e.getMessage());
                    }
                });





            }
        });

    }

    public void checkGender (View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_male:
                if (checked)
                    gender = "Male";
                break;
            case R.id.rb_female:
                if (checked)
                    gender = "Female";
                break;
        }
    }

    public void checkSize (View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_small:
                if (checked)
                    size = "Small";
                break;
            case R.id.rb_medium:
                if (checked)
                    size = "Medium";

                break;

            case R.id.rb_large:
                if (checked)
                    size = "Large";

                break;
        }
    }

    public void checkCondition(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_yes:
                if (checked)
                    condition = "Neutered";


                break;
            case R.id.rb_no:
                if (checked)
                    condition = "Not neutered";

                break;
        }
    }
}