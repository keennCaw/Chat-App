package com.keennhoward.chatapp.views.chatlog

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.data.NotificationData
import com.keennhoward.chatapp.data.PushNotification
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.databinding.ActivityChatLogBinding
import com.keennhoward.chatapp.viewmodel.ChatLogViewModel
import com.keennhoward.chatapp.viewmodel.ChatLogViewModelFactory
import com.keennhoward.chatapp.views.main.MainActivity
import com.keennhoward.chatapp.views.main.newmessage.NewMessageFragment.Companion.USER_KEY
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
            if(message.trim().isNotEmpty()){
                PushNotification(
                    NotificationData(MainActivity.currentUser!!.username, message),
                    toUser.token
                ).also {
                    chatLogViewModel.sendNotification(it)
                }
                chatLogViewModel.sendMessage(message)
                binding.chatLogEditText.text.clear()
            }
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

        //move recyclerview position to bottom on keyboard open

        binding.chatLogRecyclerview.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->

            if(bottom < oldBottom){
                binding.chatLogRecyclerview.postDelayed(Runnable {
                    binding.chatLogRecyclerview.smoothScrollToPosition(
                        bottom
                    )
                }, 100)
            }

        }

        //To User Status
        chatLogViewModel.getToUserStatus().observe(this, Observer {
            when(it){
                "online" -> {
                    binding.chatLogToolbarImage.setStrokeColorResource(R.color.purple_200)
                    binding.chatLogUserStatus.setBackgroundResource(R.color.purple_200)
                }
                "offline" -> {
                    binding.chatLogToolbarImage.setStrokeColorResource(R.color.grey)
                    binding.chatLogUserStatus.setBackgroundResource(R.color.grey)
                }
                "away" -> {
                    binding.chatLogToolbarImage.setStrokeColorResource(R.color.orange)
                    binding.chatLogUserStatus.setBackgroundResource(R.color.orange)
                }
                else ->{
                    binding.chatLogToolbarImage.setStrokeColorResource(R.color.grey)
                    binding.chatLogUserStatus.setBackgroundResource(R.color.grey)
                }
            }
        })

    }

    override fun onPause() {
        super.onPause()
        chatLogViewModel.setStatusAway()
    }

    override fun onStop() {
        //mainViewModel.setStatusOffline()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        chatLogViewModel.setStatusOnline()
    }

    override fun onDestroy() {
        chatLogViewModel.setStatusOffline()
        super.onDestroy()
    }
}