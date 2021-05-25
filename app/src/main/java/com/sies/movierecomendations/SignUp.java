package com.sies.movierecomendations;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    EditText email,password,name;
    TextView signin;
    Button register;
    ProgressBar pgbar;
    private FirebaseAuth mAuth;
    private String Name, emailId, pass;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        register = findViewById(R.id.register);
        pgbar = findViewById(R.id.progressBar2);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(v -> startActivity(new Intent(SignUp.this, SignIn.class)));

        register.setOnClickListener(v -> {

            Name = name.getText().toString().trim();
            pass = password.getText().toString().trim();
            emailId = email.getText().toString().trim();

            if(TextUtils.isEmpty(Name)) {
                name.setError("Name Required");
                name.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(emailId)) {
                email.setError("Email Required");
                email.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(pass)) {
                password.setError("Password Required");
                password.requestFocus();
                return;
            }
            if(password.length() < 8) {
                password.setError("Password should be of atleast 8 characters");
                return;
            }

            pgbar.setVisibility(View.VISIBLE);
            Sign_Up();
        });
    }
    private void Sign_Up() {
        mAuth.createUserWithEmailAndPassword(emailId, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(this, work -> {
                            if(work.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(SignUp.this, "Please Check your Email for Verification link",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, SignIn.class));
                                finish();
                            }else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                pgbar.setVisibility(View.GONE);
                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        if(Objects.requireNonNull(task.getException()).toString().contains("The email address is already in use by another account")) {
                            Toast.makeText(SignUp.this, "User already exists please log in", Toast.LENGTH_SHORT).show();
                            Runnable mUpdateTimeTask = () -> {
                                startActivity(new Intent(SignUp.this, SignIn.class));
                                finish();
                            };
                            mHandler.postDelayed(mUpdateTimeTask, 200);
                        }
                        pgbar.setVisibility(View.GONE);
                    }
                });
    }
}