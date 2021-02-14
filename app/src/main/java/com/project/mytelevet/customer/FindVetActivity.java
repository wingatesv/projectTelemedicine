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

import com.project.mytelevet.R;
import com.project.mytelevet.customer.adapter.FindVetAdapter;
import com.project.mytelevet.customer.model.FindVetItem;
import com.project.mytelevet.customer.viewmodel.FindVetViewModel;

import java.util.ArrayList;

public class FindVetActivity extends AppCompatActivity {

    FindVetViewModel model;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_vet);
        setTitle("Find a Vet");


        ProgressBar progressBar = findViewById(R.id.findVet_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        model = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FindVetViewModel.class);

        ArrayList<FindVetItem> arrayOfItems = new ArrayList<>();
        FindVetAdapter adapter = new FindVetAdapter(this, arrayOfItems);

        model.getVetItemLiveData().observe(this, Observable -> {});

        model.getVetItem().observe(this, vetList -> {

            if (vetList != null ) {
                adapter.clear();
                adapter.addAll(vetList);

                ListView listView = findViewById(R.id.findVet_listView);
                listView.setAdapter(adapter);

                progressBar.setVisibility(View.INVISIBLE);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Toast.makeText(FindVetActivity.this,  adapter.getItemName(position), Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(FindVetActivity.this, VetInfoActivity.class);
                        intent.putExtra("VetName",  adapter.getItemName(position));
                        startActivity(intent);
                        finish();



                    }
                });
            } else {

                Log.d("TAG", "awaiting for info");

            }
        });






    }
}