package com.keennhoward.chatapp.views.globalchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.ActivityGlobalChatBinding
import com.keennhoward.chatapp.viewmodel.GlobalChatViewModel
import com.keennhoward.chatapp.viewmodel.GlobalChatViewModelFactory
import com.keennhoward.chatapp.views.chatlog.ChatFromItem
import com.keennhoward.chatapp.views.chatlog.ChatToItem
import com.keennhoward.chatapp.views.main.MainActivity
import com.xwray.groupie.GroupieAdapter

class GlobalChatActivity : AppCompatActivity() {

    private lateinit var binding:ActivityGlobalChatBinding
    private lateinit var globalChatViewModel:GlobalChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalChatBinding.inflate(layoutInflater)
        val view = binding.root

        val factory = GlobalChatViewModelFactory()

        globalChatViewModel = ViewModelProvider(this, factory).get(GlobalChatViewModel::class.java)

        binding.globalChatSendButton.setOnClickListener {
            val message = binding.globalChatEditText.text.toString()

            if(message.trim().isNotEmpty()){
                globalChatViewModel.sendMessage(message,MainActivity.currentUser!!.username,MainActivity.currentUser!!.profileImageUrl)
                binding.globalChatEditText.text.clear()
            }
        }


        val adapter = GroupieAdapter()
        //listen to chat
        var tempID:String = ""
        globalChatViewModel.getLatestMessage().observe(this, Observer {
            Log.d("GLOBAL", it.toString())


            if(it.fromId == MainActivity.currentUser!!.uid){
                if(tempID == it.fromId){
                    adapter.add(
                        GlobalChatFromItem(
                            it.text,
                            it.profileImageUrl,
                            application
                        )
                    )
                }else{
                    adapter.add(
                        GlobalChatFromItem(
                            it.text,
                            it.profileImageUrl,
                            application
                        )
                    )
                }

            }else{
                adapter.add(
                    GlobalChatToItem(
                        it.text,
                        it.profileImageUrl,
                        application,
                        it.username
                    )
                )
            }
            tempID = it.fromId
            binding.globalChatRecyclerview.scrollToPosition(adapter.itemCount - 1)
        })

        binding.globalChatRecyclerview.adapter = adapter


        //toolbar

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Global Chat"

        setContentView(view)
    }
}