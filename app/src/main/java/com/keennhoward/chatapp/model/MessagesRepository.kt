package com.keennhoward.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.keennhoward.chatapp.ChatMessage

class MessagesRepository {

    private var latestMessages = MutableLiveData<ArrayList<ChatMessage>>()
    private var messagesList = ArrayList<ChatMessage>()

    fun getLatestMessaged():MutableLiveData<ArrayList<ChatMessage>>{
        return latestMessages
    }

    init {
        listenForLatestMessages()
    }

     private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                messagesList.add(chatMessage)
                messagesList.sortByDescending {
                    it.timeStamp
                }
                latestMessages.postValue(messagesList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                messagesList.forEachIndexed { index, chatMessage ->
                    if (chatMessage.toId == updatedMessage.toId) {
                        messagesList[index] = updatedMessage
                    }
                }
                messagesList.sortByDescending {
                    it.timeStamp
                }
                latestMessages.postValue(messagesList)

                Log.d("Updated MessageList", messagesList.toString())
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })
    }

}