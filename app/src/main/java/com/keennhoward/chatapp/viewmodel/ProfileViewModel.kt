package com.keennhoward.chatapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.keennhoward.chatapp.model.ProfileRepository

class ProfileViewModel(application: Application): AndroidViewModel(application)  {
    private val repository = ProfileRepository(application)

    fun updateUserWithImage(uri: Uri, username:String, oldImageId:String){
        repository.saveUserChanges(uri,username,oldImageId)
    }

    fun updateUser(profileImageUrl:String, username:String){
        repository.updateUser(profileImageUrl, username)
    }
}