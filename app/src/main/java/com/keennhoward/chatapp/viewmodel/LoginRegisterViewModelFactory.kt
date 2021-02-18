package com.keennhoward.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keennhoward.chatapp.model.LoginRegisterRepository

class LoginRegisterViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(LoginRegisterViewModel::class.java)){
            return LoginRegisterViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")

    }
}