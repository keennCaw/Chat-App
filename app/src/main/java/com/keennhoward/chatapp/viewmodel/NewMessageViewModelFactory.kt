package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewMessageViewModelFactory:ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewMessageViewModel::class.java)){
            return NewMessageViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}