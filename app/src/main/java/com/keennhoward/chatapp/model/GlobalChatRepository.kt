package com.keennhoward.chatapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keennhoward.chatapp.data.ChatMessage
import com.keennhoward.chatapp.data.GlobalMessage
import com.keennhoward.chatapp.views.main.MainActivity

class GlobalChatRepository {

    private var firebaseAuth = FirebaseAuth.getInstance()

    private val globalMessageRef = FirebaseDatabase.getInstance().getReference("/global-message/")

    private var latestMessage = MutableLiveData<GlobalMessage>()

    //private val currentUserRef = FirebaseDatabase.getInstance().getReference("/users/${firebaseAuth.uid}")

    private val currentUserRef = FirebaseDatabase.getInstance().getReference("/users/${firebaseAuth.uid}")

    init {
        listenForMessages()
    }

    fun getLatestMessage():MutableLiveData<GlobalMessage>{
        return latestMessage
    }

    fun sendMessage(text: String, username:String, profileImgUrl:String){
        val globalMessage = GlobalMessage(
            firebaseAuth.uid!!,
            username,
            text,
            profileImgUrl,
            System.currentTimeMillis() / 1000
        )
        globalMessageRef.setValue(globalMessage)
    }


    //user status
    fun setStatusAway(){
        currentUserRef.child("status").setValue("away")
    }

    fun setStatusOffline(){
        currentUserRef.child("status").setValue("offline")
    }

    fun setUserStatusOnline(){
        currentUserRef.child("status").setValue("online")
    }

    private fun listenForMessages(){
        globalMessageRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val chatMessage = snapshot.getValue(GlobalMessage::class.java)
                if(chatMessage!=null){
                    latestMessage.postValue(chatMessage!!)
                }
            }
        })
    }

}