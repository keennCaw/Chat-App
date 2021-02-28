package com.keennhoward.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keennhoward.chatapp.User
import com.keennhoward.chatapp.model.NewMessageRepository

class NewMessageViewModel : ViewModel() {

    private val repository = NewMessageRepository()
    private val firebaseUsers = repository.getUserList()

    fun fetchUsers(){
        repository.fetchUsers()
    }

    fun getUserList():MutableLiveData<ArrayList<User>>{
        return firebaseUsers
    }
}