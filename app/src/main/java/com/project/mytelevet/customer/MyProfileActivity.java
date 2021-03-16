package com.project.mytelevet.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class MyProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText tf_firstName, tf_lastName, tf_phoneNumber;

    Spinner spinner;
    private static final String[] paths = {"Johor", "Kedah", "Kelantan", "Malacca", "Negeri Sembilan", "Pahang", "Penang", "Perak", "Perlis", "Sabah"
    , "Sarawak", "Selangor", "Terengganu"};
    String state_spinner = "";



    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    //FirebaseUser user;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        tf_firstName = findViewById(R.id.tf_FirstName);
        tf_lastName = findViewById(R.id.tf_LastName);
        tf_phoneNumber = findViewById(R.id.tf_phoneNo);

        spinner = findViewById(R.id.spinner_state);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyProfileActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

    }

    public void gotoPetInfo (View view)
    {
        String first_name = tf_firstName.getText().toString().trim();
        String last_name = tf_lastName.getText().toString().trim();
        String phone_no = tf_phoneNumber.getText().toString();


        if (state_spinner.isEmpty())
        {
            Log.e("tag", "State spinner is empty");
            Toast.makeText(this, "Please select State", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(first_name))
        {
            tf_firstName.setError("First Name is required!");
            return;
        }

        if (TextUtils.isEmpty(last_name))
        {
            tf_lastName.setError("Last Name is required!");
            return;
        }

        if (TextUtils.isEmpty(phone_no))
        {
            tf_phoneNumber.setError("Phone number is required!");
            return;
        }


        DocumentReference documentReference1 = fStore.collection("users").document("userList");
        Map<String, Object> userList = new HashMap<>();

        userList.put(userID, first_name + " " + last_name);


        documentReference1.set(userList, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("tag", "User List is updated");



                DocumentReference documentReference = fStore.collection("users").document(userID);

                Map<String, Object> user = new HashMap<>();
                user.put("FirstName", first_name);
                user.put("LastName", last_name);
                user.put("PhoneNumber", phone_no);
                user.put("State", state_spinner);
                user.put("UserID", userID);
                user.put("FullName", first_name + " " + last_name);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MyProfileActivity.this, "User Profile is saved.", Toast.LENGTH_SHORT).show();
                        Log.i("tag", "User Profile is saved.");
                        startActivity(new Intent(getApplicationContext(), PetInfoActivity.class));     // goto main activity
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("tag", "User Profile is not saved." + e.getMessage());
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tag", "User List is not updated." + e.getMessage());
            }
        });






    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        state_spinner = parent.getItemAtPosition(position).toString();
        Log.i("tag", state_spinner);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}