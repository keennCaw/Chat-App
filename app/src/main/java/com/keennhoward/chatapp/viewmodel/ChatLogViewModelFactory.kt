package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChatLogViewModelFactory(private val fromId:String, private val toId:String):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChatLogViewModel::class.java)){
            return ChatLogViewModel(fromId = fromId, toId = toId) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}