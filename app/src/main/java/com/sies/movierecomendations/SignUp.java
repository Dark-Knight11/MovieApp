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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    EditText email,password,name;
    TextView signin;
    Button register;
    ProgressBar pgbar;
    private String Name, emailId, pass;

    private FirebaseAuth mAuth;

    private Handler mHandler = new Handler();

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
        pgbar = findViewById(R.id.progressBar2);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        });

        register.setOnClickListener(v -> {

            Name = name.getText().toString().trim();
            pass = password.getText().toString().trim();
            emailId = email.getText().toString().trim();

            Matcher matcher = pattern.matcher(emailId);

            // checks if name field is empty
            if(TextUtils.isEmpty(Name)) {
                name.setError("Name Required");
                name.requestFocus();
                return;
            }

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
            if (TextUtils.isEmpty(pass)) {
                password.setError("Password Required");
                password.requestFocus();
                return;
            }

            // checks if password length is greater than 8
            if (pass.length() < 8) {
                password.setError("Password should be of at least 8 characters");
                password.requestFocus();
                return;
            }
            pgbar.setVisibility(View.VISIBLE);

            Sign_Up();
        });
    }
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
                .document((mAuth.getCurrentUser()).getUid())
                .set(userData)
                .addOnSuccessListener(documentReference -> Log.d("TAG", "DocumentSnapshot added with ID: " + (mAuth.getCurrentUser()).getUid()))
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
        pgbar.setVisibility(View.GONE);
        startActivity(new Intent(SignUp.this, SignIn.class));
    }
}


    // function for uploading data on Realtime DB
/*    User user = new User(emailId, Name);
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
*/