package com.sies.movierecomendations.EntryPages.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sies.movierecomendations.R
import com.sies.movierecomendations.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {

    companion object {
        private const val TAG = "SignUp"
    }

    private lateinit var Name: String
    private lateinit var emailId: String
    private lateinit var pass: String

    private lateinit var mAuth: FirebaseAuth
    private val mHandler = Handler()

    lateinit var binding: ActivitySignUpBinding

    // get Firestore Instance
    private var db = FirebaseFirestore.getInstance()

    // Email validation regex pattern
    private val regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    private val pattern = Pattern.compile(regex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        binding.signin.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@SignUp, SignIn::class.java))
            finish()
        })
        binding.name.setOnClickListener(View.OnClickListener { binding.nameLayout.error = null })
        binding.password.setOnClickListener(View.OnClickListener { binding.passwordLayout.error = null })
        binding.email.setOnClickListener(View.OnClickListener { binding.emailLayout.error = null })

        binding.register.setOnClickListener(View.OnClickListener {
            binding.nameLayout.error = null
            binding.passwordLayout.error = null
            binding.emailLayout.error = null
            Name = binding.name.text.toString().trim()
            pass = binding.password.text.toString().trim()
            emailId = binding.email.text.toString().trim()
            if (!fieldValidations()) return@OnClickListener
            binding.progressBar.visibility = View.VISIBLE
            Sign_Up()
        })
    }

    // starts user registration process
    private fun Sign_Up() {
        mAuth.createUserWithEmailAndPassword(emailId, pass)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) sendEmail() else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    if (Objects.requireNonNull(task.exception).toString().contains("The email address is already in use by another account")) {
                        Toast.makeText(this@SignUp, "User already exists please log in", Toast.LENGTH_SHORT).show()
                        val mUpdateTimeTask = Runnable {
                            startActivity(Intent(this@SignUp, SignIn::class.java))
                            finish()
                        }
                        mHandler.postDelayed(mUpdateTimeTask, 200)
                    }
                }
            }
    }

    // function for sending verification mail
    private fun sendEmail() {
        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task1: Task<Void?> ->
            if (task1.isSuccessful) {
                Toast.makeText(this@SignUp, "Please Check your Email for Verification link", Toast.LENGTH_SHORT).show()
                enterData()
            } else {
                // If sign up fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", task1.exception)
                Toast.makeText(this@SignUp, "Authentication failed.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    // function for uploading data on Firestore
    private fun enterData() {
        val userData: MutableMap<String, Any?> = HashMap()
        userData["phone"] = ""
        userData["Name"] = Name
        userData["Email"] = emailId

        // Add a new document with a generated ID
        mAuth.currentUser?.uid?.let {
            db.collection("Users")
                .document(it)
                .set(userData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + mAuth.currentUser?.uid)
                }
                .addOnFailureListener { e: Exception? ->
                    Log.w(TAG,"Error adding document", e)
                }
        }
        binding.progressBar.visibility = View.GONE
        startActivity(Intent(this@SignUp, SignIn::class.java))
    }

    private fun fieldValidations(): Boolean {
        val matcher = pattern.matcher(emailId)

        // checks if name field is empty
        if (TextUtils.isEmpty(Name)) {
            binding.nameLayout.error = "Name Required"
            binding.nameLayout.requestFocus()
            return false
        }

        // checks if email field is empty
        if (TextUtils.isEmpty(emailId)) {
            binding.emailLayout.error = "Email Required"
            binding.emailLayout.requestFocus()
            return false
        }

        // checks if valid email is entered
        if (!matcher.matches()) {
            binding.emailLayout.error = "Please enter valid email"
            binding.emailLayout.requestFocus()
            return false
        }

        // checks if password field is empty
        if (TextUtils.isEmpty(pass)) {
            binding.passwordLayout.error = "Password Required"
            binding.passwordLayout.requestFocus()
            return false
        }

        // checks if password length is greater than 8
        if (pass.length < 8) {
            binding.passwordLayout.error = "Password should be of at least 8 characters"
            binding.passwordLayout.requestFocus()
            return false
        }

        // checks if password entered in confirm password equals to password field
//        if (confirmPass != pass) {
//            cfpassLayout.setError("Please make sure your passwords match");
//            return false;
//        }
        return true
    }
}