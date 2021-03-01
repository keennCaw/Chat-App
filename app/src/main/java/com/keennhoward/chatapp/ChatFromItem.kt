package com.keennhoward.chatapp

import android.app.Application
import android.view.View
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.databinding.ChatLogFromBinding

import com.xwray.groupie.viewbinding.BindableItem


class ChatFromItem(
    private val text: String,
    private val imageUrl: String,
    private val application: Application
) : BindableItem<ChatLogFromBinding>() {

    override fun getLayout(): Int {
        return R.layout.chat_log_from
    }

    override fun bind(viewBinding: ChatLogFromBinding, position: Int) {
        viewBinding.chatItemMessage.text = text
        Glide.with(application)
            .load(imageUrl)
            .into(viewBinding.chatItemImageView)
    }

    override fun initializeViewBinding(view: View): ChatLogFromBinding {
        return ChatLogFromBinding.bind(view)
    }

}