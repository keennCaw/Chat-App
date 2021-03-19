package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GlobalChatViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GlobalChatViewModel::class.java)){
            return GlobalChatViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}