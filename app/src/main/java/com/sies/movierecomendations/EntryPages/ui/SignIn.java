//package com.sies.movierecomendations.EntryPages.ui;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.sies.movierecomendations.MainActivity;
//import com.sies.movierecomendations.R;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Objects;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class SignIn extends AppCompatActivity {
//
//    EditText email, pass;
//    Button forgot, resend;
//    ImageView login, signup;
//    ProgressBar pgbar;
//    private String emailId, password;
//
//    // local storage requirements
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    ContextWrapper cw;
//
//    FirebaseAuth mAuth;
//    FirebaseUser person;
//
//    // get Firestore Instance
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    // get image from storage
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//
//    // Email validation regex pattern
//    private final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
//    private final Pattern pattern = Pattern.compile(regex);
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        mAuth = FirebaseAuth.getInstance();
//        person = mAuth.getCurrentUser();
//        // CHECKS IF USER IS ALREADY LOGGED IN
//        if (person != null && person.isEmailVerified()) {
//            startActivity(new Intent(SignIn.this, MainActivity.class));
//            finish();
//        }
//
//    }
//
//    @SuppressLint("CommitPrefEdits")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if(!networkWhere())
//            Toast.makeText(SignIn.this, "No Internet Found", Toast.LENGTH_SHORT).show();
//
//        setContentView(R.layout.activity_sign_in);
//        sharedPreferences = getSharedPreferences("com.sies.cinemania.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        email = findViewById(R.id.email);
//        pass = findViewById(R.id.password);
//        signup = findViewById(R.id.signup);
//        forgot = findViewById(R.id.forgotPassword);
//        resend = findViewById(R.id.resend);
//        login = findViewById(R.id.login);
//        pgbar = findViewById(R.id.progressBar);
//
//        // redirects to signUp page
//        signup.setOnClickListener(v -> {
//            startActivity(new Intent(SignIn.this, SignUp.class));
//            finish();
//        });
//
//        // redirects to forgot password page
//        forgot.setOnClickListener(v -> {
//            startActivity(new Intent(SignIn.this, ForgotPassword.class));
//            finish();
//        });
//
//        // resends verification email
//        resend.setOnClickListener(v -> {
//            emailId = Objects.requireNonNull(email.getText()).toString().trim();
//            password = Objects.requireNonNull(pass.getText()).toString().trim();
//
//            if (!fieldsValidation()) return;
//
//            pgbar.setVisibility(View.VISIBLE);
//            mAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
//                        if (task1.isSuccessful())
//                            Toast.makeText(SignIn.this, "Please Check your Email for Verification link", Toast.LENGTH_SHORT).show();
//                        else {
//                            // If sign up fails, display a message to the user.
//                            Log.w("TAG", "createUserWithEmail:failure", task1.getException());
//                            Toast.makeText(SignIn.this, "Email not Sent.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Log.w("TAG", "signInUserWithEmail:failure", task.getException());
//                    if (Objects.requireNonNull(task.getException()).toString().contains("The password is invalid"))
//                        Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
//                    else if (Objects.requireNonNull(task.getException()).toString().contains("There is no user record corresponding to this identifier"))
//                        Toast.makeText(SignIn.this, "User does not exist. Please create new account", Toast.LENGTH_SHORT).show();
//
//                }
//                pgbar.setVisibility(View.GONE);
//            });
//
//        });
//
//        // logs in the user
//        login.setOnClickListener(v -> {
//            emailId = email.getText().toString().trim();
//            password = pass.getText().toString().trim();
//
//            if(!networkWhere())
//                Toast.makeText(SignIn.this, "No Internet Found", Toast.LENGTH_SHORT).show();
//
//            if (!fieldsValidation()) return;
//
//            pgbar.setVisibility(View.VISIBLE);
//            Sign_In();
//        });
//    }
//
///*-----------------------------------------------------------------------------------------------------------------------------------*/
///*---------------------------------------------------------------FUNCTIONS------------------------------------------------------------*/
///*-----------------------------------------------------------------------------------------------------------------------------------*/
//
//    // function for starting firebase authentication
//    private void Sign_In() {
//        mAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(this, task -> {
//            if (task.isSuccessful()) {
//                if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
//                    cw = new ContextWrapper(getApplicationContext());
//                    person = mAuth.getCurrentUser();
//                    storage = FirebaseStorage.getInstance();
//                    StoreImage();
//                    fbData();
//                    Toast.makeText(SignIn.this, "You are logged in", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SignIn.this, MainActivity.class));
//                    finish();
//                } else
//                    Toast.makeText(SignIn.this, "Please verify your Email", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.w("TAG", "signInUserWithEmail:failure", task.getException());
//                if (Objects.requireNonNull(task.getException()).toString().contains("The password is invalid"))
//                    Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
//                else if (Objects.requireNonNull(task.getException()).toString().contains("There is no user record corresponding to this identifier"))
//                    Toast.makeText(SignIn.this, "User does not exist. Please create new account", Toast.LENGTH_SHORT).show();
//            }
//            pgbar.setVisibility(View.GONE);
//        });
//    }
//
//    // function for fetching data from Firestore DB
//    public void fbData() {
//        db.collection("Users")
//                .document(person.getUid())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Map<String, Object> document = Objects.requireNonNull(task.getResult()).getData();
//                        Log.d("fbData", task.getResult().getId()+ " => " + document);
//                        editor.putString("name", Objects.requireNonNull(Objects.requireNonNull(document).get("Name")).toString());
//                        editor.putString("phone", Objects.requireNonNull(document.get("phone")).toString());
//                        editor.putString("email", Objects.requireNonNull(person.getEmail()));
//                        editor.commit();
//                    } else
//                        Log.w("fbData", "Error getting documents.", task.getException());
//                });
//    }
//
//    // function for fetching image from firebase and storing it locally
//    private void StoreImage() {
//        // gets images from firebase storage
//        StorageReference pathReference = storageRef.child("images/" + person.getUid());
//        pathReference.getDownloadUrl()
//             // stores default pfp if image not found on Firebase
//            .addOnFailureListener(e -> {
//                Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.default_pfp);
//                File directory = cw.getDir("CineMania", Context.MODE_PRIVATE);
//                File mypath = new File(directory, "profile.jpg");
//                FileOutputStream fos;
//                try {
//                    fos = new FileOutputStream(mypath);
//                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.flush();
//                    fos.close();
//                } catch (IOException z) {
//                    z.printStackTrace();
//                }
//            })
//             // stores image fetched from firebase to local storage
//            .addOnSuccessListener(uri -> {
//                long MAXBYTES = 4096 * 4096;
//                pathReference.getBytes(MAXBYTES).addOnSuccessListener(bytes -> {
//                    Log.i("TAG", "firebaseImageDwl: ImageDownloaded");
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    File directory = cw.getDir("CineMania", Context.MODE_PRIVATE);
//                    File mypath = new File(directory, "profile.jpg");
//                    FileOutputStream fos;
//                    try {
//                        fos = new FileOutputStream(mypath);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                        fos.flush();
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            });
//    }
//
//    // function for validating all input fields
//    private boolean fieldsValidation() {
//        Matcher matcher = pattern.matcher(emailId);
//
//        // checks if email field is empty
//        if (TextUtils.isEmpty(emailId)) {
//            email.setError("Email Required");
//            email.requestFocus();
//            return false;
//        }
//
//        // checks if valid email is entered
//        if (!matcher.matches()) {
//            email.setError("Please enter valid email");
//            email.requestFocus();
//            return false;
//        }
//
//        // checks if password field is empty
//        if (TextUtils.isEmpty(password)) {
//            pass.setError("Password Required");
//            pass.requestFocus();
//            return false;
//        }
//
//        // checks if password length is greater than 8
//        if (password.length() < 8) {
//            pass.setError("Password should be of at least 8 characters");
//            pass.requestFocus();
//            return false;
//        }
//        return true;
//    }
//
//    // function to check if internet is active
//    private boolean networkWhere() {
//
//        boolean have_WIFI = false;
//        boolean have_MobileData = false;
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
//
//        for(NetworkInfo info: networkInfos) {
//            if (info.getTypeName().equalsIgnoreCase("WIFI"))
//                if (info.isConnected())
//                    have_WIFI = true;
//            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
//                if (info.isConnected())
//                    have_MobileData = true;
//
//        }
//        return have_MobileData || have_WIFI;
//    }
//}