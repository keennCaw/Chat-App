package com.keennhoward.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.keennhoward.chatapp.ChatMessage
import com.keennhoward.chatapp.LatestMessage
import com.keennhoward.chatapp.User

class MessagesRepository {

    private var latestMessages = MutableLiveData<ArrayList<ChatMessage>>()
    private var messagesList = ArrayList<ChatMessage>()

    private var messageInfo = MutableLiveData<ArrayList<LatestMessage>>()

    private var messageInfoList = ArrayList<LatestMessage>()

    fun getLatestMessaged(): MutableLiveData<ArrayList<ChatMessage>> {
        return latestMessages
    }

    fun getMessagesInfo(): MutableLiveData<ArrayList<LatestMessage>> {
        return messageInfo
    }

    init {
        listenForLatestMessages()
    }

    fun getMessageUserInfo() {
        messageInfoList.clear()
        messagesList.forEach {
            val ref = FirebaseDatabase.getInstance().getReference("/users/${it.toId}")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java) ?: return

                    messageInfoList.add(
                        LatestMessage(
                            it.id,
                            it.text,
                            it.fromId,
                            it.toId,
                            it.timeStamp,
                            user.username,
                            user.profileImageUrl
                        )
                    )
                    messageInfo.postValue(messageInfoList)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }


    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                messagesList.add(chatMessage)
                latestMessages.postValue(messagesList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                messagesList.forEachIndexed { index, chatMessage ->
                    if (chatMessage.toId == updatedMessage.toId) {
                        messagesList[index] = updatedMessage
                    }
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