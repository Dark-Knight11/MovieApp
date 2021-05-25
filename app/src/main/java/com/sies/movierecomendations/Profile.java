package com.sies.movierecomendations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    private EditText nameF, phoneF, emailF;
    private String name, phone, email;
    private ImageView pfp;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameF = findViewById(R.id.name);
        emailF = findViewById(R.id.email);
        phoneF = findViewById(R.id.phone);
        logout = findViewById(R.id.logout);

        name = nameF.getText().toString().trim();
        email = emailF.getText().toString().trim();
        phone = phoneF.getText().toString().trim();

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Profile.this, SignIn.class));
            finish();
        });

    }
}