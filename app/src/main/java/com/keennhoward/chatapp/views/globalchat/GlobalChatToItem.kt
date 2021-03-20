package com.keennhoward.chatapp.views.globalchat

import android.app.Application
import android.view.View
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.ChatLogToBinding

import com.xwray.groupie.viewbinding.BindableItem


class GlobalChatToItem(
    private val text: String,
    private val imageUrl: String,
    private val application: Application
): BindableItem<ChatLogToBinding>(){
    override fun getLayout(): Int {
        return R.layout.global_chat_to
    }

    override fun bind(viewBinding: ChatLogToBinding, position: Int) {
        viewBinding.chatItemMessage.text = text
        Glide.with(application)
            .load(imageUrl)
            .into(viewBinding.chatItemImageView)
    }

    override fun initializeViewBinding(view: View): ChatLogToBinding {
        return ChatLogToBinding.bind(view)
    }

}