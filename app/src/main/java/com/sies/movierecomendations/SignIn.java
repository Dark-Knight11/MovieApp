package com.sies.movierecomendations;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    EditText email,pass;
    TextView signup;
    Button login;
    ProgressBar pgbar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String emailId, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null && user.isEmailVerified()) {
            Toast.makeText(SignIn.this, "You are logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }
        else {
            email = findViewById(R.id.email);
            pass = findViewById(R.id.password);
            signup = findViewById(R.id.signup);
            login = findViewById(R.id.login);
            pgbar = findViewById(R.id.progressBar);

            signup.setOnClickListener(v -> {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            });

            login.setOnClickListener(v -> {
                emailId = email.getText().toString().trim();
                password = pass.getText().toString().trim();

                if (TextUtils.isEmpty(emailId)) {
                    email.setError("Email Required");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    pass.setError("Password Required");
                    pass.requestFocus();
                    return;
                }
                if (password.length() < 8) {
                    pass.setError("Password should be of atleast 8 characters");
                    pass.requestFocus();
                    return;
                }
                pgbar.setVisibility(View.VISIBLE);
                Sign_In();
            });
        }
    }

    private void Sign_In() {
        mAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(this, task ->  {
                if(task.isSuccessful()) {
                    if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                        Toast.makeText(SignIn.this, "You are logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignIn.this, MainActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(SignIn.this, "Please verify your Email",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignIn.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                }
            pgbar.setVisibility(View.GONE);
        });
    }
}