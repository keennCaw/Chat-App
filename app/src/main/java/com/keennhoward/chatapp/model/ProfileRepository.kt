package com.keennhoward.chatapp.model

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.FirebaseDatabase


class ProfileRepository (val application: Application){

    private val firebaseAuth:FirebaseAuth = getInstance()

    private val currentUserRef = FirebaseDatabase.getInstance().getReference("/users/${firebaseAuth.uid}")

    fun saveUserChanges(profileImageUrl:String, username:String){
        currentUserRef.child("username").setValue(username)
        currentUserRef.child("profileImageUrl").setValue(profileImageUrl)
    }
}