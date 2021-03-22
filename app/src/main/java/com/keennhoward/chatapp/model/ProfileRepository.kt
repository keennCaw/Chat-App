package com.keennhoward.chatapp.model

import android.app.Application
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ProfileRepository (val application: Application){

    private val firebaseAuth:FirebaseAuth = getInstance()

    private val currentUserRef = FirebaseDatabase.getInstance().getReference("/users/${firebaseAuth.uid}")

    fun saveUserChanges(uri: Uri, username:String){

        val filename = UUID.randomUUID().toString()
        val firebaseStorageRef = FirebaseStorage.getInstance().getReference("/images/$filename")

        firebaseStorageRef.putFile(uri)
            .addOnSuccessListener {
                //Log.d("Register", "path: ${it.metadata?.path}")

                firebaseStorageRef.downloadUrl.addOnSuccessListener {

                    Log.d("Register", "File Location: $it")

                    updateUser(it.toString(), username)
                }
            }.addOnFailureListener {
                Log.d("Register", "failed: ${it.message.toString()}")
            }
    }

    fun updateUser(profileImageUrl:String, username:String){
        currentUserRef.child("username").setValue(username)
        currentUserRef.child("profileImageUrl").setValue(profileImageUrl)
    }
}