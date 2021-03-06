package com.keennhoward.chatapp.views.globalchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.ActivityGlobalChatBinding
import com.keennhoward.chatapp.viewmodel.GlobalChatViewModel
import com.keennhoward.chatapp.viewmodel.GlobalChatViewModelFactory
import com.keennhoward.chatapp.views.chatlog.ChatFromItem
import com.keennhoward.chatapp.views.chatlog.ChatToItem
import com.keennhoward.chatapp.views.main.MainActivity
import com.xwray.groupie.GroupieAdapter
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.snackbars.fire.NoInternetSnackbarFire

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
        globalChatViewModel.getLatestMessage().observe(this, Observer {
            Log.d("GLOBAL", it.toString())
            if(it.fromId == MainActivity.currentUser!!.uid){
                    adapter.add(
                        GlobalChatFromItem(
                            it.text,
                            it.profileImageUrl,
                            application
                        )
                    )
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
            binding.globalChatRecyclerview.scrollToPosition(adapter.itemCount - 1)
        })

        binding.globalChatRecyclerview.adapter = adapter

        //adjust position on keyboard open
        binding.globalChatRecyclerview.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->

            if(bottom < oldBottom){
                binding.globalChatRecyclerview.postDelayed(Runnable {
                    binding.globalChatRecyclerview.smoothScrollToPosition(
                        bottom
                    )
                }, 100)
            }

        }

        //toolbar

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Global Chat"

        // No Internet Snackbar: Fire
        NoInternetSnackbarFire.Builder(
            binding.root,
            lifecycle
        ).apply {
            snackbarProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        // ...
                    }
                }

                duration = Snackbar.LENGTH_INDEFINITE // Optional
                noInternetConnectionMessage = "No active Internet connection!" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode!" // Optional
                snackbarActionText = "Settings" // Optional
                showActionToDismiss = false // Optional
                snackbarDismissActionText = "OK" // Optional
            }
        }.build()

        setContentView(view)


    }

    override fun onPause() {
        super.onPause()
        globalChatViewModel.setStatusAway()
    }

    override fun onResume() {
        super.onResume()
        globalChatViewModel.setStatusOnline()
    }

    override fun onDestroy() {
        globalChatViewModel.setStatusOffline()
        super.onDestroy()
    }
}