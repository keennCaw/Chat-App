package com.keennhoward.chatapp

import android.view.View
import com.keennhoward.chatapp.databinding.ChatLogFromBinding

import com.xwray.groupie.viewbinding.BindableItem


class ChatFromItem(): BindableItem<ChatLogFromBinding>(){
    override fun getLayout(): Int {
        return R.layout.chat_log_from
    }

    override fun bind(viewBinding: ChatLogFromBinding, position: Int) {
        viewBinding.chatItemMessage.text = "This is a From Message...."
    }

    override fun initializeViewBinding(view: View): ChatLogFromBinding {
        return ChatLogFromBinding.bind(view)
    }

}