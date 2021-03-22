package com.keennhoward.chatapp.model

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ProfileRepository (val application: Application){

    private val firebaseAuth:FirebaseAuth = getInstance()

    private val currentUserRef = FirebaseDatabase.getInstance().getReference("/users/${firebaseAuth.uid}")

    fun saveUserChanges(uri: Uri, username:String, oldImageId:String){

        val filename = UUID.randomUUID().toString()
        val firebaseStorageRef = FirebaseStorage.getInstance().getReference("/images/$filename")

        firebaseStorageRef.putFile(uri)
            .addOnSuccessListener {
                //Log.d("Register", "path: ${it.metadata?.path}")

                firebaseStorageRef.downloadUrl.addOnSuccessListener {

                    Log.d("Register", "File Location: $it")

                    updateUser(it.toString(), username, filename)
                    deleteOldImage(oldImageId)
                }
            }.addOnFailureListener {
                Log.d("Register", "failed: ${it.message.toString()}")
            }
    }

    fun updateUser(profileImageUrl:String, username:String, newImageId:String? = null){
        currentUserRef.child("username").setValue(username)
        currentUserRef.child("profileImageUrl").setValue(profileImageUrl)
        if(newImageId != null){
            currentUserRef.child("profileImageId").setValue(newImageId)
        }
    }

    private fun deleteOldImage(oldImageId:String){
        val deleteImageRef = FirebaseStorage.getInstance().getReference("/images/$oldImageId")
        deleteImageRef.delete()
            .addOnSuccessListener {
                Toast.makeText(application, "image deleted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(application, "image delete failed", Toast.LENGTH_SHORT).show()
            }
    }
}