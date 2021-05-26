package com.sies.movierecomendations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private EditText nameF, phoneF, emailF;
    private String name, phone, email;
    private ImageView pfp;
    private Button logout;

    FirebaseUser person = FirebaseAuth.getInstance().getCurrentUser();

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


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    // Get Post object and use the values to update the UI
                    User user = dataSnapshot.getValue(User.class);
                    nameF.setText(user.Name);
                    emailF.setText(user.emailid);
                } else
                    Toast.makeText(Profile.this, "Data not present", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }

        };
        FirebaseDatabase.getInstance().getReference().child("Users").child(person.getUid()).addValueEventListener(postListener);


    }
}