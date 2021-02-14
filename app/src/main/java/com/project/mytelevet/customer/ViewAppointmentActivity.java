package com.project.mytelevet.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.adapter.ViewAppointmentAdapter;
import com.project.mytelevet.customer.model.ViewAppointmentItem;
import com.project.mytelevet.customer.viewmodel.ViewAppointmentViewModel;

import java.util.ArrayList;

public class ViewAppointmentActivity extends AppCompatActivity {

    String userID;

    ListView listView;
    ProgressBar progressBar;

    ViewAppointmentViewModel viewAppointmentViewModel;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        firebaseAuth = FirebaseAuth.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        viewAppointmentViewModel =  new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewAppointmentViewModel.class);



        ArrayList<ViewAppointmentItem> arrayOfItems = new ArrayList<>();
        ViewAppointmentAdapter adapter = new ViewAppointmentAdapter(this, arrayOfItems);

        viewAppointmentViewModel.getAppointmentLiveData(userID).observe(this, Observable -> {});

        viewAppointmentViewModel.getItem().observe(this, item -> {

            if (item != null ) {
                adapter.clear();
                adapter.addAll(item);

                listView = findViewById(R.id.viewAppointment_listView);
                progressBar = findViewById(R.id.viewAppointment_progressBar);


                listView.setAdapter(adapter);

                progressBar.setVisibility(View.INVISIBLE);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(ViewAppointmentActivity.this,  adapter.getItemName(position), Toast.LENGTH_SHORT ).show();

                        String time = adapter.getItemName(position).substring(adapter.getItemName(position).lastIndexOf("at")+3);
                        String vetName = adapter.getItemName(position).substring(0, adapter.getItemName(position).lastIndexOf("at")-1);

                        Intent intentAppointmentInfo = new Intent(getApplicationContext(), AppointmentInfoActivity.class);
                        intentAppointmentInfo.putExtra("time", time);
                        intentAppointmentInfo.putExtra("vetName", vetName);
                        startActivity(intentAppointmentInfo);


                    }
                });
            } else {

                Log.d("TAG", "awaiting for info");

            }
        });




    }
}