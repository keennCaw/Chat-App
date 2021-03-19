package com.keennhoward.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.keennhoward.chatapp.data.ChatMessage
import com.keennhoward.chatapp.data.LatestMessage
import com.keennhoward.chatapp.data.User
import kotlin.random.Random

class MessagesRepository{

    private var messageInfoList = ArrayList<LatestMessage>()
    private var latestMessageLiveData = MutableLiveData<ArrayList<LatestMessage>>()
    private var usersList = ArrayList<User>()

    private val usersRef = FirebaseDatabase.getInstance().getReference("/users/")

    init {
        listenForMessage()
        listenForUsersList()
    }

    fun getLatestMessages():MutableLiveData<ArrayList<LatestMessage>>{
        return latestMessageLiveData
    }

    fun getRandomUser():User?{
        if(usersList.size!=0){
            return usersList[Random.nextInt(usersList.size)]
        }else{
            return null
        }
    }

    private fun listenForUsersList(){

        usersRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for(postSnapshot in snapshot.children){
                    val user = postSnapshot.getValue(User::class.java)
                    usersList.add(user!!)
                }
            }

        })
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
                    var isNew = true
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

                            if(isNew){
                                messageInfoList.add(mapToLatestMessage(chatMessage, user))
                            }else{
                                //add functionality to display if online
                            }
                            messageInfoList.sortByDescending { it.timeStamp }
                            latestMessageLiveData.postValue(messageInfoList)
                            isNew = false
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
            user.email,
            user.status
        )
    }

}

