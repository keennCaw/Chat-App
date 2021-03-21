package com.keennhoward.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.keennhoward.chatapp.model.ProfileRepository

class ProfileViewModel(application: Application): AndroidViewModel(application)  {
    private val repository = ProfileRepository(application)

    fun saveUserChanges(profileImageUrl:String, username:String){
        repository.saveUserChanges(profileImageUrl,username)
    }
}