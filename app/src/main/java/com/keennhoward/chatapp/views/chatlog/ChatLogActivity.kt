package com.keennhoward.chatapp.views.chatlog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        chatLogViewModel = ViewModelProvider(this, factory).get(ChatLogViewModel::class.java)


        binding.chatLogSendButton.setOnClickListener {
            chatLogViewModel.sendMessage(binding.chatLogEditText.text.toString())
        }

        Log.d("Current User", MainActivity.currentUser.toString())

        supportActionBar!!.title = toUser.username

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