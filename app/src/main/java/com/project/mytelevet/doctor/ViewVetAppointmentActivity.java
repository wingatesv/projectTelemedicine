package com.project.mytelevet.doctor;

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
import com.project.mytelevet.customer.AppointmentInfoActivity;
import com.project.mytelevet.customer.ViewAppointmentActivity;
import com.project.mytelevet.customer.adapter.ViewAppointmentAdapter;
import com.project.mytelevet.customer.model.ViewAppointmentItem;
import com.project.mytelevet.doctor.adapter.ViewVetAppointmentAdapter;
import com.project.mytelevet.doctor.model.ViewVetAppointmentItem;
import com.project.mytelevet.doctor.viewmodel.ViewVetAppointmentViewModel;


import java.util.ArrayList;

public class ViewVetAppointmentActivity extends AppCompatActivity {

    String userID;

    ListView listView;
    ProgressBar progressBar;

    ViewVetAppointmentViewModel viewModel;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vet_appointment);

        firebaseAuth = FirebaseAuth.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        viewModel =  new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewVetAppointmentViewModel.class);



        ArrayList<ViewVetAppointmentItem> arrayOfItems = new ArrayList<>();
        ViewVetAppointmentAdapter adapter = new ViewVetAppointmentAdapter(this, arrayOfItems);

        viewModel.getAppointmentLiveData(userID).observe(this, Observable -> {});

        viewModel.getItem().observe(this, item -> {

            if (item != null ) {
                adapter.clear();
                adapter.addAll(item);

                listView = findViewById(R.id.listView_viewVetApp);
                progressBar = findViewById(R.id.progressBar_viewVetApp);


                listView.setAdapter(adapter);

                progressBar.setVisibility(View.INVISIBLE);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(ViewVetAppointmentActivity.this,  adapter.getItemName(position), Toast.LENGTH_SHORT ).show();

                        String time = adapter.getItemName(position).substring(adapter.getItemName(position).lastIndexOf("at")+3);
                        String customerName = adapter.getItemName(position).substring(0, adapter.getItemName(position).lastIndexOf("at")-1);

                        Intent intentAppointmentInfo = new Intent(getApplicationContext(), VetAppointmentInfoActivity.class);
                        intentAppointmentInfo.putExtra("time", time);
                        intentAppointmentInfo.putExtra("customerName", customerName);
                        startActivity(intentAppointmentInfo);




                    }
                });
            } else {

                Log.d("TAG", "awaiting for info");

            }
        });




    }
}