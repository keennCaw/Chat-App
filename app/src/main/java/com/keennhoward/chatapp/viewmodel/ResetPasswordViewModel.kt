package com.keennhoward.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.keennhoward.chatapp.model.ResetPasswordRepository

class ResetPasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ResetPasswordRepository(application)

    private var resetEmailSent = repository.getResetEmailResult()

    fun getResetEmailResult():MutableLiveData<Boolean>{
        return resetEmailSent
    }

    fun sendResetPasswordEmail(email:String){
        repository.sendResetPasswordEmail(email)
    }
}