package com.project.mytelevet.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.mytelevet.R;
import com.project.mytelevet.customer.MainActivity;
import com.project.mytelevet.customer.MyProfileActivity;
import com.project.mytelevet.doctor.RegisterVetProfile;

public class RegisterActivity extends AppCompatActivity {

    EditText tf_email, tf_password, tf_confirmPwd;

    TextView lbl_registerVet;

    Button btn_register;

    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tf_email = findViewById(R.id.tf_registerEmail);
        tf_password = findViewById(R.id.tf_registerPassword);
        tf_confirmPwd = findViewById(R.id.tf_confirmPwd);
        lbl_registerVet = findViewById(R.id.lbl_registerVet);
        btn_register = findViewById(R.id.btn_register);





        progressBar = findViewById(R.id.progressBar_register);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null)     // check for existing user
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }




    }


    public void gotoLogin(View view)
    {


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void registerCustomer(View view)
    {
        String email = tf_email.getText().toString().trim();
        String password = tf_password.getText().toString().trim();
        String confirm_pwd = tf_confirmPwd.getText().toString().trim();


        if (TextUtils.isEmpty(email))   // true if email is not written
        {
            tf_email.setError("Email is required!");
            return;
        }

        if (TextUtils.isEmpty(password))   // true if password is not written
        {
            tf_password.setError("Password is required!");
            return;
        }

        if (TextUtils.isEmpty(confirm_pwd))   // true if confirm password is not written
        {
            tf_confirmPwd.setError("Confirm password is required!");
            return;
        }

        if (password.length() < 6)      // true if password is less than 6 chars
        {
            tf_password.setError("Password must be more than 6 characters");
            return;
        }

        if (!password.contentEquals(confirm_pwd))   // check password & confirm password is same or not
        {
            Toast.makeText(this, "Passwords are not equal.", Toast.LENGTH_SHORT).show();
            tf_password.getText().clear();
            tf_confirmPwd.getText().clear();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);    // set progress bar visible

        // register user in firebase



        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                            // send verification link

                            FirebaseUser user = fAuth.getCurrentUser();

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification email is sent.", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));     // goto main activity
                                    finish();



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Verification email is not sent." + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });



                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }


                    }
                });




    }

    public void registerVet(View view)
    {

        String email = tf_email.getText().toString().trim();
        String password = tf_password.getText().toString().trim();
        String confirm_pwd = tf_confirmPwd.getText().toString().trim();


        if (TextUtils.isEmpty(email))   // true if email is not written
        {
            tf_email.setError("Email is required!");
            return;
        }

        if (TextUtils.isEmpty(password))   // true if password is not written
        {
            tf_password.setError("Password is required!");
            return;
        }

        if (TextUtils.isEmpty(confirm_pwd))   // true if confirm password is not written
        {
            tf_confirmPwd.setError("Confirm password is required!");
            return;
        }

        if (password.length() < 6)      // true if password is less than 6 chars
        {
            tf_password.setError("Password must be more than 6 characters");
            return;
        }

        if (!password.contentEquals(confirm_pwd))   // check password & confirm password is same or not
        {
            Toast.makeText(this, "Passwords are not equal.", Toast.LENGTH_SHORT).show();
            tf_password.getText().clear();
            tf_confirmPwd.getText().clear();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);    // set progress bar visible

        // register user in firebase



        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                            // send verification link

                            FirebaseUser user = fAuth.getCurrentUser();

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification email is sent.", Toast.LENGTH_SHORT).show();


                                    startActivity(new Intent(getApplicationContext(), RegisterVetProfile.class));     // goto main activity
                                   // finish();




                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Verification email is not sent." + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });



                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }


                    }
                });





    }
}