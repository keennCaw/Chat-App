package com.keennhoward.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.model.MainRepository

class MainViewModel(application: Application):AndroidViewModel(application) {

    private val repository = MainRepository(application)

    private var firebaseUser = repository.getFirebaseUser()

    private var currentUserData = repository.getCurrentUserData()

    fun signOut(){
        repository.signOut()
    }

    fun setStatusOnline(){
        repository.setUserStatusOnline()
    }

    fun setStatusOffline(){
        repository.setStatusOffline()
    }

    fun setStatusAway(){
        repository.setStatusAway()
    }

    fun getFirebaseUser() : MutableLiveData<FirebaseUser> {
        return firebaseUser
    }

    fun getCurrentUserData():MutableLiveData<User>{
        return currentUserData
    }

}