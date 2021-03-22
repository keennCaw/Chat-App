package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keennhoward.chatapp.data.ChatMessage
import com.keennhoward.chatapp.data.PushNotification
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.model.ChatLogRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatLogViewModel(toId:String, fromId:String):ViewModel() {

    private val repository = ChatLogRepository(toId = toId, fromId = fromId)

    private val currentUserData = repository.getCurrentUserData()

    private val toUserStatus = repository.getToUserStatus()

    fun sendMessage(text:String){
        repository.sendMessage(text)
    }

    fun latestMessageRead(){
        repository.readLatestMessage()
    }

    fun getCurrentUserData(): MutableLiveData<User> {
        return currentUserData
    }

    fun getToUserStatus():MutableLiveData<String>{
        return toUserStatus
    }

    //User Status
    fun setStatusOnline(){
        repository.setUserStatusOnline()
    }

    fun setStatusOffline(){
        repository.setStatusOffline()
    }

    fun setStatusAway(){
        repository.setStatusAway()
    }




    fun getChatLog(): MutableLiveData<ArrayList<ChatMessage>>{
        return repository.getChatLog()
    }

    fun sendNotification(notification:PushNotification): Job = viewModelScope.launch {
        repository.sendNotification(notification)
    }
}