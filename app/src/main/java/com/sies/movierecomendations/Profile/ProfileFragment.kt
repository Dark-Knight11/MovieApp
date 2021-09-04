package com.sies.movierecomendations.Profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImage.getActivityResult
import com.canhub.cropper.CropImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sies.movierecomendations.EntryPages.ui.SignIn
import com.sies.movierecomendations.R
import com.sies.movierecomendations.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        // Variables
        private const val PICK_IMAGE_REQUEST = 43
        private const val PERMISSION_FILE = 23
        private val NAME = stringPreferencesKey("name")
        private val PHONE = stringPreferencesKey("phone")
        private val EMAIL = stringPreferencesKey("email")
    }

    private val viewModel: ProfileViewModel by viewModels()
    @Inject lateinit var dataStore: DataStore<Preferences>
    private var resultUri: Uri? = null
    lateinit var cw: ContextWrapper
    lateinit var binding: FragmentProfileBinding
    lateinit var phone: String
    lateinit var name: String

    /* FIREBASE Initializations */

    // get user
    var person = FirebaseAuth.getInstance().currentUser

    // get Firestore Instance
    var db = FirebaseFirestore.getInstance()

    // get image from storage
    var storage = FirebaseStorage.getInstance()
    var pathReference = storage.reference.child("images/" + person?.uid)

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = this

        cw = ContextWrapper(activity)

        fetchDB()

        // fullscreen image
        binding.pfp.setOnClickListener {
            startActivity(Intent(context,FullScreenPFP::class.java))
        }

        // logs out the current user
        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, SignIn::class.java))
            requireActivity().finish()
        }
        if (networkWhere()) {
            // update user data
            binding.update.setOnClickListener {
                phone = binding.phone.text.toString().trim()
                name = binding.name.text.toString().trim()
                if (!fieldsValidation()) return@setOnClickListener
                updateDB()
            }
        }

        // select image for pfp
        binding.select.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_FILE)
            else SelectImage()
        }

        // upload image on firebase
        if (networkWhere()) binding.upload.setOnClickListener { upload() }

        return binding.root
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------*/ /*---------------------------------------------------------------FUNCTIONS------------------------------------------------------------*/ /*-----------------------------------------------------------------------------------------------------------------------------------*/ // function for fetching user data
    private fun fetchDB() {
        lifecycleScope.launch {
            val pref = dataStore.data.first()
            binding.name.setText(pref[NAME])
            binding.phone.setText(pref[PHONE])
            binding.email.text = pref[EMAIL]
        }
//        val userName = sharedPreferences!!.getString("name", "")
//        val phoneNo = sharedPreferences!!.getString("phone", "")
//        val emailId = sharedPreferences!!.getString("email", "")
//        binding.name.setText(userName)
//        binding.phone.setText(phoneNo)
//        binding.email.text = emailId
        fetchImage()
    }

    // function for fetching locally stored image
    private fun fetchImage() {
        val directory = cw.getDir("CineMania", Context.MODE_PRIVATE)
        val filepath = directory.absoluteFile
        try {
            val f = File(filepath, "profile.jpg")
            val bitmap = BitmapFactory.decodeStream(FileInputStream(f))
            binding.pfp.setImageBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    // function for storing Image locally
    private fun StoreImage() {
        val bitmap = (binding.pfp.drawable as BitmapDrawable).bitmap
        val directory = cw.getDir("CineMania", Context.MODE_PRIVATE)
        val mypath = File(directory, "profile.jpg")
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(mypath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // function for updating realtime database
    private fun updateDB() {
        lifecycleScope.launch {
            dataStore.edit {
                it[NAME] = name
                it[PHONE] = phone
            }
        }
        val user: MutableMap<String, Any?> = HashMap()
        user["Name"] = name
        user["phone"] = phone

        // Add a new document with a generated ID
        person?.uid?.let {
            db.collection("Users")
                .document(it)
                .set(user)
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot added with ID: " + person!!.uid)
                }
                .addOnFailureListener { e: Exception? ->
                    Log.w("TAG", "Error adding document", e)
                }
        }
            Toast.makeText(context, "Data was successfully updated", Toast.LENGTH_SHORT).show()
            fetchDB()
        }

    // function for selecting image from gallery
    private fun SelectImage() {
        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Profile Pic"), PICK_IMAGE_REQUEST)
    }

    // reads the image selected by user
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
             CropImage.activity(data.data)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .start(requireContext(), this)

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult? = getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                result?.let {
                    resultUri = result.uriContent!!
                    binding.pfp.setImageURI(resultUri)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert(result != null)
                val error: Exception = result?.error!!
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_FILE)
            // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                SelectImage()
    }

    // function for uploading image in firebase
    private fun upload() {
        if (resultUri !=  null) {
            pathReference.putFile(resultUri!!)
                .addOnSuccessListener {
                    StoreImage()
                    Toast.makeText(context, "Image was uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e: Exception ->
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
        } else
            Toast.makeText(context, "Please Select an Image", Toast.LENGTH_SHORT).show()
        resultUri = null
    }

    // checks for Internet Connectivity
    private fun networkWhere(): Boolean {
        var have_WIFI = false
        var have_MobileData = false
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectivityManager.allNetworkInfo
        for (info in networkInfos) {
            if (info.typeName.equals("WIFI", ignoreCase = true)) if (info.isConnected) have_WIFI =
                true
            if (info.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (info.isConnected) have_MobileData = true
        }
        return have_MobileData || have_WIFI
    }

    // function for validating all input fields
    private fun fieldsValidation(): Boolean {

        // checks if name field is empty
        if (TextUtils.isEmpty(name)) {
            binding.name.error = "Please Enter your Username"
            binding.name.requestFocus()
            return false
        }

        // checks if phone field is empty
        if (TextUtils.isEmpty(phone)) {
            binding.phone.error = "Phone Number Required"
            binding.phone.requestFocus()
            return false
        }
        return true
    }
}