package com.keennhoward.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.keennhoward.chatapp.model.MainRepository

class MainViewModel(application: Application):AndroidViewModel(application) {

    private val repository = MainRepository(application)

    private var firebaseUser = repository.getFirebaseUser()

    fun signOut(){
        repository.signOut()
    }

    fun getFirebaseUser() : MutableLiveData<FirebaseUser> {
        return firebaseUser
    }

}