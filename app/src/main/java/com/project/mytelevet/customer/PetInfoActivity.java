package com.project.mytelevet.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.project.mytelevet.R;

public class PetInfoActivity extends AppCompatActivity {

    EditText tf_petName;

    ImageButton btn_dog, btn_cat;

    String pet_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);

        tf_petName = findViewById(R.id.tf_petName);
        btn_dog = findViewById(R.id.btn_dog);
        btn_cat = findViewById(R.id.btn_cat);

        btn_dog.setOnClickListener(v -> {

            pet_type  = "dog";
            Toast.makeText(this,pet_type, Toast.LENGTH_SHORT).show();

        });

        btn_cat.setOnClickListener(v ->
        {
            pet_type  = "cat";
            Toast.makeText(this,pet_type, Toast.LENGTH_SHORT).show();
        });
    }

    public void gotoPetDetails(View view)
    {
        String pet_name = tf_petName.getText().toString().trim();
        Intent intent = new Intent(this, PetDetailsActivity.class);

        intent.putExtra("PetName", pet_name);
        intent.putExtra("PetType", pet_type);

        startActivity(intent);
        //finish();





    }
}