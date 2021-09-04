package com.sies.movierecomendations.EntryPages.ui

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sies.movierecomendations.MainActivity
import com.sies.movierecomendations.R
import com.sies.movierecomendations.databinding.ActivitySignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class SignIn  : AppCompatActivity() {

    companion object {
        private val NAME = stringPreferencesKey("name")
        private val PHONE = stringPreferencesKey("phone")
        private val EMAIL = stringPreferencesKey("email")
    }

    lateinit var binding: ActivitySignInBinding
    @Inject lateinit var dataStore: DataStore<Preferences>

    // Email validation regex pattern
    private val regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    private val pattern = Pattern.compile(regex)

    lateinit var emailId: String
    lateinit var password: String

    lateinit var mAuth: FirebaseAuth
    lateinit var person: FirebaseUser

    // local storage requirements
    lateinit var cw: ContextWrapper

    // get Firestore Instance
    private var db = FirebaseFirestore.getInstance()

    // get image from storage
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.lifecycleOwner = this

        mAuth = FirebaseAuth.getInstance()
        // CHECKS IF USER IS ALREADY LOGGED IN
        if (mAuth.currentUser != null) {
            Log.i("SignIn", "user not null")
            if (mAuth.currentUser!!.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // redirects to signUp page
        binding.signup.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        // redirects to forgot password page
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
            finish()
        }

        // resends verification email
        binding.resend.setOnClickListener {
            emailId = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (!fieldsValidation()) return@setOnClickListener
            binding.progressBar.visibility = View.VISIBLE
            mAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener {
                        if (it.isSuccessful)
                            Toast.makeText(this, "Please Check your Email for Verification link", Toast.LENGTH_SHORT).show()
                        else {
                            // If sign up fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", it.exception)
                            Toast.makeText(this, "Email not Sent.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.w("TAG", "signInUserWithEmail:failure", task.exception)
                    if (task.exception.toString().contains("The password is invalid"))
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
                    else if (task.exception.toString().contains("There is no user record corresponding to this identifier"))
                        Toast.makeText(this, "User does not exist. Please create new account", Toast.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = View.GONE
            }
        }

        // logs in the user
        binding.login.setOnClickListener {
            emailId = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            if (!networkWhere())
                Toast.makeText(this, "No Internet Found", Toast.LENGTH_SHORT).show()
            if (!fieldsValidation())
                return@setOnClickListener
            binding.progressBar.visibility = View.VISIBLE
            Sign_In()
        }
    }
/*-----------------------------------------------------------------------------------------------------------------------------------*/
/*---------------------------------------------------------------FUNCTIONS------------------------------------------------------------*/
/*-----------------------------------------------------------------------------------------------------------------------------------*/ // function for starting firebase authentication
    private fun Sign_In() {
        mAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(this) { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                if (mAuth.currentUser?.isEmailVerified == true) {
                    cw = ContextWrapper(applicationContext)
                    person = mAuth.currentUser!!
                    storage = FirebaseStorage.getInstance()
                    StoreImage()
                    lifecycleScope.launch { fbData() }
                    Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else Toast.makeText(this, "Please verify your Email", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Log.w("TAG", "signInUserWithEmail:failure", task.exception)
                if (Objects.requireNonNull(task.exception).toString().contains("The password is invalid"))
                    Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
                else if (Objects.requireNonNull(task.exception).toString().contains("There is no user record corresponding to this identifier"))
                    Toast.makeText(this, "User does not exist. Please create new account", Toast.LENGTH_SHORT).show()
            }
            binding.progressBar.visibility = View.GONE
        }
    }

    // function for fetching data from Firestore DB
    suspend fun fbData() {
        person.let {
            db.collection("Users")
                .document(it.uid)
                .get()
                .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                    if (task.isSuccessful) {
                        val document = task.result?.data
                        Log.d("fbData", task.result?.id + " => " + document)
                        lifecycleScope.launch {
                            dataStore.edit { pref ->
                                pref[NAME] = document?.get("Name").toString()
                                pref[PHONE] = document?.get("phone").toString()
                                pref[EMAIL] = it.email.toString()
                            }
                        }
                    } else
                        Log.w("fbData", "Error getting documents.", task.exception)
                }
        }
    }

    // function for fetching image from firebase and storing it locally
    private fun StoreImage() {
        // gets images from firebase storage
        val pathReference = storageRef.child("images/" + person?.uid)
        pathReference.downloadUrl // stores default pfp if image not found on Firebase
            .addOnFailureListener {
                val bm = BitmapFactory.decodeResource(resources, R.drawable.default_pfp)
                val directory = cw.getDir("CineMania", MODE_PRIVATE)
                val mypath = File(directory, "profile.jpg")
                val fos: FileOutputStream
                try {
                    fos = FileOutputStream(mypath)
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush()
                    fos.close()
                } catch (z: IOException) {
                    z.printStackTrace()
                }
            } // stores image fetched from firebase to local storage
            .addOnSuccessListener { uri: Uri? ->
                val MAXBYTES = (4096 * 4096).toLong()
                pathReference.getBytes(MAXBYTES).addOnSuccessListener { bytes: ByteArray ->
                    Log.i("TAG", "firebaseImageDwl: ImageDownloaded")
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val directory =
                        cw.getDir("CineMania", MODE_PRIVATE)
                    val mypath = File(directory, "profile.jpg")
                    val fos: FileOutputStream
                    try {
                        fos = FileOutputStream(mypath)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        fos.flush()
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
    }


    // function for validating all input fields
    private fun fieldsValidation(): Boolean {
        val matcher = pattern.matcher(emailId)
        val email = binding.email
        val pass = binding.password

        // checks if email field is empty
        if (TextUtils.isEmpty(emailId)) {
            email.error = "Email Required"
            email.requestFocus()
            return false
        }

        // checks if valid email is entered
        if (!matcher.matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return false
        }

        // checks if password field is empty
        if (TextUtils.isEmpty(password)) {
            pass.error = "Password Required"
            pass.requestFocus()
            return false
        }

        // checks if password length is greater than 8
        if (password.length < 8) {
            pass.error = "Password should be of at least 8 characters"
            pass.requestFocus()
            return false
        }
        return true
    }

    private fun networkWhere(): Boolean {
        var have_WIFI = false
        var have_MobileData = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectivityManager.allNetworkInfo
        for (info in networkInfos) {
            if (info.typeName.equals("WIFI", ignoreCase = true))
                if (info.isConnected) have_WIFI = true
            if (info.typeName.equals("MOBILE", ignoreCase = true))
                if (info.isConnected) have_MobileData = true
        }
        return have_MobileData || have_WIFI
    }

}