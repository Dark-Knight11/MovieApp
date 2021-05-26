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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {

    EditText email, pass;
    TextView signup, forgot;
    Button login;
    ProgressBar pgbar;
    private FirebaseAuth mAuth;
    private String emailId, password;

    // Email validation regex pattern
    private final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final Pattern pattern = Pattern.compile(regex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && user.isEmailVerified()) {
            Toast.makeText(SignIn.this, "You are logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }
        else {
            email = findViewById(R.id.email);
            pass = findViewById(R.id.password);
            signup = findViewById(R.id.signup);
            forgot = findViewById(R.id.forgotPassword);
            login = findViewById(R.id.login);
            pgbar = findViewById(R.id.progressBar);

            signup.setOnClickListener(v -> {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            });

            forgot.setOnClickListener(v -> {
                startActivity(new Intent(SignIn.this, ForgotPassword.class));
                finish();
            });

            login.setOnClickListener(v -> {
                emailId = email.getText().toString().trim();
                password = pass.getText().toString().trim();

                Matcher matcher = pattern.matcher(emailId);

                // checks if email field is empty
                if (TextUtils.isEmpty(emailId)) {
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

                // checks if password field is empty
                if (TextUtils.isEmpty(password)) {
                    pass.setError("Password Required");
                    pass.requestFocus();
                    return;
                }

                // checks if password length is greater than 8
                if (password.length() < 8) {
                    pass.setError("Password should be of at least 8 characters");
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
                    Log.w("TAG", "signInUserWithEmail:failure", task.getException());
                    if (Objects.requireNonNull(task.getException()).toString().contains("The password is invalid"))
                        Toast.makeText(SignIn.this, "Wrong Password",Toast.LENGTH_SHORT).show();
                    else if (Objects.requireNonNull(task.getException()).toString().contains("There is no user record corresponding to this identifier")) {
                        Toast.makeText(SignIn.this, "User does not exist. Please create new account", Toast.LENGTH_SHORT).show();
                    }

                }
                pgbar.setVisibility(View.GONE);
        });
    }
}