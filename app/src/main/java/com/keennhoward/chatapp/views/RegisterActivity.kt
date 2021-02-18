package com.keennhoward.chatapp.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.keennhoward.chatapp.User
import com.keennhoward.chatapp.databinding.ActivityRegisterBinding
import com.keennhoward.chatapp.viewmodel.LoginRegisterViewModel
import com.keennhoward.chatapp.viewmodel.LoginRegisterViewModelFactory
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel

    companion object {
        const val SELECT_IMAGE_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        val factory = LoginRegisterViewModelFactory(application)
        loginRegisterViewModel =
            ViewModelProvider(this, factory).get(LoginRegisterViewModel::class.java)

        loginRegisterViewModel.getFirebaseUser().observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Toast.makeText(this, "User created ${it.metadata.toString()}", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        binding.registerButton.setOnClickListener {
            if (!inputFieldChecker()) return@setOnClickListener

            loginRegisterViewModel.register(
                binding.registerEmailEt.text.toString(),
                binding.registerPasswordEt.text.toString(),
                binding.registerUsernameEt.text.toString(),
                selectedPhotoUri!!
            )
        }

        binding.registerImage.setOnClickListener {
            val selectImageIntent = Intent(Intent.ACTION_PICK)
            selectImageIntent.type = "image/*"
            startActivityForResult(
                selectImageIntent,
                SELECT_IMAGE_REQUEST
            )
        }

        setContentView(view)
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            binding.registerImageTv.visibility = View.GONE

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            binding.registerImage.setImageBitmap(bitmap)
        }
    }


    private fun inputFieldChecker(): Boolean {

        var result = true
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        //EmptyCheck
        if (binding.registerUsernameEt.text.toString().trim().isEmpty()) {
            binding.registerUsernameEt.error = "Username Cannot be blank"
            result = false
        }

        if (binding.registerPasswordEt.text.toString().trim().isEmpty()) {
            binding.registerPasswordEt.error = "Password Cannot be blank"
            result = false
        }

        if (binding.registerEmailEt.text.toString().trim().isEmpty()) {
            binding.registerEmailEt.error = "Email Cannot be blank"
            result = false
        }

        if (selectedPhotoUri == null) {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
            result = false
        }

        //format check
        if (!binding.registerEmailEt.text.toString().trim { it <= ' ' }
                .matches(emailPattern.toRegex())) {
            binding.registerEmailEt.error = "Please Enter a valid Email"
            result = false
        }

        if (binding.registerPasswordEt.text.toString().contains(" ")) {
            binding.registerPasswordEt.error = "No Spaces Allowed"
            result = false
        }
        if(binding.registerPasswordEt.text.toString().trim().length < 6){
            binding.registerPasswordEt.error = "Must be at least 6 characters"
            result = false
        }

        return result
    }

}
