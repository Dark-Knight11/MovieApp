package com.sies.movierecomendations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    // Variables
    private static final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private EditText nameF, phoneF, emailF;
    private String name, phone, email;
    private ImageView pfp;
    private Button logout, update, select, upload;

    /* FIREBASE Initializations */

    // get user
    FirebaseUser person = FirebaseAuth.getInstance().getCurrentUser();

    // get realtime DB
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();

    // get image storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(networkWhere()) {
            download();     // get the pfp from DB
            fetchDB();      // get user data
        }

        // assign views to variables
        nameF = findViewById(R.id.name);
        emailF = findViewById(R.id.email);
        phoneF = findViewById(R.id.phone);
        logout = findViewById(R.id.logout);
        update = findViewById(R.id.update);
        select = findViewById(R.id.select);
        upload = findViewById(R.id.upload);
        pfp = findViewById(R.id.pfp);

        // logs out the current user
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Profile.this, SignIn.class));
            finish();
        });

        if(networkWhere()) {
            // update user data
            update.setOnClickListener(v -> {
                phone = phoneF.getText().toString().trim();
                name = nameF.getText().toString().trim();
                email = emailF.getText().toString().trim();
                updateDB();
            });
        }

        // select image for pfp
        select.setOnClickListener(v -> SelectImage());

        // upload image on firebase
        if(networkWhere())
            upload.setOnClickListener(v -> upload());

    }

    // function for fetching user data
    private void fetchDB() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    // Get Post object and use the values to update the UI
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    nameF.setText(user.Name);
                    phoneF.setText(user.phone);
                    emailF.setText(person.getEmail());
                } else
                    Toast.makeText(Profile.this, "Data not present", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("Users").child(person.getUid()).addValueEventListener(postListener);
    }

    // function for downloading image from firebase and setting it as pfp
    private void download() {
        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("images/" + person.getUid());

        long MAXBYTES = 1024*1024;
        pathReference.getBytes(MAXBYTES).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            pfp.setImageBitmap(bitmap);
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    // function for updating realtime database
    private void updateDB() {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("phone", phone);
        childUpdates.put("Name", name);
        childUpdates.put("Email", email);
        mDatabase.child("Users").child(person.getUid()).updateChildren(childUpdates);
        Toast.makeText(Profile.this, "Data was successfull updated", Toast.LENGTH_SHORT).show();
    }

    // function for selecting image from gallery
    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // reads the image selected by user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            pfp.setImageURI(filePath);
        }
    }

    // function for uploading image in firebase
    private void upload() {
        if(filePath!=null) {
            final String key = person.getUid();
            StorageReference profilePic = storageRef.child("images/" + key);
            profilePic.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot ->
                            Toast.makeText(Profile.this, "Image was uploaded", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else
            Toast.makeText(Profile.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
    }

    // checks for Internet Connectivity
    private boolean networkWhere() {

        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info: networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData = true;

        }
        return have_MobileData || have_WIFI;
    }

}