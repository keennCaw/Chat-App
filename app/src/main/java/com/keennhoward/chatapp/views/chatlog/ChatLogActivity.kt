package com.keennhoward.chatapp.views.chatlog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.data.NotificationData
import com.keennhoward.chatapp.data.PushNotification
import com.keennhoward.chatapp.views.main.newmessage.NewMessageFragment.Companion.USER_KEY
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.databinding.ActivityChatLogBinding
import com.keennhoward.chatapp.viewmodel.ChatLogViewModel
import com.keennhoward.chatapp.viewmodel.ChatLogViewModelFactory
import com.keennhoward.chatapp.views.main.MainActivity
import com.xwray.groupie.GroupieAdapter

class ChatLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatLogBinding

    private lateinit var chatLogViewModel: ChatLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatLogBinding.inflate(layoutInflater)
        val view = binding.root

        val toUser = intent.getParcelableExtra<User>(USER_KEY)

        val factory = ChatLogViewModelFactory(MainActivity.currentUser!!.uid, toUser.uid)

        Log.d("CHATLOGMAIN", MainActivity.currentUser!!.username)
        chatLogViewModel = ViewModelProvider(this, factory).get(ChatLogViewModel::class.java)

        binding.chatLogSendButton.setOnClickListener {
            val message = binding.chatLogEditText.text.toString()
            if(message.isNotEmpty()){
                PushNotification(
                    NotificationData(MainActivity.currentUser!!.username, message),
                    toUser.token
                ).also {
                    chatLogViewModel.sendNotification(it)
                }
            }
            chatLogViewModel.sendMessage(message)
        }

        Log.d("Current User", MainActivity.currentUser.toString())

        //
        //supportActionBar?.hide()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = toUser.username

        Glide.with(this)
            .load(toUser.profileImageUrl)
            .into(binding.chatLogToolbarImage)

        binding.chatLogToolbarEmail.text = toUser.email

        setContentView(view)

        val adapter = GroupieAdapter()

        var chatLoaded = false

        var chatCount = 0
        chatLogViewModel.getChatLog().observe(this, Observer {
            if (!chatLoaded) {
                it.forEach {
                    if (it.fromId == MainActivity.currentUser!!.uid) {
                        adapter.add(
                            ChatFromItem(
                                it.text,
                                MainActivity.currentUser!!.profileImageUrl,
                                application
                            )
                        )
                    } else {
                        adapter.add(
                            ChatToItem(
                                it.text,
                                toUser.profileImageUrl,
                                application
                            )
                        )
                    }
                    chatCount++
                }
                chatLoaded = true
            } else {
                if (it[chatCount].fromId == MainActivity.currentUser!!.uid) {
                    adapter.add(
                        ChatFromItem(
                            it[chatCount].text,
                            MainActivity.currentUser!!.profileImageUrl,
                            application
                        )
                    )
                }else{
                    adapter.add(
                        ChatToItem(
                            it[chatCount].text,
                            toUser.profileImageUrl,
                            application
                        )
                    )
                }
                chatCount++
            }
            binding.chatLogRecyclerview.scrollToPosition(adapter.itemCount - 1)
        })

        binding.chatLogRecyclerview.adapter = adapter


    }
}