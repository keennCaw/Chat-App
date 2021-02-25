package com.keennhoward.chatapp.views

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.keennhoward.chatapp.LoginActivity
import com.keennhoward.chatapp.MainActivity
import com.keennhoward.chatapp.databinding.ActivityRegisterBinding
import com.keennhoward.chatapp.viewmodel.RegisterViewModel
import com.keennhoward.chatapp.viewmodel.RegisterViewModelFactory


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    companion object {
        const val SELECT_IMAGE_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        val factory = RegisterViewModelFactory(application)
        registerViewModel =
            ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        registerViewModel.getFirebaseUser().observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Toast.makeText(this, "User created ${it.metadata.toString()}", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.registerButton.setOnClickListener {
            if (!inputFieldChecker()) return@setOnClickListener

            registerViewModel.register(
                binding.registerEmailEt.text.toString(),
                binding.registerPasswordEt.text.toString(),
                binding.registerUsernameEt.text.toString(),
                selectedPhotoUri!!
            )

            
        }

        binding.registerImage.setOnClickListener {
            val selectImageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            selectImageIntent.type = "image/*"
            startActivityForResult(
                selectImageIntent,
                SELECT_IMAGE_REQUEST
            )
        }

        binding.registerLoginTv.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(loginIntent)
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
