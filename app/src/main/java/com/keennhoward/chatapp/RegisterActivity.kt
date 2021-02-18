package com.keennhoward.chatapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.keennhoward.chatapp.databinding.ActivityRegisterBinding
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    companion object{
        const val SELECT_IMAGE_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        //assign
        firebaseAuth = Firebase.auth

        binding.registerImage.setOnClickListener {

            val selectImageIntent = Intent(Intent.ACTION_PICK)
            selectImageIntent.type = "image/*"
            startActivityForResult(selectImageIntent, SELECT_IMAGE_REQUEST)

        }

        binding.registerButton.setOnClickListener {
            performRegister()
        }

        setContentView(view)
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SELECT_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){

            binding.registerImageTv.visibility = View.GONE

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            binding.registerImage.setImageBitmap(bitmap)
        }
    }


    private fun performRegister(){

        //Firebase Authentication to create a user with email and password

        if(binding.registerEmailEt.text.toString().trim().isEmpty() ||
            binding.registerEmailEt.text.toString().trim().isEmpty()) return

        firebaseAuth.createUserWithEmailAndPassword(
            binding.registerEmailEt.text.toString(),
            binding.registerPasswordEt.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("REGISTER", "Success uid: ${task.result!!.user!!.uid}")


                uploadImageToFirebaseStorage()
            }
            else {
                //failed
            }
        }.addOnFailureListener {
            Log.d("REGISTER", "Failed message: ${it.message}")
        }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "path: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register", "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("Register", "failed: ${it.message.toString()}")
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl:String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, binding.registerUsernameEt.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register DB", "Success")
            }
            .addOnFailureListener {
                Log.d("Register DB", "failed")
            }
    }
}
