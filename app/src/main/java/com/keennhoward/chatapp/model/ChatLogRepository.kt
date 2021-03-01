package com.keennhoward.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.keennhoward.chatapp.ChatMessage

class ChatLogRepository {


    private var firebaseAuth = FirebaseAuth.getInstance()

    private var chatLog = MutableLiveData<ArrayList<ChatMessage>>()


    fun sendMessage(text: String, toId: String) {

        val fromId = firebaseAuth.uid!!



        val fromReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

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

        toReference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("To Chat Message", "Success")
            }
            .addOnFailureListener {
                Log.d("To Chat Message", "Failed")
            }
    }

    private fun listenForMessages() {


    }


}