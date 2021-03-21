package com.keennhoward.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResetPasswordViewModelFactory(private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ResetPasswordViewModel::class.java)){
            return ResetPasswordViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}