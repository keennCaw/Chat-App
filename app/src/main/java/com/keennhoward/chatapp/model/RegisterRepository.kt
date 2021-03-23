package com.keennhoward.chatapp.model

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.keennhoward.chatapp.data.User
import java.util.*

class RegisterRepository(val application: Application){

    private var firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var failureListener: MutableLiveData<Boolean> = MutableLiveData()

    fun register(email:String, password:String, username:String, uri:Uri){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if(task.isSuccessful){
                    uploadImageToFirebaseStorage(uri,username,email,password)
                    //firebaseUser.postValue(firebaseAuth.currentUser)
                    failureListener.postValue(true)
                }else{
                    Toast.makeText(application, "Registration Failed: ${task.exception!!.message}"
                    ,Toast.LENGTH_SHORT).show()
                    failureListener.postValue(false)
                }
            }
            .addOnFailureListener {
                //Toast.makeText(application, "Registration Failed: ${it.message.toString()}"
                 //   ,Toast.LENGTH_SHORT).show()
                failureListener.postValue(false)
            }
    }

    fun getFailureListener():MutableLiveData<Boolean>{
        return failureListener
    }

    fun getFirebaseUser() : MutableLiveData<FirebaseUser>{
        return firebaseUser
    }

    private fun uploadImageToFirebaseStorage(uri: Uri, username: String, email:String,password: String){
        val filename = UUID.randomUUID().toString()
        val firebaseStorageRef = FirebaseStorage.getInstance().getReference("/images/$filename")

        firebaseStorageRef.putFile(uri)
            .addOnSuccessListener {
                Log.d("Register", "path: ${it.metadata?.path}")

                firebaseStorageRef.downloadUrl.addOnSuccessListener {

                    Log.d("Register", "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString(), username,email,filename,password)
                }
            }.addOnFailureListener {
                Log.d("Register", "failed: ${it.message.toString()}")
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl:String, username: String,email: String,filename:String,password: String){
        val uid = firebaseAuth.uid ?: ""
        val firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid = uid,
            username = username,
            profileImageUrl = profileImageUrl,
            email = email,
            token = "",
            status = "offline",
            profileImageId = filename
        )

        firebaseDatabaseReference.setValue(user)
            .addOnSuccessListener {
                Log.d("Register DB", "Success")
                signIn(email, password)
            }
            .addOnFailureListener {
                Log.d("Register DB", "failed")
            }
    }

    private fun signIn(email:String, password:String){

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    firebaseUser.postValue(firebaseAuth.currentUser)
                }else{
                    Toast.makeText(application, "Login Failed: ${task.exception!!.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Log.d("Login", "failed: ${it.message.toString()}")
            }
    }
}