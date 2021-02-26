package com.project.mytelevet.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.mytelevet.R;
import com.project.mytelevet.common.LoginActivity;
import com.project.mytelevet.customer.ViewMyProfileActivity;

public class VetMainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_vetProfile:
                Intent intentView = new Intent(this, ViewVetProfileActivity.class);
                startActivity(intentView);
                return true;
            case R.id.menu_tc:
                Toast.makeText(this, "Menu TC", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_contactUs:
                Toast.makeText(this, "Menu Contact", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logOut:
                firebaseAuth.signOut();   // logout firebaseUser
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            firebaseAuth.signOut();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goToViewVetApp(View view)
    {
        Intent viewVetAppIntent = new Intent(this, ViewVetAppointmentActivity.class);
        startActivity(viewVetAppIntent);
    }



}