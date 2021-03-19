package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keennhoward.chatapp.data.ChatMessage
import com.keennhoward.chatapp.model.GlobalChatRepository

class GlobalChatViewModel :ViewModel(){

    private val repository = GlobalChatRepository()

    private var latestMessage = repository.getLatestMessage()

    fun getLatestMessage(): MutableLiveData<ChatMessage>{
        return latestMessage
    }

    fun sendMessage(text:String){
        repository.sendMessage(text)
    }
}