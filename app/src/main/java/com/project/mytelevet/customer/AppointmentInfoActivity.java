package com.project.mytelevet.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.mytelevet.R;
import com.project.mytelevet.common.VideoCallActivity;
import com.project.mytelevet.customer.viewmodel.AppointmentInfoViewModel;

import java.util.Map;

public class AppointmentInfoActivity extends AppCompatActivity {

    TextView lbl_vetName, lbl_time, lbl_petName, lbl_reason, lbl_status, lbl_bookingDate;
    String userID, vetID;

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

                vetID  = appItem.get("VetID").toString();
            });

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