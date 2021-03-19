package com.keennhoward.chatapp.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.keennhoward.chatapp.data.ChatMessage

class GlobalChatRepository {

    private var firebaseAuth = FirebaseAuth.getInstance()

    private val globalMessageRef = FirebaseDatabase.getInstance().getReference("/global-message/")

    private var latestMessage = MutableLiveData<ChatMessage>()

    //private val currentUserRef = FirebaseDatabase.getInstance().getReference("/users/${firebaseAuth.uid}")

    init {
        listenForMessages()
    }

    fun getLatestMessage():MutableLiveData<ChatMessage>{
        return latestMessage
    }

    fun sendMessage(text: String){
        val chatMessage = ChatMessage(
            "GLOBAL-ID",
            text,
            firebaseAuth.uid!!,
            "Global",
            System.currentTimeMillis() / 1000,
            true
        )
        globalMessageRef.setValue(chatMessage)
    }

    private fun listenForMessages(){
        globalMessageRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if(chatMessage!=null){
                    latestMessage.postValue(chatMessage!!)
                }
            }
        })
    }
}