package com.project.mytelevet.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.MainActivity;
import com.project.mytelevet.customer.MyProfileActivity;
import com.project.mytelevet.customer.PetInfoActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterVetProfile extends AppCompatActivity {

    EditText tf_vetFirstName, tf_vetLastName, tf_vetPhone, tf_vetAvail, tf_vetDescription;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vet_profile);

        tf_vetFirstName = findViewById(R.id.tf_vetFirstName);
        tf_vetLastName = findViewById(R.id.tf_vetLastName);
        tf_vetPhone = findViewById(R.id.tf_vetPhoneNumber);
        tf_vetAvail = findViewById(R.id.tf_vetAvailability);
        tf_vetDescription = findViewById(R.id.tf_vetDescription);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

    }

    public void saveInfo(View view)
    {
        String first_name = tf_vetFirstName.getText().toString().trim();
        String last_name = tf_vetLastName.getText().toString().trim();
        String phone_no = tf_vetPhone.getText().toString();
        String availability = tf_vetAvail.getText().toString();
        String description = tf_vetDescription.getText().toString();


        if (TextUtils.isEmpty(first_name))
        {
            tf_vetFirstName.setError("First Name is required!");
            return;
        }

        if (TextUtils.isEmpty(last_name))
        {
            tf_vetLastName.setError("Last Name is required!");
            return;
        }

        if (TextUtils.isEmpty(phone_no))
        {
            tf_vetPhone.setError("Phone number is required!");
            return;
        }

        if (TextUtils.isEmpty(availability))
        {
            tf_vetAvail.setError("Availability is required!");
            return;
        }

        if (TextUtils.isEmpty(description))
        {
            tf_vetDescription.setError("Description is required!");
            return;
        }

        DocumentReference documentReference1 = fStore.collection("doctors").document("doctorList");
        Map<String, Object> userList = new HashMap<>();

        userList.put(userID, first_name + " " + last_name);


        documentReference1.set(userList, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("tag", "Doctor List is updated");



                DocumentReference documentReference = fStore.collection("doctors").document(userID);

                Map<String, Object> user = new HashMap<>();
                user.put("FirstName", first_name);
                user.put("LastName", last_name);
                user.put("PhoneNumber", phone_no);
                user.put("Availability", availability);
                user.put("Description", description);
                user.put("VetID", userID);
                user.put("FullName", first_name + " " + last_name);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterVetProfile.this, "Vet Profile is saved.", Toast.LENGTH_SHORT).show();
                        Log.i("tag", "Vet Profile is saved.");
                        startActivity(new Intent(getApplicationContext(), VetMainActivity.class));     // goto main activity
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("tag", "Vet Profile is not saved." + e.getMessage());
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tag", "Doc List is not updated." + e.getMessage());
            }
        });





    }
}