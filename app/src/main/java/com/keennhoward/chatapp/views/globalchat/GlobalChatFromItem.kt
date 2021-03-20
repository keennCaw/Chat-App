package com.keennhoward.chatapp.views.globalchat

import android.app.Application
import android.view.View
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.ChatLogFromBinding
import com.keennhoward.chatapp.databinding.GlobalChatFromBinding

import com.xwray.groupie.viewbinding.BindableItem


class GlobalChatFromItem(
    private val text: String,
    private val imageUrl: String,
    private val application: Application
) : BindableItem<GlobalChatFromBinding>() {

    override fun getLayout(): Int {
        return R.layout.global_chat_from
    }

    override fun bind(viewBinding: GlobalChatFromBinding, position: Int) {
        viewBinding.chatItemMessage.text = text
        Glide.with(application)
            .load(imageUrl)
            .into(viewBinding.chatItemImageView)
    }

    override fun initializeViewBinding(view: View): GlobalChatFromBinding {
        return GlobalChatFromBinding.bind(view)
    }

}