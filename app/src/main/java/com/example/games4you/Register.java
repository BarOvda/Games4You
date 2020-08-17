package com.example.games4you;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.games4you.logic.User;
import com.example.games4you.services.MyLocationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Register extends AppCompatActivity {
    EditText mEmailId, mPassword, muserName;
    Button btnSignUp;
    static Register instance;
    ProgressBar progressBar;
    FirebaseAuth mFirebaseAuth;

    private FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static Register getInstance() {
        return instance;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        instance = this;
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mEmailId = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        muserName = findViewById(R.id.user_name_text);

        btnSignUp = findViewById(R.id.signUpButton);

        progressBar = findViewById((R.id.progressBar3));
        progressBar.setVisibility(View.INVISIBLE);

        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailId.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String userName = muserName.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmailId.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                } else if (password.length() < 6) {
                    mPassword.setError("Password Must Be At Least 6 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                final User user = new User(email, userName, "");
                mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();

                            db.collection("users")
                                    .document(user.getEmail())
                                    .set(user)
                            ;

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(Register.this, "Error! " + task.getException(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

    }


}