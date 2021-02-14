package com.project.mytelevet.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.model.MyPetsItem;
import com.project.mytelevet.customer.ui.mypets.MyPetsAdapter;
import com.project.mytelevet.customer.ui.mypets.MyPetsViewModel;
import com.project.mytelevet.customer.viewmodel.VetInfoViewModel;
import com.project.mytelevet.customer.viewmodel.ViewMyPetViewModel;
import com.project.mytelevet.customer.viewmodel.ViewMyProfileViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VetInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String vetName = "";
    String vetID = "";

    TextView lbl_vetName, lbl_description, lbl_available;

    Spinner spinner;

    EditText tf_reason, tf_time;

    Button btn_bookAppointment;

    ProgressBar progressBar;

    ImageView vetPic;

    VetInfoViewModel vetInfoViewModel;
    MyPetsViewModel myPetsViewModel;
    ViewMyPetViewModel viewMyPetViewModel;
    ViewMyProfileViewModel viewMyProfileModel;

    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;

    String customerID;

     String petName = "";
    String customerName;

    FirebaseAuth firebaseAuth;

    String currentDate;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_info);

        tf_reason = findViewById(R.id.tf_reasonAppointment);
        tf_time = findViewById(R.id.tf_preferredTime);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        customerID = firebaseAuth.getCurrentUser().getUid();



        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        vetName = intent.getStringExtra("VetName");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        currentDate = df.format(c);

        vetInfoViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(VetInfoViewModel.class);

        vetInfoViewModel.getVetInfoLiveData(vetName).observe(this, Observable -> {});


        vetInfoViewModel.getVetInfoItem().observe(this, item -> {

           lbl_vetName = findViewById(R.id.lbl_vetFullName);
           lbl_description = findViewById(R.id.lbl_vetDesciption);
           vetPic = findViewById(R.id.vetInfo_profilePic);
           progressBar = findViewById(R.id.vetInfo_progressBar);
           lbl_available = findViewById(R.id.lbl_vetAvailability);

            Map<String, Object> vetItem;

            vetItem = item;

            lbl_vetName.setText("Dr. " + vetItem.get("FullName").toString());
            lbl_description.setText(vetItem.get("Description").toString());
            lbl_available.setText(vetItem.get("Availability").toString());

            if (vetItem.containsKey("VetID") && !vetItem.get("VetID").toString().isEmpty()) {
                vetID = vetItem.get("VetID").toString();
            }
                else
                {
                    Log.i("tag", "VetID is empty in item list");
                }

            if (!vetID.isEmpty())
            {
                StorageReference petPicRef = storageReference.child("users/" + vetID + "/profilePic.jpg");

                petPicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(vetPic);
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.i("tag", "pet id is" + vetID);

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       progressBar.setVisibility(View.INVISIBLE);
                        Log.e("tag", e.getMessage());
                    }
                });

            }

            else
            {
                //progressBar.setVisibility(View.INVISIBLE);
                Log.i("tag", "Pet ID is missing in Pet Details");
            }



        });

        myPetsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MyPetsViewModel.class);

        viewMyPetViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewMyPetViewModel.class);

        viewMyProfileModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewMyProfileViewModel.class);


        ArrayList<MyPetsItem> arrayOfItems = new ArrayList<>();
        MyPetsAdapter adapter = new MyPetsAdapter(this, arrayOfItems);



        myPetsViewModel.getMyPetsFragmentLiveData().observe(this, Observable -> {});



        myPetsViewModel.getMyPets().observe(this, myPetsItems -> {
            if (myPetsItems != null ) {

                adapter.clear();
                adapter.addAll(myPetsItems);
                ArrayList<String> petList = new ArrayList<>();


                for(int i = 0; i < adapter.getCount(); i++)
                {
                   petList.add(adapter.getItemName(i));
                }

                String[] paths = new String[petList.size()];

                for (int i = 0; i< petList.size(); i++)
                {
                    paths[i] = petList.get(i);
                }


                ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(VetInfoActivity.this,android.R.layout.simple_spinner_item, paths);
                spinner = findViewById(R.id.spinner_petList);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter1);
                spinner.setOnItemSelectedListener(this);



                viewMyProfileModel.getViewMyProfileLiveData(customerID).observe(this, Observable -> {});

                viewMyProfileModel.getMyProfileItem().observe(this, item -> {



                    Map<String, Object> myProfile;

                    myProfile = item;

                    customerName = myProfile.get("FullName").toString();




                });







                btn_bookAppointment = findViewById(R.id.btn_bookAppointment);
                btn_bookAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String reason = tf_reason.getText().toString().trim();
                        String time = tf_time.getText().toString();

                        if (reason.isEmpty() || time.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Details are incomplete.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.i("tag", "Nigga pls "  + petName + customerID+ customerName + currentDate);

                       DocumentReference documentReference = firebaseFirestore.collection("appointments").document();

                       Map<Object, String> booking = new HashMap<>();
                       booking.put("CustomerName", customerName);
                       booking.put("CustomerID", customerID);
                       booking.put("BookingDate", currentDate);
                       booking.put("PetName", petName);
                       booking.put("Details", reason);
                       booking.put("Status", "pending");
                       booking.put("AppointmentTime", time);
                       booking.put("VetName", vetName);
                       booking.put("VetID", vetID);

                       documentReference.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {

                               DocumentReference documentReference1 = firebaseFirestore.collection("users").document(customerID).collection("appointment").document("appointmentList");

                               Map<Object, String> receipt = new HashMap<>();
                               receipt.put(currentDate + "_" + time, vetName + " at " + time);

                               documentReference1.set(receipt, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {


                                       Toast.makeText(getApplicationContext(), "Booking success", Toast.LENGTH_SHORT).show();

                                       Intent intentMain = new Intent(VetInfoActivity.this, ViewAppointmentActivity.class);
                                       startActivity(intentMain);

                                       finish();


                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Log.e("tag", e.getMessage());
                                   }
                               });



                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.e("tag", e.getMessage());

                           }
                       });



                    }
                });




            } else {

                Log.d("TAG", "awaiting for info");

            }
        });
        //Log.i("tag", petName);



















    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        petName = parent.getItemAtPosition(position).toString();
        Log.i("tag", "pet name is " + petName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}