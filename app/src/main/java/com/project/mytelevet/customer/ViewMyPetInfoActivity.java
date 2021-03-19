package com.project.mytelevet.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Calendar;
import com.google.firebase.auth.FirebaseAuth;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.viewmodel.ViewMyPetViewModel;

import java.util.HashMap;
import java.util.Map;

public class ViewMyPetInfoActivity extends AppCompatActivity {

    ViewMyPetViewModel viewMyPetViewModel;

    EditText tf_petName, tf_breed, tf_DOB;
    DatePickerDialog picker;
    RadioGroup rg_gender, rg_size, rg_petType, rg_condition;

    Button btn_update;

    FirebaseAuth firebaseAuth;


    String gender = "";
    String size = "";
    String condition = "";
    String petType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_pet_info);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String petName = intent.getStringExtra("petName");
        String userID = firebaseAuth.getCurrentUser().getUid();

        viewMyPetViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ViewMyPetViewModel.class);

        viewMyPetViewModel.getViewMyPetLiveData(petName, userID).observe(this, Observable -> {});

        viewMyPetViewModel.getMyPetItem().observe(this, item -> {

            tf_petName = findViewById(R.id.tf_ViewPetName);
            tf_breed = findViewById(R.id.tf_ViewBreed);
            tf_DOB = findViewById(R.id.tf_ViewDOB);
            tf_DOB.setInputType(InputType.TYPE_NULL);
            rg_gender = findViewById(R.id.rg_ViewGender);
            rg_size = findViewById(R.id.rg_ViewSize);
            rg_petType = findViewById(R.id.rg_ViewPetType);
            rg_condition = findViewById(R.id.rg_ViewCondition);

            tf_DOB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(ViewMyPetInfoActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    tf_DOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, year, month, day);
                    picker.show();
                }
            });


            Map<String, Object> myPet = new HashMap<>();

            myPet = item;

            gender = myPet.get("Gender").toString();
            size = myPet.get("Size").toString();
            condition = myPet.get("Condition").toString();
            petType = myPet.get("PetType").toString();

            if (gender.contentEquals("Male"))
            {
                rg_gender.check(R.id.rb_ViewMale);
            }

                else if (gender.contentEquals("Female"))
                {
                    rg_gender.check(R.id.rb_ViewFemale);
                }

            if (petType.contentEquals("dog"))
            {
                rg_petType.check(R.id.rb_ViewDog);
            }

                else if (petType.contentEquals("cat"))
                {
                    rg_petType.check(R.id.rb_ViewCat);
                }

            if (condition.contentEquals("Neutered"))
            {
                rg_condition.check(R.id.rb_ViewYes);
            }

                    else if (condition.contentEquals("Not neutered"))
                {
                    rg_condition.check(R.id.rb_ViewNo);
                }

            if (size.contentEquals("Small"))
            {
                rg_size.check(R.id.rb_ViewSmall);
            }

                else if (size.contentEquals("Medium"))
                {
                    rg_size.check(R.id.rb_ViewMedium);
                }

                    else if (size.contentEquals("Large"))
                    {
                        rg_size.check(R.id.rb_ViewLarge);
                    }



            tf_petName.setText(myPet.get("PetName").toString());
            tf_breed.setText(myPet.get("Breed").toString());
            tf_DOB.setText(myPet.get("DOB").toString());

        });

        btn_update = findViewById(R.id.btn_ViewMyPetUpdate);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String update_petName = tf_petName.getText().toString().trim();
                String update_breed = tf_breed.getText().toString().trim();
                String update_DOB = tf_DOB.getText().toString().trim();
                String update_petType = petType;
                String update_size = size;
                String update_condition = condition;
                String update_gender = gender;

                if (update_petName.isEmpty() || update_breed.isEmpty() || update_DOB.isEmpty())
                {
                    Toast.makeText(ViewMyPetInfoActivity.this, "Details are incomplete.", Toast.LENGTH_SHORT).show();
                    return;
                }

               if( viewMyPetViewModel.updateData(update_petName, update_breed, update_DOB, update_petType, update_size, update_condition, update_gender))
               {
                   Toast.makeText(ViewMyPetInfoActivity.this, "Pet Profile is updated", Toast.LENGTH_SHORT).show();
               }

                   else
                   {
                       Toast.makeText(ViewMyPetInfoActivity.this, "Pet Profile is not updated", Toast.LENGTH_SHORT).show();
                   }

                startActivity(new Intent(ViewMyPetInfoActivity.this, MainActivity.class));     // goto main activity
                finish();





            }
        });





    }

    public void checkGender (View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_ViewMale:
                if (checked) {
                    gender = "Male";
                    Toast.makeText(ViewMyPetInfoActivity.this, gender , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_ViewFemale:
                if (checked) {
                    gender = "Female";
                    Toast.makeText(ViewMyPetInfoActivity.this, gender , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void checkPetType (View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_ViewDog:
                if (checked) {
                    petType = "dog";
                    Toast.makeText(ViewMyPetInfoActivity.this, petType , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_ViewCat:
                if (checked) {
                    petType = "cat";
                    Toast.makeText(ViewMyPetInfoActivity.this, petType , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void checkSize (View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_ViewSmall:
                if (checked) {
                    size = "Small";
                    Toast.makeText(ViewMyPetInfoActivity.this, size , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_ViewMedium:
                if (checked) {
                    size = "Medium";
                    Toast.makeText(ViewMyPetInfoActivity.this, size , Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.rb_ViewLarge:
                if (checked) {
                    size = "Large";
                    Toast.makeText(ViewMyPetInfoActivity.this, size , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void checkCondition(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_ViewYes:
                if (checked) {
                    condition = "Neutered";
                    Toast.makeText(ViewMyPetInfoActivity.this, condition , Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rb_ViewNo:
                if (checked) {
                    condition = "Not neutered";
                    Toast.makeText(ViewMyPetInfoActivity.this, condition , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}