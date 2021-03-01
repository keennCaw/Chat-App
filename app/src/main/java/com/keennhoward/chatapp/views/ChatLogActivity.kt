package com.keennhoward.chatapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.keennhoward.chatapp.ChatFromItem
import com.keennhoward.chatapp.ChatToItem
import com.keennhoward.chatapp.NewMessageFragment.Companion.USER_KEY
import com.keennhoward.chatapp.User
import com.keennhoward.chatapp.databinding.ActivityChatLogBinding
import com.xwray.groupie.GroupieAdapter

class ChatLogActivity : AppCompatActivity() {


    private lateinit var binding:ActivityChatLogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatLogBinding.inflate(layoutInflater)

        val view = binding.root

        val user = intent.getParcelableExtra<User>(USER_KEY)

        Log.d("Current User" , MainActivity.currentUser.toString())

        supportActionBar!!.title = user.username

        setContentView(view)

        val adapter = GroupieAdapter()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        binding.chatLogRecyclerview.adapter = adapter


    }
}