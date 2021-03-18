package com.keennhoward.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.keennhoward.chatapp.data.ChatMessage
import com.keennhoward.chatapp.data.LatestMessage
import com.keennhoward.chatapp.data.User

class MessagesRepository{

    private val messageInfoList = ArrayList<LatestMessage>()
    private val latestMessageLiveData = MutableLiveData<ArrayList<LatestMessage>>()

    init {
        listenForMessage()
    }

    fun getLatestMessages():MutableLiveData<ArrayList<LatestMessage>>{
        return latestMessageLiveData
    }



    private fun listenForMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                messageInfoList.clear()
                for (postSnapshot in snapshot.children) {
                    //val toId = postSnapshot.getValue(ChatMessage::class.java)!!.toId
                    val chatMessage = postSnapshot.getValue(ChatMessage::class.java)!!
                    var user: User
                    Log.d(
                        "LatestMessagesList",
                        postSnapshot.getValue(ChatMessage::class.java).toString()
                    )

                    var userRef:DatabaseReference

                    if(fromId == chatMessage.toId){
                        userRef =
                            FirebaseDatabase.getInstance().getReference("/users/${chatMessage.fromId}")
                    }else{
                        userRef =
                            FirebaseDatabase.getInstance().getReference("/users/${chatMessage.toId}")
                    }

                    userRef.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            user = snapshot.getValue(User::class.java)!!
                            Log.d("LatestMessageInfoUser", user.toString())
                            messageInfoList.add(mapToLatestMessage(chatMessage, user))

                            messageInfoList.sortByDescending { it.timeStamp }
                            latestMessageLiveData.postValue(messageInfoList)
                        }
                    })
                }
            }
        })

    }

    private fun mapToLatestMessage(chatMessage: ChatMessage, user: User): LatestMessage {
        return LatestMessage(
            chatMessage.id,
            chatMessage.text,
            chatMessage.fromId,
            chatMessage.toId,
            chatMessage.timeStamp,
            user.username,
            user.profileImageUrl,
            chatMessage.read,
            user.token,
            user.email
        )
    }

}

