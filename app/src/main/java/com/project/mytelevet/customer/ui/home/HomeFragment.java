package com.project.mytelevet.customer.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.mytelevet.customer.FindVetActivity;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.ViewAppointmentActivity;

public class HomeFragment extends Fragment {



    TextView verifyEmail, lbl_username;


    ListView listView;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();




   private HomeViewModel homeViewModel;     // create view model

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);



        homeViewModel.getHomeLiveData().observe(getActivity(), Observable -> {});

        homeViewModel.getText().observe(getActivity(), userName -> {

            lbl_username = root.findViewById(R.id.text_home);
            lbl_username.setText("Hello " + userName + "!");

        });



        homeViewModel.getVerifyEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                verifyEmail = root.findViewById(R.id.lbl_emailVer);
                verifyEmail.setText(s);

                if (!firebaseUser.isEmailVerified())
                {

                    verifyEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Verification email is sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Verification email is not sent." + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });


                }



            }
        });


        homeViewModel.getHomeList().observe(getActivity(), homeList ->{

            listView = root.findViewById(R.id.home_listView);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, homeList);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {

                String component = listView.getItemAtPosition(position).toString();


                if (component.matches("Find a Veterinarian"))
                {
                    Intent intent = new Intent(getActivity(), FindVetActivity.class);
                    startActivity(intent);

                }

                if (component.matches("Upcoming Appointment"))
                {
                    Intent intent = new Intent(getActivity(), ViewAppointmentActivity.class);
                    startActivity(intent);
                }




            });

        });












        return root;



    }







}