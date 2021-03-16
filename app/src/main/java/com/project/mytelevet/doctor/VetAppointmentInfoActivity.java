package com.project.mytelevet.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.MainActivity;
import com.project.mytelevet.doctor.viewmodel.VetAppointmentInfoViewModel;

import java.util.HashMap;
import java.util.Map;

public class VetAppointmentInfoActivity extends AppCompatActivity {

    TextView lbl_customerName, lbl_time, lbl_petName, lbl_reason, lbl_status, lbl_bookingDate;

    VetAppointmentInfoViewModel viewModel;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String appID, customerID, petID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_appointment_info);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String customerName = intent.getStringExtra("customerName");
        String time = intent.getStringExtra("time");
        String userID = firebaseAuth.getCurrentUser().getUid();

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(VetAppointmentInfoViewModel.class);

        if (!customerName.isEmpty() && !time.isEmpty()) {
            viewModel.getAppointmentInfoLiveData(customerName, time, userID).observe(this, Observable -> {
            });

            viewModel.getAppointmentItem().observe(this, item -> {

                lbl_customerName = findViewById(R.id.lbl_vetAppInfoCustomerName);
                lbl_time = findViewById(R.id.lbl_vetAppInfoTime);
                lbl_petName = findViewById(R.id.lbl_vetAppInfoPetName);
                lbl_reason = findViewById(R.id.lbl_vetAppInfoReason);
                lbl_status = findViewById(R.id.lbl_vetAppInfoStatus);
                lbl_bookingDate = findViewById(R.id.lbl_vetAppInfoBookingDate);

                Map<String, Object> appItem;

                appItem = item;

                lbl_customerName.setText("Customer : " + appItem.get("CustomerName").toString());
                lbl_time.setText("Appointment Time : " + appItem.get("AppointmentTime").toString());
                lbl_petName.setText("Pet Name : " + appItem.get("PetName").toString());
                lbl_reason.setText("Appointment Reason : " + appItem.get("Details").toString());
                lbl_status.setText("Status : " + appItem.get("Status").toString());
                lbl_bookingDate.setText("Booking Date : " + appItem.get("BookingDate").toString());
                appID = appItem.get("AppID").toString();
                customerID = appItem.get("CustomerID").toString();




                lbl_customerName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intentAppointmentInfo = new Intent(getApplicationContext(), ViewCustomerProfileActivity.class);
                        intentAppointmentInfo.putExtra("customerID", customerID);
                        startActivity(intentAppointmentInfo);

                    }
                });

                lbl_petName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intentAppointmentInfo = new Intent(getApplicationContext(), ViewCustomerPetProfileActivity.class);
                        intentAppointmentInfo.putExtra("customerID", customerID);
                        intentAppointmentInfo.putExtra("petName", lbl_petName.getText().toString().substring(11));
                        startActivity(intentAppointmentInfo);

                    }
                });







            });

        }


    }

    public void confirmVetApp(View view)
    {
        if (!appID.isEmpty())
        {
            DocumentReference documentReference = firebaseFirestore.collection("appointments").document(appID);
            Map<String, Object> update = new HashMap<>();

          update.put("Status", "Confirmed");
          documentReference.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                  Log.i("tag", "Appointment is confirmed");
                  Toast.makeText(getApplicationContext(), "Appointment is confirmed", Toast.LENGTH_SHORT).show();
                  Toast.makeText(getApplicationContext(), "Customer will be notified", Toast.LENGTH_SHORT).show();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.e("tag", e.getMessage());
              }
          });


        }

        else {
            Log.i("tag", "appID is null");
        }
    }

    public void cancelVetApp(View view)
    {
        if (!appID.isEmpty())
        {
            DocumentReference documentReference = firebaseFirestore.collection("appointments").document(appID);
            Map<String, Object> update = new HashMap<>();

            update.put("Status", "Cancelled");
            documentReference.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("tag", "Appointment is cancelled");
                    Toast.makeText(getApplicationContext(), "Appointment is cancelled", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VetMainActivity.class));     // goto main activity
                    finish();

                    // pop up window to type in reason for cancellation and will send to customer
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("tag", e.getMessage());
                }
            });


        }

        else {
            Log.i("tag", "appID is null");
        }
    }



    }