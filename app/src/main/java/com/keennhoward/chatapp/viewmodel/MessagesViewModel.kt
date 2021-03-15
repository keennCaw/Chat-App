package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keennhoward.chatapp.data.LatestMessage
import com.keennhoward.chatapp.model.MessagesRepository

class MessagesViewModel:ViewModel() {

    private val repository = MessagesRepository()
    private val latestMessages = repository.getLatestMessages()

    fun getLatestMessages():MutableLiveData<ArrayList<LatestMessage>>{
        return latestMessages
    }
}