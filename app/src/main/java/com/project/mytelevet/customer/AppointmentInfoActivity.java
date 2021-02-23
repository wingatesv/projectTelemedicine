package com.project.mytelevet.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.viewmodel.AppointmentInfoViewModel;

import java.util.Map;

public class AppointmentInfoActivity extends AppCompatActivity {

    TextView lbl_vetName, lbl_time, lbl_petName, lbl_reason, lbl_status, lbl_bookingDate;

    AppointmentInfoViewModel appointmentInfoViewModel;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String vetName = intent.getStringExtra("vetName");
        String time = intent.getStringExtra("time");
        String userID = firebaseAuth.getCurrentUser().getUid();

        appointmentInfoViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(AppointmentInfoViewModel.class);

        if (!vetName.isEmpty() && !time.isEmpty())
        {
            appointmentInfoViewModel.getAppointmentInfoLiveData(vetName, time, userID).observe(this, Observable -> {});

            appointmentInfoViewModel.getAppointmentItem().observe(this, item -> {

                lbl_vetName = findViewById(R.id.lbl_doctor);
                lbl_time = findViewById(R.id.lbl_time);
                lbl_petName = findViewById(R.id.lbl_appointmentPetName);
                lbl_reason = findViewById(R.id.lbl_appointmentReason);
                lbl_status = findViewById(R.id.lbl_appointmentStatus);
                lbl_bookingDate = findViewById(R.id.lbl_bookingDate);

                Map<String, Object> appItem;

                appItem = item;

                lbl_vetName.setText("Attending Vet : Dr." + appItem.get("VetName").toString() );
                lbl_time.setText(appItem.get("AppointmentTime").toString());
                lbl_petName.setText("Pet Name : " + appItem.get("PetName").toString());
                lbl_reason.setText("Appointment Reason : " + appItem.get("Details").toString());
                lbl_status.setText("Status : " + appItem.get("Status").toString());
                lbl_bookingDate.setText("Booking Date : " + appItem.get("BookingDate").toString());
            });

            }











    }
}