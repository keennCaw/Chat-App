package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keennhoward.chatapp.ChatMessage
import com.keennhoward.chatapp.model.MessagesRepository

class MessagesViewModel:ViewModel() {

    private val repository = MessagesRepository()

    private val latestMessages = repository.getLatestMessaged()

    fun getLatestMessages():MutableLiveData<ArrayList<ChatMessage>>{
        return latestMessages
    }
}