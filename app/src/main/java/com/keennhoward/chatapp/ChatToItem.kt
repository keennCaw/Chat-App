package com.keennhoward.chatapp

import android.view.View
import com.keennhoward.chatapp.databinding.ChatLogFromBinding
import com.keennhoward.chatapp.databinding.ChatLogToBinding

import com.xwray.groupie.viewbinding.BindableItem


class ChatToItem(): BindableItem<ChatLogToBinding>(){
    override fun getLayout(): Int {
        return R.layout.chat_log_to
    }

    override fun bind(viewBinding: ChatLogToBinding, position: Int) {
        viewBinding.chatItemMessage.text = "This is a To Message....."
    }

    override fun initializeViewBinding(view: View): ChatLogToBinding {
        return ChatLogToBinding.bind(view)
    }

}