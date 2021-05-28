package com.sies.movierecomendations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    // Variables
    private static final int PICK_IMAGE_REQUEST = 43;
    private static final int PERMISSION_FILE = 23;
    private Uri resultUri;
    private EditText nameF, phoneF, emailF;
    private String name, phone, email;
    private de.hdodenhof.circleimageview.CircleImageView pfp;
    private Button logout, update, select, upload;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ContextWrapper cw;

    /* FIREBASE Initializations */

    // get user
    FirebaseUser person = FirebaseAuth.getInstance().getCurrentUser();

    // get realtime DB
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();

    // get image from storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference pathReference = storage.getReference().child("images/" + person.getUid());

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("com.sies.cinemania.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cw = new ContextWrapper(getApplicationContext());

        // assign views to variables
        nameF = findViewById(R.id.name);
        emailF = findViewById(R.id.email);
        phoneF = findViewById(R.id.phone);
        logout = findViewById(R.id.logout);
        update = findViewById(R.id.update);
        select = findViewById(R.id.select);
        upload = findViewById(R.id.upload);
        pfp = findViewById(R.id.pfp);

        // fetch User data
        fetchDB();

        // fullscreen image
        pfp.setOnClickListener(v -> startActivity(new Intent(Profile.this, FullScreenPFP.class)));

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
        select.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_FILE);
            else
                SelectImage();
        });

        // upload image on firebase
        if(networkWhere()) upload.setOnClickListener(v -> upload());

    }

/*-----------------------------------------------------------------------------------------------------------------------------------*/
/*---------------------------------------------------------------FUNCTIONS------------------------------------------------------------*/
/*-----------------------------------------------------------------------------------------------------------------------------------*/

    // function for fetching user data
    public void fetchDB() {
        String userName = sharedPreferences.getString("name", "");
        String phoneNo = sharedPreferences.getString("phone", "");
        String emailId = sharedPreferences.getString("email", "");
        nameF.setText(userName);
        phoneF.setText(phoneNo);
        emailF.setText(emailId);
        fetchImage();
    }

    // function for fetching locally stored image
    public void fetchImage() {
        File directory = cw.getDir("CineMania", Context.MODE_PRIVATE);
        File filepath  = directory.getAbsoluteFile();
        try {
            File f = new File(filepath, "profile.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            pfp.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // function for storing Image locally
    private void StoreImage() {
        Bitmap bitmap = ((BitmapDrawable) pfp.getDrawable()).getBitmap();
        File directory = cw.getDir("CineMania", Context.MODE_PRIVATE);
        File mypath = new File(directory,"profile.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // function for updating realtime database
    private void updateDB() {
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.commit();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("phone", phone);
        childUpdates.put("Name", name);
        childUpdates.put("Email", email);
        mDatabase.child("Users").child(person.getUid()).updateChildren(childUpdates);
        Toast.makeText(Profile.this, "Data was successfully updated", Toast.LENGTH_SHORT).show();
        fetchDB();
    }

    // function for selecting image from gallery
    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Profile Pic"), PICK_IMAGE_REQUEST);
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
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .start(this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUriContent();
                pfp.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // function for uploading image in firebase
    private void upload() {
        if(resultUri!=null) {
            pathReference.putFile(resultUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        StoreImage();
                        Toast.makeText(Profile.this, "Image was uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                        Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } else
            Toast.makeText(Profile.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
        resultUri = null;
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


    // function for downloading image from firebase and setting it as pfp
    /*public void download() {
        long MAXBYTES = 4096*4096;
        pathReference.getBytes(MAXBYTES).addOnSuccessListener(bytes -> {
            Log.i("TAG", "firebaseImageDwl: ");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            pfp.setImageBitmap(bitmap);
        }).addOnFailureListener(Throwable::printStackTrace);
    }*/

    // fetching realtime database
    /* public void fbData() {
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
    } */
}