package com.keennhoward.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keennhoward.chatapp.User

class NewMessageRepository{

    private val firebaseDatabase = FirebaseDatabase.getInstance().getReference("/users")
    private val userList:ArrayList<User> = ArrayList<User>()
    private val firebaseUsersList = MutableLiveData<ArrayList<User>>()

    fun fetchUsers(){
        firebaseDatabase.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    userList.add(it.getValue(User::class.java)!!)
                }
                firebaseUsersList.postValue(userList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getUserList():MutableLiveData<ArrayList<User>>{
        return firebaseUsersList
    }

}