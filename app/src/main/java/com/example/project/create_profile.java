package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class create_profile extends Activity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        mAuth = FirebaseAuth.getInstance();

    }

    TextView  pass, email;
    Activity a;
    Button b;
    public void btnclick(View v){
        a= this;
        b= findViewById(R.id.create_profile);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        if(email.getText().toString().equals(""))
        {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(pass.getText().toString().equals("")){
            pass.setError("password is required");
            pass.requestFocus();
            return;
        }

        b.setEnabled(false);
        b.setClickable(false);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent obj = new Intent(getApplicationContext(), Add_information.class);
                            finish();
                            startActivity(obj);


                        } else {
                            b.setEnabled(true);
                            b.setClickable(true);
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                email.requestFocus();
                                email.setError("Email format not correct");
                            }
                            else if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                email.requestFocus();
                                email.setError("Email already registered");
                            }
                            else if(task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                pass.requestFocus();
                                pass.setError("weak Password");
                            }
                            else
                                Toast.makeText(a, "Failed "+ task.getException().toString(), Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

    }
}
