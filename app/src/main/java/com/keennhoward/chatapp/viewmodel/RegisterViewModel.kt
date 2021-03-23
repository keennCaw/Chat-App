package com.keennhoward.chatapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.keennhoward.chatapp.model.RegisterRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = RegisterRepository(application)
    private var firebaseUser = repository.getFirebaseUser()
    private var failureListener = repository.getFailureListener()


    fun register(email:String, password:String, username:String, uri:Uri){
        repository.register(email, password,username, uri)
    }

    fun getFirebaseUser(): MutableLiveData<FirebaseUser>{
        return firebaseUser
    }

    fun getFailureListener():MutableLiveData<Boolean>{
        return failureListener
    }
}