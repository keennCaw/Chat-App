package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keennhoward.chatapp.data.ChatMessage
import com.keennhoward.chatapp.model.ChatLogRepository

class ChatLogViewModel(toId:String, fromId:String):ViewModel() {

    private val repository = ChatLogRepository(toId = toId, fromId = fromId)


    fun sendMessage(text:String){
        repository.sendMessage(text)
    }

    fun getChatLog(): MutableLiveData<ArrayList<ChatMessage>>{
        return repository.getChatLog()
    }

}