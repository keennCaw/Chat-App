package com.keennhoward.chatapp.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainRepository(val application: Application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    init{
        firebaseUser.postValue(firebaseAuth.currentUser)
    }

    fun signOut(){
        firebaseAuth.signOut()
        firebaseUser.postValue(firebaseAuth.currentUser)
    }

    fun getFirebaseUser() : MutableLiveData<FirebaseUser>{
        return firebaseUser
    }

}