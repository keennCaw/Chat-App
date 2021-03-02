package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MessagesViewModelFactory:ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MessagesViewModel::class.java)){
            return MessagesViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}