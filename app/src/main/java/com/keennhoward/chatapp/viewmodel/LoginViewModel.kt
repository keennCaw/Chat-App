package com.keennhoward.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.keennhoward.chatapp.model.LoginRepository

class LoginViewModel(application: Application):AndroidViewModel(application) {

    private val repository = LoginRepository(application)
    private var firebaseUser = repository.getFirebaseUser()

    fun signIn(email:String, password:String){
        repository.signIn(email,password)
    }

    fun getFirebaseUser() : MutableLiveData<FirebaseUser> {
        return firebaseUser
    }
}