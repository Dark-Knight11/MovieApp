package com.sies.movierecomendations;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText email;
    TextView signin;
    Button reset;

    // Email validation regex pattern
    private final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final Pattern pattern = Pattern.compile(regex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        reset = findViewById(R.id.resetPassword);
        signin = findViewById(R.id.signin);
        email = findViewById(R.id.email);

        signin.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPassword.this, SignIn.class));
            finish();
        });

        reset.setOnClickListener(v -> {
            String emailAddress = email.getText().toString().trim();
            Matcher matcher = pattern.matcher(emailAddress);

            // checks if email field is empty
            if (TextUtils.isEmpty(emailAddress)) {
                email.setError("Email Required");
                email.requestFocus();
                return;
            }

            // checks if valid email is entered
            if (!matcher.matches()) {
                email.setError("Please enter valid email");
                email.requestFocus();
                return;
            }
            resetPassword(emailAddress);
        });

    }

    private void resetPassword(String emailAddress) {
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "Email sent.");
                        Toast.makeText(ForgotPassword.this, "Please check your Email",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this, SignIn.class));
                        finish();
                    }
                    else {
                        Log.w("TAG", "onForgot: ", task.getException());
                        if(Objects.requireNonNull(task.getException()).toString().contains("There is no user record corresponding to this identifier")) {
                            Toast.makeText(ForgotPassword.this, "User does not exist. Please create new account", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPassword.this, SignUp.class));
                            finish();
                        }
                    }
                });
    }
}