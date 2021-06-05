package com.sies.movierecomendations.Fragments;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sies.movierecomendations.FullScreenPFP;
import com.sies.movierecomendations.R;
import com.sies.movierecomendations.SignIn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    // get Firestore Instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // get image from storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference pathReference = storage.getReference().child("images/" + person.getUid());

    // Email validation regex pattern
    private final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final Pattern pattern = Pattern.compile(regex);

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences("com.sies.cinemania.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cw = new ContextWrapper(getActivity());

        // assign views to variables
        nameF = view.findViewById(R.id.name);
        emailF = view.findViewById(R.id.email);
        phoneF = view.findViewById(R.id.phone);
        logout = view.findViewById(R.id.logout);
        update = view.findViewById(R.id.update);
        select = view.findViewById(R.id.select);
        upload = view.findViewById(R.id.upload);
        pfp = view.findViewById(R.id.pfp);

        fetchDB();

        // fullscreen image
        pfp.setOnClickListener(v -> startActivity(new Intent(getContext(), FullScreenPFP.class)));

        // logs out the current user
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), SignIn.class));
            getActivity().finish();
        });

        if(networkWhere()) {
            // update user data
            update.setOnClickListener(v -> {
                phone = phoneF.getText().toString().trim();
                name = nameF.getText().toString().trim();
                email = emailF.getText().toString().trim();
                if(!fieldsValidation()) return;
                updateDB();
            });
        }

        // select image for pfp
        select.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
            else
                SelectImage();
        });

        // upload image on firebase
        if(networkWhere()) upload.setOnClickListener(v -> upload());

        return view;
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

        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("phone", phone);
        user.put("Email", email);

        // Add a new document with a generated ID
        db.collection("Users")
                .document(person.getUid())
                .set(user)
                .addOnSuccessListener(documentReference -> Log.d("TAG", "DocumentSnapshot added with ID: " + person.getUid()))
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
        Toast.makeText(getContext(), "Data was successfully updated", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    .start(getContext(), this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUriContent();
                pfp.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_FILE)
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                SelectImage();
    }

    // function for uploading image in firebase
    private void upload() {
        if(resultUri!=null) {
            pathReference.putFile(resultUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        StoreImage();
                        Toast.makeText(getContext(), "Image was uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
        } else
            Toast.makeText(getContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
        resultUri = null;
    }
    // checks for Internet Connectivity

    private boolean networkWhere() {

        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    // function for validating all input fields
    private boolean fieldsValidation() {
        Matcher matcher = pattern.matcher(email);

        // checks if email field is empty
        if (TextUtils.isEmpty(email)) {
            emailF.setError("Email Required");
            emailF.requestFocus();
            return false;
        }

        // checks if valid email is entered
        if (!matcher.matches()) {
            emailF.setError("Please enter valid email");
            emailF.requestFocus();
            return false;
        }

        // checks if name field is empty
        if (TextUtils.isEmpty(name)) {
            nameF.setError("Please Enter your Username");
            nameF.requestFocus();
            return false;
        }

        // checks if phone field is empty
        if (TextUtils.isEmpty(phone)) {
            phoneF.setError("Phone Number Required");
            phoneF.requestFocus();
            return false;
        }

        return true;
    }

}