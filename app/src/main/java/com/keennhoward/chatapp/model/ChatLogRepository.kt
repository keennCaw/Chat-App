package com.keennhoward.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.keennhoward.chatapp.ChatMessage
import com.keennhoward.chatapp.User

class ChatLogRepository(private val toId:String, private val fromId:String) {


    private var firebaseAuth = FirebaseAuth.getInstance()

    private var chatLog = MutableLiveData<ArrayList<ChatMessage>>()

    private var chatLogList = ArrayList<ChatMessage>()

    private var currentUserData:MutableLiveData<User> = MutableLiveData()

    init {
        listenForMessages()
    }

    fun getChatLog():MutableLiveData<ArrayList<ChatMessage>>{
        return chatLog
    }



    fun sendMessage(text: String) {

        val fromReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val latestMessageFromRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val chatMessage = ChatMessage(
            fromReference.key!!,
            text,
            fromId,
            toId,
            System.currentTimeMillis() / 1000
        )

        fromReference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("From Chat Message", "Success")
            }
            .addOnFailureListener {
                Log.d("From Chat Message", "Failed")
            }

        latestMessageFromRef.setValue(chatMessage)

        if(fromId!=toId){
            toReference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d("To Chat Message", "Success")
                }
                .addOnFailureListener {
                    Log.d("To Chat Message", "Failed")
                }

            latestMessageToRef.setValue(chatMessage)
        }
    }

    private fun listenForMessages() {

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")



        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                chatLogList.add(snapshot.getValue(ChatMessage::class.java)!!)
                chatLog.postValue(chatLogList)
                Log.d(" Chat Log ", chatLogList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }

}