package com.project.mytelevet.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.mytelevet.R;
import com.project.mytelevet.common.VideoCallActivity;
import com.project.mytelevet.customer.viewmodel.AppointmentInfoViewModel;

import java.util.HashMap;
import java.util.Map;

public class AppointmentInfoActivity extends AppCompatActivity {

    TextView lbl_vetName, lbl_time, lbl_petName, lbl_reason, lbl_status, lbl_bookingDate;
    String userID, vetID, appID;

    AppointmentInfoViewModel appointmentInfoViewModel;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String vetName = intent.getStringExtra("vetName");
        String time = intent.getStringExtra("time");
        userID = firebaseAuth.getCurrentUser().getUid();

        appointmentInfoViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(AppointmentInfoViewModel.class);

        if (!vetName.isEmpty() && !time.isEmpty())
        {
            appointmentInfoViewModel.getAppointmentInfoLiveData(vetName, time, userID).observe(this, Observable -> {});

            appointmentInfoViewModel.getAppointmentItem().observe(this, item -> {

                lbl_vetName = findViewById(R.id.lbl_myAppInfoCustomerName);
                lbl_time = findViewById(R.id.lbl_myAppInfoTime);
                lbl_petName = findViewById(R.id.lbl_myAppInfoPetName);
                lbl_reason = findViewById(R.id.lbl_myAppInfoReason);
                lbl_status = findViewById(R.id.lbl_myAppInfoStatus);
                lbl_bookingDate = findViewById(R.id.lbl_myAppInfoBookingDate);

                Map<String, Object> appItem;

                appItem = item;

                lbl_vetName.setText("Attending Vet : Dr." + appItem.get("VetName").toString() );
                lbl_time.setText(appItem.get("AppointmentTime").toString());
                lbl_petName.setText("Pet Name : " + appItem.get("PetName").toString());
                lbl_reason.setText("Appointment Reason : " + appItem.get("Details").toString());
                lbl_status.setText("Status : " + appItem.get("Status").toString());
                lbl_bookingDate.setText("Booking Date : " + appItem.get("BookingDate").toString());
                appID = appItem.get("AppID").toString();
                vetID  = appItem.get("VetID").toString();
            });

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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));     // goto main activity
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.appMenu_videoCall:
                if (vetID!= null )
                {
                    Intent intentView = new Intent(this, VideoCallActivity.class);
                    intentView.putExtra("channelName",  vetID + "_" + userID);
                    startActivity(intentView);
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}