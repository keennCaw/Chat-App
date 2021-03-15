package com.keennhoward.chatapp.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.keennhoward.chatapp.data.User

class MainRepository(val application: Application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    private var currentUserData:MutableLiveData<User> = MutableLiveData()

    private val currentUserRef = FirebaseDatabase.getInstance().getReference("/users/${firebaseAuth.uid}")


    init{
        firebaseUser.postValue(firebaseAuth.currentUser)
        fetchCurrentUser()
        getToken()
    }

    fun getCurrentUserData(): MutableLiveData<User>{
        return currentUserData
    }

    fun signOut(){
        firebaseAuth.signOut()
        firebaseUser.postValue(firebaseAuth.currentUser)
        currentUserRef.child("token").setValue("")
    }

    fun getFirebaseUser() : MutableLiveData<FirebaseUser>{
        return firebaseUser
    }

    private fun fetchCurrentUser(){
        currentUserRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUserData.postValue(snapshot.getValue(User::class.java))
            }

        })
    }

    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MAIN_ACTIVITY", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            currentUserRef.child("token").setValue(token)

        })

    }
}