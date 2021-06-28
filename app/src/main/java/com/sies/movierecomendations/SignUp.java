package com.sies.movierecomendations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    TextInputEditText email,password,name;
    TextInputLayout emailLayout, passwordLayout, nameLayout;
    Button signin;
    ImageView register;
    ProgressBar pgbar;
    private String Name, emailId, pass;

    private FirebaseAuth mAuth;

    private final Handler mHandler = new Handler();

    // requiremnets for local storage
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // get Firestore Instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Email validation regex pattern
    private final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final Pattern pattern = Pattern.compile(regex);

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPreferences = getSharedPreferences("com.sies.cinemania.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        register = findViewById(R.id.register);
        pgbar = findViewById(R.id.progressBar);

        nameLayout = findViewById(R.id.name_layout);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        });

        name.setOnClickListener(v -> nameLayout.setError(null));
        password.setOnClickListener(v -> passwordLayout.setError(null));
        email.setOnClickListener(v -> emailLayout.setError(null));

        register.setOnClickListener(v -> {
            nameLayout.setError(null);
            passwordLayout.setError(null);
            emailLayout.setError(null);
            Name = Objects.requireNonNull(name.getText()).toString().trim();
            pass = Objects.requireNonNull(password.getText()).toString().trim();
            emailId = Objects.requireNonNull(email.getText()).toString().trim();

            if (!fieldValidations()) return;
            pgbar.setVisibility(View.VISIBLE);

            Sign_Up();
        });
    }

    // starts user registration process
    private void Sign_Up() {
        mAuth.createUserWithEmailAndPassword(emailId, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                        sendEmail();
                    else {
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
                    }
                });
    }

    // function for sending verification mail
    private void sendEmail() {
        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()) {
                Toast.makeText(SignUp.this, "Please Check your Email for Verification link", Toast.LENGTH_SHORT).show();
                enterData();
            } else {
                // If sign up fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", task1.getException());
                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                pgbar.setVisibility(View.GONE);
            }
        });
    }

    // function for uploading data on Firestore
    private void enterData() {

        Map<String, Object> userData = new HashMap<>();
        userData.put("phone", "");
        userData.put("Name", Name);
        userData.put("Email", emailId);

        // Add a new document with a generated ID
        db.collection("Users")
                .document((Objects.requireNonNull(mAuth.getCurrentUser())).getUid())
                .set(userData)
                .addOnSuccessListener(documentReference -> Log.d("TAG", "DocumentSnapshot added with ID: " + (mAuth.getCurrentUser()).getUid()))
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
        pgbar.setVisibility(View.GONE);
        startActivity(new Intent(SignUp.this, SignIn.class));
    }

    private boolean fieldValidations() {
        Matcher matcher = pattern.matcher(emailId);

        // checks if name field is empty
        if(TextUtils.isEmpty(Name)) {
            nameLayout.setError("Name Required");
            nameLayout.requestFocus();
            return false;
        }

        // checks if email field is empty
        if (TextUtils.isEmpty(emailId)) {
            emailLayout.setError("Email Required");
            emailLayout.requestFocus();
            return false;
        }

        // checks if valid email is entered
        if (!matcher.matches()) {
            emailLayout.setError("Please enter valid email");
            emailLayout.requestFocus();
            return false;
        }

        // checks if password field is empty
        if (TextUtils.isEmpty(pass)) {
            passwordLayout.setError("Password Required");
            passwordLayout.requestFocus();
            return false;
        }

        // checks if password length is greater than 8
        if (pass.length() < 8) {
            passwordLayout.setError("Password should be of at least 8 characters");
            passwordLayout.requestFocus();
            return false;
        }

        // checks if password entered in confirm password equals to password field
//        if (confirmPass != pass) {
//            cfpassLayout.setError("Please make sure your passwords match");
//            return false;
//        }

        return true;
    }
}


// function for uploading data on Realtime DB
/*private uploadRTDB() {
    User user = new User(emailId, Name);
    FirebaseDatabase.getInstance().getReference("Users")
        .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
        .setValue(user)
        .addOnCompleteListener(task2 -> {
            if(task2.isSuccessful()) {
                Log.i("TAG", "onComplete: congo");
                editor.putString("name", Name);
                editor.putString("email", emailId);
                editor.commit();
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            } else {
                Log.w("TAG", "saveUserDetails:failure", task2.getException());
                pgbar.setVisibility(View.GONE);
            }
    });
}
*/